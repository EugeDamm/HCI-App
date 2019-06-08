package devices;

public class Oven extends DeviceType {

    private String status, heat, grill, convection, typeId;
    private int temperature;

    public Oven(String status, int temperature, String heat, String grill, String convection, String typeId) {
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
        this.typeId = typeId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public String getTypeId() {
        return typeId;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public String getConvection() {
        return convection;
    }

    @Override
    public String getGrill() {
        return grill;
    }

    @Override
    public String getHeat() {
        return heat;
    }
}
