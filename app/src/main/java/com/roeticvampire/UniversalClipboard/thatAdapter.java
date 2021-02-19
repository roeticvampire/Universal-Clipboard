package com.roeticvampire.UniversalClipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class thatAdapter extends RecyclerView.Adapter<thatAdapter.SimpleViewHolder> {

    private final ClipboardDataset clipboardDataset;

    private Context context;

    public thatAdapter(Context zcontext, @NonNull ClipboardDataset clipboardDataset) {
        this.clipboardDataset = clipboardDataset;
        context = zcontext;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_example_view, parent, false);
        return new SimpleViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, final int position) {
        holder.tvPrefix.setText(String.valueOf(position+1)+". "+ this.clipboardDataset.getClipboardItems().get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();


                //we'll be back baby
                try {
                    ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("ClipboardDataset",clipboardDataset.getClipboardItems().get(position) );
                    manager.setPrimaryClip(data);
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                //maybe we're just back

            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return this.clipboardDataset.getCount();
        } catch (Exception er) {
            return 5;
        }

    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrefix;
        LinearLayout linearLayout;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.textBox);
            linearLayout = itemView.findViewById(R.id.outerLayout);

        }
    }


}

