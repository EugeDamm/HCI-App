package devices;

public class Blinds extends DeviceType{

    private String status, typeId;
    private int level;

    public Blinds(String status, int level, String typeId) {
        this.status = status;
        this.level = level;
        this.typeId = typeId;
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
    public int getLevel() {
        return level;
    }
}
