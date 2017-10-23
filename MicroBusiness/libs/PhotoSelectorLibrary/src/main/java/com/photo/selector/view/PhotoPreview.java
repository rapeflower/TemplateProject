package com.photo.selector.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.photo.selector.R;
import com.photo.selector.model.PhotoModel;
import com.photo.selector.polites.GestureImageView;

public class PhotoPreview extends LinearLayout implements OnClickListener {

	private ProgressBar pbLoading;
	private GestureImageView ivContent;
	private Context mContext;
	private OnClickListener l;

	public PhotoPreview(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);

		pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	public void loadImage(PhotoModel photoModel) {
		loadImage("file://" + photoModel.getOriginalPath());
	}

	private void loadImage(String path) {
		DrawableTypeRequest<String> drawableTypeRequest = Glide.with(mContext).load(path);
		drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.placeholder(R.drawable.ic_picture_loading)
				.error(R.drawable.ic_picture_loadfailed)
				.crossFade();

		drawableTypeRequest.asBitmap().into(new ImageViewTarget<Bitmap>(ivContent) {
			@Override
			protected void setResource(Bitmap resource) {
				ivContent.setImageBitmap(resource);
				pbLoading.setVisibility(View.GONE);
			}

			@Override
			public void onLoadFailed(Exception e, Drawable errorDrawable) {
				ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
				pbLoading.setVisibility(View.GONE);
				super.onLoadFailed(e, errorDrawable);
			}
		});
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	};

}
