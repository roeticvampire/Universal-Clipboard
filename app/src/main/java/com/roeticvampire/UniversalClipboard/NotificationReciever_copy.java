package com.roeticvampire.UniversalClipboard;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReciever_copy extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String clip = intent.getStringExtra("Current Clip Item");
            ClipData data = ClipData.newPlainText("ClipboardDataset", clip );
            manager.setPrimaryClip(data);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
