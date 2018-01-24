package com.apps.synclogin.syncloginapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.synclogin.syncloginapp.util.UserProfile;
import com.firebase.ui.auth.User;

import java.util.ArrayList;

/**
 * Created by robert.arifin on 09/10/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    public static final String DATABASE_NAME = "SyncLogin.db";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GOOGLE_ID = "googleID";
    public static final String COLUMN_INSTAGRAM_ID = "instaID";
    public static final String COLUMN_FB_ID = "fbID";
    public static final String COLUMN_GITHUB_ID = "githubID";
    public static final String COLUMN_TWITTER_ID = "twitterID";

    public SQLiteHelper(Context context)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public  void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY " +
                "KEY AUTOINCREMENT," +  COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " + COLUMN_GOOGLE_ID + " TEXT, "
                + COLUMN_INSTAGRAM_ID + " TEXT, " + COLUMN_FB_ID + " TEXT, "
                + COLUMN_GITHUB_ID + " TEXT, " + COLUMN_TWITTER_ID + " TEXT)");
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecord(UserProfile record) {
        database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, record.getDate());
        values.put(COLUMN_AMOUNT, record.getAmount());
        values.put(COLUMN_CATEGORY, record.getCategory());
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void updateRecord(FinanceModel record) {
        database = this.getReadableDatabase();
        database.execSQL("update " + TABLE_NAME + " set " +
                COLUMN_AMOUNT + " = '" + record.getAmount() +
                "' where " + COLUMN_ID + " = '" + record.getID() + "'");
        database.close();
    }

//    public void deleteRecord(FinanceModel record) {
//        database = this.getReadableDatabase();
//        database.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + record.getID() + "'");
//        database.close();
//    }

    public UserProfile checkRecord(UserProfile userProfile, String loginID) {
        database = this.getReadableDatabase();
        UserProfile user = new UserProfile();
        //ArrayList<UserProfile> record = new ArrayList<UserProfile>();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FIRST_NAME
                + ", " + COLUMN_LAST_NAME + ", " + COLUMN_EMAIL + " from " + TABLE_NAME + "WHERE "
                + loginID + "= '" + userProfile.getId() +"'", null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                user.setFirstName(cursor.getString(0));
                user.setLastName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
            }
        }
        cursor.close();
        database.close();

        return user;
    }

    public  ArrayList<UserProfile> getAllRecords() {
        database = this.getReadableDatabase();
        UserProfile userP;
        ArrayList<FinanceModel> records = new ArrayList<FinanceModel>();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_DATE + ", " +
                COLUMN_AMOUNT + ", " + COLUMN_CATEGORY +
                " from financeYearlyReports" , null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                financeModel = new FinanceModel();
                financeModel.setID(cursor.getInt(0));
                financeModel.setDate(cursor.getLong(1));
                financeModel.setAmount(cursor.getInt(2));
                financeModel.setCategory(cursor.getString(3));

                records.add(financeModel);
            }
        }
        cursor.close();
        database.close();

        return records;
    }

}