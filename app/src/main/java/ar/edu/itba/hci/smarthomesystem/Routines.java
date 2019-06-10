package ar.edu.itba.hci.smarthomesystem;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import api.Api;


/**
 * A simple {@link Fragment} subclass.
 */
public class Routines extends Fragment implements RecyclerAdapter.OnItemListener {


    private final String TAG = "Routines";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Routine> list;
    RoutinesRecyclerAdapter<Routine> adapter;
    static final int ROUTINES_BOX = 0;


    public Routines() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list = getArguments().getParcelableArrayList("routines");
        View view = inflater.inflate(R.layout.fragment_routines, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_routines);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RoutinesRecyclerAdapter<>(list, this);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(final int position, Context context) {
        String name = list.get(position).getName();
        Toast.makeText(getContext(), name + " On", Toast.LENGTH_LONG).show();
        makeActions(list.get(position));
        new CountDownTimer(1000, 50) {
            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub
                getView().setBackgroundColor(Color.BLACK);
            }

            @Override
            public void onFinish() {
                getView().setBackgroundColor(Color.YELLOW);
            }
        }.start();
    }

    private void makeActions(Routine routine) {
        // TODO: Tendria que hacer las acciones de la rutina aca.
        Log.d("MAKEACTION", routine.id);
        Log.d("ACTION", routine.actions[0].toString());
        try {
            JSONObject action = new JSONObject(routine.actions[0].toString());
            String name = action.getString("actionName");
            String deviceId = action.getString("deviceId");
            String params = action.getString("params");
            Api.getInstance(this.getContext()).makeActions(deviceId, name, params);
        }
        catch (JSONException e) {
            Log.d("JSONError", e.toString());
        }
    }
}
