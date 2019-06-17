package ar.edu.itba.hci.smarthomesystem;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import api.*;
import api.Error;

public class SpecificRoomActivity extends AppCompatActivity implements RecyclerAdapter.OnItemListener {

    private static final String TAG = "SpecificRoomActivity";
    private List<Device> list;
    public Context context = this;
    private LinearLayout linearLayout;
    private TextView emptyText;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter<Device> adapter;
    private DevicesViewModel viewModel;
    private Room room;
    private final Handler handler = new Handler();
    private Notifications notifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        room = (Room) bundle.get("room_name");
        if(room != null && actionBar != null)
            actionBar.setTitle(room.getName());
        setContentView(R.layout.fragment_room_devices);
        emptyText = findViewById(R.id.empty_device_list);
        recyclerView = findViewById(R.id.recycler_view_devices);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<>(this, "device");
        viewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        if(room != null)
            viewModel.setRoomId(room.getId());
        final Observer<ArrayList<Device>> deviceObserver = new Observer<ArrayList<Device>>() {
            @Override
            public void onChanged(final ArrayList<Device> rooms) {
                adapter.setElements(rooms);
                list = rooms;
                if(list == null || list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }
            }
        };
        if(viewModel != null)
            viewModel.getDevices().observe(this, deviceObserver);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        getResponseAfterInterval.run();
        notifications = new Notifications();
    }

    private Runnable getResponseAfterInterval = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 30 * 1000);
            Api.getInstance(getApplicationContext()).getDevicesForRoom(new Response.Listener<ArrayList<Device>>() {
                @Override
                public void onResponse(ArrayList<Device> response) {
                    if(!list.toString().equals(response.toString())) {
                        sendNotifications("Smart Home System", "There was a change in one room! Click to view.");
                        adapter.setElements(response);
                        list = response;
                        recyclerView.setAdapter(adapter);
                        if(list == null || list.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyText.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyText.setVisibility(View.GONE);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            }, viewModel.getRoomId());
        }
    };

    public void sendNotifications(String title, String text) {
        notifications.sendNotifications(4, title, text, getApplicationContext(), NotificationsChannel.SPECIFIC_ROOM_DEVICES_CHANNEL_ID, room.getName());
    }

    @Override
    public void onItemClick(int position, Context context, View view) {
        Intent intent = new Intent(context, SpecificDeviceActivity.class);
        final Device deviceToExpand = list.get(position);
        Bundle bundleToAdd = new Bundle();
        bundleToAdd.putString("device_name", deviceToExpand.getName());
        bundleToAdd.putString("device_id", deviceToExpand.getId());
        bundleToAdd.putString("device_type_id", deviceToExpand.getTypeId());
        bundleToAdd.putParcelable("go_back", room);
        intent.putExtras(bundleToAdd);
        startActivity(intent);
    }

    public Context getContext() {
        return context;
    }

    private void handleError(VolleyError error) {
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
        Log.e(TAG, error.toString());
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(SpecificRoomActivity.this, text, Toast.LENGTH_LONG).show();
    }


}
