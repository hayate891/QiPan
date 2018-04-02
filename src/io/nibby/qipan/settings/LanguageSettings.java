package io.nibby.qipan.settings;

import com.sun.istack.internal.Nullable;
import io.nibby.qipan.QiPan;
import io.nibby.qipan.ui.UIStylesheets;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LanguageSettings implements SettingsModule {

    public enum SupportedLocales {
        en_US("en_US", new Locale("en", "US")),
        zh_CN("zh_CN", new Locale("zh", "CN"));

        private String resourceFolder;
        private Locale locale;
        private Map<String, ResourceBundle> resourceBundles;

        SupportedLocales(String resourceFolder, Locale locale) {
            this.resourceFolder = resourceFolder;
            this.locale = locale;
        }

        private void initialize() {
            if (resourceBundles != null)
                return;

            resourceBundles = new HashMap<>();
            String sep = System.getProperty("file.separator");
            String path = SupportedLocales.class.getResource(sep + resourceFolder).getPath();
            File[] files = new File(path).listFiles();
            for (File file : files) {
                if (!file.getName().endsWith(".properties"))
                    continue;
                ResourceBundle bundle;
                String fileName = file.getName().replace(".properties", "");
                String bundleName = resourceFolder + "." + fileName;
                if (locale == null)
                    bundle = ResourceBundle.getBundle(bundleName);
                else
                    bundle = ResourceBundle.getBundle(bundleName, locale);

                resourceBundles.put(fileName, bundle);
            }

            Scanner fontScanner = new Scanner(SupportedLocales.class.getResourceAsStream(sep + resourceFolder + sep + "font"));
            String fontFile = fontScanner.nextLine();
            fontScanner.close();
            QiPan.SYSTEM_FONT = Font.loadFont(SupportedLocales.class.getResourceAsStream(fontFile), 12);

            String languageStylesheet = sep + resourceFolder + sep + "lang.css";
            UIStylesheets.add(languageStylesheet);
        }

        @Nullable
        public ResourceBundle getBundle(String bundleName) {
            return resourceBundles.get(bundleName + "_" + resourceFolder);
        }
    }

    private static final Path CONFIG_FILE = Settings.BASE_DIRECTORY.resolve("language.config");
    private SupportedLocales locale;

    private void saveDefaults() {
        locale = SupportedLocales.en_US;
        locale.initialize();

        save();
    }

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(CONFIG_FILE, Charset.forName(QiPan.CHARSET)))) {
            writer.println(locale.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        if (Files.notExists(CONFIG_FILE)) {
            saveDefaults();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            try {
                locale = SupportedLocales.valueOf(reader.readLine());
            } catch (Exception e) {
                locale = SupportedLocales.en_US;
            }
            locale.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SupportedLocales getLocale() {
        return locale;
    }

    public void setLocale(SupportedLocales locale) {
        this.locale = locale;
        save();
    }
}
