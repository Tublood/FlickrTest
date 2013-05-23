package com.cnc.flickrtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InfoActivity extends Activity implements OnTouchListener {
	private Photo 		photo;
	private Bitmap 		image, avatar;
	private String 		userNameString, userLocationString,
						dateUpedString, viewCountString,
						descriptionString;
	
	private ImageView 	view, avatarView;
	private TextView 	userName, userLocation, dateUped, viewCount,
						description;
	private WebView 	webview;
	private Handler 	m_handler = new Handler();
	private ListView 	commentListView;
	private String		photoID;
	private CommentListAdapter commentAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infor_layout);
		Bundle bundle 	= getIntent().getExtras();
		this.photo 		= (Photo) bundle.get("photo");
		this.photoID	= photo.getId();
		this.image 		= (Bitmap) bundle.get("image");
		this.avatar 	= (Bitmap) bundle.get("avatar");
		this.userNameString 	= (String) bundle.get("username");
		this.userLocationString = (String) bundle.get("userlocation");
		this.dateUpedString 	= (String) bundle.get("dateuped");
		this.viewCountString 	= (String) bundle.get("viewcount");
		this.descriptionString 	= (String) bundle.get("description");
		view = (ImageView) findViewById(R.id.image_info);
		view.setImageBitmap(image);
//		view.setScaleType(ImageView.ScaleType.FIT_CENTER);
//		view.setOnTouchListener(this);
		
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
		
//		description = (TextView ) findViewById(R.id.description_info);
//		description.setText(descriptionString);
		
		commentAdapter = new CommentListAdapter(this);
		commentListView = (ListView) findViewById(R.id.listView_info);
		commentListView.setAdapter(commentAdapter);
		
		processComment(this.photoID);
//		
//		m_handler = new Handler();
//		loadImage();
	}

	public void loadImage() 
	{
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				final String url = photo.getMediumUrl();
				try {
					final InputStream is = (InputStream) new URL(url)
							.getContent();
					final Bitmap bm = BitmapFactory.decodeStream(is);

					m_handler.post(new Runnable() 
					{
						@Override
						public void run() 
						{

							view.setImageBitmap(bm);
						}
					});
				} catch (final IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	String FlickrQuery_url1 = "http://api.flickr.com/services/rest/?method=flickr.photos.comments.getList";
	// String FlickrQuery_user = "&user_id=";
	String FlickrQuery_photo = "&photo_id=";
	String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	String FlickrQuery_format = "&format=json";
	String FlickrApiKey = "2cb46fe99c9874b4ac741ce4a74e351c";
	String FlickrQuery_key = "&api_key=";
	public void processComment(final String photoID)
	{
		Log.d("ProcessComment", "ProcessComment");
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				Log.d("threadRun", "threadRun");
				
				try 
				{
					JSONObject JsonObject 	= new JSONObject(queryComment(photoID));
					JSONObject comments		= JsonObject.getJSONObject("comments");
					JSONArray  comment		= comments.getJSONArray("comment");
					int size = comment.length();
					for( int i = 0; i < size; i++  )
					{
						JSONObject photoComment = comment.getJSONObject(i);
						final String userid 		= photoComment.getString("author");
						final String userServer  = photoComment.getString("iconserver");
						final String userFarm	= photoComment.getString("iconfarm");
						final String userName	= photoComment.getString("authorname");
						final String commentString = photoComment.getString("_content");
						Log.d(userName, commentString);
						final Bitmap bm = getAvatar(userFarm, userServer, userid);
						m_handler.post(new Runnable() 
						{
							@Override
							public void run() 
							{
								commentAdapter.addComment(commentString);
								commentAdapter.addUserName(userName);
								commentAdapter.addAvatar(bm);
								commentAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	public String queryComment(String photoId)
	{
		Log.d("query", "query");
		String qResult = null;
		String qString =
		FlickrQuery_url1
		+ FlickrQuery_nojsoncallback
		+ FlickrQuery_format
		+ FlickrQuery_key + FlickrApiKey + FlickrQuery_photo + photoId;
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(qString);
		try 
		{
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
			if (httpEntity != null) 
			{
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();
				String stringReadLine = null;
				while ((stringReadLine = bufferedreader.readLine()) != null) 
				{
					stringBuilder.append(stringReadLine + "\n");
				}
				qResult = stringBuilder.toString();
			}

		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return qResult;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		Intent t = new Intent(InfoActivity.this, ShowDetailActivity.class);
		t.putExtra("photo", photo);
		startActivity(t);
		return false;
	}
	
	public Bitmap getAvatar(String iconFarm, String Server, String nsid) 
	{
		Log.d("getAvatar", "getAvatar");
		Bitmap bm = null;

		String FlickrPhotoPath = "http://farm" + iconFarm
				+ ".static.flickr.com/" + Server + "/buddyicons/" + nsid
				+ ".jpg";

		URL FlickrPhotoUrl = null;

		try {
			FlickrPhotoUrl = new URL(FlickrPhotoPath);

			HttpURLConnection httpConnection = (HttpURLConnection) FlickrPhotoUrl
					.openConnection();
			httpConnection.setDoInput(true);
			httpConnection.connect();
			InputStream inputStream = httpConnection.getInputStream();
			bm = BitmapFactory.decodeStream(inputStream);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return bm;
	}
}
