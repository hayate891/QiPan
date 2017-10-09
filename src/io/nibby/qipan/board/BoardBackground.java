package io.nibby.qipan.board;

import java.net.URL;

public enum BoardBackground {

    KAYA("kaya.jpg") {
        @Override
        public String getFriendlyName() {
            return "Light Kaya";
        }
    };

    private String resource;

    BoardBackground(String resource) {
        this.resource = resource;
    }

    public abstract String getFriendlyName();

    public String getResource() {
        return "/board/" + resource;
    }
}
