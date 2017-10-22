package com.example.samson.cryptocompare;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by SAMSON on 10/13/2017.
 */

public class CryptoTask {

    public static String LOAD_DATA = "load-data";
    public static String LOAD_REMINDER =  "load-reminder";

    public static String LOAD_DATA_WITH_NOTIFICATION = "load-data-notification";
    final static String url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC,ETH&" +
            "tsyms=USD,EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES";


   static void executeTask(Context context, String action){
        if(action.equals(LOAD_DATA)){
            loadingData(context);
        }else if(action.equals(LOAD_REMINDER)){
            loadingData(context);
        }else if(action.equals(LOAD_DATA_WITH_NOTIFICATION)){
            loadWithNotification(context);
        }
    }

    private static void loadWithNotification(Context context){
        loadingData(context);
        NotificationUtils.remindForUpdate(context);
    }

    private static void loadingData(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if(networkInfo == null ){
          return ;
        }


        if (!networkInfo.isConnected() ) {
            return;
        }

        String data = NetworkUtil.makeUrlConnection(url);

        List<ContentValues> list = ReadingJson.loadJsonToContentProvider(context, data);

        context.getContentResolver().delete(CryptoContract.CONTENT_URI, null, null);
        if ( list == null ) {
            return;
        }

        if(list.size() > 0) {
            for (ContentValues c : list) {
                Uri uri = context.getContentResolver().insert(CryptoContract.CONTENT_URI, c);
                Log.e("INSERTING :", "Content inserted at " + uri.toString());
            }

        }

    }
}
