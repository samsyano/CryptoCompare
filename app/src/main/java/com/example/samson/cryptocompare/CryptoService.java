package com.example.samson.cryptocompare;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by SAMSON on 10/11/2017.
 */

public class CryptoService extends IntentService {

    public CryptoService() {
        super(CryptoService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();
        Log.e("SERVICE ", "Service working.." + action);
        if(action.equals(CryptoTask.LOAD_DATA)){

            CryptoTask.executeTask(this, action);
            Log.e("SERVICE ", "Service working.." + action);
        }

    }
}
