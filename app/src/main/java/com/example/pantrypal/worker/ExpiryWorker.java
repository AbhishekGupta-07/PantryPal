package com.example.pantrypal.worker;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.PantryItem;
import com.example.pantrypal.R;
import com.example.pantrypal.utils.ExpiryUtils;
import com.example.pantrypal.utils.NotificationUtils;

import java.util.List;

public class ExpiryWorker extends Worker {

    public ExpiryWorker(@NonNull Context context,
                        @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();

        // Create notification channel (safe to call multiple times)
        NotificationUtils.createChannel(context);

        List<PantryItem> items =
                PantryDatabase.getInstance(context)
                        .pantryDao()
                        .getAllItems();

        for (PantryItem item : items) {
            String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());

            if ("Expiring Soon".equals(status)) {
                sendNotification(
                        context,
                        item.getName(),
                        item.getExpiryDate(),
                        item.getId()   // 👈 unique notification id
                );
            }
        }

        return Result.success();
    }

    private void sendNotification(Context context,
                                  String name,
                                  String expiry,
                                  int notificationId) {

        // Android 13+ permission safety
        if (Build.VERSION.SDK_INT >= 33 &&
                context.checkSelfPermission(
                        android.Manifest.permission.POST_NOTIFICATIONS)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Item Expiring Soon")
                        .setContentText(name + " expires on " + expiry)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);

        if (manager != null) {
            manager.notify(notificationId, builder.build());
        }
    }
}
