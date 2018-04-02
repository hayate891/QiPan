package io.nibby.qipan.settings;

import io.nibby.qipan.QiPan;
import io.nibby.qipan.ui.UIStyle;
import io.nibby.qipan.ui.board.BoardBackgroundStyle;
import io.nibby.qipan.ui.board.BoardStyle;
import io.nibby.qipan.ui.board.StoneStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiSettings implements SettingsModule {

    private static final Path CONFIG_FILE = Settings.BASE_DIRECTORY.resolve("ui.config");

    private UIStyle uiStyle;
    private StoneStyle gameStoneStyle;
    private BoardStyle gameBoardStyle;
    private BoardBackgroundStyle gameBoardBgStyle;

    private void saveDefaults() {
        uiStyle = UIStyle.MEGUMI;
        uiStyle.initialize();
        gameStoneStyle = StoneStyle.getDefault();
        gameBoardStyle = BoardStyle.getDefault();
        gameBoardBgStyle = BoardBackgroundStyle.getDefault();

        save();
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(CONFIG_FILE, Charset.forName(QiPan.CHARSET)))) {
            writer.println(uiStyle.name());
            writer.println(gameStoneStyle.name());
            writer.println(gameBoardStyle.name());
            writer.println(gameBoardBgStyle.name());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        if (!Files.exists(CONFIG_FILE)) {
            saveDefaults();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            try {
                uiStyle = UIStyle.valueOf(reader.readLine());
            } catch (Exception e) {
                uiStyle = UIStyle.MEGUMI;
            }
            uiStyle.initialize();

            try {
                gameStoneStyle = StoneStyle.valueOf(reader.readLine());
            } catch (Exception e) {
                gameStoneStyle = StoneStyle.CERAMIC;
            }

            try {
                gameBoardStyle = BoardStyle.valueOf(reader.readLine());
            } catch (Exception e) {
                gameBoardStyle = BoardStyle.KAYA;
            }

            try {
                gameBoardBgStyle = BoardBackgroundStyle.valueOf(reader.readLine());
            } catch (Exception e) {
                gameBoardBgStyle = BoardBackgroundStyle.TATAMI;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UIStyle getUiStyle() {
        return uiStyle;
    }

    public void setUiStyle(UIStyle uiStyle) {
        this.uiStyle = uiStyle;
        save();
    }

    public StoneStyle getGameStoneStyle() {
        return gameStoneStyle;
    }

    public void setGameStoneStyle(StoneStyle gameStoneStyle) {
        this.gameStoneStyle = gameStoneStyle;
        save();
    }

    public BoardStyle getGameBoardStyle() {
        return gameBoardStyle;
    }

    public void setGameBoardStyle(BoardStyle gameBoardStyle) {
        this.gameBoardStyle = gameBoardStyle;
        save();
    }

    public BoardBackgroundStyle getGameBoardBgStyle() {
        return gameBoardBgStyle;
    }

    public void setGameBoardBgStyle(BoardBackgroundStyle gameBoardBgStyle) {
        this.gameBoardBgStyle = gameBoardBgStyle;
        save();
    }
}
