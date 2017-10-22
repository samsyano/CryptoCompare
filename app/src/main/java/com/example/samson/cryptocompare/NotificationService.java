package com.example.samson.cryptocompare;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by SAMSON on 10/22/2017.
 */

public class NotificationService extends JobService{

    AsyncTask asyncTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        Log.e("JOB", "CryptoJobService working");
        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                Context context = NotificationService.this;
                Log.e("samsyano ", "loading async background");
                CryptoTask.executeTask(context, CryptoTask.LOAD_DATA_WITH_NOTIFICATION);



                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                Log.e("samsyano ", "job loading finished");

            }
        };
        asyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(asyncTask != null)asyncTask.cancel(true);
        return true;
    }
}
