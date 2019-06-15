package ar.edu.itba.hci.smarthomesystem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;

import api.Api;
import devices.Alarm;
import devices.DeviceType;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment{
    Button changeAlarmPassword;
    View view;
    Bundle bundle = new Bundle();
    private final HashMap<String, String> modes = new HashMap<>();
    private Alarm alarm;
    private String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    Switch homeModeSwitch = null;
    Switch outHomeModeSwitch = null;


    public AlarmFragment() {
        // Required empty public constructor
    }

    private void init() {
        modes.put("armStay", getResources().getString(R.string.home_mode_activated));
        modes.put("armAway",  getResources().getString(R.string.out_home_mode_activated));
        modes.put("disarm",  getResources().getString(R.string.disarm_mode));
        bundle = getArguments();
        if(bundle != null) {
            alarm = (Alarm) bundle.getSerializable("alarm");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        init();
        view = inflater.inflate(R.layout.fragment_alarm, container , false);
        changeAlarmPassword = view.findViewById(R.id.changePassword);
        homeModeSwitch = view.findViewById(R.id.homeModeSwitch);
        outHomeModeSwitch = view.findViewById(R.id.outHomeModeSwitch);
        Api.getInstance(getContext()).getDeviceState(new Response.Listener<State>() {
            @Override
            public void onResponse(State response) {
                response.setDeviceType(ALARM_TYPE_ID);
                final DeviceType alarm = response.getCreatedDevice();
                if(alarm.getStatus().equals("armedStay")) {
                    homeModeSwitch.setChecked(true);
                    outHomeModeSwitch.setClickable(false);
                }else if(alarm.getStatus().equals("armedAway")) {
                    outHomeModeSwitch.setChecked(true);
                    homeModeSwitch.setClickable(false);
                }else {
                    outHomeModeSwitch.setChecked(false);
                    outHomeModeSwitch.setClickable(true);
                    homeModeSwitch.setChecked(false);
                    homeModeSwitch.setClickable(true);
                }

                homeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String mode;
                        if(isChecked){
                            mode = "armStay";
                        }else {
                            mode = "disarm";
                        }

                        pickUpDialog(mode);
                    }
                });

                outHomeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String mode;
                        if(isChecked){
                            mode = "armAway";
                        }else {
                            mode = "disarm";
                        }
                        pickUpDialog(mode);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, alarm.getId());

        changeAlarmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAlarmCode fragment = new ChangeAlarmCode();
                Bundle b = new Bundle();
                b.putSerializable("alarm", alarm);
                fragment.setArguments(b);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        // view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        return view;
    }

    public void pickUpDialog(final String mode) {
        final AlertDialog.Builder popUp = new AlertDialog.Builder(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText et = new EditText(getActivity());
        et.setLayoutParams(lp);
        String title;
        if(mode.equals("armStay")) {
            title = getResources().getString(R.string.home_mode);
        } else if(mode.equals("armAway")) {
            title = getResources().getString(R.string.out_home_mode);
        } else {
            title = getResources().getString(R.string.disactivate_mode);
        }
        popUp.setTitle(title);
        popUp.setMessage(getResources().getString(R.string.pop_message));
        popUp.setView(et);
        popUp.setPositiveButton(R.string.save_changes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = et.getText().toString();
                ArrayList<String> params = new ArrayList<>();
                params.add(code);
                Api.getInstance(getContext()).activateMode(alarm.getId(), params, mode, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if(response) {
                            Toast.makeText(getContext(), modes.get(mode), Toast.LENGTH_LONG).show();
                        } else {
                                Toast.makeText(getContext(), R.string.wrong_code, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorHandler.handleError(error, getActivity());
                    }
                });
            }
        });

        popUp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                homeModeSwitch.setChecked(false);
                dialog.dismiss();
            }
        });
        popUp.show();
    }
}
