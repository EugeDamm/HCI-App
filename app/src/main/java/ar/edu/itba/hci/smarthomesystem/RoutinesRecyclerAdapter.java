package ar.edu.itba.hci.smarthomesystem;

import android.support.annotation.NonNull;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RoutinesRecyclerAdapter<T> extends RecyclerAdapter<T> {


    private OnItemListener onItemListener;

    public RoutinesRecyclerAdapter(OnItemListener onItemListener, String type) {
        super(onItemListener, type);
//        this.elements = elements;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routine_text_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(textView, onItemListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        String elementName = "  " + elements.get(i).toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(elementName);
        viewHolder.textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}
