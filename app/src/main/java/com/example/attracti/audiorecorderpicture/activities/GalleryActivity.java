package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.GridViewAdapter;
import com.example.attracti.audiorecorderpicture.model.Folder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/30/16.
 * <p>
 * This is the class which displays pictures from the folders of the user phone
 */

public class GalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
    private String pathExtra = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PICTURES";

    private File[] listFile;
    private File[] listFileExtra;
    private File[] listFolders;
    private ArrayList<Folder> FOLDERS = null;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public void getFromSdcardFolders(String path) {

        FOLDERS = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            listFolders = file.listFiles(new ImageFileFilter());

            for (int i = 0; i < listFolders.length; i++) {
                String name = listFolders[i].getName();
                if (name.toCharArray()[0] != '.') {
                    Folder folderobject = new Folder();
                    folderobject.setName(listFolders[i].getName());

                    File picturelist = new File(path, listFolders[i].getName());
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
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        getFromSdcardFolders(path);
        //  getFromSdcardFolders(pathExtra);
        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), FOLDERS);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);




        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent chooseScreen = new Intent(getApplicationContext(), ChooseActivity.class);
        chooseScreen.putExtra("LIST_FILES", FOLDERS.get(position).getPictureList());
        startActivity(chooseScreen);
    }

    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }
}
