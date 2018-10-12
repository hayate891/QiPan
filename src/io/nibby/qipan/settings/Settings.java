package io.nibby.qipan.settings;

import io.nibby.qipan.QiPan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    public static final Path BASE_DIRECTORY = resolveBaseDirectory();

    private List<SettingsModule> modules = new ArrayList<>();
    public static final GeneralSettings general = new GeneralSettings();
    public static final LanguageSettings language = new LanguageSettings();
    public static final GuiSettings gui = new GuiSettings();
    public static final OgsAuthSettings ogsToken = new OgsAuthSettings();

    static {
        // Validate base directory
        if (BASE_DIRECTORY != null && Files.notExists(BASE_DIRECTORY)) {
            try {
                Files.createDirectories(BASE_DIRECTORY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Initialize modules
        new Settings();
    }

    private static Path resolveBaseDirectory() {
        String osName = System.getProperty("os.name");
        Path home = Paths.get(System.getProperty("user.home"));

        if (osName.contains("Mac"))
            return home.resolve("Library").resolve("Application Support").resolve(QiPan.NAME);
        else if (osName.contains("Win"))
            return home.resolve("AppData").resolve("Roaming").resolve(QiPan.NAME);
        else if (osName.contains("nix"))
            return home.resolve(QiPan.NAME);
        else
            return null;
    }

    private void loadAll() {
        for (SettingsModule module : modules) {
            module.load();
        }
    }

    private Settings() {
        modules.add(general);
        modules.add(language);
        modules.add(gui);
        modules.add(ogsToken);

        loadAll();
    }
}
