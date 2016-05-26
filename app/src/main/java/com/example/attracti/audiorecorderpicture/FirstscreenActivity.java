package com.example.attracti.audiorecorderpicture;

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

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 5/17/16.
 */

/*
* First screen of the project with RecyclerView  (shows pictures of the all projects)
*/

public class FirstscreenActivity extends AppCompatActivity {

    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    RecyclerView list;

    File[] listFile;
    File[] listFolders;

    ArrayList<Folder> FOLDERS = new ArrayList<>();
    public static LruCache<String, Bitmap> mMemoryCache;


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Picture");
        if (file.isDirectory()) {
            listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Picture", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    listFile = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
                Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        list = (RecyclerView) findViewById(R.id.list);

        list.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        getFromSdcardFolders();

        list.setLayoutManager(mLayoutManager);

        Log.wtf("TAG", "Folders size!!:" + FOLDERS.size());
        mAdapter = new MyAdapter(this, FOLDERS);
        list.setAdapter(mAdapter);
       // mAdapter.notifyDataSetChanged();

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItem:
                Intent nextScreen = new Intent(getApplicationContext(), AudioRecord.class);
                startActivity(nextScreen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

