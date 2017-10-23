package com.photo.selector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.photo.selector.R;
import com.photo.selector.model.AlbumModel;

public class AlbumItem extends LinearLayout {

	private ImageView ivAlbum, ivIndex;
	private TextView tvName, tvCount;
	private Context mContext;

	public AlbumItem(Context context) {
		this(context, null);
	}

	public AlbumItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_album, this, true);

		ivAlbum = (ImageView) findViewById(R.id.iv_album_la);
		ivIndex = (ImageView) findViewById(R.id.iv_index_la);
		tvName = (TextView) findViewById(R.id.tv_name_la);
		tvCount = (TextView) findViewById(R.id.tv_count_la);
	}

	public AlbumItem(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	/**
	 * 设置相册封面
	 *
	 * @param path
	 */
	public void setAlbumImage(String path) {
		Glide.with(mContext).load("file://" + path)
				.placeholder(R.drawable.ic_picture_loading)
				.error(R.drawable.ic_picture_loadfailed)
				.crossFade()
				.into(ivAlbum);
	}

	/**
	 * 更新"相册"信息
	 *
	 * @param album
	 */
	public void update(AlbumModel album) {
		setAlbumImage(album.getRecent());
		setName(album.getName());
		setCount(album.getCount());
		isCheck(album.isCheck());
	}

	public void setName(CharSequence title) {
		tvName.setText(title);
	}

	public void setCount(int count) {
		tvCount.setText(count + "张");
	}

	public void isCheck(boolean isCheck) {
		if (isCheck) {
			ivIndex.setVisibility(View.VISIBLE);
		} else {
			ivIndex.setVisibility(View.GONE);
		}
	}

}
