package ar.edu.itba.hci.smarthomesystem;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {

    public static final String ROOMS_CHANNEL_ID = "rooms_channel";
    public static final String ROUTINE_CHANNEL_ID = "routines_channel";
    public static final String ALARM_CHANNEL_ID = "alarm_channel";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationsChannel();
    }



    private void createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String rooms_channel_name = "Room Channel Name";
            String rooms_channel_description = "Room Channel Description";


            String routines_channel_name = "Routines Channel Name";
            String routines_channel_description = "Routines Channel Description";

            String alarm_channel_name = "Alarm Channel Name";
            String alarm_channel_description = "Alarm Channel Description";


            int importance_low = NotificationManager.IMPORTANCE_LOW;
            int importance_high = NotificationManager.IMPORTANCE_HIGH;




            NotificationChannel rooms_channel = new NotificationChannel(ROOMS_CHANNEL_ID, rooms_channel_name, importance_low);
            rooms_channel.setDescription(rooms_channel_description);

            NotificationChannel routines_channel = new NotificationChannel(ROUTINE_CHANNEL_ID, routines_channel_name, importance_high);
            routines_channel.setDescription(routines_channel_description);

            NotificationChannel alarm_channel = new NotificationChannel(ALARM_CHANNEL_ID, alarm_channel_name, importance_low);
            alarm_channel.setDescription(alarm_channel_description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(rooms_channel);
            notificationManager.createNotificationChannel(routines_channel);
            notificationManager.createNotificationChannel(alarm_channel);


        }
    }
}


