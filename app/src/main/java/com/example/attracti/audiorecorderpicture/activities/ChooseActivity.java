package com.example.attracti.audiorecorderpicture.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.GridChooseAdapter;

import java.io.File;

/**
 * Created by Iryna on 6/30/16.
 *
 * In this class user can choose pictures from the folders of his/her phone
 *
 */

public class ChooseActivity extends Activity {

    private File[] listFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_grid);


        Intent intent = getIntent();
        listFile = (File[]) intent.getSerializableExtra("LIST_FILES");

        //Todo: test how to work adapter GridChooseAdapter

        GridChooseAdapter adapter = new GridChooseAdapter(this, listFile);
        GridView gridView = (GridView) findViewById(R.id.gridChooseView);
        gridView.setAdapter(adapter);

    }
}
