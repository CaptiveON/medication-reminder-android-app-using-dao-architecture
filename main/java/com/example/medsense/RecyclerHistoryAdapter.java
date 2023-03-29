package com.example.medsense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {

    ArrayList<Reminder> reminderHistory;

    public RecyclerHistoryAdapter(ArrayList<Reminder> redHis) {
        this.reminderHistory = redHis;
    }

    @NonNull
    @Override
    public RecyclerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHistoryAdapter.ViewHolder holder, int position) {
        int day, month, year, hour, min;
        String medTime, AM_PM;

        holder.medName.setText(reminderHistory.get(position).MedicineName);

        medTime = reminderHistory.get(position).time;

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

        holder.medTime.setText(thisTime);
    }

    @Override
    public int getItemCount() {
        return reminderHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView medName, medTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medNameHistory);
            medTime = itemView.findViewById(R.id.medTimeHistory);
        }
    }
}
