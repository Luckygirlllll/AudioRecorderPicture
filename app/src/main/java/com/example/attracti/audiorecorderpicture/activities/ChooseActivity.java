package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_grid);

        Intent intent = getIntent();
        listFile = (ArrayList) intent.getSerializableExtra("LIST_FILES");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        adapter = new GridChooseAdapter(this, listFile);
        GridView gridView = (GridView) findViewById(R.id.gridChooseView);
        gridView.setAdapter(adapter);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneItem:
                Intent sortScreen = new Intent(getApplicationContext(), SortActivity.class);
                startActivity(sortScreen);
                chooseItems = adapter.getChoosedItems();
                for(int i=0; i<chooseItems.size(); i++) {
                    Log.wtf("Pictures  ", (String) chooseItems.get(i));
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
