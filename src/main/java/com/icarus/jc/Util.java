package com.bitel.mobiletv.utils;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bitel.mobiletv.R;
import com.bitel.mobiletv.customview.DialogConfirmAuthenticate;
import com.bitel.mobiletv.delegate.OnAuthenticate;
import com.bitel.mobiletv.delegate.OnReloadListener;
import com.bitel.mobiletv.model.LiveTVPackage;
import com.bitel.mobiletv.model.VOD;
import com.bitel.mobiletv.model.VODCategory;
import com.bitel.mobiletv.model.Video;
import com.bitel.mobiletv.model.VodPackage;
import com.squareup.picasso.Picasso;

/**
 * Collection of utility functions used in this package.
 */
public class Util {

	private Util() {

	}

	/*
	 * Compute the sample size as a function of minSideLength and
	 * maxNumOfPixels. minSideLength is used to specify that minimal width or
	 * height of a bitmap. maxNumOfPixels is used to specify the maximal size in
	 * pixels that are tolerable in terms of memory usage.
	 * 
	 * The function returns a sample size based on the constraints. Both size
	 * and minSideLength can be passed in as IImage.UNCONSTRAINED, which
	 * indicates no care of the corresponding constraint. The functions prefers
	 * returning a sample size that generates a smaller bitmap, unless
	 * minSideLength = IImage.UNCONSTRAINED.
	 */

