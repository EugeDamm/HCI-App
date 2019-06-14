package devices;

public class Timer extends DeviceType {

    private String status, typeId;
    private int interval, remaining;

    public Timer(String status, int interval, int remaining, String typeId) {
        this.status = status;
        this.interval = interval;
        this.remaining = remaining;
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
    public int getInterval() {
        return interval;
    }

    @Override
    public int getRemaining() {
        return remaining;
    }
}
