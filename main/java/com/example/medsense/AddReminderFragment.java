package com.example.medsense;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AddReminderFragment extends Fragment {

    String timeToNoti;
    EditText inputMedName, inputMedNote, inputMedCount, inputMedTime, inputMedDate;
    Button btn_inputMedDone;
    Button btn_showHistory;
    AddReminderListener listener;
    AdRequest adRequest;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddReminderListener) {
            listener = (AddReminderListener) context;
        }
    }

    public AddReminderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);

        List<String> testDeviceIds = Collections.singletonList("3364B3CA6F57B33D4BA470665645F82E");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this.requireContext(), initializationStatus -> {
        });
        MobileAds.setRequestConfiguration(configuration);

        AdView mAdView = view.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inputMedName = view.findViewById(R.id.medicineNameInput);
        inputMedNote = view.findViewById(R.id.medicineNoteInput);
        inputMedCount = view.findViewById(R.id.medicineCountInput);
        inputMedTime = view.findViewById(R.id.medicineTimeInput);
        inputMedDate = view.findViewById(R.id.medicineDateInput);

        inputMedTime.setOnClickListener(v -> timePop());
        inputMedDate.setOnClickListener(v -> datePop());

        btn_inputMedDone = view.findViewById(R.id.medInputDone);
        btn_showHistory = view.findViewById(R.id.medHistory);

        btn_inputMedDone.setOnClickListener(v -> dataEnteredListener());
        btn_showHistory.setOnClickListener(v -> listener.showHistory());

        return view;
    }

    public void dataEnteredListener(){
        String name = String.valueOf(inputMedName.getText());
        String note = String.valueOf(inputMedNote.getText());
        String count = String.valueOf(inputMedCount.getText());
        String time = timeToNoti;
        String date = String.valueOf(inputMedDate.getText());
        String dateANDtime = date + "&" + time;
        if(name.isEmpty() || note.isEmpty() || count.isEmpty() || time.isEmpty()){
            if(name.isEmpty()){
                inputMedName.setError("ERROR: Field Required");
            }
            else if(note.isEmpty()){
                inputMedNote.setError("ERROR: Field Required");
            }
            else if(count.isEmpty()){
                inputMedCount.setError("ERROR: Field Required");
            }
        }
        else {
            listener.AddReminder(name,note,Integer.parseInt(count),dateANDtime);
        }
    }

    //DatePicker Dialogue with present Date at default
    public void datePop(){
        int mDay,mMonth,mYear;
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            inputMedDate.setText(String.format(Locale.getDefault(),"%2d-%2d-%4d",dayOfMonth,month,year));
        };
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),onDateSetListener,mYear,mMonth,mDay);
        datePickerDialog.setTitle("SELECT DATE:");
        datePickerDialog.show();
    }

    //TimePicker dialogue with present time at default
    public void timePop(){
        int mMin,mHour;
        final String[] AM_PM = new String[1];
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            if(hourOfDay < 12){
                AM_PM[0] = "AM";
            }
            else {
                AM_PM[0] = "PM";
            }
            timeToNoti = hourOfDay+":"+minute;
            inputMedTime.setText(String.format(Locale.getDefault(),"%2d : %2d %s",hourOfDay,minute,AM_PM[0]));
        };
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),onTimeSetListener,mHour,mMin,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public interface AddReminderListener {
        void AddReminder(String name,String note,int count,String time);
        void showHistory();
    }
}