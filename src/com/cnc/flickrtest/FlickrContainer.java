package com.cnc.flickrtest;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.googlecode.flickrjandroid.photos.Photo;

public class FlickrContainer 
{
	public static FlickrContainer instance;
	private List<Photo>  photos;
	private List<Bitmap> bitmap;
	private List<Bitmap> avatar;
	private List<String> userName;
	private List<String> userLocation;
	private List<String> dateUped;
	private List<String> viewCount;
	private List<String> description;
	private List<String> userID;
	private List<String> userFarm;
	private List<String> userServer;
	
	
	public static FlickrContainer getInstance()
	{
		if( instance == null )
		{
			instance = new FlickrContainer();
			return instance;
		}
		else return instance;
	}
	
	public FlickrContainer() 
	{
		photos 			= new ArrayList<Photo>();
		bitmap			= new ArrayList<Bitmap>();
		avatar			= new ArrayList<Bitmap>();
		userName 		= new ArrayList<String>();
		userLocation 	= new ArrayList<String>();
		dateUped 		= new ArrayList<String>();
		viewCount	 	= new ArrayList<String>();
		description 	= new ArrayList<String>();
		userID		 	= new ArrayList<String>();
		userFarm	 	= new ArrayList<String>();
		userServer	 	= new ArrayList<String>();	
				
	}
	
	public void addPhoto(Photo photo)
	{
		photos.add(photo);
	}
	public void addBitmap(Bitmap bm)
	{
		bitmap.add(bm);
	}
	public void addAvatar(Bitmap avatar)
	{
		this.avatar.add(avatar);
	}
	public void addUserName( String userName )
	{
		this.userName.add(userName);
	}
	public void addUserLocation( String userLocation )
	{
		this.userLocation.add(userLocation);
	}
	public void addDateUped( String dateUped )
	{
		this.dateUped.add(dateUped);
	}
	public void addViewCount( String viewCount )
	{
		this.viewCount.add(viewCount);
	}
	public void addDescription( String description )
	{
		this.description.add(description);
	}
	public void addUserID( String userID )
	{
		this.userID.add(userID);
	}
	public void addUserFarm( String userFarm )
	{
		this.userFarm.add(userFarm);
	}
	public void addUserServer( String userServer )
	{
		this.userServer.add(userServer);
	}
	
	public Photo getPhoto(int posistion )
	{
		return photos.get(posistion);
	}
	public Bitmap getBitmap( int position )
	{
		return bitmap.get(position);
	}
	public Bitmap getAvatar( int position )
	{
		return avatar.get(position);
	}
	public String getUserName( int position )
	{
		return userName.get(position);
	}
	public String getUserLocation( int position )
	{
		return userLocation.get(position);
	}
	public String getDateUped( int position )
	{
		return dateUped.get(position);
	}
	public String getViewCount( int position )
	{
		return viewCount.get(position);
	}
	public String getUserId( int position )
	{
		return userID.get(position);
	}
	public String getDescription( int position )
	{
		return description.get(position);
	}
	public String getUserFarm( int position )
	{
		return userFarm.get(position);
	}
	public String getUserServer( int position )
	{
		return userServer.get(position);
	}
	public int getLength()
	{
		return userName.size();
	}
}
