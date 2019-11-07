package com.polacik.david.cleanerplanner.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.bean.PaymentBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.Objects;


public class ClientsActivityAdd extends AppCompatActivity {

    private ImageButton clientsActivityAddCalendarIconImageButton;

    private EditText clientsActivityAddNameEditText;
    private EditText clientsActivityAddAddressEditText;
    private EditText clientsActivityAddPhoneEditText;
    private EditText clientsActivityAddEmailEditText;
    private EditText clientsActivityAddPaymentEditText;
    private EditText clientsActivityAddRepeatEditText;
    private EditText clientsActivityStartWorkEditText;
    private EditText clientsActivityAddDescriptionEditText;

    private Button clientsActivityAddCancelButton;
    private Button clientsActivityAddDeleteButton;
    private Button clientsActivityAddSubmitButton;

    private Intent updateClients;

    private Integer clientsFragmentListViewPosition;
    private String clienId;
    private Integer clientPaymentStatus;

    private Double clientBalance;
    private Double clientTotal;

    private String dateFormatString;
    private int weekWork;
    private int yearWork;

    private int dateWeekInt;
    private int dateYearInt;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_add);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceClients = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");

        clientsActivityAddCalendarIconImageButton = findViewById(R.id.activity_clients_add_calendar_icon);

        clientsActivityAddNameEditText = findViewById(R.id.activity_clients_add_name_edit_text);
        clientsActivityAddAddressEditText = findViewById(R.id.activity_clients_add_address_edit_text);
        clientsActivityAddPhoneEditText = findViewById(R.id.activity_clients_add_phone_edit_text);
        clientsActivityAddEmailEditText = findViewById(R.id.activity_clients_add_email_edit_text);
        clientsActivityAddPaymentEditText = findViewById(R.id.activity_clients_add_payment_edit_text);
        clientsActivityAddRepeatEditText = findViewById(R.id.activity_clients_add_repeat_edit_text);
        clientsActivityStartWorkEditText = findViewById(R.id.activity_clients_start_work_edit_text);
        clientsActivityAddDescriptionEditText = findViewById(R.id.activity_clients_add_description_auto_text_view);

        clientsActivityAddCancelButton = findViewById(R.id.activity_clients_add_cancel_button);
        clientsActivityAddDeleteButton = findViewById(R.id.activity_clients_add_delete_button);
        clientsActivityAddSubmitButton = findViewById(R.id.activity_clients_add_submit_button);

        updateClients = getIntent();

        clientsFragmentListViewPosition = updateClients.getIntExtra(IntentConstant.KEY_CLIENTLISTVIEWPOSITION, -1);
        clientsActivityStartWorkEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANSTARTWORK));
        if (clientsFragmentListViewPosition != -1) {
            clientsActivityAddDeleteButton.setVisibility(View.VISIBLE);
        }

        clienId = updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANID);
        clientBalance = updateClients.getDoubleExtra(IntentConstant.KEY_CLIENTBEANBALANCE, 0);
        clientTotal = updateClients.getDoubleExtra(IntentConstant.KEY_CLIENTBEANTOTAL, 0);
        clientPaymentStatus = updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANEPAYMENTSTATUS, -1);
        clientsActivityAddNameEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANNAME));
        clientsActivityAddAddressEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANADDRESS));
        clientsActivityAddPhoneEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANPHONE));
        clientsActivityAddEmailEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANEMAIL));
        clientsActivityAddPaymentEditText.setText(String.valueOf(updateClients.getDoubleExtra(IntentConstant.KEY_CLIENTBEANPAYMENT, 0)));
        clientsActivityAddRepeatEditText.setText(String.valueOf(updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANREPEAT, 0)));
        clientsActivityStartWorkEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANSTARTWORK));
        weekWork = updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANWEEKWORK, 0);
        yearWork = updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANYEARWORK, 0);
        clientsActivityAddDescriptionEditText.setText(updateClients.getStringExtra(IntentConstant.KEY_CLIENTBEANDESCRIPTION));
        dateWeekInt = updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANWEEKWORK, 0);
        dateYearInt = updateClients.getIntExtra(IntentConstant.KEY_CLIENTBEANYEARWORK, 0);

        clickOnCancelButton();
        clickOnDeleteButton();
        clickOnSubmitButton();
        clickOnCalendarImageButton();

    }


    private void clickOnSubmitButton() {
        clientsActivityAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = clientsActivityAddNameEditText.getText().toString();
                String address = clientsActivityAddAddressEditText.getText().toString();
                String phone = clientsActivityAddPhoneEditText.getText().toString();
                String email = clientsActivityAddEmailEditText.getText().toString();
                Double payment = Double.parseDouble(clientsActivityAddPaymentEditText.getText().toString());
                Integer repeatWork = Integer.parseInt(clientsActivityAddRepeatEditText.getText().toString());
                String workStart = clientsActivityStartWorkEditText.getText().toString();
                String description = clientsActivityAddDescriptionEditText.getText().toString();

                if (name.trim().isEmpty() || name.length() > 30 || !name.matches("[a-zA-Z ]+")) {
                    if (name.trim().isEmpty()) clientsActivityAddNameEditText.setError("Null");
                    else if (name.length() > 30)
                        clientsActivityAddNameEditText.setError("Name is long. Max size is 30 char.");
                    else if (!name.matches("[a-zA-Z ]+"))
                        clientsActivityAddNameEditText.setError("Wrong char.");
                } else if (address.trim().isEmpty() || address.length() > 30 || !address.matches("[a-zA-Z0-9 -/]+")) {
                    if (address.trim().isEmpty())
                        clientsActivityAddAddressEditText.setError("Null");
                    else if (address.length() > 30)
                        clientsActivityAddAddressEditText.setError("Address is long. Max size is 30 char.");
                    else if (!address.matches("[a-zA-Z0-9 -/]+"))
                        clientsActivityAddAddressEditText.setError("Wrong char.");
                } else if (!email.isEmpty() && !email.matches("[\\w+[\\.]]+@\\w+\\.[a-z]{2,3}")) {
                    if (!email.matches("[\\w+[\\.]]+@\\w+\\.[a-z]{2,3}")) {
                        clientsActivityAddEmailEditText.setError("Wrong email");
                    }
                } else if (payment == 0 || !payment.toString().matches("\\d{1,4}\\.\\d{1,2}")) {
                    if (payment == 0) clientsActivityAddPaymentEditText.setError("Null");
                    else if (!payment.toString().matches("\\d{1,4}\\.\\d{1,2}"))
                        clientsActivityAddPaymentEditText.setError("Wrong payment format. Format must be 0000.00");
                } else if (repeatWork == 0 || repeatWork > 30) {
                    if (repeatWork == 0) clientsActivityAddRepeatEditText.setError("Null");
                    else if (repeatWork > 30)
                        clientsActivityAddRepeatEditText.setError("Repeat work is long. Max size is 30.");
                } else if (workStart.trim().isEmpty() || !workStart.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) {
                    if (workStart.trim().isEmpty())
                        clientsActivityStartWorkEditText.setError("Null");
                    else if (!workStart.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}"))
                        clientsActivityStartWorkEditText.setError("Wrong date format. Format must be DD.MM.YYYY");
                } else {

                    if (dateWeekInt != 0 && dateYearInt != 0) {

                        weekWork = dateWeekInt;
                        yearWork = dateYearInt;
                    }

                    if (clientsFragmentListViewPosition != -1) {
                        ClientsBean clientsBean = new ClientsBean(clienId, name, address, phone, email, payment, clientBalance, clientTotal, repeatWork, workStart, description,
                                weekWork, yearWork);
                        DatabaseReference updateData = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                                .child("clients").child(clienId);
                        updateData.setValue(clientsBean);

                    } else {
                        String id = databaseReferenceClients.push().getKey();
                        if (id != null) {
                            ClientsBean clientsBean = new ClientsBean(id, name, address, phone, email, payment, 0.0, 0.0, repeatWork, workStart, description,
                                    dateWeekInt, dateYearInt);
                            databaseReferenceClients.child(id).setValue(clientsBean);
                        }

                    }
                    finish();
                }
            }
        });
    }

    private void clickOnDeleteButton() {
        clientsActivityAddDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceClients.child(clienId).removeValue();

                final DatabaseReference deleteClientPayments = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .child("payments");

                deleteClientPayments.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PaymentBean paymentBean = snapshot.getValue(PaymentBean.class);

                            if (Objects.requireNonNull(paymentBean).getClientId().contains(clienId)) {
                                deleteClientPayments.child(paymentBean.getPaymentId()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                finish();
            }
        });
    }

    private void clickOnCancelButton() {
        clientsActivityAddCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickOnCalendarImageButton() {
        clientsActivityAddCalendarIconImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showClientsActivityAdd = new Intent(ClientsActivityAdd.this, CalendarActivity.class);
                startActivityForResult(showClientsActivityAdd, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                dateFormatString = data.getStringExtra(IntentConstant.KEY_CLIENTADDCALENDARDATE);
                dateWeekInt = data.getIntExtra(IntentConstant.KEY_CALENDARWEEK, 0);
                dateYearInt = data.getIntExtra(IntentConstant.KEY_CALENDARYEAR, 0);
                clientsActivityStartWorkEditText.setText(dateFormatString);

            }
        }
    }
}
