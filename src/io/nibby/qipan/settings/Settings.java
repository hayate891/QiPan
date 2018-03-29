package io.nibby.qipan.settings;

import io.nibby.qipan.QiPan;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static final Path BASE_DIRECTORY = resolveBaseDirectory();

    private List<SettingsModule> modules = new ArrayList<>();
    public static final GeneralSettings general = new GeneralSettings();
    public static final LanguageSettings language = new LanguageSettings();
    public static final GuiSettings gui = new GuiSettings();

    static {
        // Initialize modules
        new Settings();
    }

    private static Path resolveBaseDirectory() {
        String osName = System.getProperty("os.name");
        Path home = Paths.get(System.getProperty("user.home"));

        if (osName.contains("mac"))
            return home.resolve("Library").resolve("Application Support").resolve(QiPan.NAME);

        else if (osName.contains("win"))
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

        loadAll();
    }
}
