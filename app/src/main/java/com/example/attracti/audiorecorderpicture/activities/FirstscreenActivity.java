package com.example.attracti.audiorecorderpicture.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.adapters.ItemAdapter;
import com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter;
import com.example.attracti.audiorecorderpicture.model.Item;
import com.example.attracti.audiorecorderpicture.utils.SdCardDataRetriwHеlper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Iryna on 5/17/16.
 * <p>
 * First screen of the project with RecyclerView  (shows pictures of the all projects)
 */


public class FirstscreenActivity extends AppCompatActivity implements
        ItemAdapter.ItemListener {

    private static final String LOG_TAG = FirstscreenActivity.class.getSimpleName();

    private LruCache<String, Bitmap> mMemoryCache;

    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String mCurrentProject = null;
    private RecyclerView mList;

    public LruCache<String, Bitmap> getmMemoryCache() {
        return mMemoryCache;
    }

    private BottomSheetDialog mBottomSheetDialog;
    BottomSheetBehavior behavior;
    private ItemAdapter mAdapterItem;
    private FloatingActionButton floatButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View viewCustom = getLayoutInflater().inflate(R.layout.custom_toolbar, null);
        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(viewCustom, layout);

        Window window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        mList = (RecyclerView) findViewById(R.id.list);

        mList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(FirstscreenActivity.this, SdCardDataRetriwHеlper.getFromSdcardFolders());
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        floatButton = (FloatingActionButton) findViewById(R.id.float_button);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, 700);
                } else {
                    mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                //1100

                // LinearLayout.LayoutParams.WRAP_CONTENT
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

            }
        });


        View bottomSheet = findViewById(R.id.bottom_sheet);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


        mAdapterItem = new ItemAdapter(createItems(), this);
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

    private void showBottomSheetDialog() {

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        mBottomSheetDialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter(createItems(), new ItemAdapter.ItemListener() {
            @Override
            public void onItemClick(Item item) {
                if (mBottomSheetDialog != null) {
                    if (item.getTitle() == "Camera") {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                        Date now = new Date();
                        mCurrentProject = String.valueOf(formatter.format(now));
                        Intent nextScreen = new Intent(getApplicationContext(), AudioRecordActivity.class);
                        nextScreen.putExtra("currentProject", mCurrentProject);
                        startActivity(nextScreen);
                    } else if (item.getTitle() == "Gallery") {
                        Intent galleryScreen = new Intent(getApplicationContext(), GalleryActivity.class);
                        startActivity(galleryScreen);
                    }

                    mBottomSheetDialog.dismiss();
                }
            }
        }));

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapterItem.setListener(null);
    }

    public List<Item> createItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("New record"));
        items.add(new Item(R.drawable.camera_gray, "Camera"));
        items.add(new Item(R.drawable.folder_multiple_gray, "Gallery"));
        items.add(new Item(R.drawable.google_drive, "Google Drive"));
        return items;
    }


    @Override
    public void onItemClick(Item item) {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}

