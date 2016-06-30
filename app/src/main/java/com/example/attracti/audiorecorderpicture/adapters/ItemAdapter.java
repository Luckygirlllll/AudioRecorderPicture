package com.example.attracti.audiorecorderpicture.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.model.Item;

import java.util.List;

/**
 * Created by Iryna on 6/24/16.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItems;
    private ItemListener mListener;

    public ItemAdapter(List<Item> items, ItemListener listener) {
        mItems = items;
        mListener = listener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new ViewHolderModified(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bottom_title, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            ViewHolderModified  holderModified = (ViewHolderModified)holder;
            holderModified.setData(mItems.get(position));
        }
        else {
            ViewHolder  viewHolder = (ViewHolder)holder;
            viewHolder.setData(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView textView;
        public Item item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setData(Item item) {
            this.item = item;
            imageView.setImageResource(item.getDrawableResource());
            textView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    public class ViewHolderModified extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public Item item;

        public ViewHolderModified(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.titleView);
        }

        public void setData(Item item) {
            this.item = item;
            textView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }


    public interface ItemListener {
        void onItemClick(Item item);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
