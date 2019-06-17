package ar.edu.itba.hci.smarthomesystem;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;

public class Notifications {

    private NotificationManagerCompat notificationManager;

    public Notifications() {
    }

    public void sendNotifications(int id, String title, String text, Context context, String channel, String destination) {
        PendingIntent contentIntent;
        switch (id) {
            case 4: {
                Intent activityIntent = new Intent(context, SpecificRoomActivity.class);
                activityIntent.putExtra("room_name", destination);
                contentIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);
                break;
            }
            default: {
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.putExtra("fragment", destination);
                contentIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);
                break;
            }
        }

        Intent dismissIntent = new Intent(context, NotificationReciever.class);
        dismissIntent.putExtra("toDismiss", id);
        PendingIntent dismissActionIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setContentTitle(title)
                .setContentText(text)
                .addAction(R.mipmap.ic_launcher, "Dismiss", dismissActionIntent)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .setColor(Color.GREEN)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH).build();

        notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id, notification);
    }
}

