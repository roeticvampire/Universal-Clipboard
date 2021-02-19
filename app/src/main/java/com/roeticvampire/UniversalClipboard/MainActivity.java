package com.roeticvampire.UniversalClipboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
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

public class MainActivity extends AppCompatActivity {
    ClipboardManager clipboard;
    TextView footheader;

    ImageButton copy_btn,close_btn;
    RecyclerView recyclerBoi;
    ClipboardDataset clipboardDataset;
    DatabaseReference myRef;
    CountDownLatch done = new CountDownLatch(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboard= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        footheader=findViewById(R.id.footheader);
        close_btn=findViewById(R.id.close_btn);
        copy_btn=findViewById(R.id.copy_btn);
        myRef = FirebaseDatabase.getInstance().getReference("SomeOtherLocation");
        footheader.setText("Last three items");
        List<String> tempList= new ArrayList<>();
        tempList.add("Entry #1");
        tempList.add("Entry #2");
        tempList.add("Entry #3");

        clipboardDataset = new ClipboardDataset(tempList);

        InitialiseClipboard();


        //Recycler part
        recyclerBoi=findViewById(R.id.recyclerBoi);
        recyclerBoi.setLayoutManager(new LinearLayoutManager(this));
        recyclerBoi.setAdapter(new thatAdapter(this, clipboardDataset));
        recyclerBoi.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



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
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    });
    }




        public void InitialiseClipboard(){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){ clipboardDataset=snapshot.getValue(ClipboardDataset.class);
                       recyclerBoi.setAdapter(new thatAdapter( MainActivity.this, clipboardDataset));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    List<String> tempList= new ArrayList<>();
                    clipboardDataset = new ClipboardDataset(tempList);

                }
            });

        }
        public void sendToClipboard(){

            clipboardDataset.updateData(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
            recyclerBoi.getAdapter().notifyDataSetChanged();
            myRef.setValue(clipboardDataset);
            Toast.makeText(MainActivity.this, "Added to Clipboard", Toast.LENGTH_SHORT).show();
        }
}
