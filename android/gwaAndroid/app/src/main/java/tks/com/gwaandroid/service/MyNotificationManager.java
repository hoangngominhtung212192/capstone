package tks.com.gwaandroid.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import tks.com.gwaandroid.MainActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.constant.AppConstant;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class MyNotificationManager {

    private Context ctx;
    private static MyNotificationManager instance;

    public MyNotificationManager(Context ctx) {
        this.ctx = ctx;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new MyNotificationManager(context);
        }

        return instance;
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, AppConstant.CHANNEL_ID)
                .setSmallIcon(R.drawable.gundam_icon)
                .setContentTitle("Gunpla World")
                .setContentText(body);

        Intent intent = new Intent(ctx, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}
