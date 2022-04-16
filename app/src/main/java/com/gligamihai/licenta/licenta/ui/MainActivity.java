package com.gligamihai.licenta.licenta.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.models.Document;
import com.gligamihai.licenta.licenta.utils.DocumentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView currentUserEmail;
    public static final String INTENT_DOCUMENT_ID="document_id";
    //TextView test;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference documentRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Documents");
    private DocumentAdapter documentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        currentUserEmail = findViewById(R.id.currentUserEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserEmail.setText(user.getEmail());
        } else {
            //TODO treat exception
            Toast.makeText(this, "Nu a mers", Toast.LENGTH_SHORT).show();
        }
        setUpRecyclerView();
        FloatingActionButton fab=findViewById(R.id.addDocumentFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddDocumentActivity();
            }
        });
       // fab.show();

        //  test=findViewById(R.id.hometext);
//        db.collection("Users")
//                .document(FirebaseAuth.getInstance().getUid())
//                .collection("Documents")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }

    public void setUpRecyclerView() {
        Query query = documentRef.orderBy("type", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Document> options = new FirestoreRecyclerOptions.Builder<Document>()
                .setQuery(query, Document.class)
                .build();
        documentAdapter = new DocumentAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.documents_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(documentAdapter);
        removeSelectedDocument(recyclerView);

    }

    public void removeSelectedDocument(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
               new AlertDialog.Builder(viewHolder.itemView.getContext())
                       .setMessage("Are you sure you want to delete this document?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               documentAdapter.deleteDocument(viewHolder.getBindingAdapterPosition());
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               documentAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                           }
                       })
                       .create()
                       .show();

            }
        }).attachToRecyclerView(recyclerView);
        documentAdapter.setOnItemClickListeners(new DocumentAdapter.OnItemClickListeners() {
            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
                String id=documentSnapshot.getId();
                //Toast.makeText(MainActivity.this,id,Toast.LENGTH_SHORT).show();
                Intent goToUpdateDocumentActivity=new Intent(MainActivity.this,UpdateDocumentActivity.class);
                goToUpdateDocumentActivity.putExtra(INTENT_DOCUMENT_ID,id);
                startActivity(goToUpdateDocumentActivity);
            }

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id=documentSnapshot.getId();
                //Toast.makeText(MainActivity.this,id,Toast.LENGTH_SHORT).show();
                Intent goToDocumentInfoActivity=new Intent(MainActivity.this,DocumentInfoActivity.class);
                goToDocumentInfoActivity.putExtra(INTENT_DOCUMENT_ID,id);
                startActivity(goToDocumentInfoActivity);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        documentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        documentAdapter.stopListening();
    }

    public void clickMenu(View view) {
        //Open drawer layout
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void clickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHome(View view) {
        recreate();
    }

    public void clickWeather(View view){
        startActivity(new Intent(MainActivity.this,WeatherActivity.class));
    }
    public  void clickLogout(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(view.getContext());
        alertLogout.setTitle("Logout");
        alertLogout.setMessage("Are you sure you want to log out?");
        alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO make a method for this code
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        alertLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertLogout.show();
    }

    public void goToAddDocumentActivity(){
        Intent intent=new Intent(MainActivity.this, AddDocumentActivity.class);
        startActivity(intent);
    }
}