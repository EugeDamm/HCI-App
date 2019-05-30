package ar.edu.itba.hci.smarthomesystem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Routines extends Fragment {

    private LinearLayout linearLayout = null;

    public Routines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routines, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        final List<Routine> routines = getRoutines();
        for (int i = 0; i < routines.size(); i++) {
            final int index = i;
            //Create your Controls(UI widget, Button,TextView) and add into layout
            Button btn = new Button(getActivity());
            btn.setText(routines.get(i).name);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 20;
            params.leftMargin = 20;
            params.rightMargin = 20;
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    String name = button.getText().toString();
                    Log.d("CLICK", name);
                    Toast.makeText(getContext(), name + " On", Toast.LENGTH_LONG).show();
                    makeActions(routines.get(index));
                }
            });
            linearLayout.addView(btn);
            }
        }

    private List<Routine> getRoutines() {
        List<Routine> list = new ArrayList<>();
        // TODO: I should ask the API for the routines, and transform each into a Routine.class, and add it to the ArrayList
        // Example ->
        String array[] = {}; // This are the actions to make.
        Routine example = new Routine("1234", "Routine 1", array);
        Routine example2 = new Routine("5678", "Routine 2", array);
        list.add(example);
        list.add(example2);
        return list;
    }

    private void makeActions(Routine routine) {
        // TODO: Tendria que hacer las acciones de la rutina aca.
        Log.d("MAKEACTION", routine.id);
    }

}
