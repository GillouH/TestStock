package com.example.teststock.models.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.example.teststock.R;

public class NotificationHelper{
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";

    private final Context context;

    public NotificationHelper(Context context){
        this.context = context;
    }

    @SuppressWarnings("SameParameterValue")
    private void sendNotification(
            String channelID,
            String channelName,
            @ColorRes Integer notificationColor,
            @DrawableRes Integer notificationIcon,
            String title,
            String subTitle,
            String littleText,
            String longText,
            Intent intent,
            NotificationButton[] notificationButtonArray,
            int notificationID,
            boolean showBadge
    ){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setShowBadge(showBadge);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Notification.Builder notificationBbuilder = new Notification.Builder(context, channelID);
            notificationBbuilder.setContentTitle(title);
            if(subTitle != null){
                notificationBbuilder.setSubText(subTitle);
            }
            notificationBbuilder.setContentText(littleText);
            if(notificationIcon != null){
                notificationBbuilder.setSmallIcon(notificationIcon);
            }
            if(longText != null){
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
                String[] stringArrayList = longText.split("\n");
                for(String string : stringArrayList){
                    inboxStyle.addLine(string);
                }
                notificationBbuilder.setStyle(inboxStyle);
            }

            if(intent != null){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingTapIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBbuilder.setContentIntent(pendingTapIntent);
                notificationBbuilder.setAutoCancel(false);
            }

            if(notificationButtonArray != null){
                Intent buttonIntent;
                PendingIntent pendingIntent;
                Icon icon;
                Notification.Action.Builder notificationActionBuilder;
                Notification.Action action;
                for(NotificationButton notificationButton : notificationButtonArray){
                    if(notificationButton != null){
                        buttonIntent = notificationButton.getIntent();
                        buttonIntent.setAction("Action");
                        pendingIntent = PendingIntent.getActivity(context, 0, buttonIntent, 0);
                        icon = Icon.createWithResource(context, notificationButton.getDrawable());
                        notificationActionBuilder = new Notification.Action.Builder(icon, notificationButton.getButtonName(), pendingIntent);
                        action = notificationActionBuilder.build();
                        notificationBbuilder.addAction(action);
                    }
                }
            }

            if(notificationColor != null){
                notificationBbuilder.setColor(context.getResources().getColor(notificationColor, null));
            }

            Notification notification = notificationBbuilder.build();
            if(notificationManager != null){
                notificationManager.notify(notificationID, notification);
            }
        }
    }

    private void sendNotification(String title, String subTitle, String littleText, String longText, Intent intent, NotificationButton[] notificationButtonArray, int notificationID){
        sendNotification(CHANNEL_ID, CHANNEL_NAME, R.color.colorPrimary, R.drawable.ic_baseline_warning_24, title, subTitle, littleText, longText, intent, notificationButtonArray, notificationID, true);
    }

    public void sendNotification(String title, String subTitle, String littleText, String longText, Intent intent, NotificationButton notificationButtonArray, int notificationID){
        sendNotification(title, subTitle, littleText, longText, intent, new NotificationButton[]{notificationButtonArray, null, null}, notificationID);
    }

    public void sendNotification(String title, String subTitle, String littleText, String longText, Intent intent, int notificationID){
        sendNotification(title, subTitle, littleText, longText, intent, (NotificationButton[])null, notificationID);
    }
}