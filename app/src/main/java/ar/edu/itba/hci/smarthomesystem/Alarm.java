package ar.edu.itba.hci.smarthomesystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class Alarm extends Fragment{
    Button changeAlarmPassword;
    View view;

    public Alarm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm, container , false);
        changeAlarmPassword = view.findViewById(R.id.change_password);
        final Switch homeModeSwitch = view.findViewById(R.id.home_mode_switch);
       final  Switch outHomeModeSwitch = view.findViewById(R.id.out_home_mode_switch);
        homeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChangeAlarmCode())
                        .addToBackStack(null)
                        .commit();
//                Log.d("Switch State=", ""+isChecked);
                if(isChecked) {
                    outHomeModeSwitch.setClickable(false);
                }else{
                    outHomeModeSwitch.setClickable(true);
                }
            }
        });

        outHomeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChangeAlarmCode())
                        .addToBackStack(null)
                        .commit();
//                Log.d("Switch State=", ""+isChecked);
                if(isChecked) {
                    homeModeSwitch.setClickable(false);
                }else{
                    homeModeSwitch.setClickable(true);
                }
            }
        });

        changeAlarmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChangeAlarmCode())
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
