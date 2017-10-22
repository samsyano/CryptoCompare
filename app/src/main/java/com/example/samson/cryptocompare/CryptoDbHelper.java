package com.example.samson.cryptocompare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SAMSON on 10/13/2017.
 */

public class CryptoDbHelper extends SQLiteOpenHelper {

    public static String db_name = "Cryptocurrency";
    public static int DB_VERSION = 1;

    public CryptoDbHelper(Context context) {
        super(context, db_name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_query = "CREATE TABLE "  + CryptoContract.CryptoEntry.TABLE_NAME + " (" +
                CryptoContract.CryptoEntry.ID + " INTEGER PRIMARY KEY, " +
                CryptoContract.CryptoEntry.COUNTRY_CURRENCY + " TEXT NOT NULL, " +
                CryptoContract.CryptoEntry.BTC_EQUIVALENT + " TEXT NOT NULL, "   +
                CryptoContract.CryptoEntry.ETH_EQUIVALENT + " TEXT NOT NULL);";

        db.execSQL(sql_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql_query = "DROP TABLE IF EXISTS" + CryptoContract.CryptoEntry.TABLE_NAME;
        db.execSQL(sql_query);
        onCreate(db);
    }
}
