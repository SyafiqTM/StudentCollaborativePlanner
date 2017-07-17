package com.example.syafiq.smartplanner;
//Created by syafiq on 20/12/2016.

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        NotificationManager notificationManager;

        int notificationID = 35;

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(context)
                .setContentTitle("Student Collaborative Planner")
                .setContentText("You have 1 task on due")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.ic_launcher);

        Intent moreInfoIntent = new Intent(context,ViewTask.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(ViewTask.class);
        taskStackBuilder.addNextIntent(moreInfoIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificBuilder.setAutoCancel(true);
        notificBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationID,notificBuilder.build());



//        Toast.makeText(context, "Your Alarm is Ringing ☺",
//                Toast.LENGTH_LONG).show();
//        // Vibrate the mobile phone
    }

}