package com.example.attracti.audiorecorderpicture.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.widgets.dynamicgrid.BaseDynamicGridAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 7/5/16.
 */


public class SortDynamicAdapter extends BaseDynamicGridAdapter {


    ArrayList pictureList;

    public ArrayList getPictureList() {
        return pictureList;
    }

        public SortDynamicAdapter(Context context, List<?> items, int columnCount) {
            super(context, items, columnCount);
            pictureList= (ArrayList) items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheeseViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
                holder = new CheeseViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (CheeseViewHolder) convertView.getTag();
            }
            holder.build(position);
            return convertView;
        }

        private class CheeseViewHolder {
            private ImageView image;

            private CheeseViewHolder(View view) {
                image = (ImageView) view.findViewById(R.id.item_img);
            }

            void build( int position) {
                Bitmap bitmap = decodeSampledBitmapFromResource((String)pictureList.get(position), 100, 100);
                image.setImageBitmap(bitmap);
            }
        }
    }



