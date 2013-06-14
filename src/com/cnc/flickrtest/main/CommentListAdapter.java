package com.cnc.flickrtest.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cnc.flickrtest.R;
import com.googlecode.flickrjandroid.photos.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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

public class CommentListAdapter extends BaseAdapter
{

	public static class ViewHolder
	{
		public TextView 	userName;
		public TextView 	comment;
		public ImageView 	avatar;
	}
	private Context 		context;
	private List<Bitmap> 	avatar;
	private List<String> 	usernames;
	private List<String> 	comment;
	private List<String> 	avatarUrls;
	
	private ImageLoader		imageLoader;	
	private DisplayImageOptions options;
	
	public CommentListAdapter(Context context) 
	{	
		this.context	= context;
//		avatar			= new ArrayList<Bitmap>();
		usernames		= new ArrayList<String>();
		comment			= new ArrayList<String>();
		avatarUrls		= new ArrayList<String>();
		
		options = new DisplayImageOptions.Builder( ) 
	        .resetViewBeforeLoading( ) 
	        .imageScaleType( ImageScaleType.EXACTLY ) 
	        .bitmapConfig( Bitmap.Config.RGB_565 ) 
	        .cacheInMemory( ) 
	        .cacheOnDisc( ) 
	        .build( );
		imageLoader		= ImageLoader.getInstance();
	}


	public void addAvatar(Bitmap avatarBitmap)
	{
		avatar.add(avatarBitmap);
	}
	public void addUserName( String name )
	{
		usernames.add(name);
	}

	public void addComment( String comment )
	{
		this.comment.add(comment);
	}
	public void addAvatarUrl( String url )
	{
		this.avatarUrls.add(url);
	}
	public Bitmap getAvatar(int position)
	{
		return avatar.get(position);
	}
	public String getUserName( int position )
	{
		return usernames.get(position);
	}
	public String getComment( int position )
	{
		return comment.get(position);
	}
	public String getAvatarUrl( int position )
	{
		return avatarUrls.get(position);
	}
	public void clearSearchData()
	{
		usernames.clear();
//		avatar.clear();
		avatarUrls.clear();
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
			convertView 		= inflater.inflate(R.layout.comment_layout, parent, false);
			holder.userName 	= (TextView) convertView.findViewById(R.id.comment_userName);
			holder.comment		= (TextView) convertView.findViewById(R.id.comment_comment);
			holder.avatar		= (ImageView) convertView.findViewById(R.id.comment_avatar);
			convertView.setTag(holder);
		}
		else
		{
			holder =(ViewHolder)convertView.getTag();
		}
//		holder.avatar.setImageBitmap(avatar.get(position));
		imageLoader.displayImage( getAvatarUrl( position ), holder.avatar, options );
		holder.userName.setText(usernames.get(position));
		holder.comment.setText(comment.get(position));
 
		return convertView;
	}

	@Override
	public int getCount() 
	{
		
		return usernames.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		
		return usernames.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}
}
