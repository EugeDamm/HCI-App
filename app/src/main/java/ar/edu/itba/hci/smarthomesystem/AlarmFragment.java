package ar.edu.itba.hci.smarthomesystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment{
    Button changeAlarmPassword;
    View view;
    View view2;
    private String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    Bundle bundle = new Bundle();
    private final HashMap<String, String> modes = new HashMap<>();
    private Alarm alarm;
    private boolean alarmCreated = false;
    private boolean activated = false;


    public AlarmFragment() {
        // Required empty public constructor
    }

    private void init() {
        modes.put("armStay", getResources().getString(R.string.home_mode_activated));
        modes.put("armAway",  getResources().getString(R.string.out_home_mode_activated));
        modes.put("disarm",  getResources().getString(R.string.disarm_mode));
        bundle = getArguments();
        Log.d("TUVIEJA", "init: " + bundle);
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
        final Switch homeModeSwitch = view.findViewById(R.id.homeModeSwitch);
        final  Switch outHomeModeSwitch = view.findViewById(R.id.outHomeModeSwitch);

        Api.getInstance(getContext()).getDeviceState(new Response.Listener<State>() {
            @Override
            public void onResponse(State response) {
                if(response.getStatus().equals("disarm")) {
                    turnOnOff(R.id.homeModeSwitch, R.id.outHomeModeSwitch, view, true);
                }else if(response.getStatus().equals("armStay")) {
                    turnOnOff(R.id.homeModeSwitch, R.id.outHomeModeSwitch, view, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, alarm.getId());

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
//                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

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
            title = getResources().getString(R.string.disarm_mode);
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
                               Toast.makeText(getContext(), R.string.old_code_wrong, Toast.LENGTH_LONG).show();
                           }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorHandler.handleError(error, getActivity());
                        }
                    });
                    dialog.dismiss();
            }
        });

        popUp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activated = false;
            }
        });
        popUp.show();
    }

    public void turnOnOff(int id1, int id2, View view, boolean config) {
        Switch mode1 = view.findViewById(id1);
        Switch mode2 = view.findViewById(id2);
        mode1.setChecked(config);
        mode2.setChecked(!config);
        changeAlarmPassword.setClickable(false);
    }
}