	public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp) {

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to mFilter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b1 != source) {
			b1.recycle();
		}

		return b2;
	}

	public static void closeSilently(Closeable c) {

		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			// do nothing
		}
	}

	public static String convert(String org) {
		// convert to VNese no sign. @haidh 2008
		char arrChar[] = org.toCharArray();
		char result[] = new char[arrChar.length];
		for (int i = 0; i < arrChar.length; i++) {
			switch (arrChar[i]) {
			case '\u00E1':
			case '\u00E0':
			case '\u1EA3':
			case '\u00E3':
			case '\u1EA1':
			case '\u0103':
			case '\u1EAF':
			case '\u1EB1':
			case '\u1EB3':
			case '\u1EB5':
			case '\u1EB7':
			case '\u00E2':
			case '\u1EA5':
			case '\u1EA7':
			case '\u1EA9':
			case '\u1EAB':
			case '\u1EAD':
			case '\u0203':
			case '\u01CE': {
				result[i] = 'a';
				break;
			}
			case '\u00E9':
			case '\u00E8':
			case '\u1EBB':
			case '\u1EBD':
			case '\u1EB9':
			case '\u00EA':
			case '\u1EBF':
			case '\u1EC1':
			case '\u1EC3':
			case '\u1EC5':
			case '\u1EC7':
			case '\u0207': {
				result[i] = 'e';
				break;
			}
			case '\u00ED':
			case '\u00EC':
			case '\u1EC9':
			case '\u0129':
			case '\u1ECB': {
				result[i] = 'i';
				break;
			}
			case '\u00F3':
			case '\u00F2':
			case '\u1ECF':
			case '\u00F5':
			case '\u1ECD':
			case '\u00F4':
			case '\u1ED1':
			case '\u1ED3':
			case '\u1ED5':
			case '\u1ED7':
			case '\u1ED9':
			case '\u01A1':
			case '\u1EDB':
			case '\u1EDD':
			case '\u1EDF':
			case '\u1EE1':
			case '\u1EE3':
			case '\u020F': {
				result[i] = 'o';
				break;
			}
			case '\u00FA':
			case '\u00F9':
			case '\u1EE7':
			case '\u0169':
			case '\u1EE5':
			case '\u01B0':
			case '\u1EE9':
			case '\u1EEB':
			case '\u1EED':
			case '\u1EEF':
			case '\u1EF1': {
				result[i] = 'u';
				break;
			}
			case '\u00FD':
			case '\u1EF3':
			case '\u1EF7':
			case '\u1EF9':
			case '\u1EF5': {
				result[i] = 'y';
				break;
			}
			case '\u0111': {
				result[i] = 'd';
				break;
			}
			case '\u00C1':
			case '\u00C0':
			case '\u1EA2':
			case '\u00C3':
			case '\u1EA0':
			case '\u0102':
			case '\u1EAE':
			case '\u1EB0':
			case '\u1EB2':
			case '\u1EB4':
			case '\u1EB6':
			case '\u00C2':
			case '\u1EA4':
			case '\u1EA6':
			case '\u1EA8':
			case '\u1EAA':
			case '\u1EAC':
			case '\u0202':
			case '\u01CD': {
				result[i] = 'A';
				break;
			}
			case '\u00C9':
			case '\u00C8':
			case '\u1EBA':
			case '\u1EBC':
			case '\u1EB8':
			case '\u00CA':
			case '\u1EBE':
			case '\u1EC0':
			case '\u1EC2':
			case '\u1EC4':
			case '\u1EC6':
			case '\u0206': {
				result[i] = 'E';
				break;
			}
			case '\u00CD':
			case '\u00CC':
			case '\u1EC8':
			case '\u0128':
			case '\u1ECA': {
				result[i] = 'I';
				break;
			}
			case '\u00D3':
			case '\u00D2':
			case '\u1ECE':
			case '\u00D5':
			case '\u1ECC':
			case '\u00D4':
			case '\u1ED0':
			case '\u1ED2':
			case '\u1ED4':
			case '\u1ED6':
			case '\u1ED8':
			case '\u01A0':
			case '\u1EDA':
			case '\u1EDC':
			case '\u1EDE':
			case '\u1EE0':
			case '\u1EE2':
			case '\u020E': {
				result[i] = 'O';
				break;
			}
			case '\u00DA':
			case '\u00D9':
			case '\u1EE6':
			case '\u0168':
			case '\u1EE4':
			case '\u01AF':
			case '\u1EE8':
			case '\u1EEA':
			case '\u1EEC':
			case '\u1EEE':
			case '\u1EF0': {
				result[i] = 'U';
				break;
			}

			case '\u00DD':
			case '\u1EF2':
			case '\u1EF6':
			case '\u1EF8':
			case '\u1EF4': {
				result[i] = 'Y';
				break;
			}
			case '\u0110':
			case '\u00D0':
			case '\u0089': {
				result[i] = 'D';
				break;
			}
			default:
				result[i] = arrChar[i];
			}
		}
		return new String(result);
	}

	public static boolean isFileExistsInSD(String sFileName) {
		if (sFileName == null)
			return false;
		java.io.File file = new java.io.File(sFileName);
		if (file == null)
			return false;
		return file.exists();
	}

	private static class BackgroundJob extends
			MonitoredActivity.LifeCycleAdapter implements Runnable {

		private final MonitoredActivity mActivity;
		private final ProgressDialog mDialog;
		private final Runnable mJob;
		private final Handler mHandler;
		private final Runnable mCleanupRunner = new Runnable() {
			public void run() {

				mActivity.removeLifeCycleListener(BackgroundJob.this);
				if (mDialog.getWindow() != null)
					mDialog.dismiss();
			}
		};

		public BackgroundJob(MonitoredActivity activity, Runnable job,
				ProgressDialog dialog, Handler handler) {

			mActivity = activity;
			mDialog = dialog;
			mJob = job;
			mActivity.addLifeCycleListener(this);
			mHandler = handler;
		}

		public void run() {

			try {
				mJob.run();
			} finally {
				mHandler.post(mCleanupRunner);
			}
		}

		@Override
		public void onActivityDestroyed(MonitoredActivity activity) {
			// We get here only when the onDestroyed being called before
			// the mCleanupRunner. So, run it now and remove it from the queue
			mCleanupRunner.run();
			mHandler.removeCallbacks(mCleanupRunner);
		}

		@Override
		public void onActivityStopped(MonitoredActivity activity) {

			mDialog.hide();
		}

		@Override
		public void onActivityStarted(MonitoredActivity activity) {

			mDialog.show();
		}
	}

	public static void startBackgroundJob(MonitoredActivity activity,
			String title, String message, Runnable job, Handler handler) {
		// Make the progress dialog uncancelable, so that we can gurantee
		// the thread will be done before the activity getting destroyed.
		ProgressDialog dialog = ProgressDialog.show(activity, title, message,
				true, false);
		new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
	}

	// Returns Options that set the puregeable flag for Bitmap decode.
	public static BitmapFactory.Options createNativeAllocOptions() {

		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inNativeAlloc = true;
		return options;
	}

	// Thong added for rotate
	public static Bitmap rotateImage(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);
		Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
				src.getHeight(), matrix, true);
		return bmp;
	}

	public static void setHeightForListView(final ListView listView,
			final Context context) {
		final ListAdapter listAdapter = listView.getAdapter();
		listView.post(new Runnable() {

			@Override
			public void run() {
				if (listAdapter == null) {
					// pre-condition
					return;
				}

				int totalHeight = listView.getPaddingTop()
						+ listView.getPaddingBottom();
				int desiredWidth = MeasureSpec.makeMeasureSpec(
						listView.getWidth(), MeasureSpec.AT_MOST);
				for (int i = 0; i < listAdapter.getCount(); i++) {
					View listItem = listAdapter.getView(i, null, listView);

					if (listItem != null) {

						listItem.setLayoutParams(new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT));
						listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
						totalHeight += listItem.getMeasuredHeight();

					}
				}

				ViewGroup.LayoutParams params = listView.getLayoutParams();

				// int footerHeight = context.getResources()
				// .getDimensionPixelSize(R.dimen.footer_size);

				params.height = totalHeight
						+ (listView.getDividerHeight() * (listView.getCount() - 1));
				// + footerHeight;
				listView.setLayoutParams(params);
				listView.requestLayout();

			}
		});

	}

	public static int getOrientationInDegree(Activity activity) {

		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		return degrees;
	}

	public static JSONArray listMostViewedArray;
	public static JSONArray listNewestVODArray;
	public static JSONArray listLiveTVPackageArray;
	public static JSONArray listVODPackagesArray;
	public static JSONArray listVODCategoryArray;

	/**
	 * 
	 * @param mContext
	 * @param url
	 * @param width
	 * @param height
	 * @param imv
	 */
	public static void loadImageByUrl(Context mContext, String url, int width,
			int height, ImageView imv) {
		if (url != null && !url.equals("")) {
			Picasso.with(mContext).cancelRequest(imv);
			if (width > 0 && height > 0) {
				Picasso.with(mContext).load(url)
						.error(R.drawable.default_image)
						.placeholder(R.drawable.default_image)
						.resize(width, height).into(imv);
			} else {
				Picasso.with(mContext).load(url)
						.error(R.drawable.default_image)
						.placeholder(R.drawable.default_image).fit().into(imv);
			}
		} else {
			imv.setImageResource(R.drawable.default_image);
		}
	}

	/**
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static List<LiveTVPackage> parseJSONArrayToListLiveTVPackage(
			JSONArray jsonArray) {
		List<LiveTVPackage> listLiveTVPackage = new ArrayList<LiveTVPackage>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				LiveTVPackage liveTVPackage = new LiveTVPackage(
						jsonArray.getJSONObject(i));
				listLiveTVPackage.add(liveTVPackage);
			} catch (JSONException e) {
			}
		}
		return listLiveTVPackage;
	}

	public static List<VOD> parseJSONArrayToListVOD(JSONArray jsonArray) {
		List<VOD> listVOD = new ArrayList<VOD>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				VOD vod = new VOD(jsonArray.getJSONObject(i));
				listVOD.add(vod);
			} catch (JSONException e) {
			}
		}
		return listVOD;
	}

	/**
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static List<VodPackage> parseJSONArrayToListVODPackage(
			JSONArray jsonArray) {
		List<VodPackage> listVODPackage = new ArrayList<VodPackage>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				VodPackage vodPackage = new VodPackage(
						jsonArray.getJSONObject(i));
				listVODPackage.add(vodPackage);
			} catch (JSONException e) {
			}
		}
		return listVODPackage;
	}

	/**
	 * 
	 * @param jsonArray
	 * @return List<Video>
	 */
	public static List<Video> parseJSONArrayToListVideo(JSONArray jsonArray) {
		List<Video> listVideo = new ArrayList<Video>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				Video video = new Video(jsonArray.getJSONObject(i));
				listVideo.add(video);
			} catch (JSONException e) {
			}
		}
		return listVideo;
	}

	/**
	 * Show message
	 * 
	 * @param context
	 * @param message
	 */
	public static void showMessage(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void changeColorText(TextView tv, String textFull,
			String textColor, Context context) {
		Spannable wordtoSpan = new SpannableString(textFull);

		wordtoSpan.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.line_popup)), 0, textColor.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// wordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
		// textColor.length(), 0);

		tv.setText(wordtoSpan);
	}

	/**
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static List<VODCategory> parseJSONArrayToListVODCategory(
			JSONArray jsonArray) {
		List<VODCategory> listVODCategory = new ArrayList<VODCategory>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				VODCategory vodCategory = new VODCategory(
						jsonArray.getJSONObject(i));
				listVODCategory.add(vodCategory);
			} catch (JSONException e) {
			}
		}
		return listVODCategory;
	}

	public static void showConfirmAuthenticate(final Context context,
			final SharedPref sharedPref, final RequestQueue queue,
			final OnReloadListener onListener) {
		DialogConfirmAuthenticate dialogInfo = new DialogConfirmAuthenticate(
				context) {

			@Override
			public void setActionOk() {
				// thay doi check wifi thanh 3G viettel
				if (ClientConnection.checkWifi(context)) {
					doAuthenticateReload(context, sharedPref, queue, onListener);
				} else {
					new ToastUtil(context).show(context.getResources()
							.getString(R.string.no_connection_wifi_3g));
				}
			}

			@Override
			public void setActionCancel() {

			}
		};

		dialogInfo.getWindow().getAttributes().windowAnimations = R.style.AnimationDialogDownloadConfirm;
		dialogInfo.show();
		dialogInfo.setCanceledOnTouchOutside(true);
	}

	public static void doAuthenticateReload(final Context context,
			final SharedPref sharedPref, final RequestQueue queue,
			final OnReloadListener onListener) {

		StringRequest postRequest = new StringRequest(Constants.METHOD,
				UrlHelper.Authenticate, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject != null) {
								String errorCode = jsonObject
										.getString(Constants.ERROR_CODE);
								String message = jsonObject
										.getString(Constants.MESSAGE);

								if (errorCode.equals(Constants.OK)) {
									String token = jsonObject
											.getString(Constants.TOKEN);
									sharedPref.saveToken(token);
									onListener.onReload();
								} else {
									String token = Constants.TOKEN_NULL;
									sharedPref.saveToken(token);
									new ToastUtil(context).show(message);

								}
							}
						} catch (Exception e) {
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Util.showMessage(context, context.getResources()
								.getString(R.string.error));
					}
				});
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				Constants.MY_SOCKET_TIMEOUT_MS, Constants.MAX_RETRIES_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(postRequest);
	}

	/**
	 * @param id
	 * @param type
	 */
	public static void doAuthenticate(final Context context,
			final SharedPref sharedPref, final RequestQueue queue,
			final OnAuthenticate onListener, final boolean is3G) {

		StringRequest postRequest = new StringRequest(Constants.METHOD,
				UrlHelper.Authenticate, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject != null) {
								String errorCode = jsonObject
										.getString(Constants.ERROR_CODE);
								String message = jsonObject
										.getString(Constants.MESSAGE);

								if (errorCode.equals(Constants.OK)) {
									String token = jsonObject
											.getString(Constants.TOKEN);
									sharedPref.saveToken(token);
									onListener.doAfterAuthen();

								} else {
									String token = Constants.TOKEN_NULL;
									sharedPref.saveToken(token);
									onListener.doAfterAuthen();

								}
							}
						} catch (Exception e) {
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Util.showMessage(context, context.getResources()
								.getString(R.string.label_connection_ws_error));
					}
				});
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				Constants.MY_SOCKET_TIMEOUT_MS, Constants.MAX_RETRIES_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(postRequest);
	}

	/**
	 * 
	 * @param keyword2
	 * @return
	 */
	public static String MyEncodeKeywordSearch(String keyword2) {

		String keyword = keyword2;
		// encode for GL
		keyword = keyword.replaceAll("%C3%A%E1%BA%A2", "%E1%BB%83");
		keyword = keyword.replaceAll("%C4%82%CC%89", "%C4%82");
		keyword = keyword.replaceAll("%C4%83%CC%89", "%C4%83");
		keyword = keyword.replaceAll("%C3%82%CC%89", "%C3%82");
		keyword = keyword.replaceAll("%C3%A2%CC%89", "%C3%A2");
		keyword = keyword.replaceAll("%C4%82%CC%80", "%E1%BA%B0");
		keyword = keyword.replaceAll("%C4%83%CC%80", "%E1%BA%B1");
		keyword = keyword.replaceAll("%C3%82%CC%80", "%E1%BA%A6");
		keyword = keyword.replaceAll("%C3%A2%CC%80", "%E1%BA%A7");
		keyword = keyword.replaceAll("%C3%8A%CC%80", "%C3%8A%CC%80");
		keyword = keyword.replaceAll("%C3%A2%CC%80", "%E1%BB%81");
		keyword = keyword.replaceAll("%C3%94%CC%80", "%E1%BB%92");
		keyword = keyword.replaceAll("%C3%B4%CC%80", "%E1%BB%93");
		keyword = keyword.replaceAll("%C6%A0%CC%80", "%E1%BB%9C");
		keyword = keyword.replaceAll("%C6%A1%CC%80", "%E1%BB%9D");
		keyword = keyword.replaceAll("%C6%AF%CC%80", "%E1%BB%AA");
		keyword = keyword.replaceAll("%C6%B0%CC%80", "%E1%BB%AB");
		keyword = keyword.replaceAll("%C4%82%CC%89", "%E1%BA%B2");
		keyword = keyword.replaceAll("%C4%83%CC%89", "%E1%BA%B3");
		keyword = keyword.replaceAll("%C3%82%CC%89", "%E1%BA%A8");
		keyword = keyword.replaceAll("%C3%A2%CC%89", "%E1%BA%A9");
		keyword = keyword.replaceAll("%C3%8A%CC%89", "%E1%BB%82");
		keyword = keyword.replaceAll("%C3%AA%CC%89", "%E1%BB%83");
		keyword = keyword.replaceAll("%C3%94%CC%89", "%E1%BB%94");
		keyword = keyword.replaceAll("%C3%B4%CC%89", "%E1%BB%95");
		keyword = keyword.replaceAll("%C6%A0%CC%89", "%E1%BB%9E");
		keyword = keyword.replaceAll("%C6%A1%CC%89", "%E1%BB%9F");
		keyword = keyword.replaceAll("%C6%AF%CC%89", "%E1%BB%AC");
		keyword = keyword.replaceAll("%C6%B0%CC%89", "%E1%BB%AD");
		keyword = keyword.replaceAll("%C4%82%CC%83", "%E1%BA%B4");
		keyword = keyword.replaceAll("%C4%83%CC%83", "%E1%BA%B5");
		keyword = keyword.replaceAll("%C3%82%CC%83", "%E1%BA%AA");
		keyword = keyword.replaceAll("%C3%A2%CC%83", "%E1%BA%AB");
		keyword = keyword.replaceAll("%C3%8A%CC%83", "%C3%8A%CC%83");
		keyword = keyword.replaceAll("%C3%AA%CC%83", "%E1%BB%85");
		keyword = keyword.replaceAll("%C3%94%CC%83", "%E1%BB%96");
		keyword = keyword.replaceAll("%C3%B4%CC%83", "%E1%BB%97");
		keyword = keyword.replaceAll("%C6%A0%CC%8", "%E1%BB%A0");
		keyword = keyword.replaceAll("%C6%A1%CC%83", "%E1%BB%A1");
		keyword = keyword.replaceAll("%C6%AF%CC%83", "%E1%BB%AE");
		keyword = keyword.replaceAll("%C6%B0%CC%83", "%E1%BB%AF");
		keyword = keyword.replaceAll("%C4%82%CC%81", "%E1%BA%AE");
		keyword = keyword.replaceAll("%C4%83%CC%81", "%E1%BA%AF");
		keyword = keyword.replaceAll("%C3%82%CC%81", "%E1%BA%A4");
		keyword = keyword.replaceAll("%C3%A2%CC%81", "%E1%BA%A5");
		keyword = keyword.replaceAll("%C3%8A%CC%81", "%E1%BA%BE");
		keyword = keyword.replaceAll("%C3%AA%CC%81", "%E1%BA%BF");
		keyword = keyword.replaceAll("%C3%94%CC%81", "%E1%BB%90");
		keyword = keyword.replaceAll("%C3%B4%CC%81", "%E1%BB%91");
		keyword = keyword.replaceAll("%C6%A0%CC%81", "%E1%BB%9A");
		keyword = keyword.replaceAll("%C6%A1%CC%81", "%E1%BB%9B");
		keyword = keyword.replaceAll("%C6%AF%CC%81", "%E1%BB%A8");
		keyword = keyword.replaceAll("%C6%B0%CC%81", "%C6%B0%CC%81");
		keyword = keyword.replaceAll("%C4%82%CC%A3", "%E1%BA%B6");
		keyword = keyword.replaceAll("%C4%83%CC%A3", "%E1%BA%B7");
		keyword = keyword.replaceAll("%C3%8A%CC%A3", "%E1%BB%86");
		keyword = keyword.replaceAll("%C3%AA%CC%A3", "%E1%BB%87");
		keyword = keyword.replaceAll("%C3%94%CC%A3", "%E1%BB%98");
		keyword = keyword.replaceAll("%C3%B4%CC%A3", "%E1%BB%99");
		keyword = keyword.replaceAll("%C6%A0%CC%A3", "%E1%BB%A2");
		keyword = keyword.replaceAll("%C6%A1%CC%A3", "%E1%BB%A3");
		keyword = keyword.replaceAll("%C6%AF%CC%A3", "%E1%BB%B0 ");
		keyword = keyword.replaceAll("%C6%B0%CC%A3", "%E1%BB%B1");
		keyword = keyword.replaceAll("A%CC%80", "%C3%80");
		keyword = keyword.replaceAll("a%CC%80", "%C3%A0");
		keyword = keyword.replaceAll("E%CC%80", "%C3%88");
		keyword = keyword.replaceAll("e%CC%80", "%C3%A8");
		keyword = keyword.replaceAll("I%CC%80", "%C3%8C");
		keyword = keyword.replaceAll("i%CC%80", "%C3%AC");
		keyword = keyword.replaceAll("O%CC%80", "%C3%92");
		keyword = keyword.replaceAll("o%CC%80", "%C3%B2");
		keyword = keyword.replaceAll("U%CC%80", "%C3%99");
		keyword = keyword.replaceAll("u%CC%80", "%C3%B9");
		keyword = keyword.replaceAll("Y%CC%80", "%E1%BB%B2");
		keyword = keyword.replaceAll("y%CC%80", "%E1%BB%B3");

		// character ?
		keyword = keyword.replaceAll("A%CC%89", "%E1%BA%A2");
		keyword = keyword.replaceAll("a%CC%89", "%E1%BA%A3");
		keyword = keyword.replaceAll("E%CC%89", "%E1%BA%BA");
		keyword = keyword.replaceAll("e%CC%89", "%E1%BA%BB");
		keyword = keyword.replaceAll("I%CC%89", "%E1%BB%88");
		keyword = keyword.replaceAll("i%CC%89", "%E1%BB%89");
		keyword = keyword.replaceAll("O%CC%89", "%E1%BB%8E");
		keyword = keyword.replaceAll("o%CC%89", "%E1%BB%8F");
		keyword = keyword.replaceAll("U%CC%89", "%E1%BB%A6");
		keyword = keyword.replaceAll("u%CC%89", "%E1%BB%A7");
		keyword = keyword.replaceAll("Y%CC%89", "%E1%BB%B6");
		keyword = keyword.replaceAll("y%CC%89", "%E1%BB%B7");
		// character ~
		keyword = keyword.replaceAll("A%CC%83", "%C3%83");
		keyword = keyword.replaceAll("a%CC%83", "%C3%A3");
		keyword = keyword.replaceAll("E%CC%83", "%E1%BA%BC");
		keyword = keyword.replaceAll("e%CC%83", "%E1%BA%BD");
		keyword = keyword.replaceAll("I%CC%83", "%C4%A8");
		keyword = keyword.replaceAll("i%CC%83", "%C4%A9");
		keyword = keyword.replaceAll("O%CC%83", "%C3%95");
		keyword = keyword.replaceAll("o%CC%83", "%C3%B5");
		keyword = keyword.replaceAll("U%CC%83", "%C5%A8");
		keyword = keyword.replaceAll("u%CC%83", "%C5%A9");
		keyword = keyword.replaceAll("Y%CC%83", "%E1%BB%B8");
		keyword = keyword.replaceAll("y%CC%83", "%E1%BB%B9");

		// character '
		keyword = keyword.replaceAll("A%CC%81", "%C3%81");
		keyword = keyword.replaceAll("a%CC%81", "%C3%A1");
		keyword = keyword.replaceAll("E%CC%81", "%C3%89");
		keyword = keyword.replaceAll("e%CC%81", "%C3%A9");
		keyword = keyword.replaceAll("I%CC%81", "%C3%8D");
		keyword = keyword.replaceAll("i%CC%81", "%C3%AD");
		keyword = keyword.replaceAll("O%CC%81", "%C3%93 ");
		keyword = keyword.replaceAll("o%CC%81", "%C3%B3");
		keyword = keyword.replaceAll("U%CC%81", "%C3%9A");
		keyword = keyword.replaceAll("u%CC%81", "%C3%BA");
		keyword = keyword.replaceAll("Y%CC%81", "%C3%9D");
		keyword = keyword.replaceAll("y%CC%81", "%C3%BD");
		// character .
		keyword = keyword.replaceAll("A%CC%A3", "%E1%BA%A0");
		keyword = keyword.replaceAll("a%CC%A3", "%E1%BA%A1");
		keyword = keyword.replaceAll("E%CC%A3", "%E1%BA%B8");
		keyword = keyword.replaceAll("e%CC%A3", "%E1%BA%B9");
		keyword = keyword.replaceAll("I%CC%A3", "%E1%BB%8A");
		keyword = keyword.replaceAll("I%CC%A3", "%E1%BB%8B");
		keyword = keyword.replaceAll("O%CC%A3", "%E1%BB%8C");
		keyword = keyword.replaceAll("o%CC%A3", "%E1%BB%8D");
		keyword = keyword.replaceAll("U%CC%A3", "%E1%BB%A4");
		keyword = keyword.replaceAll("u%CC%A3", "%E1%BB%A5");
		keyword = keyword.replaceAll("Y%CC%A3", "%E1%BB%B4");
		keyword = keyword.replaceAll("y%CC%A3", "%E1%BB%B5");

		return keyword;
	}
}
