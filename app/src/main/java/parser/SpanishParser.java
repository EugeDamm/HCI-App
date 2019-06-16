package parser;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import api.Api;
import ar.edu.itba.hci.smarthomesystem.ErrorHandler;
import ar.edu.itba.hci.smarthomesystem.SpecificDeviceActivity;
import devices.Ac;
import devices.Blinds;
import devices.DeviceType;
import devices.Door;
import devices.Lamp;
import devices.Oven;
import devices.Timer;

public class SpanishParser implements Parser{

    private DeviceType deviceType;
    private String deviceId;
    private Context context;
    private Activity activity;

    public SpanishParser(DeviceType deviceType, String deviceId, Context context, Activity activity) {
        this.context = context;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.activity = activity;
    }

    @Override
    public String doAction(String input) {
        String data = null;
        if(input != null) {
            if (deviceType != null) {
                data = getData(input);
                if(data != null) {
                    Api.getInstance(context).doAction(new Response.Listener<Boolean>() {
                        @Override
                        public void onResponse(Boolean response) { }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorHandler.handleError(error, activity);
                        }
                    }, deviceId, data);
                }
            } else {
                // TODO Aca iria la logica de las rutinas
            }
        } else {
            Toast toast = Toast.makeText(context, "Invalid Voice Action", Toast.LENGTH_SHORT);
            toast.show();
        }
        return data;
    }

    @Override
    public String getData(String input) {
        String data;
        if(input.startsWith("prender") || input.startsWith("apagar")) {
            if(deviceType.getClass() == Lamp.class || deviceType.getClass() == Oven.class || deviceType.getClass() == Ac.class) {
                String[] splitted = input.split(" ");
                data = splitted[0].equals("prender") ? "turnOn" : "turnOff";
                return data;
            } else {
                return null;
            }
        } else if(input.startsWith("abrir") || input.startsWith("cerrar")) {
            if(deviceType.getClass() == Blinds.class || deviceType.getClass() == Door.class) {
                String[] splitted = input.split(" ");
                data = splitted[0].equals("abrir") ? "open" : "close";
                return data;
            } else {
                return null;
            }
        } else if(input.startsWith("trabar") || input.startsWith("destrabar")) {
            if(deviceType.getClass() == Door.class) {
                String[] splitted = input.split(" ");
                data = splitted[0].equals("trabar") ? "lock" : "unlock";
                return data;
            } else {
                return null;
            }
        } else if(input.startsWith("empezar") || input.startsWith("parar") || input.startsWith("frenar")) {
            if (deviceType.getClass() == Timer.class) {
                String[] splitted = input.split(" ");
                data = splitted[0].equals("empezar") ? "start" : "stop";
                return data;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
