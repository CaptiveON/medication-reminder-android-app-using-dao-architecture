package com.example.medsense;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class SetAlertService extends Service {

    private static final String LOG_TAG = "SetAlertService: ";
    private IBinder mBinder = new MyBinder();
    DAOReminder daoReminder;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        daoReminder = new MedSQLiteDAO(this);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
    }

    public void SetAlertFromDB(String reminderID) {
        Reminder temp = Reminder.loadSingleReminder(daoReminder, reminderID);
        setNotification(temp.id, temp.time);
    }

    public void setNotification(String id, String medTime) {
        int day, month, year, hour, min;
        Random random = new Random();
        String AM_PM;
        Calendar calendar;
        Intent notificationIntent;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;

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

        String thisTime = String.valueOf(hour) + ":" + String.valueOf(min) + " " + AM_PM + "  " +
                String.valueOf(day) + "-" +
                String.valueOf(month) + "-" +
                String.valueOf(year);

        Log.d(LOG_TAG + ": REMINDER DATE AND TIME:", thisTime);

        //Setting calender time to set Reminder
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONDAY, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        Reminder.addNotification(daoReminder,id, calendar.getTimeInMillis());

        notificationIntent = new Intent(this, notiReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, random.nextInt(), notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public class MyBinder extends Binder {
        SetAlertService getService() {
            return SetAlertService.this;
        }
    }
}
