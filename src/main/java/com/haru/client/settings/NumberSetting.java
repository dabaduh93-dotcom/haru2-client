package com.haru.client.settings;

public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, clamp(defaultValue, min, max));
        if (min > max || step <= 0) throw new IllegalArgumentException("Invalid number setting bounds");
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public void increase() { setValue(Math.min(max, getValue() + step)); }
    public void decrease() { setValue(Math.max(min, getValue() - step)); }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}