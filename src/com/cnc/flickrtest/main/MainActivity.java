package com.cnc.flickrtest.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnc.flickrtest.R;
import com.cnc.flickrtest.camera.CameraActivity;
import com.cnc.flickrtest.utils.FlickrContainer;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import android.app.Application;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.LoaderSettings.SettingsBuilder;
import com.novoda.imageloader.core.cache.LruBitmapCache;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private Handler m_handler = new Handler();
	private List<Photo> photoes = new ArrayList<Photo>();
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private int page = 1;
	private ImageView view;
	private Button button, button2;
	private EditText editText;
	private ListView listview;
	private boolean isLoading;
	private FlickrContainer fc;
	private ImageListAdapter adapter;
	public static ImageManager imageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		normalImageManagerSettings();
		fc = FlickrContainer.getInstance();
		button = (Button) findViewById(R.id.button1);
		button2  = (Button) findViewById(R.id.button2);
		
		editText = (EditText) findViewById(R.id.editText1);
		view = (ImageView) findViewById(R.id.imageView1);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new ImageListAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnScrollListener(new ListView.OnScrollListener() 
		{
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) 
			{
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) 
			{
				// arg1 = firstVisibleItem
				// arg2 = visibleItemCount
				// arg3 = totalItemCount
				int loadedItems = arg1 + arg2;
				if ((loadedItems == arg3) && !isLoading) 
				{
					isLoading = true;
					continueSearch(editText.getText().toString());
				}
			}

		});

		listview.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{				
				Intent t = new Intent(MainActivity.this, InfoActivity.class);
				t.putExtra("position", arg2);
				startActivity(t);
			}

		});
		button.setOnClickListener(new Button.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				newSearch(editText.getText().toString());
				// search(editText.getText().toString());
			}
		});

		button2.setOnClickListener(new Button.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Log.d("TEST", "LAYOUT CHANGE");
				
				Intent t = new Intent(MainActivity.this, CameraActivity.class);
				startActivity(t);
				finish();
			}
		});
		

	}

	private void normalImageManagerSettings() 
	{
		imageManager = new ImageManager(this, new SettingsBuilder()
				.withCacheManager(new LruBitmapCache(this)).build(this));
	}

	@SuppressWarnings("unused")
	private void verboseImageManagerSettings() 
	{
		SettingsBuilder settingsBuilder = new SettingsBuilder();

		// You can force the urlConnection to disconnect after every call.
		settingsBuilder.withDisconnectOnEveryCall(true);

		// We have different types of cache, check cache package for more info
		settingsBuilder.withCacheManager(new LruBitmapCache(this));

		// You can set a specific read timeout
		settingsBuilder.withReadTimeout(30000);

		// You can set a specific connection timeout
		settingsBuilder.withConnectionTimeout(30000);

		// You can disable the multi-threading ability to download image
		settingsBuilder.withAsyncTasks(false);

		// You can set a specific directory for caching files on the sdcard
		// settingsBuilder.withCacheDir(new File("/something"));

		// Setting this to false means that file cache will use the url without
		// the query part
		// for the generation of the hashname
		settingsBuilder.withEnableQueryInHashGeneration(false);

		LoaderSettings loaderSettings = settingsBuilder.build(this);
		imageManager = new ImageManager(this, loaderSettings);
	}

	public static ImageManager getImageLoader() 
	{
		return imageManager;
	}

	public void search(final String string) 
	{
		Thread t = new Thread() 
		{
			public void run()
			{
				String key = "2cb46fe99c9874b4ac741ce4a74e351c";
				String svr = "www.flickr.com";

				REST rest = null;
				try 
				{
					rest = new REST();
					rest.setHost(svr);
				} catch (ParserConfigurationException e) 
				{
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
				// Initialize PhotosInterface object
				PhotosInterface photosInterface = flickr.getPhotosInterface();
				// Execute search with entered tags
				PhotoList photoList = null;
				try {
					photoList = photosInterface.search(searchParams, 10, page);
				} catch (IOException e) 
				{
					e.printStackTrace();
				} catch (FlickrException e) 
				{
					e.printStackTrace();
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
				// get search result and fetch the photo object and get small
				// square imag's url
				if (photoList != null) 
				{
					// Get search result and check the size of photo result
					for (int i = 0; i < photoList.size(); i++) 
					{
						// get photo object
						final Photo photo = (Photo) photoList.get(i);
						Log.d("pic:" + String.valueOf(i), photo.getTitle());
						Log.d("id:" + String.valueOf(i), photo.getId());
						// Load image
						final String url = photoList.get(i).getSmallUrl();
						try 
						{
							final InputStream is = (InputStream) new URL(url)
									.getContent();
							final Bitmap bm = BitmapFactory.decodeStream(is);
							// Load user information

							JSONObject JsonObject = new JSONObject(
									QueryFlickrUser(photo.getId()));
							final String userName = JsonObject.getJSONObject("photo")
									.getJSONObject("owner")
									.getString("username");
							final String userLocation = JsonObject.getJSONObject("photo")
									.getJSONObject("owner")
									.getString("location");
							final String date = JsonObject.getJSONObject("photo")
									.getJSONObject("dates").getString("taken");
							final String viewCount = JsonObject.getJSONObject("photo")
									.getString("views");
							final String iconFarm = JsonObject.getJSONObject("photo")
									.getJSONObject("owner")
									.getString("iconfarm");
							final String server = JsonObject.getJSONObject("photo")
									.getJSONObject("owner")
									.getString("iconserver");
							final String nsid = JsonObject.getJSONObject("photo")
									.getJSONObject("owner").getString("nsid");
							final String description = JsonObject.getJSONObject("photo")
									.getJSONObject("description").getString("_content");
							final Bitmap avatar = getAvatar(iconFarm, server,
									nsid);
//							final String a = userName, b = userLocation,
//									c = date, d = viewCount, e = description;
							m_handler.post(new Runnable() 
							{
								@Override
								public void run() 
								{
									//add to adapter
									adapter.addPhoto(photo);
									adapter.addBitmap(bm);
									adapter.addAvatar(avatar);
									adapter.addUserName(userName);
									adapter.addUserLocation(userLocation);
									adapter.addPhotoDate(date);
									adapter.addViewCount(viewCount);
									adapter.addDescription(description);
									adapter.notifyDataSetChanged();
									Log.d("Load Image", "loaded");
									//add to flick container
									fc.addPhoto(photo);
									fc.addBitmap(bm);
									fc.addAvatar(avatar);
									fc.addUserName(userName);
									fc.addUserLocation(userLocation);
									fc.addViewCount(viewCount);
									fc.addDateUped(date);
									fc.addUserID(nsid);
									fc.addUserFarm(iconFarm);
									fc.addUserServer(server);
								}
							});
						} catch (final IOException e) 
						{
							e.printStackTrace();
						} catch (JSONException e) 
						{
							e.printStackTrace();
						}
					}
					isLoading = false;
				}
			};
		};

		t.start();
	}

	public void continueSearch(final String string) 
	{
		page++;
		search(string);
	}

	public void newSearch(final String string) 
	{
		adapter.clearSearchData();
		adapter.notifyDataSetChanged();
		page = 1;
		search(string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ------------------------------------
	// String FlickrQuery_url1 =
	// "http://api.flickr.com/services/rest/?method=flickr.people.getinfo";
	String FlickrQuery_url1 = "http://api.flickr.com/services/rest/?method=flickr.photos.getinfo";
	// String FlickrQuery_user = "&user_id=";
	String FlickrQuery_photo = "&photo_id=";
	String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	String FlickrQuery_format = "&format=json";
	String FlickrApiKey = "2cb46fe99c9874b4ac741ce4a74e351c";
	String FlickrQuery_key = "&api_key=";

	private String QueryFlickrUser(String photoID) 
	{
		String qResult = null;
		String qString =
		FlickrQuery_url1
		+ FlickrQuery_nojsoncallback
		+ FlickrQuery_format
		+ FlickrQuery_key + FlickrApiKey + FlickrQuery_photo + photoID;
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

	public Bitmap getAvatar(String iconFarm, String Server, String nsid) 
	{
		Bitmap bm = null;
		String FlickrPhotoPath = "http://farm" + iconFarm
				+ ".static.flickr.com/" + Server + "/buddyicons/" + nsid
				+ ".jpg";
		URL FlickrPhotoUrl = null;
		try 
		{
			FlickrPhotoUrl = new URL(FlickrPhotoPath);

			HttpURLConnection httpConnection = (HttpURLConnection) FlickrPhotoUrl
					.openConnection();
			httpConnection.setDoInput(true);
			httpConnection.connect();
			InputStream inputStream = httpConnection.getInputStream();
			bm = BitmapFactory.decodeStream(inputStream);

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return bm;
	}
}
