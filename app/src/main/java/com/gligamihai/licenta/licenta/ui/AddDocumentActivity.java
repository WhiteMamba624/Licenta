package com.gligamihai.licenta.licenta.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class AddDocumentActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference documentRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Documents");
    Button buttonAddDocument;
    EditText textType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        buttonAddDocument=findViewById(R.id.button_add_document);
        textType=findViewById(R.id.editTextType);
        buttonAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDocument(textType.getText().toString().trim());
            }
        });
    }

    public void addDocument(String documentType){
        Document document=new Document(documentType);
        documentRef.add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddDocumentActivity.this,"Document added succesfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddDocumentActivity.this,MainActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                     Toast.makeText(AddDocumentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }



}