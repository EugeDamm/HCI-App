package devices;

public class Alarm extends DeviceType {
    private String id;
    private String state;
    private String typeId;
    private String name;
    private final int codeLength = 4;

    public Alarm(String name, String id, String typeId) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;

//        this.typeId = typeId;
    }


    public String getId() {
        return id;
    }

//    @Override
//    public String getTypeId() {
//        return typeId;
//    }


    public void setId(String id) {
        this.id = id;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getTypeId() {
        return this.typeId;
    }

    public String getName() { return this.name; }
}
