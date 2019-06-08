package devices;

public class Lamp extends DeviceType {

    private String status, color, typeId;
    private int brightness;

    public Lamp(String status, String color, int brightness, String typeId) {
        this.status = status;
        this.color = color;
        this.brightness = brightness;
        this.typeId = typeId;
    }

    @Override
    public int getBrightness() {
        return brightness;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public String getTypeId() {
        return typeId;
    }
}
