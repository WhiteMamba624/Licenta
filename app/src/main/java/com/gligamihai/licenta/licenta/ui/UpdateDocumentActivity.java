package com.gligamihai.licenta.licenta.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import static com.gligamihai.licenta.licenta.ui.MainActivity.INTENT_DOCUMENT_ID;

public class UpdateDocumentActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView documentType;
    Button updateButton;
    public String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_document);
        if (getIntent() != null) {
            id = getIntent().getStringExtra(INTENT_DOCUMENT_ID);
        }
        documentType = findViewById(R.id.editTextUpdateType);
        updateButton = findViewById(R.id.button_update_document);
        getDocument(id);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Document document = new Document(documentType.getText().toString().trim());
                updateDocument(document );
            }
        });
    }

    public void getDocument(String id) {
        DocumentReference docRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Documents")
                .document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Document document = documentSnapshot.toObject(Document.class);
                documentType.setText(document.getType());
            }
        });
    }

    public void updateDocument(Document document){
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Documents")
                .document(id)
                .set(document)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateDocumentActivity.this, "Document updated succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateDocumentActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(UpdateDocumentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateDocumentActivity.this,MainActivity.class));
    }
}