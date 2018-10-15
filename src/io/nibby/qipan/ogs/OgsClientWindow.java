package io.nibby.qipan.ogs;

import com.sun.istack.internal.NotNull;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.UIStylesheets;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OgsClientWindow extends Stage {

    private OgsPlayer player;

    public OgsClientWindow() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        UIStylesheets.applyTo(scene);
        setTitle("QiPan: OGS");
        setScene(scene);
        setResizable(true);

        setOnShowing(this::openSocket);
    }

    private void openSocket(WindowEvent windowEvent) {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = true;
            options.reconnectionDelay = 500;
            options.reconnectionDelayMax = 60000;
            options.transports = new String[]{ "websocket" };
            Socket socket = IO.socket(new URL("https://online-go.com").toURI().toString(), options);
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
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Error");
                    if (objects[0] instanceof Exception) {
                        ((Exception) objects[0]).printStackTrace();
                    }
                }
            });

            socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    for (Object o : objects) {
                        System.out.println(o);
                    }
                }
            });
            socket.on(Socket.EVENT_CONNECT, e -> {


            });
            socket.connect();

            int game =14849657;
            Rest.Response rr = Rest.get("https://online-go.com/api/v1/games/" + game, true);
            JSONObject obj = rr.getJson();
            String auth = obj.getString("auth");
            String chatAuth = obj.getString("game_chat_auth");

            JSONObject jj = new JSONObject();
            jj.put("player_id", player.getId());
            jj.put("username", player.getUsername());
            jj.put("auth", auth);
            socket.emit("authenticate", jj);


            JSONObject json = new JSONObject();
            json.put("game_id", game);
            json.put("player_id", player.getId());
//            json.put("auth", auth);
            json.put("chat", false);
            socket.on("game/" + game + "/gamedata", objs -> {
                for (Object o : objs) {
                    System.out.println(o.toString());
                }
            });
            socket.on("game/" + game + "/move", objs -> {
                for (Object o : objs) {
                    System.out.println(o.toString());
                }
            });

            socket.emit("game/connect", json);

            JSONObject j = new JSONObject();
            j.put("player_id", player.getId());
            j.put("auth", auth);
            j.put("game_id", game);
            j.put("move", "ab");
            socket.emit("game/move", j);

            JSONObject j2 = new JSONObject();
            j2.put("auth", chatAuth);
            j2.put("game_id", game);
            j2.put("player_id", player.getId());
            j2.put("body", "test_message");
            j2.put("type", "discussion");
            j2.put("move_number", 3);
            j2.put("username", player.getUsername());
            j2.put("is_player", true);
            j2.put("ranking", player.getRank());
            j2.put("ui_class", "blah?");

            socket.emit("game/chat", j2);

//            JSONObject j = new JSONObject();
//            j.put("move", "ab");
//            Rest.Response r = Rest.postBody("https://online-go.com/api/v1/games/"+ game + "/resign/", true, "");
//            System.out.println("Got: " + r.getRawString());
//            System.out.println("R: " + r.getHttpResponse());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSessionPlayer(@NotNull  OgsPlayer player) {
        this.player = player;
    }

}
