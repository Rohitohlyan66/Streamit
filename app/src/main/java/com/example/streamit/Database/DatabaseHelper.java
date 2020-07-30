package com.example.streamit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.streamit.Client.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "com.example.kohlimedia.db";

    //user data table
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "user_name";
    private static final String USER_COLUMN_USER_ID = "user_id";
    private static final String USER_COLUMN_EMAIL = "user_email";
    private static final String USER_COLUMN_STATUS = "status";
    private static final String USER_COLUMN_PROFILE_IMAGE_URL = "user_profile_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_DATA_TABLE());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
    }

    private String CREATE_USER_DATA_TABLE() {
        return "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
                " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_COLUMN_NAME + " TEXT," +
                USER_COLUMN_EMAIL + " TEXT," +
                USER_COLUMN_STATUS + " TEXT," +
                USER_COLUMN_PROFILE_IMAGE_URL + " TEXT," +
                USER_COLUMN_USER_ID + " TEXT" + ")";
    }

    public long insertUserData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, user.getName());
        contentValues.put(USER_COLUMN_EMAIL, user.getEmail());
        contentValues.put(USER_COLUMN_STATUS, user.getStatus());
        contentValues.put(USER_COLUMN_PROFILE_IMAGE_URL, user.getImageUrl());
        contentValues.put(USER_COLUMN_USER_ID, user.getUserId());

        long id = db.insert(USER_TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public User getUserData() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);

        if (cursor != null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    // prepare appConfig object
                    user.setUserId(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL)));
                    user.setImageUrl(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PROFILE_IMAGE_URL)));
                    user.setStatus(cursor.getString(cursor.getColumnIndex(USER_COLUMN_STATUS)));

                    cursor.moveToNext();
                }
            }

        // close the db connection
        cursor.close();
        return user;

    }

    public long updateUserData(User user, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, user.getName());
        contentValues.put(USER_COLUMN_EMAIL, user.getEmail());
        contentValues.put(USER_COLUMN_USER_ID, user.getUserId());
        contentValues.put(USER_COLUMN_PROFILE_IMAGE_URL, user.getImageUrl());
        contentValues.put(USER_COLUMN_STATUS, user.getStatus());

        // updating row
        return db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public void deleteUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + USER_TABLE_NAME);
        db.close();
    }

    public int getUserDataCount() {
        String countQuery = "SELECT  * FROM " + USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
