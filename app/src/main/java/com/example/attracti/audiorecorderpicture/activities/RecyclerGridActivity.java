package com.example.attracti.audiorecorderpicture.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.RecyclerListAdapter;
import com.example.attracti.audiorecorderpicture.widgets.helper.OnStartDragListener;
import com.example.attracti.audiorecorderpicture.widgets.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

/**
 * Created by Iryna on 7/11/16.
 */

public class RecyclerGridActivity extends Activity implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;

    ArrayList pictureList;
    private View doneButton;

    public RecyclerGridActivity() {
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_sort);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        Intent intent = getIntent();
        pictureList = (ArrayList) intent.getSerializableExtra("chooseItems");
        final RecyclerListAdapter adapter = new RecyclerListAdapter(getApplicationContext(), this, pictureList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_sorted);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        final int spanCount = getResources().getInteger(R.integer.grid_columns);
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        doneButton = (View) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent readyRecord = new Intent(getApplicationContext(), ReadyRecordActivity.class);
                ArrayList sortedItems = adapter.getmItems();
                readyRecord.putExtra("sortedItems",sortedItems);
                startActivity(readyRecord);
                finish();
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
