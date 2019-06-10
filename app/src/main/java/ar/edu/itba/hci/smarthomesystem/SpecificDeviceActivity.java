package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import api.Api;
import api.Error;
import devices.DeviceType;
import yuku.ambilwarna.AmbilWarnaDialog;

public class SpecificDeviceActivity extends AppCompatActivity {

    private final String BLINDS_TYPE_ID = "eu0v2xgprrhhg41g";
    private final String LAMP_TYPE_ID = "go46xmbqeomjrsjr";
    private final String OVEN_TYPE_ID = "im77xxyulpegfmv8";
    private final String AC_TYPE_ID = "li6cbv5sdlatti0j";
    private final String DOOR_TYPE_ID = "lsf78ly0eqrjbz91";
    private final String TIMER_TYPE_ID = "ofglvd9gqX8yfl3l";
    private final String REFRIGERATOR_TYPE_ID = "rnizejqr2di0okho";
    private final String TAG = "SpecificDeviceActivity";
    private Room room;
    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        //setContentView(R.layout.specific_device);
        Intent currentIntent = getIntent();
        Bundle currentBundle = currentIntent.getExtras();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(currentBundle != null) {
            room = (Room) currentBundle.get("go_back");
            String deviceName = currentBundle.getString("device_name");
            final String deviceId = currentBundle.getString("device_id");
            final String deviceTypeId = currentBundle.getString("device_type_id");
            actionBar.setTitle(deviceName);
            Api.getInstance(this).getDeviceState(new Response.Listener<State>() {
                @Override
                public void onResponse(State response) {
                    response.setDeviceType(deviceTypeId);
                    DeviceType deviceType = response.getCreatedDevice();
                    createLayoutForDevice(deviceType);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            }, deviceId);
        }
    }

    private void createLayoutForDevice(DeviceType device) {
        String typeId = device.getTypeId();
        switch (typeId) {
            case BLINDS_TYPE_ID:
                createBlindsLayout(device);
                break;
            case LAMP_TYPE_ID:
                createLampLayout(device);
                break;
            case DOOR_TYPE_ID:
                createDoorLayout(device);
                break;
            case REFRIGERATOR_TYPE_ID:
                createRefrigeratorLayout(device);
                break;
            case AC_TYPE_ID:
                createAcLayout(device);
                break;
            case TIMER_TYPE_ID:
                createTimerLayout(device);
                break;
            case OVEN_TYPE_ID:
                createOvenLayout(device);
                break;
        }
    }

    private void createBlindsLayout(DeviceType device) {
        setContentView(R.layout.blinds_layout);
        Log.d(TAG, "createBlindsLayout: " + device);
        TextView statusText = findViewById(R.id.status);
        String status = device.getStatus();
        statusText.setText(status);
        Switch switchButton = findViewById(R.id.switchBlinds);
        if(status.equals("opened") || status.equals("opening"))
            switchButton.setChecked(true);
        else
            switchButton.setChecked(false);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView progressBarText = findViewById(R.id.levelValue);
        progressBar.setProgress(device.getLevel());
        String progressBarTextConcatenated = device.getLevel() + "%";
        progressBarText.setText(progressBarTextConcatenated);
    }

    private void createDoorLayout(DeviceType device) {
        setContentView(R.layout.door_layout);
        String status = device.getStatus();
        String lock = device.getLock();
        TextView statusText = findViewById(R.id.status);
        TextView lockText = findViewById(R.id.lockStatus);
        Switch statusSwitch = findViewById(R.id.switchDoorState);
        Switch lockSwitch = findViewById(R.id.switchDoorLock);
        statusText.setText(status);
        lockText.setText(lock);
        if(lock.equals("locked"))
            lockSwitch.setChecked(true);
        else
            lockSwitch.setChecked(false);
        if(status.equals("opened"))
            statusSwitch.setChecked(true);
        else
            statusSwitch.setChecked(false);
    }

    private void createLampLayout(DeviceType device) {
        setContentView(R.layout.lamp_layout);
        String color = "#" + device.getColor();
        TextView statusText = findViewById(R.id.statusText);
        TextView brightnessValue = findViewById(R.id.brightnessValue);
        String status = device.getStatus();
        statusText.setText(status);
        Switch lampSwitch = findViewById(R.id.lampSwitch);
        if(status.equals("on"))
            lampSwitch.setChecked(true);
        else
            lampSwitch.setChecked(false);
        Button colorButton = findViewById(R.id.colorButton);
        colorButton.setBackgroundColor(Color.parseColor(color));
        ProgressBar brightnessBar = findViewById(R.id.progressBarBrightness);
        final int brightness = device.getBrightness();
        brightnessBar.setProgress(brightness);
        String brightnessString = brightness + "%";
        brightnessValue.setText(brightnessString);
        /*colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorGradle(color);
            }
        });*/
    }

