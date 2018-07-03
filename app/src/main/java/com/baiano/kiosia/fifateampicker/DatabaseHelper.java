package com.baiano.kiosia.fifateampicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "team-picker";
    private static final int DB_VERSION = 1;
    private static final String SQL_FILENAME = "data/team-picker.sql";

    private InputStream sqlFile;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        try {
            sqlFile = context.getAssets().open(SQL_FILENAME);
        } catch (IOException e) {
            Log.e(TAG, "Could not open SQL file.", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAndPopulate(db);
    }

    public Cursor executeQuery(String query) {
        return getWritableDatabase().rawQuery(query, null);
    }

    private void createAndPopulate(SQLiteDatabase db) {
        if (sqlFile == null) {
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sqlFile));

        try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                db.execSQL(currentLine);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not read SQL file.", e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close SQL file.", e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
