package com.example.samson.cryptocompare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by SAMSON on 10/14/2017.
 */

public class NotificationUtils {

    private static final int PENDING_INTENT_ID = 3334;
    private static final  int NOTIFICATION_MANAGER_ID = 5612;



    static void remindForUpdate(Context context){

        String message = "Checkout for the latest conversion rate for cryptocurrency";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
              notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent(context))
                .setLargeIcon(largeIcon(context))
                .setSmallIcon(R.mipmap.launcher_icon)
                .setContentTitle("Cryptocurrency update...")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle(notificationBuilder).bigText(message))
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_MANAGER_ID, notificationBuilder.build());


    }

    static PendingIntent pendingIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(context, PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }



static Bitmap largeIcon(Context context){
    Resources res = context.getResources();

    return BitmapFactory.decodeResource(res, R.mipmap.launcher_icon);
}


}
