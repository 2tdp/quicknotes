package datnt.activity.com;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    ArrayList<Note> arrayList = new ArrayList<>();

    SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.getDefault());
    SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.getDefault());

    @Override
    public void onReceive(Context context, Intent intent) {
        SQLHelper sqlHelper = new SQLHelper(context);
        sqlHelper.getAllNote();
        arrayList.addAll(sqlHelper.getAllNote());

        //getTimeNow
        long dd = Long.parseLong(day.format(new Date()));
        long MM = Long.parseLong(month.format(new Date()));
        long yyyy = Long.parseLong(year.format(new Date()));
        long HH = Long.parseLong(hour.format(new Date()));
        long mm = Long.parseLong(minutes.format(new Date()));
        long time = HH * 60 + mm;
        long date = yyyy + MM + dd;
        Log.d("TimeNow: ", "Day: " + dd + ", Month: " + MM + ", Year: " + yyyy + ", Hour: " + HH + ", Minutes: " + mm + ", Time: " + time + ", Date: " + date);

        //getTimeDB
        for (int i = 0; i < arrayList.size(); i++) {
            Note note = arrayList.get(i);
            long timeAlarm = note.getAlarmTime();
            long dateAlarm = note.getAlarmDate();
            long noticeAlarm = note.getAlarmNotice();
            Log.e("AlarmTime: ", "onReceive: " + timeAlarm + "\n" + dateAlarm + "\n" + noticeAlarm);

            intent = new Intent(context, NewNotes.class);
            intent.putExtra("notesAlarm", note);

            String titleAlarm = note.getTitle();

            if ((timeAlarm - time) == noticeAlarm && dateAlarm == date) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                    taskStackBuilder.addParentStack(NewNotes.class);
                    taskStackBuilder.addNextIntent(intent);

                    PendingIntent taskStack = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pendingIntent = PendingIntent.getActivities(context, 1, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "05")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.iconnote)
                            .setContentTitle(titleAlarm)
                            .setContentText("Remining " + noticeAlarm + " minutes left!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setChannelId("05")
                            .setContentIntent(taskStack)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    notificationManagerCompat.notify(1, builder.build());
                    sqlHelper.deleteNotes(note.getIdNote());
                }
            }
        }
    }
}
