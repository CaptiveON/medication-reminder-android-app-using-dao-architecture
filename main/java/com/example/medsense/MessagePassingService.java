package com.example.medsense;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class MessagePassingService extends Service {
    DAOReminder daoReminder;
    Random random = new Random();
    private static final String CHANNEL_ID = "AlertNotification";
    private final static String d_notification_channel_id = "default";

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        daoReminder = new MedSQLiteDAO(this);
        Reminder temp = Reminder.loadLastReminder(daoReminder);

        Notification notification = getNotification(temp.MedicineName, temp.ReminderNote, getMyTime(temp.time));
        //Triggering Notification with channel
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(random.nextInt(), notification);

        Reminder.historyReminder(daoReminder, temp.id);
        Reminder.lessCount(daoReminder, temp.id);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }


    //Making Notification
    public Notification getNotification(String medName, String medNote, String medTime) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, d_notification_channel_id);
        builder.setContentTitle(medNote);
        builder.setContentText("Medicine: " + medName + "   " + "Time: " + medTime);
        builder.setSmallIcon(R.drawable.a);
        builder.setAutoCancel(true);
        builder.setChannelId(CHANNEL_ID);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        return builder.build();
    }

    String getMyTime(String medTime) {
        int day, month, year, hour, min;
        String AM_PM;

        //Formatting time from string to int
        String[] timeAndDate = medTime.split("&");
        String date = timeAndDate[0].trim();
        String time = timeAndDate[1].trim();

        String[] myDate = date.split("-");
        day = Integer.parseInt(myDate[0].trim());
        month = Integer.parseInt(myDate[1].trim());
        year = Integer.parseInt(myDate[2].trim());

        String[] myTime = time.split(":");
        hour = Integer.parseInt(myTime[0].trim());
        min = Integer.parseInt(myTime[1].trim());

        if (hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        return String.valueOf(hour) + ":" + String.valueOf(min) + " " + AM_PM + "  " +
                String.valueOf(day) + "-" +
                String.valueOf(month) + "-" +
                String.valueOf(year);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}