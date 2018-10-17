package io.nibby.qipan.ogs;

import io.nibby.qipan.game.AbstractRules;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameRules;
import org.json.JSONArray;
import org.json.JSONObject;

public class OgsGameData {

    public static final int GAME_PHASE_PLAYING = 0;
    public static final int GAME_PHASE_STONE_REMOVAL = 1;
    public static final int GAME_PHASE_FINISHED = 2;
    private Game game;

    String initialPlayer;
    private boolean privateGame;
    private boolean allowKo;
    private AbstractRules rules;
    private boolean scorePasses;
    private boolean scoreTerritory;
    private boolean opponentPlaysFirstAfterResume;
    private String gameName;
    private boolean scoreStones;
    private int playerBlackId;
    private boolean allowSelfCapture;
    private int playerWhiteId;
    private boolean ranked;
    private boolean allowSuperKo;
    private String outcome;
    private boolean disableAnalysis;
    private int boardHeight;
    private int id;
    private boolean automaticStoneRemoval;
    private int phase;
    private boolean scorePrisoners;
    private String superkoAlgorithm;
    private int handicap;
    private OgsPlayer playerWhite;
    private OgsPlayer playerBlack;
    private long endTime;
    private boolean pauseOnWeekends;
    private boolean scoreTerritoryInSeki;
    //TODO clock
    private boolean agaHandicapScoring;
    private boolean scoreHandicap;
    private boolean strictSekiMode;
    private boolean freeHandicapPlacement;
    private long startTime;
    private float komi;
    //TODO time controls
    private int winnerId;
    private boolean whiteMustPastLast;
    private int boardWidth;
    private boolean originalDisableAnalysis;

    public OgsGameData(int gameId) {
        this.id = gameId;
    }


    protected void parseData(JSONObject gamedata) {
        initialPlayer = gamedata.getString("initial_player");
        privateGame = gamedata.getBoolean("private");
        allowKo = gamedata.getBoolean("allow_ko");
        scorePasses = gamedata.getBoolean("score_passes");
        scoreTerritory = gamedata.getBoolean("score_territory");
        opponentPlaysFirstAfterResume = gamedata.getBoolean("opponent_plays_first_after_resume");
        gameName = gamedata.getString("game_name");
        scoreStones = gamedata.getBoolean("score_stones");
        playerBlackId = gamedata.getInt("black_player_id");
        allowSelfCapture = gamedata.getBoolean("allow_self_capture");
        playerWhiteId = gamedata.getInt("white_player_id");
        boardWidth = gamedata.getInt("width");
        boardHeight = gamedata.getInt("height");
        handicap = gamedata.getInt("handicap");
        ranked = gamedata.getBoolean("ranked");
        allowSuperKo = gamedata.getBoolean("allow_superko");
        if (gamedata.has("outcome"))
            outcome = gamedata.getString("outcome");
        disableAnalysis = gamedata.getBoolean("disable_analysis");
        boardHeight = gamedata.getInt("height");
        id = gamedata.getInt("game_id");
        automaticStoneRemoval = gamedata.getBoolean("automatic_stone_removal");
        String phaseRaw = gamedata.getString("phase");
        if (phaseRaw.contains("play"))
            phase = GAME_PHASE_PLAYING;
        else if (phaseRaw.contains("finished"))
            phase = GAME_PHASE_FINISHED;
        else if (phaseRaw.contains("removal"))
            phase = GAME_PHASE_STONE_REMOVAL;
        scorePrisoners = gamedata.getBoolean("score_prisoners");
        superkoAlgorithm = gamedata.getString("superko_algorithm");
        handicap = gamedata.getInt("handicap");
        if (gamedata.has("end_time"))
            endTime = gamedata.getLong("end_time");
        pauseOnWeekends = gamedata.getBoolean("pause_on_weekends");
        scoreTerritoryInSeki = gamedata.getBoolean("score_territory_in_seki");
        agaHandicapScoring = gamedata.getBoolean("aga_handicap_scoring");
        scoreHandicap = gamedata.getBoolean("score_handicap");
        strictSekiMode = gamedata.getBoolean("strict_seki_mode");
        freeHandicapPlacement = gamedata.getBoolean("free_handicap_placement");
        startTime = gamedata.getLong("start_time");
        komi = gamedata.getFloat("komi");
        if (gamedata.has("winner"))
            winnerId = gamedata.getInt("winner");
        whiteMustPastLast = gamedata.getBoolean("white_must_pass_last");
        boardWidth = gamedata.getInt("width");
        originalDisableAnalysis = gamedata.getBoolean("original_disable_analysis");

        String rulesRaw = gamedata.getString("rules");
        if (rulesRaw.equals("japanese"))
            rules = GameRules.JAPANESE;

        if (rules != null) {
            // TODO add more metadata to actual game object
            game = new Game(boardWidth, boardHeight, rules);
            game.setKomi(komi);
            game.setName(gameName);

            // Parse moves
            JSONArray movesRaw = gamedata.getJSONArray("moves");
            for (int i = 0; i < movesRaw.length(); i++) {
                JSONArray moveDataRaw = movesRaw.getJSONArray(i);
                int x = moveDataRaw.getInt(0);
                int y = moveDataRaw.getInt(1);
                long timeMs = moveDataRaw.getInt(2);
                game.playMove(x, y);
            }
        } else
            throw new RuntimeException("Unsupported game rules: " + rulesRaw);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public OgsPlayer getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(OgsPlayer playerWhite) {
        this.playerWhite = playerWhite;
    }

    public OgsPlayer getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(OgsPlayer playerBlack) {
        this.playerBlack = playerBlack;
    }

    public Game getGame() {
        return game;
    }
}
