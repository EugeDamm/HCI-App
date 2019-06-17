package ar.edu.itba.hci.smarthomesystem;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Api;
import api.Error;

public class Rooms extends Fragment implements RecyclerAdapter.OnItemListener {

    private final String TAG = "Rooms";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Room> list;
    private RecyclerAdapter<Room> adapter;
    private RoomListViewModel viewModel;
    private MutableLiveData<ArrayList<Room>> rooms;
    private TextView emptyText;
    private final Handler handler = new Handler();
    private Notifications notifications;
    private Map<String, ArrayList<Device>> allRoomsDevices = new HashMap<>();
    private boolean first = true;

    public Rooms() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        emptyText = view.findViewById(R.id.empty_room_list);
        recyclerView = view.findViewById(R.id.recycler_view_rooms);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<>(this, "room");
        viewModel = ViewModelProviders.of(this).get(RoomListViewModel.class);
        final Observer<ArrayList<Room>> roomsObserver = new Observer<ArrayList<Room>>() {
            @Override
            public void onChanged(final ArrayList<Room> rooms) {
                adapter.setElements(rooms);
                list = rooms;
                if(list == null || list.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        };
        if(viewModel != null)
            viewModel.getRooms().observe(this, roomsObserver);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        getResponseAfterInterval.run();
        notifications = new Notifications();
        backgroundRoomDeviceChecker.run();
        return view;
    }

    private void sendNotifications(String title, String text) {
        notifications.sendNotifications(1, title, text, getContext(), NotificationsChannel.ROOMS_CHANNEL_ID, "rooms");
    }

    private Runnable getResponseAfterInterval = new Runnable() {
        @Override
        public void run() {
        handler.postDelayed(this, 30 * 1000);
            Api.getInstance(getContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
                @Override
                public void onResponse(ArrayList<Room> response) {
                    if(list != null) {
                        if (!list.toString().equals(response.toString())) {
                            sendNotifications("Smart Home System", "There was a change in rooms! Click to view.");
                            adapter.setElements(response);
                            list = response;
                            recyclerView.setAdapter(adapter);
                            if (list == null || list.isEmpty()) {
                                emptyText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                emptyText.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.handleError(error, getActivity());
                }
            });
        }
    };

    @Override
    public void onItemClick(int position, Context context, View view) {
        Intent intent = new Intent(context, SpecificRoomActivity.class);
        intent.putExtra("room_name", list.get(position));
        startActivity(intent);
    }

    private Runnable backgroundRoomDeviceChecker = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 5 * 1000);
            final Map<String,ArrayList<Device>> toCompare = new HashMap<>();
            String idChanged = "";
            if(list != null) {
                if(first) {
                    Log.d(TAG, "run: first en true y list = " + list);
                    for (final Room room : list) {
                        Api.getInstance(getContext()).getDevicesForRoom(new Response.Listener<ArrayList<Device>>() {
                            @Override
                            public void onResponse(ArrayList<Device> response) {
                                Log.d(TAG, "onResponse: metiendo en el inicial a roomid = " + room.getId() + " le meto = " + response);
                                allRoomsDevices.put(room.getId(), response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ErrorHandler.handleError(error, getActivity());
                            }
                        }, room.getId());
                    }
                    first = false;
                }
                for (final Room room : list) {
                    Api.getInstance(getContext()).getDevicesForRoom(new Response.Listener<ArrayList<Device>>() {
                        @Override
                        public void onResponse(ArrayList<Device> response) {
                            Log.d(TAG, "onResponse: metiendo en el comparador a roomid = " + room.getId() + " le meto = " + response);
                            toCompare.put(room.getId(), response);
                            if(!toCompare.get(room.getId()).toString().equals(allRoomsDevices.get(room.getId()).toString())) {
                                sendNotifications(room);
                                allRoomsDevices = toCompare;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorHandler.handleError(error, getActivity());
                        }
                    }, room.getId());
                }

            }
        }
    };

    private void sendNotifications(Room room) {
        String title = "Smart Home System";
        String roomName = room.getName();
        String text = getString(R.string.room_device_change) + room.getName() + "! " + getString(R.string.phrase_finish);
        notifications.sendNotifications(4, title, text, getContext(), NotificationsChannel.SPECIFIC_ROOM_DEVICES_CHANNEL_ID, room.getId());
    }

}
