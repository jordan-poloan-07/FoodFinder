package com.android.foodfindertrial1.necc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class InternalStorageMethods {

	Context ctx;

	public InternalStorageMethods(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
	}

	public void writeFileToInternal(String filename, String write)
			throws IOException {
		FileOutputStream fos;

		fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);

		fos.write(write.getBytes());
	}

	public String readFileFromInternal(String filename) {
		String read = null;
		FileInputStream fis = null;

		try {
			fis = ctx.openFileInput(filename);
			byte[] wordBytes = new byte[fis.available()];
			while (fis.read(wordBytes) != -1) {
				read = new String(wordBytes);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("file not found", "missing file");
			read = null;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("error in IO", "missing file");
			read = null;
			e.printStackTrace();
		} catch (Exception e) {
			read = null;
		}

		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return read;

	}
}
