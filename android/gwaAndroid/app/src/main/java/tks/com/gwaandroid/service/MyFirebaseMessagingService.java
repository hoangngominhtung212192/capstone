package tks.com.gwaandroid.service;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        int accountID = Integer.parseInt(remoteMessage.getData().get("accountID"));

        System.out.println("[onMessageReceived] Title: " + title + " , content: " + body);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int current_accountID = sharedPreferences.getInt("ACCOUNTID", 0);

        if (current_accountID != 0 && current_accountID == accountID) {
            MyNotificationManager.getInstance(getApplicationContext())
                    .displayNotification(title, body);
        }
    }
}
