package com.example.medsense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapFragment extends Fragment {

    public MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        WebView map = view.findViewById(R.id.mapView);
        WebSettings settings = map.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        map.setWebViewClient(new WebViewClient());
        map.loadUrl("https://www.google.com/maps/search/Pharmacies");
        return view;
    }
}