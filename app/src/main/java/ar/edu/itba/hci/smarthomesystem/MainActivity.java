package ar.edu.itba.hci.smarthomesystem;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
=======
<<<<<<< HEAD
>>>>>>> 68a18050c906cf76c7ebfdda66f8f8eb64fe21cd
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.List;

<<<<<<< HEAD
public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
=======
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    Fragment roomFragment = new Fragment();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Fragment routineFragment = new Fragment();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    Fragment alarmFragment = new Fragment();
                    return true;
            }
            return false;
        }
    };
=======
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
>>>>>>> parent of 1c97c7a... Added rooms fragment
>>>>>>> 68a18050c906cf76c7ebfdda66f8f8eb64fe21cd

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.rooms:
                break;
            case R.id.routines:
                break;
            case R.id.alarm:
                fragment = new Alarm();
        }
        return loadFragment(fragment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
<<<<<<< HEAD
        navView.setOnNavigationItemSelectedListener(this);
        loadFragment(new Rooms());
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = new Rooms();
        switch (item.getItemId()) {
            case R.id.rooms:
                fragment = new Rooms();
            case R.id.routines:
                break;
            case R.id.alarm:
                fragment = new Alarm();
        }
        return loadFragment(fragment);
=======
<<<<<<< HEAD
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
>>>>>>> 68a18050c906cf76c7ebfdda66f8f8eb64fe21cd
    }

=======
        navView.setOnNavigationItemSelectedListener(this);

    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
>>>>>>> parent of 1c97c7a... Added rooms fragment
}
