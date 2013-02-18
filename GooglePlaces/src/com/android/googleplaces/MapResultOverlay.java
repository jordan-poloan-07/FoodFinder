package com.android.googleplaces;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapResultOverlay extends ItemizedOverlay<OverlayItem> {

	private Context ctx;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MapResultOverlay(Drawable drw, Context context) {
		super(boundCenterBottom(drw));
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int i) {
		// TODO Auto-generated method stub
		OverlayItem item = mOverlays.get(i);
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;

	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
}
