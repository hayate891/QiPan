package io.nibby.qipan.ui;

import java.util.Scanner;

public enum UIStyle {

    MEGUMI("Megumi", "megumi");

    private String name;
    private String directory;
    private UIMeta meta;

    UIStyle(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }

    public void initialize() {
        if (meta != null)
            return;

        Scanner scanner = new Scanner(UIStyle.class.getResourceAsStream(getMetaFilePath()));
        String line;
        meta = new UIMeta();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] kv = line.split(":");
            meta.put(kv[0], kv[1]);
        }
        scanner.close();

        UIStylesheets.add(getStylesheet());
    }

    public UIMeta getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public String getStylesheet() {
        String sep = System.getProperty("file.separator");
        return sep + directory + sep + "main.css";
    }

    public String getMetaFilePath() {
        String sep = System.getProperty("file.separator");
        return sep + directory + sep + "theme.properties";
    }
}
