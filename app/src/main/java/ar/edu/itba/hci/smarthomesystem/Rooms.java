package ar.edu.itba.hci.smarthomesystem;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import api.Api;

public class Rooms extends Fragment implements RecyclerAdapter.OnItemListener {

    private final String TAG = "Rooms";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Room> list;
    private RecyclerAdapter<Room> adapter;
    private RoomListViewModel viewModel;
    private MutableLiveData<ArrayList<Room>> rooms;
    private TextView emptyText;
    private final Handler handler = new Handler();

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
        // Create the observer which updates the UI.
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
        // Observe the LiveData.
        if(viewModel != null)
            viewModel.getRooms().observe(this, roomsObserver);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        getResponseAfterInterval.run();
        return view;
    }

    private Runnable getResponseAfterInterval = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 30 * 1000);
            Api.getInstance(getContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
                @Override
                public void onResponse(ArrayList<Room> response) {
                    if(!list.toString().equals(response.toString())) {
                        adapter.setElements(response);
                        list = response;
                        recyclerView.setAdapter(adapter);
                        if(list == null || list.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    };

    @Override
    public void onItemClick(int position, Context context) {
        Intent intent = new Intent(context, SpecificRoomActivity.class);
        intent.putExtra("room_name", list.get(position));
        startActivity(intent);
    }
}
