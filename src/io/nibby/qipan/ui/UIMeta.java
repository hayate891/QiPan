package io.nibby.qipan.ui;

import com.sun.istack.internal.Nullable;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class UIMeta {

    private Map<String, String> propertyMap = new HashMap<>();

    @Nullable
    public Color getAsColor(String key) {
        String value = get(key);
        if (value == null)
            return null;

        String[] rgb = value.split(",");
        if (rgb.length < 3)
            return null;

        double r = Double.parseDouble(rgb[0]) / 255.0d;
        double g = Double.parseDouble(rgb[1]) / 255.0d;
        double b = Double.parseDouble(rgb[2]) / 255.0d;
        double a = (rgb.length == 4) ? Double.parseDouble(rgb[3]) : 1.0d;

        if (r < 0.0d)
            r = 0.0d;
        if (r > 1.0d)
            r = 1.0d;
        if (g < 0.0d)
            g = 0.0d;
        if (g > 1.0d)
            g = 1.0d;
        if (b < 0.0d)
            b = 0.0d;
        if (b > 1.0d)
            b = 1.0d;

        return new Color(r, g, b, a);
    }

    public void put(String key, String value) {
        propertyMap.put(key, value);
    }

    public String get(String key) {
        return propertyMap.get(key);
    }
}
