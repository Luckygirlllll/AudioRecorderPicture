package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.GridChooseAdapter;

import java.util.ArrayList;

/**
 * Created by Iryna on 6/30/16.
 *
 * In this class user can choose pictures from the folders of his/her phone
 *
 */

public class ChooseActivity extends AppCompatActivity {

    private ArrayList listFile;
    private GridChooseAdapter adapter;
    private ArrayList chooseItems;

    private Button backButton;
    private TextView doneButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_grid);

        Intent intent = getIntent();
        listFile = (ArrayList) intent.getSerializableExtra("LIST_FILES");

        backButton=(Button) findViewById(R.id.back_button);
        doneButton =(TextView) findViewById(R.id.done);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseActivity = new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(chooseActivity);
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recyclerGridActivity = new Intent(getApplicationContext(), RecyclerGridActivity.class);
                chooseItems = adapter.getChoosedItems();
                recyclerGridActivity.putExtra("chooseItems", chooseItems);
                startActivity(recyclerGridActivity);

            }
        });

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        adapter = new GridChooseAdapter(this, listFile);
        GridView gridView = (GridView) findViewById(R.id.gridChooseView);
        gridView.setAdapter(adapter);
    }
}
