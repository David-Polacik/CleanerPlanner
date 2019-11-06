package com.polacik.david.cleanerplanner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.Objects;

public class PaymentActivityAdd extends Activity {

    private EditText paymentActivityAddPaidTextView;

    private Button paymentActivityAddSubmitButton;
    private Button paymentActivityAddCancelButton;

    private String clientId;
    private Double clientBalance;
    private Double clientTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_add);

        paymentActivityAddPaidTextView = findViewById(R.id.activity_payment_add_paid_edit_text);
        paymentActivityAddSubmitButton = findViewById(R.id.activity_payment_add_submit_button);
        paymentActivityAddCancelButton = findViewById(R.id.activity_payment_add_cancel_button);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        Intent updateData = getIntent();

        if (updateData != null) {
            clientId = updateData.getStringExtra(IntentConstant.KEY_CLIENTBEANID);
            clientBalance = updateData.getDoubleExtra(IntentConstant.KEY_CLIENTBEANBALANCE, 0);
            clientTotal = updateData.getDoubleExtra(IntentConstant.KEY_CLIENTBEANTOTAL, 0);
        }

        clickOnCancelButton();
        clickOnSubmitButton();

    }

    private void clickOnCancelButton() {

        paymentActivityAddCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickOnSubmitButton() {

        paymentActivityAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if (paymentActivityAddPaidTextView.getText().toString().trim().isEmpty()) {
                    paymentActivityAddPaidTextView.setError("Null");

                } else {
                    Double paymentPaid = Double.valueOf(paymentActivityAddPaidTextView.getText().toString());
                    Double paymentTotal = clientTotal + paymentPaid;
                    Double paymentAdd= clientBalance - paymentPaid;

                    DatabaseReference updateClientBalance = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientBalance");
                    updateClientBalance.setValue(paymentAdd);
                    DatabaseReference updateClientTotal = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientTotal");
                    updateClientTotal.setValue(paymentTotal);

                    setResult(Activity.RESULT_OK);
                    Toast.makeText(PaymentActivityAdd.this, "Payment added.", Toast.LENGTH_LONG).show();
                }

                finish();

            }
        });
    }
}
