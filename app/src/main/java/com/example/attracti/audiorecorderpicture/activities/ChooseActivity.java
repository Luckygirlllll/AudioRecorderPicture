package com.example.attracti.audiorecorderpicture.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.attracti.audiorecorderpicture.R;

/**
 * Created by Iryna on 6/30/16.
 *
 * In this class user can choose pictures from the folders of his/her phone
 *
 */

public class ChooseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_pictures);

    }
}
