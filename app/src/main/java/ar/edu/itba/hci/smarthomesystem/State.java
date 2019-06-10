package ar.edu.itba.hci.smarthomesystem;

import android.util.Log;

import java.io.Serializable;

import devices.Ac;
import devices.Blinds;
import devices.DeviceType;
import devices.Door;
import devices.Lamp;
import devices.Oven;
import devices.Refrigerator;
import devices.Timer;

public class State implements Serializable {
    private final String TAG = "State";
    private final String BLINDS_TYPE_ID = "eu0v2xgprrhhg41g";
    private final String LAMP_TYPE_ID = "go46xmbqeomjrsjr";
    private final String OVEN_TYPE_ID = "im77xxyulpegfmv8";
    private final String AC_TYPE_ID = "li6cbv5sdlatti0j";
    private final String DOOR_TYPE_ID = "lsf78ly0eqrjbz91";
    private final String TIMER_TYPE_ID = "ofglvd9gqX8yfl3l";
    private final String REFRIGERATOR_TYPE_ID = "rnizejqr2di0okho";

    private DeviceType createdDevice;
    private String mode, status, color, heat, grill, convection, verticalSwing, horizontalSwing, fanSpeed, lock;
    private int freezerTemperature, temperature, level, brightness, interval, remaining;

    public void setDeviceType(String typeId) {
        if(typeId.equals(LAMP_TYPE_ID)) {
            this.createdDevice = new Lamp(status, color, brightness, LAMP_TYPE_ID);
        }else if(typeId.equals(DOOR_TYPE_ID)) {
            this.createdDevice = new Door(status, lock, DOOR_TYPE_ID);
        } else if(typeId.equals(REFRIGERATOR_TYPE_ID)) {
            this.createdDevice = new Refrigerator(freezerTemperature, temperature, mode, REFRIGERATOR_TYPE_ID);
        }else if(typeId.equals(AC_TYPE_ID)) {
            this.createdDevice = new Ac(status, temperature, mode, verticalSwing, horizontalSwing, fanSpeed, AC_TYPE_ID);
        } else if(typeId.equals(BLINDS_TYPE_ID)) {
            this.createdDevice = new Blinds(status, level, BLINDS_TYPE_ID);
        }else if(typeId.equals(OVEN_TYPE_ID)) {
            this.createdDevice = new Oven(status, temperature, heat, grill, convection, OVEN_TYPE_ID);
        } else
            this.createdDevice = new Timer(status, interval, remaining, TIMER_TYPE_ID);
    }

    public DeviceType getCreatedDevice() {
        return createdDevice;
    }
}