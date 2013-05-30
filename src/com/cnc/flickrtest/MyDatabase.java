package com.cnc.flickrtest;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase {

	// ten database
	private static final String DATABASE_NAME = "Image";

	// version
	private static final int DATABASE_VERSION = 1;

	// Ten table va cac cot
	private static final String TABLE_NAME = "myImage";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "tentaikhoan";
	public static final String COLUMN_LOC = "vitri";

	/* Các đối tượng khác */
	private static Context context;
	static SQLiteDatabase db;
	private OpenHelper openHelper;

	/* Hàm dựng, khởi tạo đối tượng */
	public MyDatabase(Context c) {
		MyDatabase.context = c;
	}

	/* Hàm mở kết nối tới database */
	public MyDatabase open() throws SQLException {
		openHelper = new OpenHelper(context);
		db = openHelper.getWritableDatabase();
		return this;
	}

	/* Hàm đóng kết nối với database */
	public void close() {
		openHelper.close();
	}
	
	 /*Hàm createData dùng để chèn dữ mới dữ liệu vào database*/
    public long createData(String user , String loc) {
        ContentValues cv = new ContentValues();
        
        cv.put(COLUMN_NAME, user);
        cv.put(COLUMN_LOC, loc);
       
        
        return db.insert(TABLE_NAME, null, cv);
    }
	
	

	// ---------------- class OpenHelper ------------------
	private static class OpenHelper extends SQLiteOpenHelper {

		/* Hàm dựng khởi tạo 1 OpenHelper */
		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			arg0.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
					+ "TEXT NOT NULL" + COLUMN_LOC + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(arg0);

		}

	}
}
