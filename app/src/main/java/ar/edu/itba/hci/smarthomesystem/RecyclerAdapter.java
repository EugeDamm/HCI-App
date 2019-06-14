package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {

    private List<T> elements;
    private OnItemListener onItemListener;
    private ViewGroup viewGroup;
    private String type;
    private String elementType;
    private MyViewHolder myViewHolder;
    private final String BLINDS_TYPE_ID = "eu0v2xgprrhhg41g";
    private final String LAMP_TYPE_ID = "go46xmbqeomjrsjr";
    private final String OVEN_TYPE_ID = "im77xxyulpegfmv8";
    private final String AC_TYPE_ID = "li6cbv5sdlatti0j";
    private final String DOOR_TYPE_ID = "lsf78ly0eqrjbz91";
    private final String TIMER_TYPE_ID = "ofglvd9gqX8yfl3l";
    private final String REFRIGERATOR_TYPE_ID = "rnizejqr2di0okho";

    public RecyclerAdapter(OnItemListener onItemListener, String type) {
        this.onItemListener = onItemListener;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewGroup = viewGroup;
        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_text_layout, viewGroup, false);
        this.myViewHolder = new MyViewHolder(textView, onItemListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        String elementName = "  " + elements.get(i).toString();
        Bitmap icon;
        if(this.type.equals("device")) {
            Log.d("Adapter", "onBindViewHolder: device = " + elements.get(i));
            Device device = (Device) elements.get(i);
            Log.d("Adapter", "onBindViewHolder: device = " + device.getTypeId());
            String deviceId = device.getTypeId();
            this.elementType = pickDevice(deviceId);
            Log.d("Adapter", "onBindViewHolder: type = " + this.elementType);
            icon = chooseBitmap();
        } else {
            icon = BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.room);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(elementName);
        ssb.setSpan(new ImageSpan(icon), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        myViewHolder.textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private String pickDevice(String typeId) {
        switch (typeId) {
            case BLINDS_TYPE_ID:
                return "blinds";
            case AC_TYPE_ID:
                return "ac";
            case DOOR_TYPE_ID:
                return "door";
            case LAMP_TYPE_ID:
                return "lamp";
            case OVEN_TYPE_ID:
                return "oven";
            case REFRIGERATOR_TYPE_ID:
                return "refrigerator";
            default:
                return "timer";
        }
    }

    private Bitmap chooseBitmap() {
        switch (this.elementType) {
            case "ac":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.ac);
            case "blinds":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.blinds);
            case "door":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.door);
            case "refrigerator":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.refrigerator);
            case "oven":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.oven);
            case "timer":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.timer);
            case "lamp":
                return BitmapFactory.decodeResource( viewGroup.getResources(), R.drawable.lamp);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        if(elements == null)
            return 0;
        return elements.size();
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
        Log.d("Recycler", "setElements: reseteo los elementos = " + elements);
        notifyDataSetChanged();
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
            onItemListener.onItemClick(getAdapterPosition(), v.getContext());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position, Context context);
    }
}
