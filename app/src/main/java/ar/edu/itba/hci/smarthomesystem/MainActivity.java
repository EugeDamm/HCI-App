package ar.edu.itba.hci.smarthomesystem;


import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import api.Api;
import devices.Alarm;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private final String LOG_TAG = "ar.edu.itba.apiexample";
    private static final String TAG = "MainActivity";
    private MutableLiveData<ArrayList<Room>> rooms;
    private Fragment fragment = null;
    private Bundle bundle = new Bundle();
    public Context context;
    private Bundle savedInstanceState;
    private String ALARM_TYPE_ID = "mxztsyjzsrq7iaqc";
    private Intent starterIntent;
    private Fragment oldFragment;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.starterIntent = getIntent();
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        Log.d(TAG, "onCreate: estoy en el onCreate");
        if (savedInstanceState == null) {
            if (isNetworkAvailable())
                fragment = new Rooms();
            else
                fragment = new NoConnectionFragment();
            loadFragment(fragment);
        } else {
            if (isNetworkAvailable())
                fragment = new Rooms();
            else
                fragment = new NoConnectionFragment();
            loadFragment(fragment);
        }
        checkConnectionRepeatedly.run();
    }

    private Runnable checkConnectionRepeatedly = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 10 * 1000);
            if(!isNetworkAvailable()) {
                recreate();
            } else {
                recreate();
            }
        }
    };

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
        if(isNetworkAvailable()) {
            switch (item.getItemId()) {
                case R.id.rooms:
                    fragment = new Rooms();
                    loadFragment(fragment);
                    break;
                case R.id.routines:
                    Api.getInstance(this).getRoutines(new Response.Listener<ArrayList<Routine>>() {
                        @Override
                        public void onResponse(ArrayList<Routine> response) {
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
                            for (Device d : response) {
                                if (d.getTypeId().equals(ALARM_TYPE_ID)) {
                                    AlarmFragment alarmFragment = new AlarmFragment();
                                    Bundle bundle = new Bundle();
                                    Alarm alarm = new Alarm("", d.getName(), d.getId());
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
}
