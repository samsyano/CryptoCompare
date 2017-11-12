package com.example.samson.cryptocompare;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SAMSON on 10/7/2017.
 */

public class NetworkUtil {



    public static final String LOGCAT = NetworkUtil.class.getSimpleName();



    public static String makeUrlConnection(String reqUrl){

        String data = null;
        try {
            URL url = getURL(reqUrl);

            Log.e(LOGCAT, "Network called ....");
            data = makeHttpConnection(url);


            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static URL getURL(String reqUrl) throws MalformedURLException {
        URL url = new URL(reqUrl);

        return url;
    }

    private static String makeHttpConnection(URL url){

        String rawData = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                InputStream ins = httpURLConnection.getInputStream();
                rawData = readingRawData(ins);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

    private static String readingRawData(InputStream ins){
        StringBuilder builder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));

        String line;
        try {
            if((line = br.readLine()) != null){
                builder.append(line);
            }
            Log.e(LOGCAT, builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
