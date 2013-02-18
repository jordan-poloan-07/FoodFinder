package com.android.foodfindertrial1.necc;

import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckerMethods {

	Context ctx;

	public CheckerMethods(Context ctx) {
		this.ctx = ctx;
	}

	public boolean isFileExisting(String filename) {
		// code snippet from stackoverflow.com
		File file = ctx.getFileStreamPath(filename);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInternetAvailable() {
		// just a snippet from androidhive.net
		// kailang pa pala i-declare Access_network_state sa permissions
		ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public boolean isGPSEnabled() {
		return false;

	}
}
