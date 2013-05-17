package com.cnc.flickrtest;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.flickrjandroid.photos.Photo;

import android.content.Context;
import android.graphics.Bitmap;
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
	public ImageListAdapter(Context context) 
	{	
		this.context	= context;
		photoes			= new ArrayList<Photo>();
		bitmaps			= new ArrayList<Bitmap>();

	}

	public void addPhoto(Photo photo)
	{
		photoes.add(photo);
	}
	public void addBitmap(Bitmap bm)
	{
		bitmaps.add(bm);
	}
	public Photo getPhoto(int position)
	{
		return photoes.get(position);
	}
	public void clearSearchData()
	{
		photoes.clear();
		bitmaps.clear();
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
////		holder.avatar.setImageBitmap(bitmaps.get(position));
		holder.userName.setText(photoes.get(position).getOwner().getUsername());
		holder.userLocation.setText(photoes.get(position).getOwner().getLocation());
		holder.dateUped.setText(String.valueOf(photoes.get(position).getDatePosted()));
		holder.viewCount.setText(String.valueOf(photoes.get(position).getViews()));
		
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