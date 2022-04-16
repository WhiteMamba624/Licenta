package com.gligamihai.licenta.licenta.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

public class AddDocumentActivity extends AppCompatActivity {

    final Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference documentRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Documents");
    Button buttonAddDocument;
    EditText editTextType;
    EditText editTextPlateNumber;
    EditText editTextExpiryDate;
    EditText editTextVinNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        buttonAddDocument=findViewById(R.id.addDocumentButton);
        editTextType=findViewById(R.id.editTextType);
        editTextPlateNumber=findViewById(R.id.editTextPlateNumber);
        editTextExpiryDate=findViewById(R.id.editTextExpiryDate);
        editTextVinNumber=findViewById(R.id.editTextVinNumber);
        buttonAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=editTextType.getText().toString().trim();
                String plateNumber=editTextPlateNumber.getText().toString().trim();
                String expiryDate=editTextExpiryDate.getText().toString().trim();
                String vinNumber=editTextVinNumber.getText().toString().trim();
                Document document=new Document(type,plateNumber,vinNumber,expiryDate);
                addDocument(document);
                //addDocument(textType.getText().toString().trim());
            }
        });
    }

    public void addDocument(Document document){
        //Document document=new Document(documentType);
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


    public void datePicker(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editTextExpiryDate.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddDocumentActivity.this,MainActivity.class));
    }

    public void goBackToMainActivity(View view) {
        onBackPressed();
    }
}