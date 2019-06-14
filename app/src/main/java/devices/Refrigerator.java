package devices;

import android.util.Log;

import java.io.Serializable;

public class Refrigerator extends DeviceType implements Serializable {

    private int freezerTemperature, temperature;
    private String mode, typeId;

    public Refrigerator(int freezerTemperature, int temperature, String mode, String typeId) {
        Log.d("onResponseRef", "Refrigerator");
        this.freezerTemperature = freezerTemperature;
        this.temperature = temperature;
        this.mode = mode;
        this.typeId = typeId;
    }

    @Override
    public String getStatus() {
        return "Fridge is on";
    }

    public String getTypeId() {
        return typeId;
    }

    public int getFreezerTemperature() {
        return freezerTemperature;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public String getMode() {
        return mode;
    }
}
