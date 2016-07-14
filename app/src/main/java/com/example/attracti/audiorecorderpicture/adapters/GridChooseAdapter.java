package com.example.attracti.audiorecorderpicture.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.async.ChooseDownloadTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/30/16.
 */
public class GridChooseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList items;
    private ArrayList choosedItems =new ArrayList();

    public ArrayList getChoosedItems() {
        return choosedItems;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choose_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.imageView.setImageBitmap(null);
            viewHolder.checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageBitmap(null);
        //  Bitmap image = items.get(position).getImage();
        File image = new File(String.valueOf(items.get(position)));

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //  Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        final String imageKey = String.valueOf(items.get(position));

        ChooseDownloadTask task = new ChooseDownloadTask(viewHolder.imageView);
        task.execute(imageKey);

//        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(items.get(position)));
//        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);

        final int checked =0;

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !choosedItems.contains(items.get(position))) {
                    choosedItems.add(items.get(position));
                }
                else{
                    choosedItems.remove(items.get(position));
                }
            }
        });


        return convertView;
    }



    static class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}

