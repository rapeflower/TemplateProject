package com.photo.selector.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import com.photo.selector.model.AlbumModel;
import com.photo.selector.model.PhotoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumManager {

	private ContentResolver resolver;

	public AlbumManager(Context context) {
		resolver = context.getContentResolver();
	}

	/**
	 * 获取最近照片列表
	 *
	 * @return
	 */
	public List<PhotoModel> getCurrent() {
		checkNull();
		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] { ImageColumns.DATA,
				ImageColumns.DATE_ADDED, ImageColumns.SIZE }, null, null, ImageColumns.DATE_ADDED);
		if (cursor == null || !cursor.moveToNext()) {
			return new ArrayList<PhotoModel>();
		}
		List<PhotoModel> photos = new ArrayList<PhotoModel>();
		cursor.moveToLast();
		do {
			if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
				PhotoModel photoModel = new PhotoModel();
				photoModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				photos.add(photoModel);
			}
		} while (cursor.moveToPrevious());
		closeCursor(cursor);
		return photos;
	}

	/**
	 * 获取所有相册列表
	 *
	 * @return
	 */
	public List<AlbumModel> getAlbums() {
		checkNull();
		List<AlbumModel> albums = new ArrayList<AlbumModel>();
		Map<String, AlbumModel> map = new HashMap<String, AlbumModel>();
		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] { ImageColumns.DATA,
				ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.SIZE }, null, null, null);
		if (cursor == null || !cursor.moveToNext()) {
			return new ArrayList<AlbumModel>();
		}
		cursor.moveToLast();
		//"最近照片"相册
		AlbumModel current = new AlbumModel("最近照片", 0, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)), true);
		albums.add(current);
		do {
			if (cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE)) < 1024 * 10)
				continue;

			current.increaseCount();
			String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
			if (map.keySet().contains(name)) {
				map.get(name).increaseCount();
			} else {
				AlbumModel album = new AlbumModel(name, 1, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				map.put(name, album);
				albums.add(album);
			}
		} while (cursor.moveToPrevious());
		closeCursor(cursor);
		return albums;
	}

	/**
	 * 获取对应相册下的照片
	 *
	 * @param name 相册名
	 * @return
	 */
	public List<PhotoModel> getAlbumPhoto(String name) {
		checkNull();
		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] { ImageColumns.BUCKET_DISPLAY_NAME,
				ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE }, "bucket_display_name = ?",
				new String[] { name }, ImageColumns.DATE_ADDED);
		if (cursor == null || !cursor.moveToNext()) {
			return new ArrayList<PhotoModel>();
		}
		List<PhotoModel> photos = new ArrayList<PhotoModel>();
		cursor.moveToLast();
		do {
			if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
				PhotoModel photoModel = new PhotoModel();
				photoModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				photos.add(photoModel);
			}
		} while (cursor.moveToPrevious());
		closeCursor(cursor);
		return photos;
	}

	/**
	 * Check Null
	 */
	private void checkNull() {
		if (resolver == null) {
			throw new NullPointerException("The ContentResolver is not null !!!");
		}
	}

	/**
	 * 关闭Cursor
	 */
	private void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
}
