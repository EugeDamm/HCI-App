package ar.edu.itba.hci.smarthomesystem;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationsChannel extends Application {

    public static final String ROOMS_CHANNEL_ID = "rooms_channel";
    public static final String ROUTINE_CHANNEL_ID = "routines_channel";
    public static final String ALARM_CHANNEL_ID = "alarm_channel";
    public static final String SPECIFIC_ROOM_DEVICES_CHANNEL_ID = "specific_room_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationsChannel();
    }

    private void createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String rooms_channel_name = "Room Channel";
            String rooms_channel_description = "A channel for all Room Notifications";

            String routines_channel_name = "Routines Channel";
            String routines_channel_description = "A channel for all Routines Notifications";

            String alarm_channel_name = "Alarm Channel";
            String alarm_channel_description = "A channel for all Alarm Notifications";

            String room_devices_channel_name = "Room Devices Channel";
            String room_devices_channel_description = "A channel for all Room Devices Notifications";

            int importance = NotificationManager.IMPORTANCE_HIGH;

            android.app.NotificationChannel rooms_channel = new android.app.NotificationChannel(ROOMS_CHANNEL_ID, rooms_channel_name, importance);
            rooms_channel.setDescription(rooms_channel_description);

            android.app.NotificationChannel routines_channel = new android.app.NotificationChannel(ROUTINE_CHANNEL_ID, routines_channel_name, importance);
            routines_channel.setDescription(routines_channel_description);

            android.app.NotificationChannel alarm_channel = new android.app.NotificationChannel(ALARM_CHANNEL_ID, alarm_channel_name, importance);
            alarm_channel.setDescription(alarm_channel_description);

            android.app.NotificationChannel room_device_channel = new android.app.NotificationChannel(SPECIFIC_ROOM_DEVICES_CHANNEL_ID, room_devices_channel_name, importance);
            room_device_channel.setDescription(room_devices_channel_description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(rooms_channel);
            notificationManager.createNotificationChannel(routines_channel);
            notificationManager.createNotificationChannel(alarm_channel);
            notificationManager.createNotificationChannel(room_device_channel);
        }
    }
}


