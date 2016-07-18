package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.GridViewAdapter;
import com.example.attracti.audiorecorderpicture.model.Folder;
import com.example.attracti.audiorecorderpicture.utils.SdCardDataRetriwHеlper;

import java.util.ArrayList;

/**
 * Created by Iryna on 6/30/16.
 * <p>
 * This is the class which displays pictures from the folders of the user phone
 */

public class GalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
    private String pathExtra = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PICTURES";

    private ArrayList<Folder> FOLDERS = null;

    private Button backButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        FOLDERS = SdCardDataRetriwHеlper.getFromSdcardPicturesFolders(path);



        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), FOLDERS);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        backButton= (Button) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstScreen = new Intent(getApplicationContext(), FirstscreenActivity.class);
                startActivity(firstScreen);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent chooseScreen = new Intent(getApplicationContext(), ChooseActivity.class);
        chooseScreen.putExtra("LIST_FILES", FOLDERS.get(position).getPictureList());
        chooseScreen.putExtra("FOLDER_NAME", FOLDERS.get(position).getName());
        startActivity(chooseScreen);
    }
}
