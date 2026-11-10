package com.haru.client.settings;

public class NumberSetting extends Setting<Double> {
    private final double minimum;
    private final double maximum;
    private final double increment;

    public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
        super(name, clamp(value, minimum, maximum));
        if (minimum > maximum) throw new IllegalArgumentException("minimum cannot exceed maximum");
        if (increment <= 0) throw new IllegalArgumentException("increment must be positive");
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    @Override
    public void setValue(Double value) {
        super.setValue(clamp(value, minimum, maximum));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public double getMinimum() { return minimum; }
    public double getMaximum() { return maximum; }
    public double getIncrement() { return increment; }
}
