package com.example.attracti.audiorecorderpicture.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.SortDynamicAdapter;
import com.example.attracti.audiorecorderpicture.widgets.dynamicgrid.DynamicGridView;

import java.util.ArrayList;

/**
 * Created by Iryna on 7/1/16.
 */
public class SortActivity extends Activity {

    private DynamicGridView gridView;
    private ArrayList pictureList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        // gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.dynamic_grid);
        gridView = new DynamicGridView(this);
        gridView.setNumColumns(3);
        container.addView(gridView);
        Intent intent = getIntent();
        pictureList = (ArrayList) intent.getSerializableExtra("chooseItems");

        gridView.setAdapter(new SortDynamicAdapter(this,
                pictureList,
                getResources().getInteger(R.integer.column_count)));
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {

            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SortActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }
}
