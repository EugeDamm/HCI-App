package ar.edu.itba.hci.smarthomesystem;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.BackgroundColorSpan;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import api.Api;
import api.Error;


/**
 * A simple {@link Fragment} subclass.
 */
public class Routines extends Fragment implements RecyclerAdapter.OnItemListener {

    private Notifications notifications = new Notifications();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Routine> list;
    RoutinesRecyclerAdapter<Routine> adapter;
    private TextView no_routines;
    private final Handler handler = new Handler();
    private Context context;
    private boolean state = false;

    public Routines() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        state = true;
        list = getArguments().getParcelableArrayList("routines");
        View view = inflater.inflate(R.layout.fragment_routines, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_routines);
        no_routines = view.findViewById(R.id.empty_routines_list);
        if (list == null || list.isEmpty()) {
            no_routines.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            no_routines.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RoutinesRecyclerAdapter<>( this, "routine");
        adapter.setElements(list);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        return view;
    }

    private Runnable getResponseAfterInterval = new Runnable() {
        public void run() {
            handler.postDelayed(this, 30*1000);
            Api.getInstance(getContext()).getRoutines(new Response.Listener<ArrayList<Routine>>() {
                @Override
                public void onResponse(ArrayList<Routine> response) {
                    if (!list.toString().equals(response.toString())) {
                        sendNotifications(R.string.routines_notification_title, R.string.routines_notification_text);
                        list = response;
                        if (state) {
                            adapter.setElements(list);
                            recyclerView.setAdapter(adapter);
                            if (list == null || list.isEmpty()) {
                                no_routines.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                no_routines.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            });
        }
    };


    @Override
    public void onItemClick(final int position, Context context, final View view) {
        String name = list.get(position).getName();
        Toast.makeText(getContext(), name + " Routine On", Toast.LENGTH_LONG).show();
        makeActions(list.get(position));
        ObjectAnimator colorFade = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), Color.rgb(23,239,31), Color.WHITE);
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

    public void sendNotifications(int title_int, int text_int) {
        if (context == null) context = getContext();
        String title = context.getResources().getString(title_int);
        String text = context.getResources().getString(text_int);
        notifications.sendNotifications(2, title, text, context, NotificationsChannel.ROUTINE_CHANNEL_ID, "routines");
    }

    private void makeActions(Routine routine) {
        try {
            JSONObject action = new JSONObject(routine.actions[0].toString());
            String name = action.getString("actionName");
            String deviceId = action.getString("deviceId");
            String params = action.getString("params");
            Api.getInstance(this.getContext()).makeActions(deviceId, name, params);
        }
        catch (JSONException e) {
            Log.e("JSONError", e.toString());
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

    public void initialize(List<Routine> list, Context context) {
        this.context = context;
        this.list = list;
        Log.d("LIST", list.toString());
        getResponseAfterInterval.run();
    }
}
