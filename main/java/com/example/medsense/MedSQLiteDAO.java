package com.example.medsense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MedSQLiteDAO implements DAOReminder {
    Context context;

    public MedSQLiteDAO(Context ctx) {
        context = ctx;
    }

    //Add and Update
    @Override
    public void addReminder(Reminder inReminder) {
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(MedSQLiteDAOHelper.REMINDER_ID, inReminder.id);
        content.put(MedSQLiteDAOHelper.REMINDER_MEDICINE_NAME, inReminder.MedicineName);
        content.put(MedSQLiteDAOHelper.REMINDER_NOTE, inReminder.ReminderNote);
        content.put(MedSQLiteDAOHelper.REMINDER_STATUS, inReminder.Status);
        content.put(MedSQLiteDAOHelper.REMINDER_MEDICINE_COUNT, inReminder.TabletCount);
        content.put(MedSQLiteDAOHelper.REMINDER_TIME, inReminder.time);

        Reminder tempReminder = loadReminder(inReminder.id);

        String[] arguments = new String[1];
        arguments[0] = inReminder.id;

        if (tempReminder != null && tempReminder.id.equals(inReminder.id)) {
            database.update(MedSQLiteDAOHelper.REMINDER_TABLE_NAME, content, "id = ?", arguments);
        } else {
            database.insert(MedSQLiteDAOHelper.REMINDER_TABLE_NAME, null, content);
        }
    }

    @Override
    public void historyReminder(String reminderID) {
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(MedSQLiteDAOHelper.REMINDER_HISTORY_ID, reminderID);

        database.insert(MedSQLiteDAOHelper.REMINDER_HISTORY_TABLE_NAME, null, content);
    }

    //Make count less by 1 so to indicate the dose has been taken
    @Override
    public void lessCountReminder(String reminderID) {
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Reminder tempReminder = loadReminder(reminderID);

        tempReminder.TabletCount = tempReminder.TabletCount - 1;
        tempReminder.Status = "TAKEN";

        ContentValues content = new ContentValues();
        content.put(MedSQLiteDAOHelper.REMINDER_ID, tempReminder.id);
        content.put(MedSQLiteDAOHelper.REMINDER_MEDICINE_NAME, tempReminder.MedicineName);
        content.put(MedSQLiteDAOHelper.REMINDER_NOTE, tempReminder.ReminderNote);
        content.put(MedSQLiteDAOHelper.REMINDER_STATUS, tempReminder.Status);
        content.put(MedSQLiteDAOHelper.REMINDER_MEDICINE_COUNT, tempReminder.TabletCount);
        content.put(MedSQLiteDAOHelper.REMINDER_TIME, tempReminder.time);

        String[] arguments = new String[1];
        arguments[0] = reminderID;
        database.update(MedSQLiteDAOHelper.REMINDER_TABLE_NAME, content, "id = ?", arguments);
    }

    //Load single reminder from database
    @Override
    public Reminder loadReminder(String reminderID) {
        Reminder tempReminder = new Reminder();
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Query: "SELECT * FROM Reminder WHERE id = ?"
        String query = "SELECT * FROM " + MedSQLiteDAOHelper.REMINDER_TABLE_NAME + " WHERE id = ?";
        String[] arguments = new String[1];
        arguments[0] = reminderID;

        Cursor cursor = database.rawQuery(query, arguments);
        cursor.moveToFirst();
        //If a record found
        if (cursor.getCount() > 0) {
            String[] column = cursor.getColumnNames();
            //Reading and initializing tempReminder with data from relevant column
            for (String col : column) {
                switch (col) {
                    case MedSQLiteDAOHelper.REMINDER_ID:
                        tempReminder.id = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_NAME:
                        tempReminder.MedicineName = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_NOTE:
                        tempReminder.ReminderNote = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_STATUS:
                        tempReminder.Status = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_COUNT:
                        tempReminder.TabletCount = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(col)));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_TIME:
                        tempReminder.time = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                }
            }
            cursor.close();
            return tempReminder;
        } else {
            cursor.close();
            return null;
        }
    }

    //Load list of reminder from database
    @Override
    public ArrayList<Reminder> loadReminderList() {
        ArrayList<Reminder> outReminder = new ArrayList<>();
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Query: "SELECT * FROM Reminder"
        String qry = "SELECT * FROM " + MedSQLiteDAOHelper.REMINDER_TABLE_NAME;
        Cursor cursor = database.rawQuery(qry, null);

        //Outer loop for each record
        while (cursor.moveToNext()) {
            String[] column = cursor.getColumnNames();
            Reminder tempReminder = new Reminder();
            //Inner loop for each column in a record
            for (String col : column) {
                switch (col) {
                    case MedSQLiteDAOHelper.REMINDER_ID:
                        tempReminder.id = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_NAME:
                        tempReminder.MedicineName = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_NOTE:
                        tempReminder.ReminderNote = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_STATUS:
                        tempReminder.Status = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_COUNT:
                        tempReminder.TabletCount = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(col)));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_TIME:
                        tempReminder.time = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                }
            }
            outReminder.add(tempReminder);
        }
        cursor.close();
        return outReminder;
    }

    @Override
    public ArrayList<Reminder> loadReminderHistory() {
        ArrayList<Reminder> tempReminderOut = new ArrayList<>();

        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Query: "SELECT * FROM Product INNER JOIN CartList ON Product.name = CartList.name"
        String qry = "SELECT * FROM " + MedSQLiteDAOHelper.REMINDER_TABLE_NAME + "," + MedSQLiteDAOHelper.REMINDER_HISTORY_TABLE_NAME + " WHERE " +
                MedSQLiteDAOHelper.REMINDER_TABLE_NAME + ".id" +
                " = " +
                MedSQLiteDAOHelper.REMINDER_HISTORY_TABLE_NAME + ".id";
        Cursor cursor = database.rawQuery(qry, null);

        while (cursor.moveToNext()) {
            String[] column = cursor.getColumnNames();
            Reminder tempReminder = new Reminder();
            //Inner loop for each column in a record
            for (String col : column) {
                switch (col) {
                    case MedSQLiteDAOHelper.REMINDER_ID:
                        tempReminder.id = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_NAME:
                        tempReminder.MedicineName = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_NOTE:
                        tempReminder.ReminderNote = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_STATUS:
                        tempReminder.Status = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_MEDICINE_COUNT:
                        tempReminder.TabletCount = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(col)));
                        break;
                    case MedSQLiteDAOHelper.REMINDER_TIME:
                        tempReminder.time = cursor.getString(cursor.getColumnIndexOrThrow(col));
                        break;
                }
            }
            tempReminderOut.add(tempReminder);
        }
        cursor.close();
        return tempReminderOut;
    }

    @Override
    public void addNotification(String inID, Long inMillis) {
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(MedSQLiteDAOHelper.NOTIFICATION_ID, inID);
        content.put(MedSQLiteDAOHelper.NOTIFICATION_TIME, inMillis);

        database.insert(MedSQLiteDAOHelper.NOTIFICATION_TABLE_NAME, null, content);
    }

    @Override
    public Reminder loadLast() {
        Reminder tempReminder = new Reminder();
        String notificationID = null;
        MedSQLiteDAOHelper dbHelper = new MedSQLiteDAOHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Query: "SELECT * FROM Reminder"
        String qry = "SELECT " + MedSQLiteDAOHelper.NOTIFICATION_ID + " FROM " + MedSQLiteDAOHelper.NOTIFICATION_TABLE_NAME +
                " WHERE " + MedSQLiteDAOHelper.NOTIFICATION_TIME + "=(" + "SELECT " + "MIN(" + MedSQLiteDAOHelper.NOTIFICATION_TIME + ")" + " FROM " + MedSQLiteDAOHelper.NOTIFICATION_TABLE_NAME + ")";
        Cursor cursor = database.rawQuery(qry, null);

        int size = cursor.getCount();
        if (size == 1) {
            cursor.moveToFirst();
            notificationID = cursor.getString(cursor.getColumnIndexOrThrow(MedSQLiteDAOHelper.NOTIFICATION_ID));
            tempReminder = loadReminder(notificationID);
        } else {
            Log.d("SQLITE RETREIVE ERROR:", "ERROR ON CURSOR");
        }

        String[] arguments = new String[1];
        arguments[0] = notificationID;
        database.delete(MedSQLiteDAOHelper.NOTIFICATION_TABLE_NAME, "id = ?", arguments);
        cursor.close();
        return tempReminder;
    }
}
