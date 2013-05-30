package com.cnc.flickrtest;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.NoCopySpan.Concrete;
import android.widget.TextView;
import android.widget.Toast;

public class MyDatabase {

	// Database Name
	private static final String DATABASE_NAME = "DB_USER";

	// Version Database
	private static final int DATABASE_VERSION = 1;

	// TABLE Name and column name
	private static final String TABLE_ACCOUNT = "table";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER = "user";
	public static final String COLUMN_LOCATION = "location";

	// other object
	private static Context context;
	static SQLiteDatabase db;
	private OpenHelper openHelper;
	private static String selectedImagePath;
	protected static TextView textView;

	// contructor object
	public MyDatabase(Context c) {
		MyDatabase.context = c;
	}

	// Open conection database
	public MyDatabase open() throws SQLException {
		openHelper = new OpenHelper(context);
		db = openHelper.getWritableDatabase();
		return this;

	}

	// Close Database
	public void close() {
		openHelper.close();
	}

	// Create Data
	public long createData(String userName, String loc) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_USER, userName);
		cv.put(COLUMN_LOCATION, loc);
		return db.insert(TABLE_ACCOUNT, null, cv);

	}

	// get data
	public String getData() {
		String[] columns = new String[] { COLUMN_ID, COLUMN_USER,
				COLUMN_LOCATION };
		Cursor c = db.query(TABLE_ACCOUNT, columns, null, null, null, null,
				null);
		String result = "";
		// get columns index
		int iRow = c.getColumnIndex(COLUMN_ID);
		int u = c.getColumnIndex(COLUMN_USER);
		int l = c.getColumnIndex(COLUMN_LOCATION);

		// Loop get cursor data
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + " " + c.getString(iRow) + "- ten :"
					+ c.getString(u) + "- vitri : " + c.getString(l);
		}
		c.close();
		return result;

	}

	// OpenHelper class
	private static class OpenHelper extends SQLiteOpenHelper {

		// Contructor OpenHelper
		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		// create database
		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			// arg0.execSQL("CREATE TABLE" + TABLE_ACCOUNT + "("
			// + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
			// + COLUMN_USER + "TEXT NOT NULL,"
			// + COLUMN_LOCATION + "TEXT NOT NULL);");
			// SQLiteDatabase myDb = openOrCreateDatabase(DATABASE_NAME,
			// Context.MODE_PRIVATE, null);

			arg0.execSQL(" CREATE TABLE " + TABLE_ACCOUNT + "(" + COLUMN_ID
					+ "INTEGER PRIMARY KEY," + COLUMN_USER + "TEXT"
					+ COLUMN_LOCATION + "TEXT );");

		}

		// void saveInDB() {
		// SQLiteDatabase myDb = openOrCreateDatabase(DATABASE_NAME,
		// Context.MODE_PRIVATE, null);
		// byte[] byteImage1 = null;
		// String s = myDb.getPath();
		//
		// myDb.execSQL("delete from " + DATABASE_NAME); // clearing the table
		// ContentValues newValues = new ContentValues();
		// String name = "CoderzHeaven";
		// newValues.put("name", name);
		// try {
		// FileInputStream instream = new FileInputStream(selectedImagePath);
		// BufferedInputStream bif = new BufferedInputStream(instream);
		// byteImage1 = new byte[bif.available()];
		// bif.read(byteImage1);
		// newValues.put("image", byteImage1);
		// long ret = myDb.insert(DATABASE_NAME, null, newValues);
		// if (ret < 0)
		// textView.append("Error");
		// } catch (IOException e) {
		// textView.append("Error Exception : " + e.getMessage());
		// }
		// myDb.close();
		// textView.append("\n Saving Details \n Name : " + name);
		// textView.append("\n Image Size : " + byteImage1.length + " KB");
		// textView.append("\n Saved in DB : " + s + "\n");
		// Toast.makeText(this.getBaseContext(),
		// "Image Saved in DB successfully.", Toast.LENGTH_SHORT).show();
		// }

		// check version database
		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
			onCreate(arg0);
		}

	}

}
