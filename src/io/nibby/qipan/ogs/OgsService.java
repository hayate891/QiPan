package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class OgsService {

    private static final int HEARTBEAT_PERIOD = 15000;
    private String chatAuth;
    private String notificationAuth;
    private List<Integer> activeGames = new ArrayList<>();

    private static final String SERVER = "https://online-go.com";
    private Socket socket;
    private OgsPlayer sessionPlayer;
    private OgsClientWindow client;

    public OgsService(OgsClientWindow client) {
        this.client = client;
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.reconnectionDelay = 500;
        options.reconnectionDelayMax = 60000;
        options.transports = new String[]{ "websocket" };

        try {
            socket = IO.socket(SERVER, options);
            socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Transport transport = (Transport) objects[0];
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {

                        @Override
                        public void call(Object... objects) {
                            String auth = Settings.ogsAuth.getTokenType() + " " + Settings.ogsAuth.getAuthToken();
                            Map<String, List<String>> mHeaders = (Map<String, List<String>>) objects[0];
                            mHeaders.put("Authorization", Arrays.asList(auth));
                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public void off(String event) {
        socket.off(event);
    }

    public void emit(String event, JSONObject obj) {
        socket.emit(event, obj);
    }

    public void initialize() throws IOException {
        // Perform REST call to fetch session user info
        // Fetch player info
        Rest.Response r = Rest.get("https://online-go.com/api/v1/me/", true);
        JSONObject j = r.getJson();
        String playerName = j.getString("username");
        int playerRating = j.getInt("rating");
        int playerRank = j.getInt("ranking");
        sessionPlayer = new OgsPlayer();
        sessionPlayer.setUsername(playerName);
        sessionPlayer.setRank(playerRank);
        sessionPlayer.setRating(playerRating);

        r = Rest.get("https://online-go.com/api/v1/me/settings/", true);
        j = r.getJson();
        int playerId = j.getJSONObject("profile").getInt("id");
        sessionPlayer.setId(playerId);

        r = Rest.get("https://online-go.com/api/v1/ui/config/", true);
        j = r.getJson();
        chatAuth = j.getString("chat_auth");
        notificationAuth = j.getString("notification_auth");

        if (!socket.connected()) {
            // Register key events
            on(Socket.EVENT_ERROR, this::onError);
            on(Socket.EVENT_CONNECT_ERROR, this::onConnectError);
            on(Socket.EVENT_CONNECTING, this::onConnecting);
            on(Socket.EVENT_CONNECT, this::onConnectSuccess);
            on(Socket.EVENT_CONNECT_TIMEOUT, this::onConnectTimeout);
            on(Socket.EVENT_MESSAGE, this::onMessage);
            on("active_game", this::onActiveGameReceive);
            on("notification", this::onNotificationReceive);

            // Connect to OGS real-time server
            socket.connect();

            // Authenticate for notifications
            j = new JSONObject();
            j.put("player_id", sessionPlayer.getId());
            j.put("username", sessionPlayer.getUsername());
            j.put("auth", notificationAuth);
            emit("notification/connect", j);

            // Authenticate for game chat (appears as online on OGS)
            j = new JSONObject();
            j.put("player_id", sessionPlayer.getId());
            j.put("username", sessionPlayer.getUsername());
            j.put("auth", chatAuth);
            emit("chat/connect", j);

            // Authenticate for authenticated-only actions later e.g. move submission
            emit("authenticate", j);

            Timer heartbeat = new Timer();
            TimerTask keepalive = new TimerTask() {
                @Override
                public void run() {
                    JSONObject heartbeatPacket = new JSONObject();
                    heartbeatPacket.put("client", System.currentTimeMillis());
                    emit("net/ping", heartbeatPacket);
                }
            };
            heartbeat.scheduleAtFixedRate(keepalive, 1000, HEARTBEAT_PERIOD);
        }
    }

    private OgsGameData connectToGame(int gameId, OgsGamePane window) {
        if (activeGames.contains(gameId))
            return null;

        JSONObject j = new JSONObject();
//        j.put("auth", sessionPlayer);
        j.put("player_id", sessionPlayer.getId());
        j.put("game_id", gameId);
        j.put("chat", true);

        OgsGameData game = new OgsGameData(gameId);

        on("game/" + gameId + "/gamedata", objs -> {
            JSONObject gamedata = new JSONObject(objs[0].toString());
            game.parseData(gamedata);
            Platform.runLater(() -> {
                window.onConnection(game);
            });
        });

        on("game/" + gameId + "/move", objs -> {
            Platform.runLater(() -> {
                JSONObject gameMove = new JSONObject(objs[0].toString());
                JSONArray moveDataRaw = gameMove.getJSONArray("move");
                int x = moveDataRaw.getInt(0);
                int y = moveDataRaw.getInt(1);
                int timeMs = moveDataRaw.getInt(2);
                window.onMovePlayed(x, y, timeMs);
            });
        });

        on("game/" + gameId + "/error", objs -> {
            JSONObject gameErr = new JSONObject(objs[0].toString());
            System.err.println("Error in " + gameId + ": " + gameErr);
        });

        emit("game/connect", j);
        activeGames.add(gameId);
        return game;
    }

    private void disconnectFromGame(OgsGameData game) {
        disconnectFromGame(game.getId());
    }

    public void disconnectFromGame(int gameId) {
        off("game/" + gameId + "/gamedata");
        off("game/" + gameId + "/move");
        off("game/" + gameId + "/error");

        activeGames.remove((Object) gameId);
    }

    public OgsGamePane openGame(int gameId) {
        if (hasActiveGame(gameId))
            return null;

        OgsGamePane window = new OgsGamePane();
        connectToGame(gameId, window);

        return window;
    }

    /**
     * Returns whether or not a connection to the designated gameId has already been established.
     * @param gameId Game ID to be checked.
     * @return
     */
    public boolean hasActiveGame(int gameId) {
        return activeGames.contains(gameId);
    }

    private void onNotificationReceive(Object... objects) {
        System.out.println("notification: " + objects[0]);
    }

    private void onConnectSuccess(Object... objects) {
        System.out.println("Connected!");
    }

    private void onMessage(Object... objects) {
        System.out.println("Message received: " + objects[0]);
    }

    private void onConnectTimeout(Object... objects) {
        System.out.println("Timeout : ");
    }

    private void onConnecting(Object... objects) {

    }

    private void onConnectError(Object... objects) {
        System.out.println("Connect error : " + objects[0]);
    }

    private void onError(Object[] objects) {
        System.out.println("error : " + objects[0]);
    }

    private void onActiveGameReceive(Object[] args) {
        JSONObject gameObj = new JSONObject(args[0].toString());
        OgsDashboardPane.GameInfo info = new OgsDashboardPane.GameInfo();
        String phaseRaw = gameObj.getString("phase");
        if (phaseRaw.contains("play")) {
            info.gamePhase = OgsGameData.GAME_PHASE_PLAYING;
        } else if (phaseRaw.contains("removal")) {
            info.gamePhase = OgsGameData.GAME_PHASE_STONE_REMOVAL;
        } else if (phaseRaw.contains("finish")) {
            info.gamePhase = OgsGameData.GAME_PHASE_FINISHED;
        } else throw new IllegalArgumentException("Bad active game phase: " + phaseRaw);

        info.paused = gameObj.getLong("paused");
        info.privateGame = gameObj.getBoolean("private");
        info.playerIdToMove = gameObj.getInt("player_to_move");
        info.playerWhite = OgsPlayer.parse(gameObj.getJSONObject("white"));
        info.moveNumber = gameObj.getInt("move_number");
        info.gameName = gameObj.getString("name");
        info.boardWidth = gameObj.getInt("width");
        info.playerBlack = OgsPlayer.parse(gameObj.getJSONObject("black"));
        info.gameId = gameObj.getInt("id");
        info.boardHeight = gameObj.getInt("height");
        if (gameObj.has("time_per_move"))
            info.timePerMove = gameObj.getInt("time_per_move");

        Platform.runLater(() -> {
            client.getDashboardPane().addActiveGame(info);
        });
    }

    public void shutdown() {
        if (socket.connected()) {
            socket.disconnect();
            socket.close();
        }
    }

    public OgsPlayer getSessionPlayer() {
        return sessionPlayer;
    }
}
