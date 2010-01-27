package com.widgetgrid.android.arimagesearch;

import com.widgetgrid.android.arimagesearch.zxing.BinaryBitmap;
import com.widgetgrid.android.arimagesearch.zxing.HybridBinarizer;
import com.widgetgrid.android.arimagesearch.zxing.PlanarYUVLuminanceSource;
import com.widgetgrid.android.arimagesearch.zxing.PointInfoObject;
import com.widgetgrid.android.arimagesearch.zxing.PointProcessor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

public class ARImageSearch extends Activity {
	private CameraPreviewSurface mPreview;
	private OverlayView mOverlay;
	private GLSurfaceView mGLSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreviewSurface(this);

		// Demon magic to do an overlay
		mOverlay = new OverlayView(this);
		
		// Demon demon magic to do an overlay
		mGLSurface = new GLSurfaceView(this);
		
		mGLSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mGLSurface.setRenderer(new com.widgetgrid.android.arimagesearch.threedee.CubeRenderer(true));
		mGLSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		setContentView(mGLSurface);
		addContentView(mPreview, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(mOverlay, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}
	
	Camera.PreviewCallback cb = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			doProcessing(data, true);
		}
	};
	
	public void doProcessing(byte[] data, boolean solid) {
		//Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	
		// TODO : Use the last four parameters to somehow crop the view nicely. 
		Point resolution = mPreview.resolution;
		PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, resolution.x, resolution.y,
				0, 0, resolution.x, resolution.y); 
			
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		
		// Identify the red spot.
		//PointProcessor pproc = new PointProcessor(extractPureBits(bitmap.getBlackMatrix()));
		PointProcessor pproc = new PointProcessor((bitmap.getBlackMatrix()));
		PointInfoObject pio = pproc.detect();
		
		mOverlay.points = pio;
		
		mOverlay.invalidate();
		
		if(solid)
			mPreview.mCamera.startPreview();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			
			if(mOverlay.testData != null) {
				
				mOverlay.testData = null;
				mOverlay.invalidate();
				return true;
				
			}
			
			Camera.Parameters p = mPreview.mCamera.getParameters();
			
			p.setPictureSize(mOverlay._width, mOverlay._height);
			//p.setPictureFormat(p.);
			
			mPreview.mCamera.setParameters(p);
			mPreview.mCamera.setOneShotPreviewCallback(cb);
			//mPreview.mCamera.takePicture(null, null, PictureProcessor);
			return true;
		}
		return false;
	}
	
	public void simpleAlert(int data) {
		new AlertDialog.Builder(this)
			.setMessage(Integer.toString(data))
			.show();
	}
	
}