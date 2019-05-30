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

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Rooms extends Fragment implements RecyclerAdapter.OnItemListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<String> list;
    RecyclerAdapter adapter;

    public Rooms() {
        // required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_rooms);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = Arrays.asList(getResources().getStringArray(R.array.fake_rooms)); // here we should do GET of all rooms
        adapter = new RecyclerAdapter(list, this);
        recyclerView.setHasFixedSize(true); // improves performance
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(int position, Context context) {
        Log.d(TAG, "onItemClick: click rooms, context: " + context);
        Intent intent = new Intent(context, SpecificRoomActivity.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }
}
