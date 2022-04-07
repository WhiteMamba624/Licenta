package com.gligamihai.licenta.licenta.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.gligamihai.licenta.licenta.ui.MainActivity.INTENT_DOCUMENT_ID;

public class DocumentInfoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView documentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);
        String id = null;
        if (getIntent() != null) {
            id = getIntent().getStringExtra(INTENT_DOCUMENT_ID);
        }
        documentType = findViewById(R.id.document_type_textview);
        getDocument(id);
    }

    public void getDocument(String id){
        DocumentReference docRef=db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Documents")
                .document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Document document=documentSnapshot.toObject(Document.class);
               documentType.setText(document.getType());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DocumentInfoActivity.this,MainActivity.class));
    }
}