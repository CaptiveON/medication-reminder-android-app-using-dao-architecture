package com.example.medsense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    RecyclerHistoryAdapter recyclerHistoryAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;

    ArrayList<Reminder> reminderHistory = new ArrayList<>();

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        if(getArguments() != null){
            reminderHistory = getArguments().getParcelableArrayList("ReminderHistoryListFromMain");
        }
        else
        {
            Log.d("ERROR IN HOME-PAGE FRAGMENT", "onCreateView: Null ArrayList reminder");
        }

        recyclerView = view.findViewById(R.id.recyclerHistory);

        recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerLayoutManager);

        recyclerHistoryAdapter = new RecyclerHistoryAdapter(reminderHistory);
        recyclerView.setAdapter(recyclerHistoryAdapter);

        return view;
    }
}