package com.photo.selector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.photo.selector.model.AlbumModel;
import com.photo.selector.view.AlbumItem;

import java.util.ArrayList;

public class AlbumAdapter extends XBaseAdapter<AlbumModel> {

	public AlbumAdapter(Context context, ArrayList<AlbumModel> models) {
		super(context, models);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlbumItem albumItem = null;
		if (convertView == null) {
			albumItem = new AlbumItem(context);
			convertView = albumItem;
		} else {
			albumItem = (AlbumItem) convertView;
		}
		albumItem.update(models.get(position));
		return convertView;
	}

}
