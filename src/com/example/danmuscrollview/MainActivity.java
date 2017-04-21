package com.example.danmuscrollview;

import java.io.File;
import java.util.List;

import com.example.utils.FileUtils;
import com.example.view.MySeekBar;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;

public class MainActivity extends Activity {
	private final static String TAG = "RxJava";

	private MySeekBar mMySeekBar;

	private SeekBar mSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// testDialog();
		// testRxJava();
		// testPackageManager();
		// testCleanup();
		// testPackageManagerObserver();

		init();
	}

	private void init() {
		mMySeekBar = (MySeekBar) findViewById(R.id.my_seek_bar);
		mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

		mSeekBar.setSplitTrack(true);
		mSeekBar.setIndeterminate(true);
	}

	private void testCleanup() {
		FileUtils.calculateCacheSize("com.yoongoo.niceplay.getv");
		// FileUtils.cleanAppCache("com.yoongoo.niceplay.getv");
	}

	/**
	 * 使用 IPackageStatsObserver 来监控获取到的 package信息
	 * */
	private void testPackageManagerObserver() {
		Log.i(TAG, "testPackageManagerObserver");
		IPackageStatsObserver observer = new IPackageStatsObserver() {

			@Override
			public IBinder asBinder() {
				return null;
			}

			@Override
			public void onGetStatsCompleted(PackageStats pStats,
					boolean succeeded) throws RemoteException {
				Log.i(TAG, pStats.toString());
			}
		};
		//
		FileUtils.getPackageSizeInfo("com.yoongoo.niceplay.getv", this,
				observer);
	}

	private void testPackageManager() {
		PackageManager manager = getPackageManager();
		List<PackageInfo> mInfos = manager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : mInfos) {
			if (!packageInfo.packageName.equals("com.yoongoo.niceplay.getv")) {
				Log.w(TAG, "package names is : " + packageInfo.packageName);
				continue;
			} else {
				Log.w(TAG, "package equals ");
			}
			String pathCache = "/data/data/";
			String sdCardPath = "/sdcard/Android/data";
			pathCache += packageInfo.packageName + "/cache/";
			sdCardPath += packageInfo.packageName + "/cache/";
			Log.i(TAG, "path is :" + pathCache);
			File parentPath = new File(pathCache);
			if (parentPath.exists()) {
				File[] files = parentPath.listFiles();
				if (files != null) {
					for (File file : files) {
						Log.e(TAG, "file name is :" + file.getName());
						FileUtils.deleteFiles(file);
					}
				}

			}

			File sdcardParentPath = new File(sdCardPath);
			if (sdcardParentPath.exists()) {
				File[] files = sdcardParentPath.listFiles();
				if (files != null) {
					for (File file : files) {
						Log.e(TAG, " sdcard file name is :" + file.getName());
					}
				}
			}
		}
	}

	/**
	 * test dialog
	 * */
	private void testDialog() {
		AlertDialog mAlertDialog = new AlertDialog.Builder(this).create();
		Window mWindow = mAlertDialog.getWindow();
		mWindow.requestFeature(Window.FEATURE_NO_TITLE);
		mAlertDialog.setView(
				LayoutInflater.from(this).inflate(
						R.layout.layout_danmu,
						(ViewGroup) mWindow.getDecorView().findViewById(
								android.R.id.content), false), 0, 0, 0, 0);
		mAlertDialog.show();
		mWindow.getDecorView().setPadding(0, 0, 0, 0);
		mWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = mWindow.getAttributes();
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		params.width = manager.getDefaultDisplay().getWidth();
		params.height = LayoutParams.MATCH_PARENT;
		mWindow.setAttributes(params);
	}

	@SuppressWarnings("static-access")
	private void testRxJava() {
		MaybeObserver<Integer> mMaybeObserver = new MaybeObserver<Integer>() {

			@Override
			public void onSubscribe(@NonNull Disposable d) {
				Log.i(TAG, "onSubscribe");
			}

			@Override
			public void onSuccess(@NonNull Integer t) {
				Log.i(TAG, "onSuccess t is :" + t);
			}

			@Override
			public void onError(@NonNull Throwable e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete() {
				Log.i(TAG, "onComplete");
			}
		};
		Observer<Integer> mObserver = new Observer<Integer>() {

			@Override
			public void onSubscribe(Disposable d) {
				d.dispose();
			}

			@Override
			public void onNext(Integer t) {
				Log.i(TAG, "t is :" + t);

			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub

			}
		};
		Observable
				.just("hello world2")
				.fromArray(new String[] { "hello world 2" })
				.fromArray(new String[] { "hello world 3" })
				.map(new Function<String, Integer>() {
					@Override
					@NonNull
					public Integer apply(@NonNull String t) throws Exception {
						if (t.contains("2")) {
							return 2;
						}
						return -1;
					}
				})
				.reduce(new BiFunction<Integer, Integer, Integer>() {

					@Override
					@NonNull
					public Integer apply(@NonNull Integer t1,
							@NonNull Integer t2) throws Exception {
						// TODO Auto-generated method stub
						return null;
					}
				}).subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(mMaybeObserver);
	}
}
