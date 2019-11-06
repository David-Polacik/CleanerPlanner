package com.polacik.david.cleanerplanner.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.PaymentBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class WorksheetsMissedActivity extends Activity {

    private AutoCompleteTextView worksheetsMissedActivityReasonAutoCompleteTextView;

    private Button worksheetsMissedActivityCancelButton;
    private Button worksheetsMissedActivitySubmitButton;


    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheets_missed);

        worksheetsMissedActivityReasonAutoCompleteTextView = findViewById(R.id.activity_worksheets_missed_auto_complete_text_view);

        worksheetsMissedActivityCancelButton = findViewById(R.id.activity_worksheets_missed_cancel_button);
        worksheetsMissedActivitySubmitButton = findViewById(R.id.activity_worksheets_missed_submit_button);


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        Intent getClientId = getIntent();
        clientId = getClientId.getStringExtra(IntentConstant.KEY_CLIENTBEANID);


        clickOnCancelButton();
        clickOnSubmitButton();

    }

    private void clickOnCancelButton() {

        worksheetsMissedActivityCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void clickOnSubmitButton() {
        worksheetsMissedActivitySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");

                String paymentReason = worksheetsMissedActivityReasonAutoCompleteTextView.getText().toString();
                String paymentDate = formatDate.format(calendar.getTime());

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                DatabaseReference createPaymentBean = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .child("payments");
                String paymentId = createPaymentBean.push().getKey();

                if (paymentId != null) {
                    PaymentBean paymentBean = new PaymentBean(paymentId, clientId, paymentReason, paymentDate, 2);
                    createPaymentBean.child(paymentId).setValue(paymentBean);
                }
                finish();
            }
        });
    }

}
