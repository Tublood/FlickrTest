package com.cnc.flickrtest.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cnc.flickrtest.R;
import com.cnc.flickrtest.main.MainActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity 
{
	protected static final String TAG = "Camera";
	protected static final int MEDIA_TYPE_IMAGE = 1;
	protected static final int K_STATE_FROZEN = 1;
	protected static final int K_STATE_BUSY = 0;
	protected static final int K_STATE_PREVIEW = 3;
	private int mPreviewState;
	private Camera mCamera;
    private CameraPreview mPreview;
    PictureCallback mPicture;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		
//		 Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
//        if (mPreview == null )
//        	Log.d("Null1", "Null");
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//        if (preview == null )
//        	Log.d("Null2", "Null");
        preview.addView(mPreview);
        mPicture = new PictureCallback() 
        {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null)
                {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");
                    return;
                }

                try 
                {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    mPreviewState = K_STATE_FROZEN;
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
					e.printStackTrace();
				}
            }
        };
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new Button.OnClickListener() 
            {
				

				@Override
				public void onClick(View arg0) 
				{
					switch(mPreviewState) 
					{
				    case K_STATE_FROZEN:
				        mCamera.startPreview();
				        mPreviewState = K_STATE_PREVIEW;
				        break;

				    default:
				    	// get an image from the camera
	                    mCamera.takePicture(null, null, mPicture);
				        mPreviewState = K_STATE_BUSY;
				    }
//					// get an image from the camera
//                    mCamera.takePicture(null, null, mPicture);
					
				}
            }
        );

        Button backButton = (Button) findViewById(R.id.camera_back);
        backButton.setOnClickListener(
                new Button.OnClickListener() 
                { 				
    				@Override
    				public void onClick(View arg0) 
    				{
    					Intent t = new Intent(CameraActivity.this, MainActivity.class);
    					startActivity(t);
    					finish();
    				}
                }
            );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private boolean checkCameraHardware(Context context) 
	{
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
	    {
	        return true;
	    } else 
	    {
	        return false;
	    }
	}
	
	public static Camera getCameraInstance()
	{
	    Camera c = null;
	    try 
	    {
	        c = Camera.open();
	    }
	    catch (Exception e)
	    {
	    }
	    return c;
	}
	
	private static Uri getOutputMediaFileUri(int type)
	{
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type)
	{
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists())
	    {
	        if (! mediaStorageDir.mkdirs())
	        {
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE)
	    {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else 
	    {
	        return null;
	    }

	    return mediaFile;
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		releaseCamera();     
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		releaseCamera(); 
	}
	private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
