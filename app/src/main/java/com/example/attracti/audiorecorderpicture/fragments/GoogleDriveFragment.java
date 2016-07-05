package com.example.attracti.audiorecorderpicture.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attracti.audiorecorderpicture.R;

/**
 * Created by Iryna on 7/5/16.
 */
public class GoogleDriveFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.test2, container, false);
        return rootView;
    }
}
