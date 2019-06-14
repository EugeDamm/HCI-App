package devices;

import ar.edu.itba.hci.smarthomesystem.Device;

public class Ac extends DeviceType{
    private String status, mode, verticalSwing, horizontalSwing, fanSpeed, typeId;
    private int temperature;

    public Ac(String status, int temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed, String typeID) {
        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
        this.typeId = typeID;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public String getMode() {
        return mode;
    }

    public String getTypeId() {
        return typeId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getFanSpeed() {
        return fanSpeed;
    }

    @Override
    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    @Override
    public String getVerticalSwing() {
        return verticalSwing;
    }

}
