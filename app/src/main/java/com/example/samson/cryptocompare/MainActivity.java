package com.example.samson.cryptocompare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<CustomObject>> {

    private static String LOGCAT = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;

    final private static int LOADER_CONSTANT = 2233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        getSupportLoaderManager().initLoader(LOADER_CONSTANT, null, this);
    }



    /**JSON DATA SOURCE*/
//    final String url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC,ETH&tsyms=USD," +
//            "EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES";

    final String url = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD," +
            "EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES";

//    https://min-api.cryptocompare.com/data/price?fsyms=BTC,ETH&tsyms=USD,EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES

    @Override
    public Loader<List<CustomObject>> onCreateLoader(int id, Bundle args) {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            return new AsyncTaskLoader<List<CustomObject>>(this) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public List<CustomObject> loadInBackground() {

                    String data = NetworkUtil.makeUrlConnection(url);

                    Log.e(LOGCAT, data);

                    List<CustomObject> objectList = ReadingJson.loadingJson(getApplicationContext(), data);
                    Log.e(LOGCAT, "size of list of customObjects" + objectList.size());


                    return objectList;
                }
            };

        }

        return null;
    }



    @Override
    public void onLoadFinished(Loader<List<CustomObject>> loader, List<CustomObject> data) {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(Loader<List<CustomObject>> loader) {

    }
}
