package com.gligamihai.licenta.licenta.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gligamihai.licenta.R;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyITPActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_itpactivity);
        webView=findViewById(R.id.webViewRCA);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://prog.rarom.ro/rarpol/");
        drawerLayout = findViewById(R.id.drawer_layout);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
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
        startActivity(new Intent(VerifyITPActivity.this, MainActivity.class));
    }

    public void clickWeather(View view) {
        startActivity(new Intent(VerifyITPActivity.this, WeatherActivity.class));
    }

    public void clickLogout(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(view.getContext());
        alertLogout.setTitle("Logout");
        alertLogout.setMessage("Are you sure you want to log out?");
        alertLogout.setPositiveButton("Yes", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        alertLogout.setNegativeButton("No", (dialog, which) -> {

        });
        alertLogout.show();
    }

    public void clickPlaces(View view) {
        startActivity(new Intent(VerifyITPActivity.this, PlacesActivity.class));
    }

    public void clickVerifyRCA(View view){
       recreate();
    }
}