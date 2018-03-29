package io.nibby.qipan.ui;

public enum UIStyle {

    MEGUMI("Megumi", "megumi"),

    ;
    private String name;
    private String directory;

    UIStyle(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public String getStylesheet() {
        String sep = System.getProperty("file.separator");
        return sep + directory + sep + "main.css";
    }

    public String getColorDefinitions() {
        String sep = System.getProperty("file.separator");
        return sep + directory + sep + "colors.properties";
    }
}
