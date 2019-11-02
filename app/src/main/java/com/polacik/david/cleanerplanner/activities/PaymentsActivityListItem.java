package com.polacik.david.cleanerplanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.bean.PaymentBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.Objects;

public class PaymentsActivityListItem extends AppCompatActivity {

    private TextView paymentsActivityListItemNameEditText;
    private TextView paymentsActivityListItemAddressEditText;
    private TextView paymentsActivityListItemChargedEditText;
    private EditText paymentsActivityListItemBalanceEditText;
    private EditText paymentsActivityListItemTotalEditText;

    private Button historyActivityListItemCancelButton;
    private Button historyActivityListItemSubmitButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceClients;

    private String clientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_list_item);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceClients = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");

        paymentsActivityListItemNameEditText = findViewById(R.id.activity_payments_list_item_name_text_view);
        paymentsActivityListItemAddressEditText = findViewById(R.id.activity_payments_list_item_address_text_view);
        paymentsActivityListItemChargedEditText = findViewById(R.id.activity_payments_list_item_charged_text_view);
        paymentsActivityListItemBalanceEditText = findViewById(R.id.activity_payments_list_item_balance_edit_text);
        paymentsActivityListItemTotalEditText = findViewById(R.id.activity_payments_list_item_total_text_view);

        historyActivityListItemCancelButton = findViewById(R.id.activity_payments_list_item_cancel_button);
        historyActivityListItemSubmitButton = findViewById(R.id.activity_payments_list_item_submit_button);

        Intent updateData = getIntent();

        clientId = updateData.getStringExtra(IntentConstant.KEY_CLIENTBEANID);
        paymentsActivityListItemNameEditText.setText(updateData.getStringExtra(IntentConstant.KEY_CLIENTBEANNAME));
        paymentsActivityListItemAddressEditText.setText(updateData.getStringExtra(IntentConstant.KEY_CLIENTBEANADDRESS));
        paymentsActivityListItemChargedEditText.setText(String.valueOf(updateData.getDoubleExtra(IntentConstant.KEY_CLIENTBEANPAYMENT, 0)));
        paymentsActivityListItemBalanceEditText.setText(String.valueOf(updateData.getDoubleExtra(IntentConstant.KEY_PAYMENTSBALANCE, 0)));
        paymentsActivityListItemTotalEditText.setText(String.valueOf(updateData.getDoubleExtra(IntentConstant.KEY_PAYMENTSTOTAL, 0)));


        clickOnCancelButton();
        clickOnSubmitButton();

    }

    private void clickOnCancelButton() {
        historyActivityListItemCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickOnSubmitButton() {

        historyActivityListItemSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paymentsActivityListItemBalanceEditText.getText().toString().trim().isEmpty()) {
                    paymentsActivityListItemBalanceEditText.setError("Null");
                } else if (paymentsActivityListItemTotalEditText.getText().toString().trim().isEmpty()) {
                    paymentsActivityListItemTotalEditText.setError("Null");
                } else {
                    Double balance = Double.parseDouble(paymentsActivityListItemBalanceEditText.getText().toString());
                    Double total = Double.parseDouble(paymentsActivityListItemTotalEditText.getText().toString());

                    DatabaseReference updateClientBalance = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientBalance");
                    updateClientBalance.setValue(balance);

                    DatabaseReference updateClientTotal = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                            .child("clients").child(clientId).child("clientTotal");
                    updateClientTotal.setValue(total);

                    finish();
                }
            }
        });

    }

}
