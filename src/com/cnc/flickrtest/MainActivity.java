package com.cnc.flickrtest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Handler m_handler = new Handler();
	ImageView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button button = (Button) findViewById(R.id.button1);
		final EditText editText = (EditText) findViewById(R.id.editText1);
		view = (ImageView) findViewById(R.id.imageView1);
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				search(editText.getText().toString());
			}
		});

	}

	public void search(final String string) {
		Thread t = new Thread() {
			public void run() {
				String key = "2cb46fe99c9874b4ac741ce4a74e351c";
				String svr = "www.flickr.com";

				REST rest = null;
				try {
					rest = new REST();
					rest.setHost(svr);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}

				// initialize Flickr object with key and rest
				Flickr flickr = new Flickr(key, rest);

				// initialize SearchParameter object, this object stores the
				// search keyword
				SearchParameters searchParams = new SearchParameters();
				searchParams.setSort(SearchParameters.RELEVANCE);

				// Create tag keyword array
				String[] tags = new String[] { string };
				searchParams.setTags(tags);
				// String aha = string;
				// searchParams.setText(aha);

				// Initialize PhotosInterface object
				PhotosInterface photosInterface = flickr.getPhotosInterface();
				// Execute search with entered tags
				PhotoList photoList = null;
				try {
					photoList = photosInterface.search(searchParams, 20, 1);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FlickrException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// get search result and fetch the photo object and get small
				// square imag's url
				if (photoList != null) {
					// Get search result and check the size of photo result
					for (int i = 0; i < photoList.size(); i++) {
						// get photo object
						Photo photo = (Photo) photoList.get(i);
						Log.d("aha", photo.getTitle());

						final String url = photoList.get(i).getSmallUrl();
						try {
							final InputStream is = (InputStream) new URL(url)
									.getContent();
							final Bitmap bm = BitmapFactory.decodeStream(is);

							m_handler.post(new Runnable() {
								@Override
								public void run() {
									view.setImageBitmap(bm);
								}
							});
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}

				}

			};
		};

		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
