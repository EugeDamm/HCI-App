package ar.edu.itba.hci.smarthomesystem;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.DrawableRes;
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
import java.util.List;

import api.Api;
import api.Error;


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
    private final Handler handler = new Handler();


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
        // getResponceAfterInterval.run();
        return view;
    }

//    private Runnable getResponceAfterInterval = new Runnable() {
//        public void run() {
//            handler.postDelayed(this, 10*1000);
//            Api.getInstance(getContext()).getRoutines(new Response.Listener<ArrayList<Routine>>() {
//                @Override
//                public void onResponse(ArrayList<Routine> response) {
//                    Log.d("CHEQUEOOOO", "Entro al chequeo");
//                    if (!list.toString().equals(response.toString())) {
//                        list = response;
//                        adapter = new RoutinesRecyclerAdapter<>(list, new Routines());
//                        recyclerView.setAdapter(adapter);
//                        // Aca tendria que mandar una notificacion => Hubo un cambio en las rutinas! Toca aquí para conocer tus rutinas.
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    handleError(error);
//                }
//            });
//        }
//    };


    @Override
    public void onItemClick(final int position, Context context, final View view) {
        String name = list.get(position).getName();
        Toast.makeText(getContext(), name + " On", Toast.LENGTH_LONG).show();
        makeActions(list.get(position));
        Log.d("CONTEXT", context.toString());
        ObjectAnimator colorFade = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), Color.rgb(23,239,31), Color.rgb(217, 221, 226));
        colorFade.setDuration(4000);
        colorFade.start();
        new CountDownTimer(4000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                // view.setBackgroundResource(R.drawable.rounded_corner);
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




    public void handleError(VolleyError error) {
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
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}
