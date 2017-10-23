package com.photo.selector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;

import com.photo.selector.R;
import com.photo.selector.model.PhotoModel;
import com.photo.selector.view.PhotoItem;
import com.photo.selector.view.PhotoItem.onItemClickListener;
import com.photo.selector.view.PhotoItem.onPhotoItemCheckedListener;

import java.util.ArrayList;

@Deprecated
public class PhotoSelectorAdapter extends XBaseAdapter<PhotoModel> {

	private int itemWidth;
	private int horizontalNum = 3;
	private onPhotoItemCheckedListener listener;
	private LayoutParams itemLayoutParams;
	private onItemClickListener mCallback;

	private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
		super(context, models);
	}

	public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models, int screenWidth,
                                onPhotoItemCheckedListener listener, onItemClickListener mCallback) {
		this(context, models);
		setItemWidth(screenWidth);
		this.listener = listener;
		this.mCallback = mCallback;
	}

	/**
	 * 设置每一个Item的宽高
	 *
	 * @param screenWidth 屏幕宽度
	 */
	public void setItemWidth(int screenWidth) {
		int horizontalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
		this.itemWidth = (screenWidth - (horizontalSpace * (horizontalNum - 1))) / horizontalNum;
		this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = null;
		// 显示图片
		if (convertView == null || !(convertView instanceof PhotoItem)) {
			item = new PhotoItem(context, listener);
			item.setLayoutParams(itemLayoutParams);
			convertView = item;
		} else {
			item = (PhotoItem) convertView;
		}

		item.setImageDrawable(models.get(position));
		item.setSelected(models.get(position).isChecked());

		item.setOnClickListener(mCallback, position);
		return convertView;
	}
}
