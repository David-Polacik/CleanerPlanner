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
    private Integer clientRepeatWork;
    private Integer clientWeekWork;
    private Integer clientYearWork;

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

        Intent getClientIntent = getIntent();
        clientId = getClientIntent.getStringExtra(IntentConstant.KEY_CLIENTBEANID);

        clientRepeatWork = getClientIntent.getIntExtra(IntentConstant.KEY_CLIENTBEANREPEAT, -1);
        clientWeekWork = getClientIntent.getIntExtra(IntentConstant.KEY_CLIENTBEANWEEKWORK, 0);
        clientYearWork = getClientIntent.getIntExtra(IntentConstant.KEY_CLIENTBEANYEARWORK, 0);


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

                calendar.set(Calendar.YEAR, clientYearWork);
                calendar.set(Calendar.WEEK_OF_YEAR, clientWeekWork);
                calendar.add(Calendar.WEEK_OF_YEAR, clientRepeatWork);

                String nextWeekWorkDate = formatDate.format(calendar.getTime());

                int weekWorkDate = calendar.get(Calendar.WEEK_OF_YEAR);
                int yearWorkDate = calendar.get(Calendar.YEAR);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                DatabaseReference updateWeekWork = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .child("clients").child(clientId).child("clientWeekWork");
                updateWeekWork.setValue(weekWorkDate);
                DatabaseReference updateYearWork = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .child("clients").child(clientId).child("clientYearWork");
                updateYearWork.setValue(yearWorkDate);
                DatabaseReference updatePaymentDate = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .child("clients").child(clientId).child("clientWorkStart");
                updatePaymentDate.setValue(nextWeekWorkDate);

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
