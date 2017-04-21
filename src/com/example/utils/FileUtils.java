package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.util.Log;

public class FileUtils {
	private final static String TAG = "FileUtils";
	private final static String PATH_INTERNAL = "/data/data/";
	private final static String PATH_EXTERNAL = "/sdcard/Android/data/";

	/**
	 * delete files recursively
	 * */
	public static void deleteFiles(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File mFile : files) {
						deleteFiles(mFile);
					}
				}
				boolean isDeleted = file.delete();
				Log.i(TAG, "directory name is :" + file.getAbsolutePath()
						+ "delete " + isDeleted);
			} else {
				boolean isFileDeleted = file.delete();
				Log.i(TAG, "file name is :" + file.getAbsolutePath()
						+ "delete " + isFileDeleted);
			}
		}
	}

	/**
	 * 清理该app的缓存
	 * 
	 * @param context
	 *            app的context
	 * */
	public static void cleanAppCache(Context context) {
		String packageName = context.getPackageName();
		if (packageName != null && !packageName.equals("")) {
			deleteFiles(new File(PATH_INTERNAL + packageName + "/cache"));
			deleteFiles(new File(PATH_EXTERNAL + packageName + "/cache"));
		}
	}

	/**
	 * 清理某个app的缓存
	 * 
	 * @param packageName
	 *            某个app对应的packageName
	 * 
	 * */
	public static void cleanAppCache(String packageName) {
		if (packageName != null && !packageName.equals("")) {
			deleteFiles(new File(PATH_INTERNAL + packageName + "/cache"));
			deleteFiles(new File(PATH_EXTERNAL + packageName + "/cache"));
		}
	}

	/**
	 * 计算app内部缓存与外部缓存之和的大小
	 * 
	 * 内部缓存路径: data/data/<packageName>/cache/ 外部缓存路径:
	 * sdcard/Android/data/<packageName>/cache
	 * 
	 * @param packageName
	 *            app的包名
	 * 
	 * */
	public static long calculateCacheSize(String packageName) {
		long internCacheSize = 0;
		long externalCacheSize = 0;
		if (packageName != null && !packageName.equals("")) {
			String internalCachePath = "data/data/" + packageName + "/cache";
			String externalCachePath = "sdcard/Android/data/" + packageName
					+ "/cache";

			File internalCacheDir = new File(internalCachePath);
			File externalCacheDir = new File(externalCachePath);

			if (internalCachePath != null && internalCacheDir.exists()) {
				internCacheSize = getFileSize(internalCacheDir);
				Log.i(TAG, "internalCacheSize is :" + internCacheSize);
			}
			if (externalCachePath != null && externalCacheDir.exists()) {
				externalCacheSize = getFileSize(externalCacheDir);
				Log.i(TAG, "externalCacheSize is :" + externalCacheSize);
			}
		}

		return (externalCacheSize + internCacheSize);
	}

	/**
	 * 计算file及其子目录下(如果该file为directory)所有file的大小.
	 * */
	private static long getFileSize(File file) {
		long size = 0;
		if (file.isDirectory() && file.listFiles() != null) {
			for (File mFile : file.listFiles()) {
				size += getFileSize(mFile);
			}
		} else if (file.isFile()) {
			size += file.length();
			Log.i(TAG, "file Name is :" + file.getName() + "file size is :"
					+ file.length());
		}

		return size;
	}

	// Executes UNIX hell command.
	private static String exec(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();
			process.waitFor();
			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void getPackageSizeInfo(String packageName, Context mContext,
			IPackageStatsObserver observer) {
		PackageManager pm = mContext.getApplicationContext()
				.getPackageManager();
		try {
			Method getPackageSizeInfo = pm.getClass().getMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			Object object = getPackageSizeInfo
					.invoke(pm, packageName, observer);
			Log.i(TAG, object.toString());
		} catch (Exception e) {
			Log.e(TAG,"error "+e.toString());
		}
	}
}
