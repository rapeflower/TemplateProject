package com.photo.selector.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.photo.selector.R;
import com.photo.selector.model.PhotoModel;

import java.util.List;
import java.util.Random;

public class PhotoAdapter extends BaseAdapter{

    private List<PhotoModel> models;
    private Context mContext;
    private LayoutInflater mInflater;
    OnItemCheckedChangedListener mOnItemCheckedChangedListener;

    public interface OnItemCheckedChangedListener {
        void onItemCheckedChanged(CompoundButton chBox, boolean isChecked, PhotoModel photoModel);
        void onShowPicture(String path, int position);
    }

    public PhotoAdapter(Context context, List<PhotoModel> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.models = list;
    }

    /**
     *
     * @param onItemCheckedChangedListener
     */
    public void setOnItemCheckedChangedListener(OnItemCheckedChangedListener onItemCheckedChangedListener) {
        this.mOnItemCheckedChangedListener = onItemCheckedChangedListener;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public Object getItem(int position) {
        return models == null ? null : models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_photo_item, null);
            holder = new ViewHolder();
            holder.photoView = (ImageView) convertView.findViewById(R.id.iv_photo_selector);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_photo_selector);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PhotoModel photoModel = models.get(position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(mContext).load("file://" + photoModel.getOriginalPath())
                        .placeholder(R.drawable.ic_picture_loading)
                        .error(R.drawable.ic_picture_loadfailed)
                        .crossFade()
                        .into(holder.photoView);
            }
        }, new Random().nextInt(10));

        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setTag(position);
        holder.cbSelect.setChecked(photoModel.isChecked());
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int index = (int) buttonView.getTag();
//                android.util.Log.w("log", " index = " + index);
                models.get(index).setChecked(isChecked);
                if (mOnItemCheckedChangedListener != null) {
                    mOnItemCheckedChangedListener.onItemCheckedChanged(buttonView, isChecked, photoModel);
                }
            }
        });

        holder.photoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnItemCheckedChangedListener != null) {
                    String path = photoModel.getOriginalPath();
                    mOnItemCheckedChangedListener.onShowPicture(path, position);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView photoView;
        CheckBox cbSelect;
    }
}
