package com.example.attracti.audiorecorderpicture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/1/16.
 *
 * in this class displays pictures from the certain project with labels and here user can listen the audio record
 * both from labels and in the order in which it was displayed.
 *
 */


public class ViewActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    public static ArrayList<Folder> FOLDERS = new ArrayList<>();


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Pictures");
        if (file.isDirectory()) {
            File[] listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());
                Log.i("List of folders: ", String.valueOf(listFolders[i].getName()));

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Pictures", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    File[] listFile
                            = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
                Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }

    public void readFromFile() {
//        Log.i("read ", "in Fragment");
//        StringBuilder text = new StringBuilder();
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//
//                text.append(line);
//                text.append('\n');
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i("TextInfo", String.valueOf(text));
//
//        filetime2 = text.toString().split("\n");
//        //  Arrays.sort(filetime2);
//        for (int i = 0; i < filetime2.length; i = i + 3) {
//            Log.i("Array 2", filetime2[i]);
//            String n = filetime2[i];
//            filetime3.add(filetime2[i]);
//            Log.i("FIletime 3 size", String.valueOf(filetime3.size()));
//        }
//        for (int i = 1; i < filetime2.length; i = i + 3) {
//            Log.i("Coordinates of X: ", filetime2[i]);
//            String n = filetime2[i];
//            xcoordin.add(filetime2[i]);
//        }
//
//        for (int i = 2; i < filetime2.length; i = i + 3) {
//            Log.i("Coordinates of Y: ", filetime2[i]);
//            String n = filetime2[i];
//            ycoordin.add(filetime2[i]);
//        }
    }








    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        FirstscreenActivity.listFile[0].getAbsolutePath();

        getFromSdcardFolders();
        Intent intent = getIntent();
        File [] array = (File []) intent.getSerializableExtra("FILE_TAG");

        AdapterViewProject viewAdapter = new AdapterViewProject(this, array );
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        for(int i=0; i<array.length; i++){
            Log.wtf("Array elements ", String.valueOf(array[i]));
        }




    }

}
