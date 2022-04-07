package com.gligamihai.licenta.licenta.utils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;


public class DocumentAdapter extends FirestoreRecyclerAdapter<Document, DocumentAdapter.DocumentHolder> {
    public OnItemClickListeners listener;

    public DocumentAdapter(@NonNull FirestoreRecyclerOptions<Document> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHolder holder, int position, @NonNull Document document) {
        holder.documentType.setText(document.getType());
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

        public DocumentHolder(View itemView) {
            super(itemView);
            documentType = itemView.findViewById(R.id.document_type);
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
                    int position= getBindingAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
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
