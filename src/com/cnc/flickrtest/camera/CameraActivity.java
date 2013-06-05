package com.cnc.flickrtest.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cnc.flickrtest.R;
import com.cnc.flickrtest.main.MainActivity;
import com.cnc.flickrtest.upload.FlickrjActivity;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
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
    FrameLayout preview;
    protected static String aha = new String("IMG_");
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		enableOrientationListener();
        mPicture = new PictureCallback() 
        {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) 
            {

            	
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null)
                {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");
                    return;
                }

                try 
                {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    
                    Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                    
                    switch( mOrientation )
                  {
                  		case ORIENTATION_PORTRAIT_NORMAL:
                  			realImage= rotateBitmap(realImage, 90);
                  		break;
                  		case ORIENTATION_LANDSCAPE_INVERTED:
                  			realImage= rotateBitmap(realImage, 180);
                  		break;
                  		case ORIENTATION_PORTRAIT_INVERTED:
                  			realImage= rotateBitmap(realImage, 270);
                  		break;
                  		case ORIENTATION_LANDSCAPE_NORMAL:                  			
                  		break;
                  }                   
                    
                    fos.write(getByteArray(realImage));
                    fos.close();                                    
                    mPreviewState = K_STATE_FROZEN;                    
                            			
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
					e.printStackTrace();
				}
                
                Intent intent = new Intent(getApplicationContext(),
    					FlickrjActivity.class);
    			intent.putExtra("flickImagePath", pictureFile.getAbsolutePath());        			
    			startActivity(intent);
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
				    	
//				    	setCameraDisplayOrientation(CameraActivity.this, 0, mCamera);
				    	
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
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "IMG_"+ timeStamp + ".jpg");
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	    	        aha+ timeStamp + ".jpg");
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
		preview.removeAllViews();
		mPreview = null;
	}
	@Override
	protected void onResume() 
	{	
		super.onResume();
		mCamera = getCameraInstance();
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		releaseCamera(); 
		disableOrientationListener();
	}

	private void releaseCamera()
	{
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

	public static Bitmap rotateBitmap(Bitmap b, float degrees) 
	{
	    Matrix m = new Matrix();
	    if (degrees != 0) {
	        // clockwise
	        m.postRotate(degrees, (float) b.getWidth() / 2,
	                (float) b.getHeight() / 2);
	    }

	    try {
	        Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
	                b.getHeight(), m, true);
	        if (b != b2) {
	            b.recycle();
	            b = b2;
	        }
	    } catch (OutOfMemoryError ex) {
	        // We have no memory to rotate. Return the original bitmap.
	    }
	    return b;
	}

	 public static Bitmap rotate(Bitmap bitmap, int degree) {
		    int w = bitmap.getWidth();
		    int h = bitmap.getHeight();

		    Matrix mtx = new Matrix();
		   //       mtx.postRotate(degree);
		    mtx.setRotate(degree);

		    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		}
	 
	 

	 public byte[] getByteArray(Bitmap bitmap) {
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.PNG, 0, bos);
		    return bos.toByteArray();
		}
	 OrientationEventListener mOrientationEventListener;
	 public void enableOrientationListener(){

		    if (mOrientationEventListener == null) {            
		        mOrientationEventListener = new OrientationEventListener(CameraActivity.this, SensorManager.SENSOR_DELAY_NORMAL) {

		            @Override
		            public void onOrientationChanged(int orientation) {

		                // determine our orientation based on sensor response
		                int lastOrientation = mOrientation;

		                Display display = null;
		                    display = ((WindowManager)CameraActivity.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();



		                if (display.getOrientation() == Surface.ROTATION_0) {   // landscape oriented devices
		                    if (orientation >= 315 || orientation < 45) {
		                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {                         
		                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
		                        }
		                    } else if (orientation < 315 && orientation >= 225) {
		                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
		                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
		                        }                       
		                    } else if (orientation < 225 && orientation >= 135) {
		                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
		                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
		                        }                       
		                    } else if (orientation <135 && orientation > 45) { 
		                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
		                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
		                        }                       
		                    }                       
		                } else {  // portrait oriented devices
		                    if (orientation >= 315 || orientation < 45) {
		                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {                          
		                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
		                        }
		                    } else if (orientation < 315 && orientation >= 225) {
		                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
		                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
		                        }                       
		                    } else if (orientation < 225 && orientation >= 135) {
		                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
		                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
		                        }                       
		                    } else if (orientation <135 && orientation > 45) { 
		                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
		                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
		                        }                       
		                    }
		                }
		            }
		        };
		    }
		    if (mOrientationEventListener.canDetectOrientation()) {
		        mOrientationEventListener.enable();
		    }
		}


		private static final int ORIENTATION_LANDSCAPE_INVERTED  =  1;
		private static final int ORIENTATION_LANDSCAPE_NORMAL  	=  2;
		private static final int ORIENTATION_PORTRAIT_NORMAL   	=  3;
		private static final int ORIENTATION_PORTRAIT_INVERTED 	=  4;
		private int mOrientation;

		public void disableOrientationListener(){
		    if(mOrientationEventListener != null){
		        mOrientationEventListener.disable();
		    }
		}
}
