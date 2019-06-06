package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {

    private List<T> elements;
    private OnItemListener onItemListener;

    public RecyclerAdapter(List<T> elements, OnItemListener onItemListener) {
        this.elements = elements;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_text_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(textView, onItemListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.textView.setText(elements.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    // Class responsible for each element on the list
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull TextView itemView, OnItemListener onItemListener) {
            super(itemView);
            this.textView = itemView;
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: clicky");
            onItemListener.onItemClick(getAdapterPosition(), v.getContext());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position, Context context);
    }
}
