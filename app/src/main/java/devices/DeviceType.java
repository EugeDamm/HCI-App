package devices;

import java.io.Serializable;

public abstract class DeviceType implements Serializable {
    private String status, color, heat, grill, convection, mode, verticalSwing, horizontalSwing, fanSpeed, lock, typeId;
    private int level, brightness, temperature, interval, remaining, freezerTemperature;

    public String getTypeId() {
        return typeId;
    }

    public int getBrightness() {
        return brightness;
    }

    public int getInterval() {
        return interval;
    }

    public int getFreezerTemperature() {
        return freezerTemperature;
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
}
