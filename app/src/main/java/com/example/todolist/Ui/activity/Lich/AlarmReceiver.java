package com.example.todolist.Ui.activity.Lich;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todolist.R;
import com.example.todolist.Ui.activity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "todo_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String taskTitle = intent.getStringExtra("TASK_TITLE");
            int taskId = intent.getIntExtra("TASK_ID", 0);

            Log.d("AlarmReceiver", "Báo thức đã nổ! Task: " + taskTitle); // Log để check xem code có chạy không

            createNotificationChannel(context);

            Intent appIntent = new Intent(context, MainActivity.class);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    taskId,
                    appIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_bell) // <--- SỬA LẠI THÀNH ic_bell
                    .setContentTitle("Nhắc nhở công việc")
                    .setContentText(taskTitle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // Hiện trên màn hình khóa

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);

            // Kiểm tra quyền (Bắt buộc với Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.e("AlarmReceiver", "Thiếu quyền thông báo!");
                    return;
                }
            }

            manager.notify(taskId, builder.build());

        } catch (Exception e) {
            Log.e("AlarmReceiver", "Lỗi hiển thị thông báo", e);
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Thông báo công việc";
            String description = "Kênh thông báo nhắc nhở công việc";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Phải là HIGH mới hiện popup
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}