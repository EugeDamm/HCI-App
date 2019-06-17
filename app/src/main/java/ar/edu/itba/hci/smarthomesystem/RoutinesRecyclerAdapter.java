package ar.edu.itba.hci.smarthomesystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private ViewGroup viewGroup;
    private OnItemListener onItemListener;

    public RoutinesRecyclerAdapter(OnItemListener onItemListener, String type) {
        super(onItemListener, type);
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewGroup = viewGroup;
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routine_text_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(textView, onItemListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        String elementName = "  " + elements.get(i).toString();
        Bitmap icon = BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.power);
        SpannableStringBuilder ssb = new SpannableStringBuilder(elementName);
        ssb.setSpan(new ImageSpan(icon), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}
