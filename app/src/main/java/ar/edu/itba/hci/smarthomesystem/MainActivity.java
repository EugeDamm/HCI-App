package ar.edu.itba.hci.smarthomesystem;


import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;

import api.Api;
import devices.Alarm;
import devices.DeviceType;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Fragment fragment = null;
    public Context context;
    private String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    private Intent starterIntent;
    private Fragment oldFragment;
    private Handler handler = new Handler();
    private Routines routines = new Routines();
    private AlarmFragment alarmFragment = new AlarmFragment();
    private Bundle routinesBundle;
    private Bundle alarmBundle;
    private BottomNavigationView navView;
    private String alarmInitState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.starterIntent = getIntent();
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        initializeRoutines();
        initializeAlarm();
        if (getIntent().getExtras() != null) {
            String intentFragment = getIntent().getStringExtra("fragment");
            switch (intentFragment) {
                case "routines": {
                    loadRoutines(true, navView);
                    return;
                }
                case "rooms": {
                    fragment = new Rooms();
                    loadFragment(fragment);
                    navView.setSelectedItemId(R.id.rooms);
                    return;
                }
                case "alarm":
                    loadAlarm(true, navView);
                    navView.setSelectedItemId(R.id.alarm);
                    return;
            }
        }
        if (savedInstanceState == null) {
            if (isNetworkAvailable())
                fragment = new Rooms();
            else
                fragment = new NoConnectionFragment();
            loadFragment(fragment);
        } else {
            if (isNetworkAvailable()) {
                if (getIntent().getExtras() != null) {
                    switch (getIntent().getStringExtra("fragment")) {
                        case "routines":
                            loadRoutines(true, navView);
                            break;
                        case "rooms":
                            fragment = new Rooms();
                            break;
                        case "alarm":
                            loadAlarm(true, navView);
                            navView.setSelectedItemId(R.id.alarm);
                            break;
                    }
                } else
                    fragment = new Rooms();
            }
            else
                fragment = new NoConnectionFragment();
            loadFragment(fragment);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.change_ip:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.change_ip);
                final EditText input = new EditText(this);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.setURL(input.getText().toString());
                        oldFragment = fragment;
                        recreate();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!connectionApi() && isNetworkAvailable()) {
            fragment = new NoApiConnection();
            loadFragment(fragment);
            return true;
        }
        if(isNetworkAvailable()) {
            switch (item.getItemId()) {
                case R.id.rooms:
                    getIntent().putExtra("fragment", "rooms");
                    fragment = new Rooms();
                    loadFragment(fragment);
                    break;
                case R.id.routines:
                    getIntent().putExtra("fragment", "routines");
                    loadRoutines(true, null);
                    break;
                case R.id.alarm:
                    getIntent().putExtra("fragment", "alarm");
                    loadAlarm(true, null);
                    navView.setSelectedItemId(R.id.alarm);
                    break;
            }
        } else {
            fragment = new NoConnectionFragment();
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadRoutines(final boolean state, final BottomNavigationView navView) {
        routinesBundle = new Bundle();
        Api.getInstance(this).getRoutines(new Response.Listener<ArrayList<Routine>>() {
            @Override
            public void onResponse(ArrayList<Routine> response) {
                routinesBundle.putParcelableArrayList("routines", response);
                if (state) {
                    fragment = routines;
                    fragment.setArguments(routinesBundle);
                    loadFragment(fragment);
                    if (navView != null) navView.setSelectedItemId(R.id.routines);
                } else finishRoutinesInitialize();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.handleError(error, MainActivity.this);
                fragment = new NoApiConnection();
                loadFragment(fragment);
            }
        });
    }

    private void loadAlarm(final boolean state, final BottomNavigationView navView) {
        alarmBundle = new Bundle();
        Api.getInstance(MainActivity.this).getDevices(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
                Alarm alarm = null;
                for (Device d : response) {
                    if (d.getTypeId().equals(ALARM_TYPE_ID)) {
                        alarm = new Alarm("", d.getName(), d.getId());
                        alarmBundle.putSerializable("alarm", alarm);
                        if (state) {
                            alarmFragment.setArguments(alarmBundle);
                            loadFragment(alarmFragment);
                            if (navView != null) navView.setSelectedItemId(R.id.alarm);
                        } else searchAlarm(alarm);
                        break;
                    }
                }
                if (alarm == null) {
                    fragment = new NoAlarm();
                    loadFragment(fragment);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.handleError(error, MainActivity.this);
                fragment = new NoApiConnection();
                loadFragment(fragment);
            }
        });
    }

    private void searchAlarm(final Alarm alarm_device) {
        Api.getInstance(context).getDeviceState(new Response.Listener<State>() {
            @Override
            public void onResponse (State response){
                response.setDeviceType(ALARM_TYPE_ID);
                final DeviceType alarm = response.getCreatedDevice();
                alarmInitState = alarm.getStatus();
                finishAlarmInitialize(alarm_device);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, alarm_device.getId());
    }

    private void initializeRoutines() {
        loadRoutines(false, null);
    }

    private void finishRoutinesInitialize() {
        List<Routine> aux = routinesBundle.getParcelableArrayList("routines");
        routines.initialize(aux, context);
    }

    private void initializeAlarm() {
        loadAlarm(false, null);
    }

    private void finishAlarmInitialize(Alarm alarm) {
        if (alarm == null) return;
        alarmFragment.initialize(context, alarmInitState, alarm);
    }

    private boolean connectionApi() {
        return Api.getInstance(context).isConnected();
    }

}
