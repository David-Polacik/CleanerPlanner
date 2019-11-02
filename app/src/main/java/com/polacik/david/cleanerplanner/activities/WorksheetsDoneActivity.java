package com.polacik.david.cleanerplanner.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.PaymentBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class WorksheetsDoneActivity extends Activity {

    private TextView worksheetsDoneActivityBalanceTextView;
    private TextView worksheetsDoneActivityChargedTextView;

    private EditText worksheetsDoneActivityPaidEditText;

    private Button worksheetsDoneActivityCancelButton;
    private Button worksheetsDoneActivitySubmitButton;

    private Spinner worksheetsDoneActivityMethodSpinner;

    private String clientId;
    private Integer clientRepeatWork;
    private Integer clientWeekWork;
    private Integer clientYearWork;
    private Double clientPayment;
    private Double clientBalance;
    private Double clientTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheets_done);

        worksheetsDoneActivityBalanceTextView = findViewById(R.id.activity_worksheets_done_balance_text_view);
        worksheetsDoneActivityChargedTextView = findViewById(R.id.activity_worksheets_done_charged_text_view);

        worksheetsDoneActivityPaidEditText = findViewById(R.id.activity_worksheets_done_paid_edit_text);

        worksheetsDoneActivityCancelButton = findViewById(R.id.activity_worksheets_done_cancel_button);
        worksheetsDoneActivitySubmitButton = findViewById(R.id.activity_worksheets_done_submit_button);

        worksheetsDoneActivityMethodSpinner = findViewById(R.id.activity_worksheets_done_method_spinner);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        ArrayAdapter<CharSequence> methodSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.worksheetsDoneActivityStringMethodSpinner, android.R.layout.simple_spinner_item);
        methodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worksheetsDoneActivityMethodSpinner.setAdapter(methodSpinnerAdapter);

        Intent getClientIntent = getIntent();
        clientId = getClientIntent.getStringExtra("SENDCLIENTID");
        clientRepeatWork = getClientIntent.getIntExtra("SENDCLIENTREPEATWORK", -1);
        clientWeekWork = getClientIntent.getIntExtra("SENDCLIENTWEEKWORK", 0);
        clientYearWork = getClientIntent.getIntExtra("SENDCLIENTYEARWORK", 0);
        clientPayment = getClientIntent.getDoubleExtra("SENDCLIENTPAYMENT", 0);
        clientBalance = getClientIntent.getDoubleExtra("SENDCLIENTBALANCE", 0);
        clientTotal = getClientIntent.getDoubleExtra("SENDCLIENTTOTAL", 0);

        worksheetsDoneActivityBalanceTextView.setText(String.valueOf(clientBalance));
        worksheetsDoneActivityChargedTextView.setText(String.valueOf(clientPayment));

        spinnerOnItemSelected();
        clickOnCancelButton();
        clickOnSubmitButton();

    }

    private void spinnerOnItemSelected() {
        worksheetsDoneActivityMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void clickOnCancelButton() {

        worksheetsDoneActivityCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void clickOnSubmitButton() {
        worksheetsDoneActivitySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (worksheetsDoneActivityPaidEditText.getText().toString().trim().isEmpty()) {
                    worksheetsDoneActivityPaidEditText.setError("Null");
                }else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
                    String paymentDate = formatDate.format(calendar.getTime());

                    calendar.set(Calendar.YEAR, clientYearWork);
                    calendar.set(Calendar.WEEK_OF_YEAR, clientWeekWork);
                    calendar.add(Calendar.WEEK_OF_YEAR, clientRepeatWork);

                    String nextWeekWorkDate = formatDate.format(calendar.getTime());

                    int weekWorkDate = calendar.get(Calendar.WEEK_OF_YEAR);
                    int yearWorkDate = calendar.get(Calendar.YEAR);

                    Double paymentCharged = Double.valueOf(worksheetsDoneActivityChargedTextView.getText().toString());
                    Double paymentPaid = Double.valueOf(worksheetsDoneActivityPaidEditText.getText().toString());
                    String paymentMethod = worksheetsDoneActivityMethodSpinner.getSelectedItem().toString();

                    Double paymentBalance = (paymentCharged - paymentPaid) + clientBalance;
                    Double paymentTotal = clientTotal + paymentPaid;

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
                    DatabaseReference updateClientBalance = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientBalance");
                    updateClientBalance.setValue(paymentBalance);
                    DatabaseReference updateClientTotal = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientTotal");
                    updateClientTotal.setValue(paymentTotal);

                    DatabaseReference createPaymentBean = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("payments");
                    String paymentId = createPaymentBean.push().getKey();

                    if (paymentId != null) {
                        PaymentBean paymentBean = new PaymentBean(paymentId, clientId, paymentCharged, paymentPaid, paymentMethod, paymentDate, 1);
                        createPaymentBean.child(paymentId).setValue(paymentBean);
                    }

                    finish();
                }
            }
        });
    }

}
