package com.photo.selector.model;

import java.io.Serializable;

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String originalPath;
	private boolean isChecked = false;
	private boolean isEnabled = true;

	public PhotoModel() {
	}

	public PhotoModel(String originalPath) {
		this.originalPath = originalPath;
	}

	public PhotoModel(String originalPath, boolean isChecked) {
		this.originalPath = originalPath;
		this.isChecked = isChecked;
	}

	public PhotoModel(String originalPath, boolean isChecked, boolean isEnabled) {
		this.originalPath = originalPath;
		this.isChecked = isChecked;
		this.isEnabled = isEnabled;
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
