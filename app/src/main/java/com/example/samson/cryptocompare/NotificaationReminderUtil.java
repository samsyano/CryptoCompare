package com.example.samson.cryptocompare;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by SAMSON on 10/22/2017.
 */

public class NotificaationReminderUtil {


    public static final int REMINDER_INTERVAL_MINUTES = 3;
    public static final int REMINDER_INTERVAL_SECONDS = (int) java.util.concurrent.TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES);
    public static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String JOB_SCHEDULE = "notification-tag";

    static boolean sInitialize = false;

    synchronized public static void scheduleReminder(Context context) {

        if (sInitialize) {
            return;
        }
        GooglePlayDriver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);


        Job reminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(NotificationService.class)
                .setTag(JOB_SCHEDULE)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS
                        + SYNC_FLEXTIME_SECONDS))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .build();
        Log.e("JOB", "job working perfectly");
        Log.e("JOB", "job working " + reminderJob.getService());
        firebaseJobDispatcher.schedule(reminderJob);

        sInitialize = true;
    }

}