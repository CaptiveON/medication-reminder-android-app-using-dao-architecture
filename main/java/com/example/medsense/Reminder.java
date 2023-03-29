package com.example.medsense;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Reminder implements Parcelable, Serializable {
    String id;
    String MedicineName;
    String ReminderNote;
    String Status = "WILL TAKE";
    int TabletCount;
    String time;

    Reminder(String medicineName, String reminderNote, int tabletCount, String time) {
        this.MedicineName = medicineName;
        this.ReminderNote = reminderNote;
        this.TabletCount = tabletCount;
        this.time = time;
        this.id = UUID.randomUUID().toString();
    }

    public Reminder() {
    }

    void setStatus(String status) {
        this.Status = status;
    }

    String getID() {
        return this.id;
    }

    void save(DAOReminder dao) {
        if (dao != null) {
            dao.addReminder(this);
        }
    }

    static ArrayList<Reminder> load(DAOReminder dao) {
        ArrayList<Reminder> temp = new ArrayList<>();
        if (dao != null) {
            temp = dao.loadReminderList();
        }
        return temp;
    }

    static ArrayList<Reminder> loadHistory(DAOReminder dao) {
        ArrayList<Reminder> temp = new ArrayList<>();
        if (dao != null) {
            temp = dao.loadReminderHistory();
        }
        return temp;
    }

    static Reminder loadSingleReminder(DAOReminder dao, String id) {
        Reminder temp = new Reminder();
        if (dao != null) {
            temp = dao.loadReminder(id);
        }
        return temp;
    }

    static void historyReminder(DAOReminder dao, String id) {
        if (dao != null) {
            dao.historyReminder(id);
        }
    }

    static Reminder loadLastReminder(DAOReminder dao) {
        Reminder temp = new Reminder();
        if (dao != null) {
            temp = dao.loadLast();
        }
        return temp;
    }

    static void addNotification(DAOReminder dao, String tempID, Long tempTime){
        if(dao != null){
            dao.addNotification(tempID,tempTime);
        }
    }

    static void lessCount(DAOReminder dao, String id) {
        if (dao != null) {
            dao.lessCountReminder(id);
        }
    }

    protected Reminder(Parcel in) {
        id = in.readString();
        MedicineName = in.readString();
        ReminderNote = in.readString();
        Status = in.readString();
        TabletCount = in.readInt();
        time = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(MedicineName);
        dest.writeString(ReminderNote);
        dest.writeString(Status);
        dest.writeInt(TabletCount);
        dest.writeString(time);
    }
}
