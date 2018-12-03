package tks.com.gwaandroid.service;

import android.app.NotificationManager;
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

        System.out.println("[onMessageReceived] Title: " + title + " , content: " + body);

        MyNotificationManager.getInstance(getApplicationContext())
                .displayNotification(title, body);
    }
}
