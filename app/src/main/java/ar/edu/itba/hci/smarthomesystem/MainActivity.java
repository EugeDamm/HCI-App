package ar.edu.itba.hci.smarthomesystem;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;

import api.Api;
import devices.Alarm;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private final String LOG_TAG = "ar.edu.itba.apiexample";
    private static final String TAG = "MainActivity";
    private MutableLiveData<ArrayList<Room>> rooms;
    private Fragment fragment = null;
    private Bundle bundle = new Bundle();
    public Context context;
    private RoomListViewModel viewModel;
    boolean ALARM_CREATED = false;
    private String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    String alarmId;
    private ArrayList<Device> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        Api.getInstance(this).getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                bundle.putParcelableArrayList("rooms", response);
                fragment = new Rooms();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.handleError(error, MainActivity.this);
            }
        });
        if(display.getRotation() == Surface.ROTATION_90) {
            loadDoubleView();
        } else {
            fragment = new Rooms();
            loadFragment(fragment);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rooms:
                fragment = new Rooms();
                loadFragment(fragment);
                break;
            case R.id.routines:
                Api.getInstance(this).getRoutines(new Response.Listener<ArrayList<Routine>>() {
                    @Override
                    public void onResponse(ArrayList<Routine> response) {
                        Log.d(TAG, "onResponse: " + "LLEGO" + response);
                        bundle.putParcelableArrayList("routines", response);
                        fragment = new Routines();
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorHandler.handleError(error, MainActivity.this);
                    }
                });
                break;
            case R.id.alarm:
                Api.getInstance(MainActivity.this).getDevices(new Response.Listener<ArrayList<Device>>() {
                    @Override
                    public void onResponse(ArrayList<Device> response) {
                        for(Device d : response) {
                            if(d.getTypeId().equals(ALARM_TYPE_ID)) {
                                AlarmFragment alarmFragment = new AlarmFragment();
                                Bundle bundle = new Bundle();
                                Alarm alarm = new Alarm(d.getName(), d.getId(), d.getTypeId());
                                bundle.putSerializable("alarm", alarm);
                                alarmFragment.setArguments(bundle);
                                loadFragment(alarmFragment);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorHandler.handleError(error, MainActivity.this);
                    }
                });
                fragment = new NoAlarm();
                break;
        }
        return loadFragment(fragment);
    }

    private void loadDoubleView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.rooms_left, new Rooms()).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.specific_room_right, new Rooms()).addToBackStack(null).commit();
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}