    private void openColorGradle(String color) {
        final int hexaColor = Color.parseColor(color);
        AmbilWarnaDialog colorGradle = new AmbilWarnaDialog(this, hexaColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) { }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

            }
        });
        colorGradle.show();
    }

    private void createTimerLayout(DeviceType device) {
        setContentView(R.layout.timer_layout);
        String status = device.getStatus();
        int intervalTime = device.getInterval();
        int remainingTime = device.getRemaining();
        int remainingPercentage = (int)(100 * ((double) remainingTime / intervalTime ));
        Log.d(TAG, "createTimerLayout: " + remainingPercentage);
        String intervalString = intervalTime + " secs.";
        String remainingString = remainingTime + " secs.";
        TextView statusText = findViewById(R.id.statusText);
        TextView intervalText = findViewById(R.id.intervalText);
        TextView remainingText = findViewById(R.id.remainingTimeText);
        ProgressBar progressBar = findViewById(R.id.remainingProgressBar);
        Switch timerSwitch = findViewById(R.id.timerSwitch);
        statusText.setText(status);
        intervalText.setText(intervalString);
        remainingText.setText(remainingString);
        progressBar.setProgress(remainingPercentage);
        if(status.equals("active"))
            timerSwitch.setChecked(true);
        else
            timerSwitch.setChecked(false);
    }

    private void createRefrigeratorLayout(DeviceType device) {
        setContentView(R.layout.refrigerator_layout);
        TextView modeText = findViewById(R.id.modeText);
        TextView freezerTempText = findViewById(R.id.freezerTemperatureText);
        TextView temperatureText = findViewById(R.id.temperatureText);
        String mode = device.getMode();
        int freezerTemp = device.getFreezerTemperature();
        int temperature = device.getTemperature();
        String freezerTempString = freezerTemp + "°C";
        String temperatureString = temperature + "°C";
        modeText.setText(mode);
        freezerTempText.setText(freezerTempString);
        temperatureText.setText(temperatureString);
    }

    private void createAcLayout(DeviceType device) {
        setContentView(R.layout.ac_layout);
        TextView statusText = findViewById(R.id.statusText);
        TextView temperatureText = findViewById(R.id.temperatureText);
        TextView modeText = findViewById(R.id.modeText);
        TextView verticalSwingText = findViewById(R.id.verticalSwingText);
        TextView horizontalSwingText = findViewById(R.id.horizontalSwingText);
        TextView fanSpeedText = findViewById(R.id.fanSpeedText);
        Switch acSwitch = findViewById(R.id.acSwitch);
        String status = device.getStatus();
        String mode = device.getMode();
        String verticalSwing = device.getVerticalSwing();
        String horizontalSwing = device.getHorizontalSwing();
        String fanSpeed = device.getFanSpeed();
        int temperature = device.getTemperature();
        String temperatureString = temperature + "°C";
        String verticalSwingString = verticalSwing.equals("auto") ? verticalSwing : verticalSwing + "°";
        String horizontalSwingString = horizontalSwing.equals("auto") ? horizontalSwing : horizontalSwing + "°";
        statusText.setText(status);
        temperatureText.setText(temperatureString);
        horizontalSwingText.setText(horizontalSwingString);
        verticalSwingText.setText(verticalSwingString);
        modeText.setText(mode);
        fanSpeedText.setText(fanSpeed);
        if(status.equals("on"))
            acSwitch.setChecked(true);
        else
            acSwitch.setChecked(false);

    }

    private void createOvenLayout(DeviceType device) {
        setContentView(R.layout.oven_layout);
        TextView statusText = findViewById(R.id.statusText);
        TextView temperatureText = findViewById(R.id.temperatureText);
        TextView heatText = findViewById(R.id.heatText);
        TextView grillText = findViewById(R.id.grillText);
        TextView convectionText = findViewById(R.id.convectionText);
        Switch ovenSwitch = findViewById(R.id.ovenSwitch);
        String status = device.getStatus();
        int temperature = device.getTemperature();
        String heat = device.getHeat();
        String grill = device.getGrill();
        String convection = device.getConvection();
        String temperatureString = temperature + "°C";
        statusText.setText(status);
        temperatureText.setText(temperatureString);
        heatText.setText(heat);
        grillText.setText(grill);
        convectionText.setText(convection);
        if(status.equals("on"))
            ovenSwitch.setChecked(true);
        else
            ovenSwitch.setChecked(false);
    }



    private void handleError(VolleyError error) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }
        Log.e(TAG, error.toString());
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(SpecificDeviceActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, SpecificRoomActivity.class);
                intent.putExtra("room_name", room);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
