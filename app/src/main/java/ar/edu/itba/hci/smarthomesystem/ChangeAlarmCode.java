package ar.edu.itba.hci.smarthomesystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import api.Api;
import devices.Alarm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAlarmCode extends Fragment {
    View view;
    private devices.Alarm alarm = null;
    public ChangeAlarmCode() {
        // Required empty public constructor
    }

    private void getAlarm(){
        if(getArguments() != null) {
            alarm = (Alarm)getArguments().getSerializable("alarm");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(alarm == null) {
            getAlarm();
        }
        view = inflater.inflate(R.layout.fragment_change_alarm_code, container, false);
        Button saveChanges = view.findViewById(R.id.save_changes);
        saveChanges.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final EditText oldCode = view.findViewById(R.id.oldCode);
                final EditText newCode = view.findViewById(R.id.newCode);
                final EditText confirmCode = view.findViewById(R.id.confirmCode);
                if( validates(oldCode.getText().toString()) && validates(newCode.getText().toString()) && validates(confirmCode.getText().toString())) {
                        ArrayList<String> params = new ArrayList<>();
                        params.add((oldCode.getText().toString()));
                        params.add((newCode.getText().toString()));
                        Api.getInstance(getActivity()).changePassword(alarm.getId(), params, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
                                if(response) {
                                    Toast.makeText(getContext(), R.string.code_changed, Toast.LENGTH_LONG).show();
                                    AlarmFragment alarmFragment = new AlarmFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("alarm", alarm);
                                    alarmFragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, alarmFragment).commit();
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

                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private boolean validates(String code) {
        if(code.length() < 4) {
            Toast.makeText(getActivity(), R.string.short_code, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
