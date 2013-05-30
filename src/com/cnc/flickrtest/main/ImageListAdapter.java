package com.cnc.flickrtest.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cnc.flickrtest.R;
import com.googlecode.flickrjandroid.photos.Photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//public class ImageListAdapter extends ArrayAdapter<Photo> 
//{
//
//	private Context 	context;
//	private List<Photo> photoes;
//	private List<Bitmap> bitmaps;
//	public ImageListAdapter(Context context, List<Photo> photoes, List<Bitmap> bitmaps) 
//	{
//		super(context, R.layout.list_layout, photoes);
//		
//		
//		this.context	= context;
//		this.photoes	= photoes;
//		this.bitmaps	= bitmaps;
////		photoes			= new ArrayList<Photo>();
////		bitmaps			= new ArrayList<Bitmap>();
//
//	}
//
//	public void addPhoto(Photo photo)
//	{
//		photoes.add(photo);
//	}
//	public void addBitmap(Bitmap bm)
//	{
//		bitmaps.add(bm);
//	}
//	public Photo getPhoto(int position)
//	{
//		return photoes.get(position);
//	}
//	public void clearSearchData()
//	{
//		photoes.clear();
//		bitmaps.clear();
//	}
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) 
//	{
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	 
//		View rowView 		= inflater.inflate(R.layout.list_layout, parent, false);
//		TextView textView 	= (TextView) rowView.findViewById(R.id.label);
//		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
//		imageView.setImageBitmap(bitmaps.get(position));
//		textView.setText(photoes.get(position).getTitle());
// 
//		return rowView;
//	}
//}
public class ImageListAdapter extends BaseAdapter
{

	public static class ViewHolder
	{
		public TextView 	userName;
		public TextView 	userLocation;
		public TextView 	dateUped;
		public TextView 	viewCount;
		public ImageView 	pic;
		public ImageView 	avatar;
	}
	private Context 	context;
	private List<Photo> photoes;
	private List<Bitmap> bitmaps;
	private List<Bitmap> avatar;
	private List<String> usernames;
	private List<String> userLocation;
	private List<String>   photoDate;
	private List<String>   views;
	private List<String>   descriptions;
	public ImageListAdapter(Context context) 
	{	
		this.context	= context;
		photoes			= new ArrayList<Photo>();
		bitmaps			= new ArrayList<Bitmap>();
		avatar			= new ArrayList<Bitmap>();
		usernames		= new ArrayList<String>();
		userLocation	= new ArrayList<String>();
		photoDate		= new ArrayList<String>();
		views			= new ArrayList<String>();
		descriptions	= new ArrayList<String>();

	}

	public void addPhoto(Photo photo)
	{
		photoes.add(photo);
	}
	public void addBitmap(Bitmap bm)
	{
		bitmaps.add(bm);
	}
	public void addAvatar(Bitmap avatarBitmap)
	{
		avatar.add(avatarBitmap);
	}
	public void addUserName( String name )
	{
		usernames.add(name);
	}
	public void addUserLocation( String location )
	{
		userLocation.add(location);
	}
	public void addPhotoDate(String date)
	{
		photoDate.add(date);
	}
	public void addViewCount(String viewCount)
	{
		views.add(viewCount);
	}
	public void addDescription(String description)
	{
		descriptions.add(description);
	}
	public Photo getPhoto(int position)
	{
		return photoes.get(position);
	}
	public Bitmap getImage(int position)
	{
		return bitmaps.get(position);
	}
	public Bitmap getAvatar(int position)
	{
		return avatar.get(position);
	}
	public String getUserName( int position )
	{
		return usernames.get(position);
	}
	public String getUserLocation( int position )
	{
		return userLocation.get(position);
	}
	public String getDateUped( int position )
	{
		return photoDate.get(position);
	}
	public String getViewCount( int position )
	{
		return views.get(position);
	}
	public String getDescription( int position )
	{
		return descriptions.get(position);
	}
	public void clearSearchData()
	{
		photoes.clear();
		bitmaps.clear();
		usernames.clear();
		userLocation.clear();
		photoDate.clear();
		views.clear();
		avatar.clear();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		
		if( convertView == null )
		{
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder				= new ViewHolder();
			convertView 		= inflater.inflate(R.layout.list_layout, parent, false);
			holder.userName 	= (TextView) convertView.findViewById(R.id.userName);
			holder.userLocation	= (TextView) convertView.findViewById(R.id.userLocation);
			holder.dateUped 	= (TextView) convertView.findViewById(R.id.dateUped);
			holder.viewCount 	= (TextView) convertView.findViewById(R.id.viewCount);
			holder.avatar		= (ImageView) convertView.findViewById(R.id.avatar);
			holder.pic			= (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(holder);
		}
		else
		{
			holder =(ViewHolder)convertView.getTag();
		}
		holder.pic.setImageBitmap(bitmaps.get(position));
		holder.avatar.setImageBitmap(avatar.get(position));
		holder.userName.setText(usernames.get(position));
		holder.userLocation.setText(userLocation.get(position));
//		holder.userLocation.setText(photoes.get(position).getOwner().getLocation());
		holder.dateUped.setText(photoDate.get(position));
		holder.viewCount.setText(views.get(position));
		
//		holder.avatar.setImageBitmap(bitmaps.get(position));
//		holder.userName.setText("Ha");
//		holder.userLocation.setText("HCM");
//		holder.dateUped.setText("20022002");
//		holder.viewCount.setText("1000000");
 
		return convertView;
	}

	@Override
	public int getCount() 
	{
		
		return photoes.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		
		return photoes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}
}
