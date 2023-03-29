package com.example.medsense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {

    RecyclerReminderAdapter recyclerReminderAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;

    ArrayList<Reminder> reminder = new ArrayList<>();
    Button btn_addReminder,btn_buyMedicine;

    public HomePageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        if(getArguments() != null){
            reminder = getArguments().getParcelableArrayList("ReminderListFromMain");
        }
        else
        {
            Log.d("ERROR IN HOME-PAGE FRAGMENT", "onCreateView: Null ArrayList reminder");
        }

        recyclerView = view.findViewById(R.id.recyclerReminder);

        recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerLayoutManager);

        recyclerReminderAdapter = new RecyclerReminderAdapter(reminder);
        recyclerView.setAdapter(recyclerReminderAdapter);

        return view;
    }
}