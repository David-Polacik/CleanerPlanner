package com.polacik.david.cleanerplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.firebase.gmail.LoginGmailActivity;

public class LoginMainActivity extends Activity {

    private Button loginMainActivityGmailButton;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        loginMainActivityGmailButton = findViewById(R.id.login_main_activity_gmail_button);

        clickLoginMainActivityGmailButton();
    }


    private void clickLoginMainActivityGmailButton() {
        loginMainActivityGmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showLoginGmailActivity = new Intent(LoginMainActivity.this, LoginGmailActivity.class);
                overridePendingTransition(0, 0);
                startActivity(showLoginGmailActivity);
            }
        });
    }

}
