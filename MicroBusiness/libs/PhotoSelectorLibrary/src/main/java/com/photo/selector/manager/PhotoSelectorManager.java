package com.photo.selector.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.photo.selector.activity.PhotoSelectorActivity;
import com.photo.selector.model.AlbumModel;
import com.photo.selector.model.PhotoModel;

import java.util.List;

@SuppressLint("HandlerLeak")
public class PhotoSelectorManager {

	private AlbumManager albumManager;

	public PhotoSelectorManager(Context context) {
		albumManager = new AlbumManager(context);
	}

	/**
	 * 获取最近照片列表
	 *
	 * @param listener
	 */
	public void getRecentPhoto(final PhotoSelectorActivity.OnLocalRecentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if (listener == null) return;
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumManager.getCurrent();
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 获取相册列表
	 *
	 * @param listener
	 */
	public void getAlbums(final PhotoSelectorActivity.OnLocalAlbumListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if (listener == null) return;
				listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AlbumModel> albums = albumManager.getAlbums();
				Message msg = new Message();
				msg.obj = albums;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 获取单个相册下的所有照片信息
	 *
	 * @param name 相册名称
	 * @param listener
	 */
	public void getAlbumPhoto(final String name, final PhotoSelectorActivity.OnLocalRecentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if (listener == null) return;
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumManager.getAlbumPhoto(name);
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

}
