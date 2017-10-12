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

   static String[] currencies = new String[]{
            "USD","EUR","NGN",
            "KRW","HKD",
            "NZD","CAD",
            "BRL","AUD","SEK","ZAR","MXN","RUB",
            "GBP","JPY","CNY","PLN","CZK",
            "DKK","KES"
    };

    public static List<CustomObject> loadingJson(Context context, String jsonStr){
        List<CustomObject> customObjects = new ArrayList<>();



        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONObject btc = jsonObject.getJSONObject("BTC");

            JSONObject eth = jsonObject.getJSONObject("ETH");
            for(int i = 0; i<currencies.length; i++){
                Double eth_currency = eth.getDouble(currencies[i]);

                Double btc_currency = btc.getDouble(currencies[i]);

                customObjects.add(new CustomObject(currencies[i], btc_currency, eth_currency));
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


            JSONObject displayObject = jsonObject.getJSONObject("RAW");

            JSONObject btcObject = displayObject.getJSONObject("BTC");

            JSONObject ethObject = displayObject.getJSONObject("ETH");

            for(int i =0; i<currencies.length; i++){

                /**
                 * This will get the ETH values
                 * */
                JSONObject eth_currency = ethObject.getJSONObject(currencies[i]);

                Double eth_price = eth_currency.getDouble("PRICE");
                Long eth_SUPPLY = eth_currency.getLong("SUPPLY");
                Double eth_VOLUME24HOUR = eth_currency.getDouble("VOLUME24HOUR");


                /**
                 * This will get the BTC values
                 * */
                JSONObject btc_currency = btcObject.getJSONObject(currencies[i]);

                Double btc_price = btc_currency.getDouble("PRICE");
                Long btc_SUPPLY = btc_currency.getLong("SUPPLY");
                Double btc_VOLUME24HOUR = btc_currency.getDouble("VOLUME24HOUR");


                customObjects.add(new CustomObject(currencies[i], btc_price, eth_price, String.valueOf(eth_SUPPLY), String.valueOf(btc_SUPPLY)));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(LOGCAT, "Values" + customObjects.size());
        return customObjects;
    }
}
