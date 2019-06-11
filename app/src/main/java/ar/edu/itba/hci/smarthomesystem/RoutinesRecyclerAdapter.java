package ar.edu.itba.hci.smarthomesystem;

import android.support.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class RoutinesRecyclerAdapter<T> extends RecyclerAdapter<T> {


    private List<T> elements;
    private OnItemListener onItemListener;

    public RoutinesRecyclerAdapter(List<T> elements, OnItemListener onItemListener) {
        super(elements, onItemListener);
        this.elements = elements;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routine_text_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(textView, onItemListener);
        return myViewHolder;
    }
}
