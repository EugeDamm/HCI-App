package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Rooms extends Fragment implements RecyclerAdapter.OnItemListener {

    private final String TAG = "Rooms";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Room> list;
    RecyclerAdapter<Room> adapter;

    public Rooms() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list = getArguments().getParcelableArrayList("rooms");
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_rooms);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<>(list, this);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(int position, Context context) {
        Log.d(TAG, "onItemClick: click rooms, context: " + context);
        Intent intent = new Intent(context, SpecificRoomActivity.class);
        intent.putExtra("room_name", list.get(position));
        startActivity(intent);
    }
}
