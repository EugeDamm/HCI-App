package ar.edu.itba.hci.smarthomesystem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Routines extends Fragment {

    private LinearLayout linearLayout = null;
    private RequestQueue mQueue;


    public Routines() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mQueue = Volley.newRequestQueue(getContext());
        getRoutines();
        return inflater.inflate(R.layout.fragment_routines, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
    }

    private void makeActions(Routine routine) {
        // TODO: Tendria que hacer las acciones de la rutina aca.
        Log.d("MAKEACTION", routine.id);
    }


    public void getRoutines() {

        final List<Routine> result = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/routines";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray routines = response.getJSONArray("routines");
                            for (int i=0 ; i < routines.length(); i++) {
                                JSONObject routine = routines.getJSONObject(i);
                                String id = routine.getString("id");
                                String name = routine.getString("name");
                                JSONArray actions = routine.getJSONArray("actions");
                                Routine nextToAdd = new Routine(id, name, actions);
                                // Log.d("ROUTINE", nextToAdd.toString());
                                result.add(nextToAdd);
                            }
                            updateRoutines(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("B", response.toString());
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("ERROR", error.toString());
            }
        });
        mQueue.add(request);
    }

    private void updateRoutines(final List<Routine> list) {
        if (list.size() == 0) {
            TextView label = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 20;
            params.leftMargin = 20;
            params.rightMargin = 20;
            label.setGravity(Gravity.CENTER);
            label.setLayoutParams(params);
            label.setText(R.string.no_routines);
            linearLayout.addView(label);
        }
        for (int i = 0; i < list.size(); i++) {
            final int index = i;
            //Create your Controls(UI widget, Button,TextView) and add into layout
            Button btn = new Button(getActivity());
            btn.setText(list.get(i).name);
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
                    makeActions(list.get(index));
                }
            });
            linearLayout.addView(btn);
        }
    }
}
