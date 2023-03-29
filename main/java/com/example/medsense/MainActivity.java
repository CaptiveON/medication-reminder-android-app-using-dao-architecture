package com.example.medsense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddReminderFragment.AddReminderListener {

    ArrayList<Reminder> reminder = new ArrayList<>();
    ArrayList<Reminder> reminderHistory = new ArrayList<>();
    HomePageFragment homePageFragment;
    AddReminderFragment addReminderFragment;
    HistoryFragment historyFragment;
    MapFragment mapFragment;
    Button btn_addReminder, btn_showMap, btn_homePage;
    DAOReminder daoReminder;
    ReminderViewModel vm;
    private static FirebaseAnalytics firebaseAnalytics;

    //Service Connection setup and Initializing
    SetAlertService mSetAlertService;
    boolean mServiceBoundStatus;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBoundStatus = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SetAlertService.MyBinder myBinder = (SetAlertService.MyBinder) service;
            mSetAlertService = myBinder.getService();
            mServiceBoundStatus = true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SetAlertService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBoundStatus) {
            unbindService(mServiceConnection);
            mServiceBoundStatus = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        daoReminder = new MedSQLiteDAO(this);

        vm = new ViewModelProvider(this).get(ReminderViewModel.class);
        //Initializing IProductDAO and ICartDAO instances in CartViewModel
        vm.setDao(daoReminder);
        //Invoking Relevant Database accessing functions in CartViewModel using Data Access Objects
        reminder = vm.getSavedReminder(savedInstanceState, "reminder");
        reminderHistory = vm.getSavedReminderHistory(savedInstanceState, "reminderHistory");

        btn_addReminder = findViewById(R.id.addReminder);
        btn_homePage = findViewById(R.id.showHome);
        btn_showMap = findViewById(R.id.buyMap);

        //To Load AddReminderFragment
        btn_addReminder.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("addReminder","Add Med Reminder Button");
            firebaseAnalytics.logEvent("btn_addReminder",bundle);
            if(!isMulti()){
                loadAddReminderFrag();
            }});
        btn_homePage.setOnClickListener(v -> loadHomeFragReplace());

        //To Load WebView Map
        btn_showMap.setOnClickListener(v -> {
            if (!isMulti()) {
                loadMapFrag();
            } else {
                Toast.makeText(this, "Keep your device in Portrait Mode", Toast.LENGTH_LONG).show();
            }
        });
        //Load Home Fragment/Responsive Mode
        loadHomeFrag();
    }

    public void loadMapFrag() {
        mapFragment = new MapFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrag, mapFragment);
        ft.commit();
    }

    void loadAddReminderFragRef() {
        addReminderFragment = new AddReminderFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeSideFrag, addReminderFragment);
        ft.commit();
    }

    public void loadAddReminderFrag() {
        addReminderFragment = new AddReminderFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrag, addReminderFragment);
        ft.commit();
    }

    public void loadHomeFrag() {
        if (isMulti()) {
            addReminderFragment = new AddReminderFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.homeSideFrag, addReminderFragment);
            ft.commit();
        }
        homePageFragment = new HomePageFragment();
        Bundle argument = new Bundle();
        argument.putParcelableArrayList("ReminderListFromMain", reminder);
        homePageFragment.setArguments(argument);
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.add(R.id.homeFrag, homePageFragment);
        ft1.commit();
    }

    void loadHomeFragReplace() {
        //Made load Static to show instant update in reminder list
        homePageFragment = new HomePageFragment();
        reminder = Reminder.load(daoReminder);
        Bundle argument = new Bundle();
        argument.putParcelableArrayList("ReminderListFromMain", reminder);
        homePageFragment.setArguments(argument);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrag, homePageFragment);
        ft.commit();
    }

    //Implemented function for using bound service
    @Override
    public void AddReminder(String medName, String medNote, int medCount, String medTime) {
        Reminder tempReminder = new Reminder(medName, medNote, medCount, medTime);
        tempReminder.save(daoReminder);
        if (!mServiceBoundStatus) {
            Intent intent = new Intent(this, SetAlertService.class);
            bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
            mSetAlertService.SetAlertFromDB(tempReminder.id);
        } else {
            mSetAlertService.SetAlertFromDB(tempReminder.id);
            unbindService(mServiceConnection);
            mServiceBoundStatus = false;
        }

        if (isMulti()) {
            loadHomeFragReplace();
            loadAddReminderFragRef();
        } else {
            loadHomeFragReplace();
        }
    }

    @Override
    public void showHistory() {
        historyFragment = new HistoryFragment();
        reminderHistory = Reminder.loadHistory(daoReminder);
        Bundle argument = new Bundle();
        argument.putParcelableArrayList("ReminderHistoryListFromMain", reminderHistory);
        historyFragment.setArguments(argument);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrag, historyFragment);
        ft.commit();
    }

    //Setting Instance in Bundle to retrieve in case of State change or OS kills the process
    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("reminder", reminder);
        state.putSerializable("reminderHistory", reminderHistory);
    }

    public boolean isMulti() {
        return getResources().getBoolean(R.bool.multiPane);
    }
}