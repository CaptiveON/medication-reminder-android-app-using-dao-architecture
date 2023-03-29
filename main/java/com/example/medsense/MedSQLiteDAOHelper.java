package com.example.medsense;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MedSQLiteDAOHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedReminder";
    private static final int DATABASE_VERSION = 1;

    public static final String REMINDER_TABLE_NAME = "Reminder";
    public static final String REMINDER_ID = "id";
    public static final String REMINDER_MEDICINE_NAME = "MedicineName";
    public static final String REMINDER_NOTE = "ReminderNote";
    public static final String REMINDER_STATUS = "status";
    public static final String REMINDER_MEDICINE_COUNT = "TabletCount";
    public static final String REMINDER_TIME = "time";
    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE " +
            REMINDER_TABLE_NAME +
            " (" +
            REMINDER_ID + " TEXT PRIMARY KEY, " +
            REMINDER_MEDICINE_NAME + " TEXT, " +
            REMINDER_NOTE + " TEXT, " +
            REMINDER_STATUS + " TEXT, " +
            REMINDER_MEDICINE_COUNT + " INTEGER," +
            REMINDER_TIME + " TEXT" +
            ")";

    public static final String REMINDER_HISTORY_TABLE_NAME = "ReminderHistory";
    public static final String REMINDER_HISTORY_ID = "id";
    private static final String CREATE_TABLE_REMINDER_HISTORY = "CREATE TABLE " +
            REMINDER_HISTORY_TABLE_NAME +
            " (" +
            REMINDER_HISTORY_ID + " TEXT, " +
            "FOREIGN KEY (" +
            REMINDER_HISTORY_ID + ") REFERENCES " +
            REMINDER_TABLE_NAME + " (" + REMINDER_ID +
            "))";

    public static final String NOTIFICATION_TABLE_NAME = "Notification";
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_TIME = "time";
    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " +
            NOTIFICATION_TABLE_NAME +
            " (" +
            NOTIFICATION_ID + " TEXT, " +
            NOTIFICATION_TIME + " BLOB, " +
            "FOREIGN KEY (" +
            NOTIFICATION_ID + ") REFERENCES " +
            REMINDER_TABLE_NAME + " (" + REMINDER_ID +
            "))";


    public MedSQLiteDAOHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REMINDER);
        db.execSQL(CREATE_TABLE_REMINDER_HISTORY);
        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE_NAME);
        onCreate(db);
    }
}
