package ar.edu.itba.hci.smarthomesystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Alarm extends Fragment {


    public Alarm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
<<<<<<< HEAD
        /*View child = container.findViewById(R.id.recycler_view_rooms);
        System.out.println(child);
        child.setEnabled(false);
        System.out.println("isActivated??????" + child.isActivated());*/
        return inflater.inflate(R.layout.fragment_alarm, container, false); // root = null
=======
        return inflater.inflate(R.layout.fragment_alarm, container , false); // root = null
>>>>>>> 68a18050c906cf76c7ebfdda66f8f8eb64fe21cd
    }

}
