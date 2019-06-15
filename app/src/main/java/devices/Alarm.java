package devices;

public class Alarm extends DeviceType {
    private String mode;
    private String typeId;
    private String status;
    private String name;
    private String id;
    private final String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    private final int codeLength = 4;

    public Alarm(String status, String mode, String id) {
        this.status = status;
        this.mode = mode;
        this.id = id;
    }

    public Alarm(String name, String id) {
        this.name = name;
        this.id = id;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String state) {
        this.status = state;
    }
    public String getTypeId() {
        return ALARM_TYPE_ID;
    }

    public String getName() { return this.name; }
}
