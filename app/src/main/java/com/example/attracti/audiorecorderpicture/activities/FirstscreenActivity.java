package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter;
import com.example.attracti.audiorecorderpicture.model.Folder;
import com.example.attracti.audiorecorderpicture.model.RecyclerItemClickListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Iryna on 5/17/16.
 * <p>
 * First screen of the project with RecyclerView  (shows pictures of the all projects)
 */


public class FirstscreenActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    private static final String LOG_TAG = FirstscreenActivity.class.getSimpleName();

    private  LruCache<String, Bitmap> mMemoryCache;

    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String mCurrentProject = null;
    private RecyclerView mList;

    private File[] listFile;
    private File[] listFolders;

    private ArrayList<Folder> FOLDERS = null;
    private File[] listFile2;

    public LruCache<String, Bitmap> getmMemoryCache() {
        return mMemoryCache;
    }

    public void getFromSdcardFolders() {
        FOLDERS = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Previews");
        if (file.isDirectory()) {
            listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());
                Log.i("List of FOLDERS: ", String.valueOf(listFolders[i].getName()));

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Previews", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    listFile = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        mList = (RecyclerView) findViewById(R.id.list);

        mList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        getFromSdcardFolders();

        mList.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(FirstscreenActivity.this, FOLDERS);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache
                = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) mList.getAdapter();
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItem:

// ----  you can delete it soon
//                AudioRecord.arrayFilepaths2.clear();
//                ChildFragment.xcoordList.clear();
//                ChildFragment.ycoordList.clear();
//                ChildFragment.positionList.clear();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                Date now = new Date();
                mCurrentProject = String.valueOf(formatter.format(now));
                Log.d("mCurrentProject ", "in FirstScreenActivity " + mCurrentProject);
                Intent nextScreen = new Intent(getApplicationContext(), AudioRecord.class);
                nextScreen.putExtra("currentProject", mCurrentProject);
                startActivity(nextScreen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View childView, int position) {

        File picturelist2 = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture/Pictures", listFolders[position].getName());
        if (picturelist2.isDirectory()) {
            listFile2 = picturelist2.listFiles();
        }
        Intent viewScreen = new Intent(getApplicationContext(), ViewActivity.class);
        viewScreen.putExtra("FILE_TAG", listFile2);
        viewScreen.putExtra("listFile", listFile );
        startActivity(viewScreen);

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}

