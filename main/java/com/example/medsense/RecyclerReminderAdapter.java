package com.example.medsense;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerReminderAdapter extends RecyclerView.Adapter<RecyclerReminderAdapter.ViewHolder> {

    ArrayList<Reminder> reminder;

    RecyclerReminderAdapter(ArrayList<Reminder> rem) {
        this.reminder = rem;
    }

    @NonNull
    @Override
    public RecyclerReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerReminderAdapter.ViewHolder holder, int position) {
        int day, month, year, hour, min;
        String medTime, AM_PM;
        holder.medicineName.setText(reminder.get(position).MedicineName);
        holder.reminderNote.setText(reminder.get(position).ReminderNote);
        holder.status.setText(reminder.get(position).Status);
        holder.medCount.setText(String.valueOf(reminder.get(position).TabletCount));

        medTime = reminder.get(position).time;

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
        holder.time.setText(thisTime);
    }

    @Override
    public int getItemCount() {
        return reminder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, reminderNote, status, medCount, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            reminderNote = itemView.findViewById(R.id.medicineNote);
            status = itemView.findViewById(R.id.medicineStatus);
            medCount = itemView.findViewById(R.id.medicineCount);
            time = itemView.findViewById(R.id.medTime);
        }
    }
}
