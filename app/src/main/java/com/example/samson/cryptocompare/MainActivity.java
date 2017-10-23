package com.example.samson.cryptocompare;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String LOGCAT = MainActivity.class.getSimpleName();


    final private static int LOADER_CONSTANT = 2233;


    TextView  btc_currency, eth_currency, to_currency1, to_currency2;
    Spinner spinner;
    CardView cardView;

    SimpleCursorAdapter simpleCursorAdapter;
    Loader dataLoader;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);



        getContentResolver().delete(CryptoContract.CONTENT_URI, null, null);

        Intent intent = new Intent(this, CryptoService.class);
        intent.setAction(CryptoTask.LOAD_DATA);
        Log.e("SERVICE", "service to start...");
        startService(intent);


        ReminderUtils.scheduleReminder(MainActivity.this);
        NotificaationReminderUtil.scheduleReminder(MainActivity.this);



        spinner = (Spinner) findViewById(R.id.spinner_spin);

        btc_currency = (TextView) findViewById(R.id.btc);
        eth_currency = (TextView) findViewById(R.id.eth);
        to_currency1 = (TextView) findViewById(R.id.to_currency);
        to_currency2 = (TextView) findViewById(R.id.to_currency2);

        cardView = (CardView) findViewById(R.id.card);




        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btc = (String) btc_currency.getText();
                String eth = (String) eth_currency.getText();

                if(TextUtils.isEmpty(btc) && TextUtils.isEmpty(eth)){
                    return;
                }




                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("btc", btc );
                intent.putExtra("eth", eth);
                intent.putExtra("currency", currency);
                startActivity(intent);

            }
        });





        dataLoader =  getSupportLoaderManager().initLoader(LOADER_CONSTANT, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().getContentResolver()
                .registerContentObserver(CryptoContract.CONTENT_URI, true, new DataObserver(new Handler()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.refresh_menu:

                getContentResolver().delete(CryptoContract.CONTENT_URI, null, null);

                Intent intent = new Intent(this, CryptoService.class);
                intent.setAction(CryptoTask.LOAD_DATA);
                Log.e("SERVICE", "service to start...");
                startService(intent);

               dataLoader =  getSupportLoaderManager().restartLoader(LOADER_CONSTANT, null, this);

                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading Data...");
        progressDialog.setMessage("loading....");
        Log.e(LOGCAT, "ProgressDialog working");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                Log.e("LOADER ", "loader started....");

                try {
                   return getContext().getContentResolver().query(CryptoContract.CONTENT_URI, null, null, null, CryptoContract.CryptoEntry.ID);
                }catch (Exception e){
                    Log.e(LOGCAT, "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.e("LOADER ", "onloadfinished loaded...");

        if(data.getCount() > 0){
            progressDialog.cancel();
        }


        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                null, new String[]{CryptoContract.CryptoEntry.COUNTRY_CURRENCY}, new int[]{android.R.id.text1}, 0);

        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(simpleCursorAdapter);

        simpleCursorAdapter.notifyDataSetChanged();
        simpleCursorAdapter.swapCursor(data);


        final Cursor cursor = data;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if( cursor == null){
                    return;
                }

                int id_columnIndex = cursor.getColumnIndex(CryptoContract.CryptoEntry.ID);

                int currency_columnIndex = cursor.getColumnIndex(CryptoContract.CryptoEntry.COUNTRY_CURRENCY);
                int btc_columnIndex = cursor.getColumnIndex(CryptoContract.CryptoEntry.BTC_EQUIVALENT);
                int eth_columnIndex = cursor.getColumnIndex(CryptoContract.CryptoEntry.ETH_EQUIVALENT);


                cursor.moveToPosition(position);

                String btc = cursor.getString(btc_columnIndex);
                String eth = cursor.getString(eth_columnIndex);

                 currency = cursor.getString(currency_columnIndex);


                btc_currency.setText(btc);
                eth_currency.setText(eth);
                to_currency1.setText(currency);
                to_currency2.setText(currency);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
String currency;
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        simpleCursorAdapter.swapCursor(null);
    }

//    final String url = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD," +
//            "EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES";

//    https://min-api.cryptocompare.com/data/price?fsyms=BTC,ETH&tsyms=USD,EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES

/**
    @Override
    public Loader<List<CustomObject>> onCreateLoader(int id, Bundle args) {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

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

//                    List<CustomObject> objectList = ReadingJson.loadingJson(getApplicationContext(), data);
                    List<CustomObject> objectList = ReadingJson.loadJson(getApplicationContext(), data);
                    Log.e(LOGCAT, "size of list of customObjects" + objectList.size());


                    return objectList;
                }
            };

        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<List<CustomObject>> loader, final List<CustomObject> data) {


        spinner = (Spinner) findViewById(R.id.spinner);

        currency_symbol = (TextView) findViewById(R.id.currency_symbol);
        currency_symbol2 = (TextView) findViewById(R.id.currency_symbol);
        btc_currency = (TextView) findViewById(R.id.btc_naira_value);
        eth_currency = (TextView) findViewById(R.id.eth_currency_value);

//        CustomObject object = data.get()
//        HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,CZK,DKK,KES
        List<String> header = new ArrayList<>();

        for(int i = 0; i < data.size(); i++){
            header.add(data.get(i).getCurrency());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, header);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomObject object = data.get(position);

                btc_currency.setText(" " + object.getBTC());
                eth_currency.setText(" " + object.getETC());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onLoaderReset(Loader<List<CustomObject>> loader) {

    }*/

class DataObserver extends ContentObserver{

    public DataObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.e("DATARECEIVER ", "dataReceiver called");
        dataLoader.forceLoad();
    }
}

class DataSetReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean dataChanged = action.equals(Intent.ACTION_DATE_CHANGED);
        if(dataChanged){
            simpleCursorAdapter.notifyDataSetChanged();
        }

    }
}
}
