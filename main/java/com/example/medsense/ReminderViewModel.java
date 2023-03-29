package com.example.medsense;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ReminderViewModel extends ViewModel {
    ArrayList<Reminder> reminder;
    ArrayList<Reminder> reminderHistory;
    DAOReminder daoReminder;

    //Returns saved instance holding list of reminder
    public ArrayList<Reminder> getSavedReminder(Bundle savedInstanceState, String key){
        if (reminder == null){
            if(savedInstanceState == null){
                if (daoReminder != null){
                    reminder = Reminder.load(daoReminder);
                }
                else reminder = new ArrayList<Reminder>();
            } else {
                reminder = (ArrayList<Reminder>) savedInstanceState.get(key);
            }
        }
        return reminder;
    }

    //Returns saved instance holding list of reminder
    public ArrayList<Reminder> getSavedReminderHistory(Bundle savedInstanceState, String key){
        if (reminderHistory == null){
            if(savedInstanceState == null){
                if (daoReminder != null){
                    reminderHistory = Reminder.loadHistory(daoReminder);
                }
                else reminderHistory = new ArrayList<Reminder>();
            } else {
                reminderHistory = (ArrayList<Reminder>) savedInstanceState.get(key);
            }
        }
        return reminderHistory;
    }

    public void setDao(DAOReminder d){
        daoReminder = d;
    }


}
