package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import api.Api;
import api.Error;

public class SpecificDeviceActivity extends AppCompatActivity {

    private final String TAG = "SpecificDeviceActivity";
    private Room room;
    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.specific_device);
        Intent currentIntent = getIntent();
        Bundle currentBundle = currentIntent.getExtras();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(currentBundle != null) {
            Log.d(TAG, "onCreate: bundle back to " + currentBundle.get("go_back"));
            Log.d(TAG, "onCreate: bundle device stored " + currentBundle.getString("device_name"));
            room = (Room) currentBundle.get("go_back");
            String deviceName = currentBundle.getString("device_name");
            String deviceId = currentBundle.getString("device_id");
            Log.d(TAG, "onCreate: deviceId " + deviceId);
            String deviceTypeId = currentBundle.getString("device_type_id");
            actionBar.setTitle(deviceName);
            Api.getInstance(this).getDeviceState(new Response.Listener<State>() {
                @Override
                public void onResponse(State response) {
                    String status = response.getStatus();
                    TextView text = findViewById(R.id.textViewSpecificDevice);
                    text.setText(status);
                    Switch switchButton = findViewById(R.id.switchSpecificDevice);
                    if(status.equals("opened") || status.equals("opening"))
                        switchButton.setChecked(true);
                    else
                        switchButton.setChecked(false);
                    Log.d(TAG, "onResponse: " + response.getStatus());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            }, deviceId);
        }
    }

    private void handleError(VolleyError error) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }
        Log.e(TAG, error.toString());
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(SpecificDeviceActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, SpecificRoomActivity.class);
                intent.putExtra("room_name", room);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
