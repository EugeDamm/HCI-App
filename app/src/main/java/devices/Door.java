package devices;

public class Door extends DeviceType{

    private String status, lock, typeId;

    public Door(String status, String lock, String typeId) {
        this.status = status;
        this.lock = lock;
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getLock() {
        return lock;
    }
}
