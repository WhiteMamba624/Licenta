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
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gligamihai.licenta.licenta.ui.MainActivity.INTENT_DOCUMENT_ID;

public class UpdateDocumentActivity extends AppCompatActivity {

    final Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText updateDocumentType;
    EditText updateDocumentPlateNumber;
    EditText updateDocumentExpiryDate;
    EditText updateDocumentVinNumber;
    Button updateButton;
    public String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_document);
        if (getIntent() != null) {
            id = getIntent().getStringExtra(INTENT_DOCUMENT_ID);
        }
        updateDocumentType = findViewById(R.id.editTextUpdateType);
        updateDocumentPlateNumber=findViewById(R.id.editTextUpdatePlateNumber);
        updateDocumentExpiryDate=findViewById(R.id.editTextUpdateExpiryDate);
        updateDocumentVinNumber=findViewById(R.id.editTextUpdateVinNumber);
        updateButton = findViewById(R.id.updateDocumentButton);
        getDocument(id);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Document document = new Document();
                document.setType(updateDocumentType.getText().toString().trim());
                document.setPlateNumber(updateDocumentPlateNumber.getText().toString().trim());
                document.setExpiryDate(updateDocumentExpiryDate.getText().toString().trim());
                document.setVinNumber(updateDocumentVinNumber.getText().toString().trim());
                document.setDeviceToken(FirebaseMessaging.getInstance().getToken().toString());
                dataValidation(document);
            }
        });
    }
    public void dataValidation(Document document) {
        if(!document.getType().isEmpty()){
            if(document.getType().equalsIgnoreCase("ITP")|| document.getType().equalsIgnoreCase("Rovinietta") || document.getType().equalsIgnoreCase("RCA")){
                if(!document.getPlateNumber().isEmpty()&&!plateNumberValidate(document.getPlateNumber())){
                    if(!document.getExpiryDate().isEmpty()){
                        if(!document.getVinNumber().isEmpty()&&document.getVinNumber().length()<17){
                            updateDocument(document);
                        }else{
                            Toast.makeText(UpdateDocumentActivity.this,"Please make sure that Vin Number is not empty or longer than 17 characters",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(UpdateDocumentActivity.this, "Please make sure that Expiry date is not empty", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateDocumentActivity.this,"Please make sure that Plate number is not empty and has valid format",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(UpdateDocumentActivity.this,"Please make sure that document is either RCA,Rovinietta or ITP",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(UpdateDocumentActivity.this,"Please make sure that Document Type is not empty",Toast.LENGTH_SHORT).show();
        }
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
                updateDocumentType.setText(document.getType());
                updateDocumentPlateNumber.setText(document.getPlateNumber());
                updateDocumentExpiryDate.setText(document.getExpiryDate());
                updateDocumentVinNumber.setText(document.getVinNumber());
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

    public void datePicker(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        updateDocumentExpiryDate.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    public void goToMainActivity(View view) {
        onBackPressed();
    }


    public boolean plateNumberValidate(String plateNumber){
        Pattern pattern=Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher=pattern.matcher(plateNumber);
        return matcher.find();
    }
}