package com.baiano.kiosia.fifateampicker.Dao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baiano.kiosia.fifateampicker.DatabaseHelper;
import com.baiano.kiosia.fifateampicker.Model.Flavor;

import java.util.ArrayList;
import java.util.List;

public class FlavorDao {
    public static List<Flavor> findAllFlavors(Context context) {
        String query = "SELECT * FROM flavor";
        return executeQuery(context, query);
    }

    @NonNull
    private static List<Flavor> executeQuery(Context context, String query) {
        List<Flavor> result = new ArrayList<>();
        Cursor cursor = DatabaseHelper.getInstance(context).executeQuery(query);
        int idColumn = cursor.getColumnIndex("id");
        int conditionColumn = cursor.getColumnIndex("conditionType");
        int operatorColumn = cursor.getColumnIndex("operator");
        int factor1Column = cursor.getColumnIndex("factor1");
        int factor2Column = cursor.getColumnIndex("factor2");
        int resultColumn = cursor.getColumnIndex("result");
        cursor.moveToFirst();
        do {
            Flavor flavor = new Flavor(cursor.getInt(idColumn),
                    cursor.getString(conditionColumn),
                    cursor.getColumnName(operatorColumn),
                    cursor.getColumnName(factor1Column),
                    cursor.getColumnName(factor2Column),
                    cursor.getColumnName(resultColumn));
            result.add(flavor);
        } while (cursor.moveToNext());
        cursor.close();
        return result;
    }
}
