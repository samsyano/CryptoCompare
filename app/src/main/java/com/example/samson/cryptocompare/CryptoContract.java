package com.example.samson.cryptocompare;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SAMSON on 10/13/2017.
 */

public class CryptoContract {

    public static final String AUTHORITY = "com.example.samson.cryptocompare";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH = "cryptocurrencies";

    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + AUTHORITY + "/"+ PATH;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + AUTHORITY + "/" + PATH;

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();


  static   class CryptoEntry implements BaseColumns{


        public static final String TABLE_NAME = "CRYPTOCURRENCY";


        public static final String ID = BaseColumns._ID;
        public static final String COUNTRY_CURRENCY = "COUNTRY_CURRENCY";
        public static final String BTC_EQUIVALENT = "BTC_EQUIVALENT";
        public static final String ETH_EQUIVALENT = "ETH_EQUIVALENT";

      public static final String IMAGE_URL = "IMAGE_URL";


    }
}
