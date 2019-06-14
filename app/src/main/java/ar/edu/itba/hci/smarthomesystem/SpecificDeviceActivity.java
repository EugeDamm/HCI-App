package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
                    createLayoutForDevice(deviceType, deviceId);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            }, deviceId);
        }
    }

    private void createLayoutForDevice(DeviceType device, String deviceId) {
        String typeId = device.getTypeId();
        switch (typeId) {
            case BLINDS_TYPE_ID:
                createBlindsLayout(device, deviceId);
                break;
            case LAMP_TYPE_ID:
                createLampLayout(device, deviceId);
                break;
            case DOOR_TYPE_ID:
                createDoorLayout(device, deviceId);
                break;
            case REFRIGERATOR_TYPE_ID:
                createRefrigeratorLayout(device, deviceId);
                break;
            case AC_TYPE_ID:
                createAcLayout(device, deviceId);
                break;
            case TIMER_TYPE_ID:
                createTimerLayout(device, deviceId);
                break;
            case OVEN_TYPE_ID:
                createOvenLayout(device, deviceId);
                break;
        }
    }

    private void createBlindsLayout(final DeviceType device, final String deviceId) {
        final String deviceIdInner = deviceId;
        setContentView(R.layout.blinds_layout);
        Log.d(TAG, "createBlindsLayout: " + device);
        final TextView statusText = findViewById(R.id.status);
        final String status = device.getStatus();
        statusText.setText(status);
        final Switch switchButton = findViewById(R.id.switchBlinds);
        if(status.equals("opened") || status.equals("opening"))
            switchButton.setChecked(true);
        else
            switchButton.setChecked(false);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView progressBarText = findViewById(R.id.levelValue);
        progressBar.setProgress(device.getLevel());
        String progressBarTextConcatenated = device.getLevel() + "%";
        progressBarText.setText(progressBarTextConcatenated);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("opened")) {
                            statusText.setText("closing");
                            switchButton.setChecked(false);
                        } else {
                            statusText.setText("opening");
                            switchButton.setChecked(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, statusText.getText().toString());
            }
        });
    }

    private void createDoorLayout(DeviceType device, String deviceId) {
        final String deviceIdInner = deviceId;
        setContentView(R.layout.door_layout);
        final String status = device.getStatus();
        final String lock = device.getLock();
        final TextView statusText = findViewById(R.id.status);
        final TextView lockText = findViewById(R.id.lockStatus);
        final Switch statusSwitch = findViewById(R.id.switchDoorState);
        final Switch lockSwitch = findViewById(R.id.switchDoorLock);
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
        lockSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(lockText.getText().equals("locked")) {
                            lockText.setText("unlocked");
                            lockSwitch.setChecked(false);
                        } else {
                            lockSwitch.setChecked(true);
                            lockText.setText("locked");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, lockText.getText().toString());
            }
        });
        statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("opened")) {
                            statusSwitch.setChecked(false);
                            statusText.setText("closed");
                        } else {
                            statusText.setText("opened");
                            statusSwitch.setChecked(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, statusText.getText().toString());
            }
        });
    }

    private void createLampLayout(DeviceType device, String deviceId) {
        final String deviceIdInner = deviceId;
        setContentView(R.layout.lamp_layout);
        final String color;
        if(device.getColor().startsWith("#"))
            color = device.getColor();
        else
            color = "#" + device.getColor();
        Log.d(TAG, "createLampLayout: COLOR " + color);
        final String colorAux = color;
        final TextView statusText = findViewById(R.id.statusText);
        final TextView brightnessValue = findViewById(R.id.brightnessValue);
        final String status = device.getStatus();
        statusText.setText(status);
        final Switch lampSwitch = findViewById(R.id.lampSwitch);
        if(status.equals("on"))
            lampSwitch.setChecked(true);
        else
            lampSwitch.setChecked(false);
        final Button colorButton = findViewById(R.id.colorButton);
        colorButton.setBackgroundColor(Color.parseColor(color));
        SeekBar brightnessBar = findViewById(R.id.progressBarBrightness);
        final int brightness = device.getBrightness();
        brightnessBar.setProgress(brightness);
        final String brightnessString = brightnessBar.getProgress() + "%";
        brightnessValue.setText(brightnessString);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorGradle(colorAux, deviceIdInner);
            }
        });
        lampSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("on")) {
                            statusText.setText("off");
                            lampSwitch.setChecked(false);
                        } else {
                            lampSwitch.setChecked(true);
                            statusText.setText("on");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, status);
            }
        });
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String toPrint = progress + "%";
                brightnessValue.setText(toPrint);
                Api.getInstance(context).setBrightness(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void openColorGradle(String color, final String deviceId) {
        final int hexaColor = Color.parseColor(color);
        AmbilWarnaDialog colorGradle = new AmbilWarnaDialog(this, hexaColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) { }

            @Override
            public void onOk(AmbilWarnaDialog dialog, final int color) {
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                Api.getInstance(context).setColor(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                },deviceId, hexColor);
            }
        });
        colorGradle.show();
    }

    private void createTimerLayout(DeviceType device, String deviceId) {
        final String deviceIdInner = deviceId;
        setContentView(R.layout.timer_layout);
        final String status = device.getStatus();
        int intervalTime = device.getInterval();
        int remainingTime = device.getRemaining();
        int remainingPercentage = (int)(100 * ((double) remainingTime / intervalTime ));
        Log.d(TAG, "createTimerLayout: " + remainingPercentage);
        String intervalString = intervalTime + " secs.";
        String remainingString = remainingTime + " secs.";
        final TextView statusText = findViewById(R.id.statusText);
        TextView intervalText = findViewById(R.id.intervalText);
        TextView remainingText = findViewById(R.id.remainingTimeText);
        ProgressBar progressBar = findViewById(R.id.remainingProgressBar);
        final Switch timerSwitch = findViewById(R.id.timerSwitch);
        statusText.setText(status);
        intervalText.setText(intervalString);
        remainingText.setText(remainingString);
        progressBar.setProgress(remainingPercentage);
        if(status.equals("active"))
            timerSwitch.setChecked(true);
        else
            timerSwitch.setChecked(false);
        timerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("active")) {
                            statusText.setText("inactive");
                            timerSwitch.setChecked(false);
                        } else {
                            statusText.setText("active");
                            timerSwitch.setChecked(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, statusText.getText().toString());
            }
        });
    }

    private void createRefrigeratorLayout(DeviceType device, String deviceId) {
        final String innerDeviceId = deviceId;
        setContentView(R.layout.refrigerator_layout);
        final TextView modeText = findViewById(R.id.modeText);
        final TextView freezerTempText = findViewById(R.id.freezerTemperatureText);
        final TextView temperatureText = findViewById(R.id.temperatureText);
        ImageButton modeButton = findViewById(R.id.modeButton);
        ImageButton temperatureButton = findViewById(R.id.temperatureButton);
        ImageButton ftemperatureButton = findViewById(R.id.fTemperatureButton);
        final String[] modes = {"default","vacation","party"};
        String mode = device.getMode();
        int freezerTemp = device.getFreezerTemperature();
        int temperature = device.getTemperature();
        final String freezerTempString = freezerTemp + "°C";
        final String temperatureString = temperature + "°C";
        modeText.setText(mode);
        freezerTempText.setText(freezerTempString);
        temperatureText.setText(temperatureString);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_mode)
                        .setItems(modes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int index = which;
                                Log.d(TAG, "onClick: " + modes[which]);
                                Api.getInstance(context).setMode(new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        modeText.setText(modes[index]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        handleError(error);
                                    }
                                },innerDeviceId, modes[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMaxValue(8);
                numberPicker.setMinValue(2);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_temperature);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setTemperature(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String newString = numberPicker.getValue() + "ºC";
                                temperatureText.setText(newString);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, innerDeviceId, numberPicker.getValue());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        ftemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                String[] negativeValues = new String[13];
                int first = -20;
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(12);
                for(int i = 0 ; i < negativeValues.length ; i++) {
                    negativeValues[i] = String.valueOf(first);
                    Log.d(TAG, "onClick: " + negativeValues[i]);
                    first++;
                }
                final String[] negVals = negativeValues;
                numberPicker.setDisplayedValues(negativeValues);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_ftemperature);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setFreezerTemperature(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String newString = Integer.valueOf(negVals[numberPicker.getValue()]) + "ºC";
                                freezerTempText.setText(newString);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, innerDeviceId, Integer.valueOf(negVals[numberPicker.getValue()]));
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });

    }

    private void createAcLayout(DeviceType device, final String deviceId) {
        final String deviceIdInner = deviceId;
        final String[] modes = {"cool", "heat", "fan"};
        setContentView(R.layout.ac_layout);
        final TextView statusText = findViewById(R.id.statusText);
        final TextView temperatureText = findViewById(R.id.temperatureText);
        final TextView modeText = findViewById(R.id.modeText);
        final TextView verticalSwingText = findViewById(R.id.verticalSwingText);
        final TextView horizontalSwingText = findViewById(R.id.horizontalSwingText);
        final TextView fanSpeedText = findViewById(R.id.fanSpeedText);
        final Switch acSwitch = findViewById(R.id.acSwitch);
        ImageButton temperatureButton = findViewById(R.id.temperatureButton);
        ImageButton vSwingButton = findViewById(R.id.verticalSwingButton);
        ImageButton hSwingButton = findViewById(R.id.horizontalSwingButton);
        ImageButton fanSpeedButton = findViewById(R.id.fanSpeedButton);
        ImageButton modeButton = findViewById(R.id.modeButton);
        final String status = device.getStatus();
        String mode = device.getMode();
        String verticalSwing = device.getVerticalSwing();
        String horizontalSwing = device.getHorizontalSwing();
        String fanSpeed = device.getFanSpeed();
        int temperature = device.getTemperature();
        final String temperatureString = temperature + "°C";
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
        acSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("on")) {
                            statusText.setText("off");
                            acSwitch.setChecked(false);
                        } else {
                            statusText.setText("on");
                            acSwitch.setChecked(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, statusText.getText().toString());
            }
        });
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_mode)
                        .setItems(modes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int index = which;
                                Api.getInstance(context).setMode(new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        modeText.setText(modes[index]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        handleError(error);
                                    }
                                },deviceIdInner, modes[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMaxValue(38);
                numberPicker.setMinValue(18);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_temperature);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + dialog);
                        Api.getInstance(context).setTemperature(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String newString = numberPicker.getValue() + "ºC";
                                temperatureText.setText(newString);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, deviceIdInner, numberPicker.getValue());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create();
                builder.show();
            }
        });
        vSwingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(4);
                final String[] swings = {"auto", "22", "45", "67", "90"};
                numberPicker.setDisplayedValues(swings);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_vertical_swing);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setVerticalSwing(new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String newString = (swings[numberPicker.getValue()]).equals("auto") ? swings[numberPicker.getValue()] : swings[numberPicker.getValue()] + "°";
                                    verticalSwingText.setText(newString);
                                }
                            }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, deviceIdInner, swings[numberPicker.getValue()]);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create();
                builder.show();
            }
        });
        hSwingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(5);
                final String[] swings = {"auto", "-90", "-45", "0", "45", "90"};
                numberPicker.setDisplayedValues(swings);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_horizontal_swing);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setHorizontalSwing(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String newString = (swings[numberPicker.getValue()]).equals("auto") ? swings[numberPicker.getValue()] : swings[numberPicker.getValue()] + "°";
                                horizontalSwingText.setText(newString);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, deviceIdInner, swings[numberPicker.getValue()]);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create();
                builder.show();
            }
        });
        fanSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(4);
                final String[] speeds = {"auto", "25", "50", "75", "100"};
                numberPicker.setDisplayedValues(speeds);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_fan_speed);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setFanSpeed(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String newString = speeds[numberPicker.getValue()];
                                fanSpeedText.setText(newString);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, deviceIdInner, speeds[numberPicker.getValue()]);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create();
                builder.show();
            }
        });
    }

    private void createOvenLayout(final DeviceType device, String deviceId) {
        final String deviceIdInner = deviceId;
        setContentView(R.layout.oven_layout);
        final TextView statusText = findViewById(R.id.statusText);
        TextView temperatureText = findViewById(R.id.temperatureText);
        TextView heatText = findViewById(R.id.heatText);
        TextView grillText = findViewById(R.id.grillText);
        TextView convectionText = findViewById(R.id.convectionText);
        final Switch ovenSwitch = findViewById(R.id.ovenSwitch);
        ImageButton temperatureButton = findViewById(R.id.temperatureButton);
        ImageButton heatButton = findViewById(R.id.heatButton);
        ImageButton grillButton = findViewById(R.id.grillButton);
        ImageButton convectionButton = findViewById(R.id.convectionButton);
        final String status = device.getStatus();
        int temperature = device.getTemperature();
        String heat = device.getHeat();
        String grill = device.getGrill();
        String convection = device.getConvection();
        String temperatureString = temperature + "°C";
        final String[] grills = {"large", "eco", "off"};
        final String[] heats = {"conventional", "bottom", "top"};
        final String[] convections = {"normal", "eco", "off"};
        statusText.setText(status);
        temperatureText.setText(temperatureString);
        heatText.setText(heat);
        grillText.setText(grill);
        convectionText.setText(convection);
        if(status.equals("on"))
            ovenSwitch.setChecked(true);
        else
            ovenSwitch.setChecked(false);
        ovenSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).toggleDevice(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(statusText.getText().equals("on")) {
                            statusText.setText("off");
                            ovenSwitch.setChecked(false);
                        }
                        else {
                            statusText.setText("on");
                            ovenSwitch.setChecked(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }, deviceIdInner, statusText.getText().toString());
            }
        });
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(SpecificDeviceActivity.this);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(14);
                final String[] temps = new String[15];
                int min = 90;
                for(int i = 0 ; i < temps.length ; i++) {
                    temps[i] = String.valueOf(min);
                    min += 10;
                }
                final String[] tempAux = temps;
                numberPicker.setDisplayedValues(tempAux);
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_temperature);
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getInstance(context).setTemperature(new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                TextView textToChange = findViewById(R.id.temperatureText);
                                String stringToPrint = Integer.valueOf(tempAux[numberPicker.getValue()]) + "ºC";
                                textToChange.setText(stringToPrint);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        }, deviceIdInner, Integer.valueOf(tempAux[numberPicker.getValue()]));
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        grillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_mode)
                        .setItems(grills, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int index = which;
                                Api.getInstance(context).setGrill(new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        TextView textToChange = findViewById(R.id.grillText);
                                        textToChange.setText(grills[index]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        handleError(error);
                                    }
                                },deviceIdInner, grills[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        heatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_mode)
                        .setItems(heats, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int index = which;
                                Api.getInstance(context).setHeat(new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        TextView textToChange = findViewById(R.id.heatText);
                                        textToChange.setText(heats[index]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        handleError(error);
                                    }
                                },deviceIdInner, heats[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        convectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpecificDeviceActivity.this);
                builder.setTitle(R.string.change_mode)
                        .setItems(convections, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int index = which;
                                Api.getInstance(context).setConvection(new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        TextView textToChange = findViewById(R.id.convectionText);
                                        textToChange.setText(convections[index]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        handleError(error);
                                    }
                                },deviceIdInner, convections[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });
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
