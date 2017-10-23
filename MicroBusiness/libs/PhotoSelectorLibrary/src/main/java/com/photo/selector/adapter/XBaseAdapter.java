package com.photo.selector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class XBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected ArrayList<T> models;

	public XBaseAdapter(Context context, ArrayList<T> models) {
		this.context = context;
		if (models == null)
			this.models = new ArrayList<T>();
		else
			this.models = models;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * 更新数据
	 *
	 * @param models
	 */
	public void notifyDataSetChanged(List<T> models) {
		if (models == null)
			return;
		this.models.clear();
		for (T t : models) {
			this.models.add(t);
		}
		notifyDataSetChanged();
	}

	public ArrayList<T> getItems() {
		return models;
	}

}
