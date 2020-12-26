package com.numerical.numerical.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.numerical.numerical.R;
import com.numerical.numerical.Utility.ErrorMessage;
import com.numerical.numerical.Utility.SavedData;
import com.numerical.numerical.activity.LatestFeedDetailActivity;
import com.numerical.numerical.database.NotificationHelper;
import com.numerical.numerical.database.Notification_Model;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SavedData.saveTokan(s);
        Log.e("", "Refreshed token:" + s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
          /*  Log.e(TAG, "From data: " + remoteMessage.getData());
            Log.e(TAG, "From data: " + remoteMessage.getFrom());
            Log.e(TAG, "From data: " + remoteMessage.getNotification().getClickAction());*/
            handleNotification(remoteMessage.getNotification().getClickAction().toString(), remoteMessage.getNotification().getBody().toString(), remoteMessage.getNotification().getTitle().toString(), remoteMessage.getNotification().getIcon().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "From data: " + e.toString());
        }
    }


    private void handleNotification(String message, String content, String title, String ImagePath) {
        try {
            int cart_item = Integer.parseInt(SavedData.getAddToCart_Count()) + 1;
            SavedData.saveAddToCart_Count(String.valueOf(cart_item));
            Notification_Model notification_model=new Notification_Model();
            notification_model.setTitle(title);
            notification_model.setBody(content);
            notification_model.setImage_url(ImagePath);
            notification_model.setCollection_id(message.substring(message.lastIndexOf("/")).replaceAll("/", ""));
            NotificationHelper.getInstance().insertNotification_Model(notification_model);
        } catch (Exception e) {}

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = null;
            pushNotification = new Intent(Config.Update);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }

        ErrorMessage.E("Exception handleNotification" + getBitmapFromURL(ImagePath));
        /*try {
            InputStream in = new URL(Icon).openStream();
            bmp = BitmapFactory.decodeStream(in);
            ErrorMessage.E("Exception>>"+bmp);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.E("Exception"+e.toString());
        }*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                ErrorMessage.E("if is working");
                final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
                Intent intent = new Intent(this, LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());

                Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setLargeIcon(getBitmapFromURL(ImagePath)).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                mNotificationUtils.getManager().notify(0, nb.build());
            }else {
                ErrorMessage.E("Else is working");
                final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
                Intent intent = new Intent(this, LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());

                Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                mNotificationUtils.getManager().notify(0, nb.build());
            }
        } else {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                final int icon = R.mipmap.ic_launcher_round;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(content);
                bigPictureStyle.bigPicture(getBitmapFromURL(ImagePath));
                Notification notification;
                notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher_round).setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setContentText(content).setContentIntent(pendingIntent).setStyle(bigPictureStyle).setSmallIcon(R.mipmap.ic_launcher_round).setLargeIcon(BitmapFactory.decodeResource(this.getResources(), icon)).build();

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
            }else {
                Intent intent = new Intent(getApplicationContext(), LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("Name", "Firebase");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());
            //Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Kamino", "By " + "Burhan");
            remoteViews.setTextViewText(R.id.title, title);
            remoteViews.setTextViewText(R.id.text, content);
            remoteViews.setImageViewBitmap(R.id.Notification_tem_img, getBitmapFromURL(ImagePath));
           // Glide.with(getApplicationContext()).load(ImagePath).into(remoteViews.setImageViewBitmap(););

            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setContent(remoteViews).setContentIntent(pIntent).setSmallIcon(R.mipmap.ic_launcher_round).setAutoCancel(true);
            mNotificationUtils.getManager().notify(0, nb.build());
        } else {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            remoteViews.setTextViewText(R.id.title, title);
            remoteViews.setTextViewText(R.id.text, content);
            remoteViews.setImageViewBitmap(R.id.Notification_tem_img, getBitmapFromURL(ImagePath));

            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("Name", "Firebase");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    // Set Icon
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    // Set Ticker Message
                    //.setTicker(getString(R.string.customnotificationticker))
                    // Dismiss Notification
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setAutoCancel(true)
                    // Set PendingIntent into Notification
                    .setContentIntent(pIntent).setAutoCancel(true)
                    // Set RemoteViews into Notification
                    .setContent(remoteViews);



            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            // Create Notification Manager
            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build Notification with Notification Manager
            notificationmanager.notify(0, builder.build());
            *//*=====================================*//*
        }*/


    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void CustomNotification(String source, String destination, String Ride_Status) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("Check", "Custom_Notification");
            intent.putExtra("Ride_Status", Ride_Status);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());
            //Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Kamino", "By " + "Burhan");
            remoteViews.setTextViewText(R.id.title, "Ride is Accepted");
            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setContent(remoteViews).setContentIntent(pIntent).setSmallIcon(R.drawable.ic_logo).setAutoCancel(true);
            mNotificationUtils.getManager().notify(101, nb.build());
        } else {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("Check", "Custom_Notification");
            intent.putExtra("Ride_Status", Ride_Status);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    // Set Icon
                    .setSmallIcon(R.drawable.ic_logo)
                    // Set Ticker Message
                    //.setTicker(getString(R.string.customnotificationticker))
                    // Dismiss Notification
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setAutoCancel(true)
                    // Set PendingIntent into Notification
                    .setContentIntent(pIntent).setAutoCancel(true)
                    // Set RemoteViews into Notification
                    .setContent(remoteViews);


            remoteViews.setTextViewText(R.id.title, "Ride is Accepted");
            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            // Create Notification Manager
            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build Notification with Notification Manager
            notificationmanager.notify(101, builder.build());
            /*=====================================*/
        }

    }
    /*private void handleNotificationlogout(String title, String Content, String message) {

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = null;
            if (title.equals("logout")) {
                pushNotification = new Intent(Config.logout);
                UserProfileHelper.getInstance().delete();
                ErrorMessage.I_clear(MyFirebaseMessagingService.this, LoginActivity.class, null);
            }
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

        } else {
            // app is in background, show the notification in notification tray
            UserProfileHelper.getInstance().delete();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("Check", "");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).
                    setSmallIcon(R.drawable.ic_logo).setContentTitle(title).setContentText(Content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());


        }


    }*/


}

