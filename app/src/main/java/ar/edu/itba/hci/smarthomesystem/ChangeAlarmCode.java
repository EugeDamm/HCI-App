package ar.edu.itba.hci.smarthomesystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAlarmCode extends Fragment {
    EditText mEditText;
    View view;
    public ChangeAlarmCode() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_alarm_code, container, false);

        // Inflate the layout for this fragment
        return view;
    }

}
