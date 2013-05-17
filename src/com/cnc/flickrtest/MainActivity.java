package com.cnc.flickrtest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;

public class MainActivity extends Activity {

	private Handler 		 m_handler = new Handler();
	private List<Photo> 	 photoes   = new ArrayList<Photo>();
	private List<Bitmap>	 bitmaps   = new ArrayList<Bitmap>();
	private int				 page 	   = 1;
	private ImageView 		 view;
	private Button 			 button;
	private EditText 		 editText;
	private ListView		 listview;
	private boolean			 isLoading;
	
	private ImageListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button 		= (Button) findViewById(R.id.button1);
		editText 	= (EditText) findViewById(R.id.editText1);
		view 		= (ImageView) findViewById(R.id.imageView1);
		listview 	= (ListView)findViewById(R.id.listview);
		adapter 	= new ImageListAdapter(this);
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
				//arg1 = firstVisibleItem
				//arg2 = visibleItemCount
				//arg3 = totalItemCount
				int loadedItems = arg1 + arg2;
				if((loadedItems == arg3) && !isLoading)
				{
					isLoading	= true;
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
				Photo photo = adapter.getPhoto(arg2);
				Intent t = new Intent( MainActivity.this, ShowDetailActivity.class );
				t.putExtra( "photo", photo);
				startActivity(t);
//				MainActivity.this.finish();
			}


		});
		button.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				newSearch(editText.getText().toString());
//				search(editText.getText().toString());
			}
		});

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
				try 
				{
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

						final String url = photoList.get(i).getSmallUrl();
						try {
							final InputStream is = (InputStream) new URL(url)
									.getContent();
							final Bitmap bm = BitmapFactory.decodeStream(is);

							m_handler.post(new Runnable() 
							{
								@Override
								public void run() 
								{
//									view.setImageBitmap(bm);
									
									adapter.addPhoto(photo);
									adapter.addBitmap(bm);
									adapter.notifyDataSetChanged();									
									Log.d("Load Image", "loaded");
								}
							});
						} catch (final IOException e) 
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

}
