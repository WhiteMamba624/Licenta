package com.gligamihai.licenta.licenta.utils;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.type.DateTime;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DocumentAdapter extends FirestoreRecyclerAdapter<Document, DocumentAdapter.DocumentHolder> {
    public OnItemClickListeners listener;

    public DocumentAdapter(@NonNull FirestoreRecyclerOptions<Document> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHolder holder, int position, @NonNull Document document) {
        holder.documentType.setText(document.getType());
        if (document.getType().equalsIgnoreCase("ITP")) {
            holder.documentImage.setBackgroundResource(R.drawable.ic_itp);
        } else if (document.getType().equalsIgnoreCase("RCA")) {
            holder.documentImage.setBackgroundResource(R.drawable.rca);
        } else {
            holder.documentImage.setBackgroundResource(R.drawable.ic_rovinieta);
        }
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //holder.documentExpiryDate.setText(document.getExpiryDate());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date currentDate1 = format.parse(currentDate);
            Date documentExpiryDate = format.parse(document.getExpiryDate());
            long diff = documentExpiryDate.getTime() - currentDate1.getTime();
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 1) {
                holder.documentExpiryDate.setText("Your document is expiring tomorrow");
            } else if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 0) {
                holder.documentExpiryDate.setTextColor(Color.parseColor("#FF0000"));
                holder.documentExpiryDate.setTextSize(24);
                holder.documentExpiryDate.setTypeface(Typeface.DEFAULT_BOLD);
                holder.documentExpiryDate.setText("Document expired "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" ago");
                holder.documentImage.setBackgroundResource(R.drawable.ic_expired_bg);
            } else {
                holder.documentExpiryDate.setText("Days " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @NotNull
    @Override
    public DocumentHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item, parent, false);
        return new DocumentHolder(v);
    }

    public void deleteDocument(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class DocumentHolder extends RecyclerView.ViewHolder {
        TextView documentType;
        TextView documentExpiryDate;
        CardView documentCardViewItem;
        ImageView documentImage;

        public DocumentHolder(View itemView) {
            super(itemView);
            documentType = itemView.findViewById(R.id.document_type);
            documentExpiryDate = itemView.findViewById(R.id.document_expiry_date);
            documentCardViewItem = itemView.findViewById(R.id.cardViewDocumentItem);
            documentImage = itemView.findViewById(R.id.document_image);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(getSnapshots().getSnapshot(position), position);
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListeners {
        void onItemLongClick(DocumentSnapshot documentSnapshot, int position);

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListeners(OnItemClickListeners listener) {
        this.listener = listener;
    }


}
