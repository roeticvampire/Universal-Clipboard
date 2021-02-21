package com.roeticvampire.UniversalClipboard;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    ClipboardManager clipboard;
    TextView footheader;

    ImageButton copy_btn, close_btn;
    RecyclerView recyclerBoi;
    public static ClipboardDataset clipboardDataset;
    DatabaseReference myRef;
    CountDownLatch done = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        footheader = findViewById(R.id.footheader);
        close_btn = findViewById(R.id.close_btn);
        copy_btn = findViewById(R.id.copy_btn);
        myRef = FirebaseDatabase.getInstance().getReference("SomeOtherLocation");
        footheader.setText("Last Five items");
        List<String> tempList = new ArrayList<>();
        tempList.add("Entry #1");
        tempList.add("Entry #2");
        tempList.add("Entry #3");
        tempList.add("Entry #4");
        tempList.add("Entry #5");
        clipboardDataset = new ClipboardDataset(tempList);

        InitialiseClipboard();

        //Recycler part
        recyclerBoi = findViewById(R.id.recyclerBoi);
        recyclerBoi.setLayoutManager(new LinearLayoutManager(this));
        recyclerBoi.setAdapter(new datasetAdapter(this, clipboardDataset));
        recyclerBoi.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Making notification channel for API26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Channel 1", "MyNotif", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        //Here we add the copy text facility
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToClipboard();

            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make temporary notification to cancel out the persistent one
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(MainActivity.this);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Channel 1")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Thank You for using our App")
                        .setContentText("We're are glad for your trust")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setColor(Color.parseColor("#192D40"));
                notificationManager.notify(1, builder.build());

                //Kill app
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
    }

    private void makeNotification() {
        Intent intent = new Intent(this, NotificationReciever_copy.class);
        intent.putExtra("Current Clip Item", clipboardDataset.getClipboardItems().get(0));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(MainActivity.this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Channel 1")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Previously Copied")
                .setContentText(clipboardDataset.getClipboardItems().get(0))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(R.drawable.copy_icon, "Copy", pendingIntent)
                .setColor(Color.parseColor("#192D40"))
                .setOnlyAlertOnce(true)
                .setOngoing(true);

        notificationManager.notify(1, builder.build());

    }


    public void InitialiseClipboard() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    clipboardDataset = snapshot.getValue(ClipboardDataset.class);
                    recyclerBoi.setAdapter(new datasetAdapter(MainActivity.this, clipboardDataset));
                    makeNotification();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                List<String> tempList = new ArrayList<>();
                clipboardDataset = new ClipboardDataset(tempList);
                recyclerBoi.getAdapter().notifyDataSetChanged();
                myRef.setValue(clipboardDataset);

            }
        });

    }

    public void sendToClipboard() {

        clipboardDataset.updateData(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
        recyclerBoi.getAdapter().notifyDataSetChanged();
        myRef.setValue(clipboardDataset);
        Toast.makeText(MainActivity.this, "Added to Clipboard", Toast.LENGTH_SHORT).show();
    }
}
