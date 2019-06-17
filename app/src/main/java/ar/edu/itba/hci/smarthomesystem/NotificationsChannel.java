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

            String rooms_channel_name = "Room Channel Name";
            String rooms_channel_description = "Room Channel Description";

            String routines_channel_name = "Routines Channel Name";
            String routines_channel_description = "Routines Channel Description";

            String alarm_channel_name = "Alarm Channel Name";
            String alarm_channel_description = "Alarm Channel Description";

            String room_devices_channel_name = "Room Devices Channel Name";
            String room_devices_channel_description = "Room Devices Channel Description";

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


