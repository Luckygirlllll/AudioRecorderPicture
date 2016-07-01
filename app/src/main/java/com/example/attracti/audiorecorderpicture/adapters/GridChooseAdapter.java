package com.example.attracti.audiorecorderpicture.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/30/16.
 */
public class GridChooseAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList items;

    public GridChooseAdapter(Context context, ArrayList items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choose_item, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        //  Bitmap image = items.get(position).getImage();
        File image = new File(String.valueOf(items.get(position)));

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //  Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
       Log.wtf("items get(position)", String.valueOf(items.get(position)));

        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(items.get(position)));
//        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        imageView.setImageBitmap(bitmap);

//        if (image != null){
//            imageView.setImageBitmap(image);
//        }
//        else {
        // If no image is provided, display a folder icon.
        //    imageView.setImageResource(R.drawable.your_folder_icon);
//        }

        return convertView;
    }
}

