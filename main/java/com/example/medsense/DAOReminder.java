package com.example.medsense;

import java.util.ArrayList;

public interface DAOReminder {
    void addReminder(Reminder inReminder);

    void addNotification(String inReminderID, Long inTime);

    void historyReminder(String reminderID);

    void lessCountReminder(String reminderID);

    Reminder loadReminder(String reminderID);

    ArrayList<Reminder> loadReminderList();

    ArrayList<Reminder> loadReminderHistory();

    Reminder loadLast();


}
