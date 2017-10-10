package com.example.samson.cryptocompare;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMSON on 10/7/2017.
 */

public class ReadingJson {

    private static String LOGCAT = ReadingJson.class.getSimpleName();

    public static List<CustomObject> loadingJson(Context context, String jsonStr){
        List<CustomObject> customObjects = new ArrayList<>();

        String[] currencies = new String[]{
                "USD","EUR","NGN",
                "KRW","HKD",
                "NZD","CAD",
                "BRL","AUD","SEK","ZAR","MXN","RUB",
                "GBP","JPY","CNY","PLN","CZK",
                "DKK","KES"
        };

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONObject btc = jsonObject.getJSONObject("BTC");

            JSONObject eth = jsonObject.getJSONObject("ETH");
            for(int i = 0; i<currencies.length; i++){
                Double eth_currency = eth.getDouble(currencies[i]);

                Double btc_currency = btc.getDouble(currencies[i]);

                customObjects.add(new CustomObject(currencies[i], eth_currency, btc_currency));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(LOGCAT, " " + customObjects.size());
        return customObjects;
    }

    public static List<CustomObject> loadJson(Context context, String jsonStr){

        List<CustomObject> customObjects = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);


            JSONObject displayObject = jsonObject.getJSONObject("DISPLAY");

            JSONObject btcObject = displayObject.getJSONObject("BTC");

            JSONObject usdObject = btcObject.getJSONObject("USD");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customObjects;
    }
}
