package ar.edu.itba.hci.smarthomesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.zip.Inflater;

public class SpecificRoomActivity extends AppCompatActivity {

    private static final String TAG = "SpecificRoomActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setContentView(R.layout.specific_room);
        TextView dynamic = findViewById(R.id.textView);
        int index = bundle.getInt("index", -1);
        if(index != -1) {
            index = bundle.getInt("index") + 1;
            String toDisplay = "Room " + index;
            dynamic.setText(toDisplay);
        }
    }

}
