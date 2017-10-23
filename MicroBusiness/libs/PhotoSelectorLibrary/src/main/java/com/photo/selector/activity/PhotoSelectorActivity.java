package com.photo.selector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.selector.R;
import com.photo.selector.adapter.AlbumAdapter;
import com.photo.selector.adapter.PhotoAdapter;
import com.photo.selector.manager.PhotoSelectorManager;
import com.photo.selector.model.AlbumModel;
import com.photo.selector.model.PhotoModel;
import com.photo.selector.utils.AnimationUtil;
import com.photo.selector.utils.CommonUtil;
import com.photo.selector.utils.PhotoSelector;
import com.photo.selector.view.PhotoItem.onItemClickListener;
import com.photo.selector.view.PhotoItem.onPhotoItemCheckedListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoSelectorActivity extends Activity implements onItemClickListener, onPhotoItemCheckedListener,
        OnItemClickListener, OnClickListener {

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;
	public static final String RECENT_PHOTO = "最近照片";

	private GridView gvPhotos;
	private ListView lvAlbum;
	private Button btnOk;
	private TextView tvAlbum, tvPreview, tvTitle;
	private PhotoSelectorManager photoSelectorDomain;
	private PhotoAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	private ArrayList<PhotoModel> photoData;
	private ArrayList<PhotoModel> selected;
	private int maxSelectable = 9;//图片选择的最多数量，默认是9张

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_photoselector);

		photoSelectorDomain = new PhotoSelectorManager(getApplicationContext());
		photoData = new ArrayList<PhotoModel>();
		selected = new ArrayList<PhotoModel>();

		tvTitle = (TextView) findViewById(R.id.tv_title_lh);
		gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
		lvAlbum = (ListView) findViewById(R.id.lv_album_ar);
		btnOk = (Button) findViewById(R.id.btn_right_lh);
		tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
		tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
		layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);

		btnOk.setOnClickListener(this);
		tvAlbum.setOnClickListener(this);
		tvPreview.setOnClickListener(this);

		photoAdapter = new PhotoAdapter(PhotoSelectorActivity.this, photoData);
		gvPhotos.setAdapter(photoAdapter);
		photoAdapter.setOnItemCheckedChangedListener(new PhotoAdapter.OnItemCheckedChangedListener() {
			@Override
			public void onItemCheckedChanged(CompoundButton chBox, boolean isChecked, PhotoModel photoModel) {
				if (isChecked) {
					if (getSelectedPhotoCount() > maxSelectable - 1) {
						Toast.makeText(PhotoSelectorActivity.this, "你最多只能选择" + maxSelectable + "张图片", Toast.LENGTH_SHORT).show();
						chBox.setChecked(false);
						photoModel.setChecked(false);
						selected.remove(photoModel);
					} else {
						selected.add(photoModel);
					}
					tvPreview.setEnabled(true);
				} else {
					selected.remove(photoModel);
				}

				tvPreview.setText(getResources().getString(R.string.psl_preview) + "(" + selected.size() + ")");   //修改预览数量

				if (selected.isEmpty()) {
					tvPreview.setEnabled(false);
					tvPreview.setText(getResources().getString(R.string.psl_preview));
				}
			}

			@Override
			public void onShowPicture(String path, int position) {
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putString("album", tvAlbum.getText().toString());
				PhotoSelector.launchActivity(PhotoSelectorActivity.this, PhotoPreviewActivity.class, bundle);
			}
		});

		albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
		lvAlbum.setAdapter(albumAdapter);
		lvAlbum.setOnItemClickListener(this);

		findViewById(R.id.bv_back_lh).setOnClickListener(this); //  返回

		photoSelectorDomain.getRecentPhoto(recentListener); //  更新最近照片
		photoSelectorDomain.getAlbums(albumListener); // 更新相册信息
	}

	private int getSelectedPhotoCount() {
		return selected == null ? 0 : selected.size();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_right_lh)
			ok();  // 选完照片
		else if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_preview_ar)
			preview();
		else if (v.getId() == R.id.bv_back_lh)
			finish();
	}

	/**
	 * 拍照
	 */
	private void catchPicture() {
		PhotoSelector.launchActivityForResult(this, new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			PhotoModel photoModel = new PhotoModel(CommonUtil.query(getApplicationContext(), data.getData()));
			selected.clear();
			selected.add(photoModel);
			ok();
		}
	}

	/**
	 * 完成
	 */
	private void ok() {
		if (selected.isEmpty()) {
			setResult(RESULT_CANCELED);
		} else {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", selected);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
		}
		finish();
	}

	/**
	 * 预览照片
	 */
	private void preview() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("photos", selected);
		PhotoSelector.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	private void album() {
		if (layoutAlbum.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	/**
	 * 弹出相册列表
	 */
	private void popAlbum() {
		layoutAlbum.setVisibility(View.VISIBLE);
		new AnimationUtil(getApplicationContext(), R.anim.translate_up_current).setLinearInterpolator().startAnimation(
				layoutAlbum);
	}

	/**
	 * 隐藏相册列表
	 */
	private void hideAlbum() {
		new AnimationUtil(getApplicationContext(), R.anim.translate_down).setLinearInterpolator().startAnimation(
				layoutAlbum);
		layoutAlbum.setVisibility(View.GONE);
	}

	/**
	 * 清空选中的图片
	 */
	private void reset() {
		selected.clear();
		tvPreview.setText(getResources().getString(R.string.psl_preview));
		tvPreview.setEnabled(false);
	}

	/**
	 * 点击查看照片
	 *
	 * @param position
	 */
	@Override
	public void onItemClick(int position) {
//		if (selected.size() >= maxSelectable)  {
//			if (photoData == null || photoData.size() == 0) return;
//			PhotoModel pm = photoData.get(position);
//			if (pm != null && !pm.isEnabled()) {
//				Toast.makeText(this, "你最多只能选择9张图片", Toast.LENGTH_SHORT).show();
//				return;
//			}
//		}
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putString("album", tvAlbum.getText().toString());
		PhotoSelector.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	/**
	 * 照片选中状态改变之后
	 *
	 * @param photoModel
	 * @param buttonView
	 * @param isChecked
	 */
	@Override
	public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			selected.add(photoModel);
			tvPreview.setEnabled(true);
		} else {
			selected.remove(photoModel);
		}
		tvPreview.setText(getResources().getString(R.string.psl_preview) + "(" + selected.size() + ")");   //修改预览数量

		if (selected.isEmpty()) {
			tvPreview.setEnabled(false);
			tvPreview.setText(getResources().getString(R.string.psl_preview));
		}
	}

	@Override
	public void onBackPressed() {
		if (layoutAlbum.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	/**
	 * 相册列表点击事件
	 *
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position)
				album.setCheck(true);
			else
				album.setCheck(false);
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		tvAlbum.setText(current.getName());
		tvTitle.setText(current.getName());

		// 更新照片列表
		if (current.getName().equals(RECENT_PHOTO))
			photoSelectorDomain.getRecentPhoto(recentListener);
		else
			photoSelectorDomain.getAlbumPhoto(current.getName(), recentListener); // 获取选中相册的照片
	}

	/**
	 * 设置最多可选择图片的数量
	 *
	 * @param maxSelectable
	 */
	public void setMaxSelectable(int maxSelectable) {
		this.maxSelectable = maxSelectable;
	}

	/**
	 * 获取本地图库照片回调
	 */
	public interface OnLocalRecentListener {
		public void onPhotoLoaded(List<PhotoModel> photos);
	}

	/**
	 * 获取本地相册信息回调
	 */
	public interface OnLocalAlbumListener {
		public void onAlbumLoaded(List<AlbumModel> albums);
	}

	private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
		@Override
		public void onAlbumLoaded(List<AlbumModel> albums) {
			albumAdapter.notifyDataSetChanged(albums);
		}
	};

	private OnLocalRecentListener recentListener = new OnLocalRecentListener() {
		@Override
		public void onPhotoLoaded(List<PhotoModel> photos) {
			photoData.clear();
			photoData.addAll(photos);
			photoAdapter.notifyDataSetChanged();
			gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
			reset();
		}
	};
}
