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
import android.widget.ListView;
import android.widget.TextView;

public class InfoActivity extends Activity implements OnTouchListener {
	private Photo photo;
	private Bitmap image, avatar;
	private String userNameString, userLocationString,
					dateUpedString, viewCountString,
					descriptionString;
	private ImageView view, avatarView;
	private TextView userName, userLocation, dateUped, viewCount,
						description;
	private Handler m_handler;
	private ListView commentListView;
	private CommentListAdapter commentAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infor_layout);
		Bundle bundle = getIntent().getExtras();
		this.photo = (Photo) bundle.get("photo");
		this.image = (Bitmap) bundle.get("image");
		this.avatar = (Bitmap) bundle.get("avatar");
		this.userNameString = (String) bundle.get("username");
		this.userLocationString = (String) bundle.get("userlocation");
		this.dateUpedString = (String) bundle.get("dateuped");
		this.viewCountString = (String) bundle.get("viewcount");
		this.descriptionString = (String) bundle.get("description");
		view = (ImageView) findViewById(R.id.image_info);
		view.setImageBitmap(image);
		view.setScaleType(ImageView.ScaleType.FIT_CENTER);
		view.setOnTouchListener(this);
		
		avatarView = (ImageView) findViewById(R.id.avatar_info);
		avatarView.setImageBitmap(avatar);
		
		userName = (TextView) findViewById(R.id.userName_info);
		userName.setText(userNameString);
		
		userLocation = (TextView) findViewById(R.id.userLocation_info);
		userLocation.setText(userLocationString);
		
		dateUped = (TextView) findViewById(R.id.dateUped_info);
		dateUped.setText(dateUpedString);
		
		viewCount = (TextView) findViewById(R.id.viewCount_info);
		viewCount.setText(viewCountString);
		
		description = (TextView ) findViewById(R.id.description_info);
		description.setText(descriptionString);
		
//		commentAdapter = new CommentListAdapter(this);
//		commentListView = (ListView) findViewById(R.id.listView1_info);
//		
//		m_handler = new Handler();
//		loadImage();
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
