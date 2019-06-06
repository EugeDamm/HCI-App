package ar.edu.itba.hci.smarthomesystem;

public class State {
    private String status, color, heat, grill, convection, mode, verticalSwing, horizontalSwing, fanSpeed, lock;
    private int level, brightness, temperature, interval, remaining;

    // blinds constructor
    public State(String status, int level) {
        this.status = status;
        this.level  = level;
    }

    // lamp constructor
    public State(String status, String color, int brightness) {
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    // oven constructor
    public State(String status, int temperature, String heat, String grill, String convection) {
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
    }

    // ac constructor
    public State(String status, int temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed) {
        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
    }

    // door constructor
    public State(String status, String lock) {
        this.status = status;
        this.lock = lock;
    }

    // timer constructor
    public State(String status, int interval, int remaining) {
        this.status = status;
        this.interval = interval;
        this.remaining = remaining;
    }

    public int getBrightness() {
        return brightness;
    }

    public int getInterval() {
        return interval;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getColor() {
        return color;
    }

    public String getConvection() {
        return convection;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public String getGrill() {
        return grill;
    }

    public String getHeat() {
        return heat;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public String getLock() {
        return lock;
    }

    public String getMode() {
        return mode;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }

    public int getLevel() {
        return level;
    }

    public String getStatus() {
        return status;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s - level: %d", this.status, this.level);
    }
}