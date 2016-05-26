package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Iryna on 5/25/16.
 */
public class RecyclerViewFragment extends Fragment {

    public static MyAdapter2 mAdapter;
    private LinearLayoutManager mLayoutManager;

    static RecyclerView list;

    File[] listFile;
    File[] listFolders;

    ArrayList<Folder> FOLDERS = new ArrayList<>();
    public static LruCache<String, Bitmap> mMemoryCache;

    MyAdapter2 myAdapter2 = new MyAdapter2(getActivity(), FOLDERS);

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
                //   Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }


//    private void updateList(){
//        mAdapter.notifyDataSetChanged();
//        list.scrollBy();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.recyclerview, container, false);

        list = (RecyclerView) rootView.findViewById(R.id.list);


        /*
        * Check when view has been recycled
        */
        list.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                Log.i("TAG", "View has been recycled");
                int adaptposition = MyAdapter2.vh.getAdapterPosition();
                Log.wtf("TAG", "adapter position of the recycled item: " + adaptposition);
                // TODO: 5/24/16  cancel AsyncTask of the exactly position isCancelled(), cancel()
               // BitmapWorkerTask.cancel(true)

                Iterator iterator = MyAdapter2.TASKS_MAP.keySet().iterator();

                if (MyAdapter2.TASKS_MAP.get(adaptposition) != null) {
                  //  MyAdapter2.TASKS_MAP.get(adaptposition).cancel(true);
                    Log.wtf("TAG", "AsyncTask was cancelled! " + adaptposition);
                }
              }

            //           }
        });

        list.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int position1 = mLayoutManager.findFirstVisibleItemPosition();
                int position2 = mLayoutManager.findLastVisibleItemPosition();
                Log.wtf("TAG", "First visible position " + position1);
                Log.wtf("TAG", "Last visible position " + position2);
            }
        });


        list.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.findLastCompletelyVisibleItemPosition();

        getFromSdcardFolders();

        list.setLayoutManager(mLayoutManager);

        // Log.wtf("TAG", "Folders size!!:" + FOLDERS.size());
        mAdapter = new MyAdapter2(getActivity(), FOLDERS);
        list.setAdapter(mAdapter);
       // mAdapter.notifyDataSetChanged();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache
                = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        return rootView;
    }
}

