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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity implements OnTouchListener 
{
	private Photo 		photo;
	private Bitmap 		image, avatar;
	private String 		userNameString, userLocationString,
						dateUpedString, viewCountString;
	
	private ImageView 	view, avatarView;
	private TextView 	userName, userLocation, dateUped, viewCount;
	private WebView 	webview;
	private Handler 	m_handler = new Handler();
	private ListView 	commentListView;
	private String		photoID;
	private CommentListAdapter commentAdapter;
	public FlickrContainer fc;
	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		fc = FlickrContainer.getInstance();
		setContentView(R.layout.infor_layout);
		Bundle bundle 	= getIntent().getExtras();
		int position 	= bundle.getInt("position");
//		this.photo 		= (Photo) bundle.get("photo");
//		this.photoID	= photo.getId();
//		this.image 		= (Bitmap) bundle.get("image");
//		this.avatar 	= (Bitmap) bundle.get("avatar");
//		this.userNameString 	= (String) bundle.get("username");
//		this.userLocationString = (String) bundle.get("userlocation");
//		this.dateUpedString 	= (String) bundle.get("dateuped");
//		this.viewCountString 	= (String) bundle.get("viewcount");
		this.photo 		= fc.getPhoto(position);
		this.photoID	= photo.getId();
		this.image 		= fc.getBitmap(position);
		this.avatar 	= fc.getAvatar(position);
		this.userNameString 	= fc.getUserName(position);
		this.userLocationString = fc.getUserLocation(position);
		this.dateUpedString 	= fc.getDateUped(position);
		this.viewCountString 	= fc.getViewCount(position);
		
		view = (ImageView) findViewById(R.id.image_info);
		view.setImageBitmap(image);
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

		
		commentAdapter = new CommentListAdapter(this);
		commentListView = (ListView) findViewById(R.id.listView_info);
		commentListView.setAdapter(commentAdapter);
		
		processComment(this.photoID);

//		pager = (ViewPager) findViewById(R.id.pager);
//		pager.setAdapter(new ImagePagerAdapter());
//		pager.setCurrentItem(position);
		
		
		
//		setContentView(R.layout.webview);
//		webview = (WebView) findViewById(R.id.info_webView);
//		webview.loadUrl("http://farm" + fc.getUserFarm(position)
//				+ ".static.flickr.com/" + fc.getUserServer(position) +
//				"/buddyicons/" + fc.getUserId(position)
//				+ ".jpg");
//		String avatar = "<html>"+ "<img src=\""+"http://farm" + fc.getUserFarm(position)
//			+ ".static.flickr.com/" + fc.getUserServer(position) +
//			"/buddyicons/" + fc.getUserId(position)
//			+ ".jpg"+"\">"+"</html>";
//		
//		String name = "<html>"+ fc.getUserName(position)+"</html>";
//		String location = "<html>"+ fc.getUserLocation(position)+"</html>";
//		String date = "<html>"+ fc.getDateUped(position)+"</html>";
//		String view = "<html>"+ fc.getViewCount(position)+"</html>";
//		String img = "<html>"+ "<img src=\""+fc.getPhoto(position).getSmallUrl()+"\">"+"</html>";
//		commentsString = new String();
//		comment(fc.getPhoto(position).getId());
//		webview.loadData(avatar +"</br>"+ name+"</br>"
//				+location+"</br>"+date+"</br>"+view
//				+"</br>"+img+"</br>"+ commentsString, "text/html", "UTF-8");
	}
	private String commentsString;
	public void comment(final String photoID)
	{
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{				
				try 
				{
					JSONObject JsonObject 	= new JSONObject(queryComment(photoID));
					JSONObject comments		= JsonObject.getJSONObject("comments");
					JSONArray  comment		= comments.getJSONArray("comment");
					int size = comment.length();
					for( int i = 0; i < size; i++  )
					{
						JSONObject photoComment = comment.getJSONObject(i);
						final String userid 	= photoComment.getString("author");
						final String userServer = photoComment.getString("iconserver");
						final String userFarm	= photoComment.getString("iconfarm");
						final String userName	= photoComment.getString("authorname");
						final String _content = photoComment.getString("_content");
						String avatar = "<html>"+ "<img src=\""+"http://farm" + userFarm
								+ ".static.flickr.com/" + userServer +
								"/buddyicons/" + userid
								+ ".jpg"+"\">"+"</html>";
						String userNameString = "<html>"+ userName +"</html>";
						String  commentString= "<html>"+ _content +"</html>";
						commentsString = commentsString +"</br>"+ avatar +"</br>"
								+userNameString+"</br>"+commentString+"</br>";
						
						
					}
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}			}
		});
		t.start();
	}
	String FlickrQuery_url1 = "http://api.flickr.com/services/rest/?method=flickr.photos.comments.getList";
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
						final String userid 	= photoComment.getString("author");
						final String userServer = photoComment.getString("iconserver");
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
	
	private class ImagePagerAdapter extends PagerAdapter 
	{

		private Photo 		photo;
		private Bitmap 		image, avatar;
		private String 		userNameString, userLocationString,
							dateUpedString, viewCountString;
		
		private ImageView 	view, avatarView;
		private TextView 	userName, userLocation, dateUped, viewCount;
		private WebView 	webview;
		private Handler 	m_handler = new Handler();
		private ListView 	commentListView;
		private String		photoID;
		private CommentListAdapter commentAdapter;
		public FlickrContainer fc;
		private LayoutInflater inflater;

		ImagePagerAdapter() 
		{
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return FlickrContainer.getInstance().getLength();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) 
		{
			View imageLayout = inflater.inflate(R.layout.infor_layout, view, false);
//			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
//			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			
			this.photo 		= fc.getPhoto(position);
			this.photoID	= photo.getId();
			this.image 		= fc.getBitmap(position);
			this.avatar 	= fc.getAvatar(position);
			this.userNameString 	= fc.getUserName(position);
			this.userLocationString = fc.getUserLocation(position);
			this.dateUpedString 	= fc.getDateUped(position);
			this.viewCountString 	= fc.getViewCount(position);
			
			this.view = (ImageView) findViewById(R.id.image_info);
			this.view.setImageBitmap(image);
			this.view.setOnTouchListener(new OnTouchListener() 
			{
				
				@Override
				public boolean onTouch(View v, MotionEvent event) 
				{
					Intent t = new Intent(InfoActivity.this, ShowDetailActivity.class);
					t.putExtra("photo", photo);
					startActivity(t);
					return false;
				}
			});
			
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

			
			commentAdapter = new CommentListAdapter(InfoActivity.this);
			commentListView = (ListView) findViewById(R.id.listView_info);
			commentListView.setAdapter(commentAdapter);
			
			processComment(this.photoID);
			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) 
		{
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) 
		{
		}

		@Override
		public Parcelable saveState() 
		{
			return null;
		}

		@Override
		public void startUpdate(View container) 
		{
		}
		
	}
}
