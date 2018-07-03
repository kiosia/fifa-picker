package com.baiano.kiosia.fifateampicker.Dao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baiano.kiosia.fifateampicker.DatabaseHelper;
import com.baiano.kiosia.fifateampicker.Model.FifaVersion;

import java.util.ArrayList;
import java.util.List;

public class FifaVersionDao {
    public static List<FifaVersion> getAllVersions(Context context) {
        String query = "SELECT * FROM fifaVersion";
        return executeQuery(context, query);
    }

    public static List<FifaVersion> getAllVersionsByReleaseAndVersion(Context context, String release, String version) {
        String query = "SELECT * FROM fifaVersion WHERE gameRelease='"+release+"' AND gameVersion='"+version+"'";
        return executeQuery(context, query);
    }

    @NonNull
    private static List<FifaVersion> executeQuery(Context context, String query) {
        List<FifaVersion> result = new ArrayList<>();
        Cursor cursor = DatabaseHelper.getInstance(context).executeQuery(query);
        int idColumn = cursor.getColumnIndex("id");
        int versionColumn = cursor.getColumnIndex("gameVersion");
        int releaseColumn = cursor.getColumnIndex("gameRelease");
        cursor.moveToFirst();
        do {
            FifaVersion version = new FifaVersion(cursor.getInt(idColumn),
                    cursor.getString(versionColumn),
                    cursor.getColumnName(releaseColumn));
            result.add(version);
        } while (cursor.moveToNext());
        cursor.close();
        return result;
    }
}
