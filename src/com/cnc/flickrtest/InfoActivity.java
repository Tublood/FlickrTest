package com.cnc.flickrtest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.googlecode.flickrjandroid.photos.Photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class InfoActivity extends Activity implements OnTouchListener {
	private Photo photo;
	private ImageView view;
	private Handler m_handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		Bundle bundle = getIntent().getExtras();
		this.photo = (Photo) bundle.get("photo");
		view = (ImageView) findViewById(R.id.detailImage);
		view.setScaleType(ImageView.ScaleType.FIT_CENTER);
		view.setOnTouchListener(this);
		m_handler = new Handler();
		loadImage();
	}

	public void loadImage() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				final String url = photo.getMediumUrl();
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
		});
		t.start();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Intent t = new Intent(InfoActivity.this, ShowDetailActivity.class);
		t.putExtra("photo", photo);
		startActivity(t);
		return false;
	}
}
