package com.polacik.david.cleanerplanner.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.activities.WorksheetsDoneActivity;
import com.polacik.david.cleanerplanner.activities.WorksheetsMissedActivity;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.bean.SettingsBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.List;
import java.util.Objects;

public class WorksheetsAdapter extends ArrayAdapter<ClientsBean> {

    private View worksheetsAdapterView;
    private ClientsBean clientsBean;
    private List<ClientsBean> worksheetsBeanList;

    private LinearLayout worksheetsAdapterPhoneLinearLayout;
    private LinearLayout worksheetsAdapterEmailLinearLayout;
    private LinearLayout worksheetsAdapterDescriptionLinearLayout;
    private LinearLayout worksheetsAdapterCallLinearLayout;
    private LinearLayout worksheetsAdapterSmsLinearLayout;
    private LinearLayout worksheetsAdapterSendEmailLinearLayout;

    private TextView worksheetsAdapterNameTextView;
    private TextView worksheetsAdapterAddressTextView;
    private TextView worksheetsAdapterPhoneTextView;
    private TextView worksheetsAdapterEmailTextView;
    private TextView worksheetsAdapterPaymentTextView;
    private TextView worksheetsAdapterWorkTextView;
    private TextView worksheetsAdapterDescriptionTextView;

    private Button worksheetsAdapterDoneButton;
    private Button worksheetsAdapterMissedButton;

    private ImageButton worksheetsAdapterCallImageButton;
    private ImageButton worksheetsAdapterSmsImageButton;
    private ImageButton worksheetsAdapterEmailImageButton;
    private ImageButton worksheetsAdapterGpsImageButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceSettings;

    public WorksheetsAdapter(@NonNull Context context, int resource, @NonNull List<ClientsBean> objects) {
        super(context, resource, objects);
        worksheetsBeanList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        worksheetsAdapterView = convertView;
        clientsBean = worksheetsBeanList.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceSettings = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("settings");

        if (worksheetsAdapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            worksheetsAdapterView = layoutInflater.inflate(R.layout.adapter_worksheets, null);
        }

        if (clientsBean != null) {

            worksheetsAdapterPhoneLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_phone_linear_layout);
            worksheetsAdapterEmailLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_email_linear_layout);
            worksheetsAdapterDescriptionLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_description_linear_layout);
            worksheetsAdapterCallLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_call_linear_layout);
            worksheetsAdapterSmsLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_sms_linear_layout);
            worksheetsAdapterSendEmailLinearLayout = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_send_email_linear_layout);

            worksheetsAdapterNameTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_name_text_view);
            worksheetsAdapterAddressTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_address_text_view);
            worksheetsAdapterPhoneTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_phone_text_view);
            worksheetsAdapterEmailTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_email_text_view);
            worksheetsAdapterPaymentTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_payment_text_view);
            worksheetsAdapterWorkTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_due_date_text_view);
            worksheetsAdapterDescriptionTextView = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_description_text_view);

            worksheetsAdapterDoneButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_done_button);
            worksheetsAdapterMissedButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_missed_button);

            worksheetsAdapterCallImageButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_call_image_button);
            worksheetsAdapterSmsImageButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_sms_image_button);
            worksheetsAdapterEmailImageButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_email_image_button);
            worksheetsAdapterGpsImageButton = worksheetsAdapterView.findViewById(R.id.adapter_worksheets_gps_image_button);

            if (clientsBean.getClientPhone().isEmpty()) {
                worksheetsAdapterPhoneLinearLayout.setVisibility(View.GONE);
                worksheetsAdapterCallLinearLayout.setVisibility(View.GONE);
                worksheetsAdapterSmsLinearLayout.setVisibility(View.GONE);
            }
            if (clientsBean.getClientEmail().isEmpty()) {
                worksheetsAdapterEmailLinearLayout.setVisibility(View.GONE);
                worksheetsAdapterSendEmailLinearLayout.setVisibility(View.GONE);
            }
            if (clientsBean.getClientDescription().isEmpty()) {
                worksheetsAdapterDescriptionLinearLayout.setVisibility(View.GONE);
            }

            worksheetsAdapterNameTextView.setText(clientsBean.getClientName());
            worksheetsAdapterAddressTextView.setText(clientsBean.getClientAddress());
            worksheetsAdapterPhoneTextView.setText(clientsBean.getClientPhone());
            worksheetsAdapterEmailTextView.setText(clientsBean.getClientEmail());
            worksheetsAdapterPaymentTextView.setText(("\u00A3" + clientsBean.getClientPayment()));
            worksheetsAdapterWorkTextView.setText(clientsBean.getClientWorkStart());
            worksheetsAdapterDescriptionTextView.setText(clientsBean.getClientDescription());


            worksheetsAdapterCallImageButton.setTag(clientsBean.getClientPhone());
            worksheetsAdapterSmsImageButton.setTag(clientsBean.getClientPhone());
            worksheetsAdapterEmailImageButton.setTag(clientsBean.getClientEmail());
            worksheetsAdapterGpsImageButton.setTag(clientsBean.getClientAddress());

            worksheetsAdapterDoneButton.setTag(position);
            worksheetsAdapterMissedButton.setTag(position);

        }


        clickOnCallImageButton();
        clickOnSmsImageButton();
        clickOnEmailImageButton();
        clickOnGpsImageButton();

        clickOnDoneButton();
        clickOnMissedButton();

        return worksheetsAdapterView;
    }

    private void clickOnCallImageButton() {

        worksheetsAdapterCallImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = (String) view.getTag();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                getContext().startActivity(intent);
            }
        });
    }

    private void clickOnSmsImageButton() {

        worksheetsAdapterSmsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = (String) view.getTag();
                final Intent sendSmsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                databaseReferenceSettings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SettingsBean settingsBean = dataSnapshot.getValue(SettingsBean.class);
                        if (settingsBean != null) {
                            String textSms = settingsBean.getSettingsDefaultSms();
                            sendSmsIntent.putExtra("sms_body", textSms);
                            getContext().startActivity(sendSmsIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void clickOnEmailImageButton() {

        worksheetsAdapterEmailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientEmail = (String) view.getTag();
                final Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
                sendEmailIntent.setType("text/plain");
                sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{clientEmail});

                databaseReferenceSettings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SettingsBean settingsBean = dataSnapshot.getValue(SettingsBean.class);
                        if (settingsBean != null) {
                            sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, settingsBean.getSettingsDefaultEmailSubject());
                            sendEmailIntent.putExtra(Intent.EXTRA_TEXT, settingsBean.getSettingsDefaultEmailText());

                            try {
                                getContext().startActivity(sendEmailIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void clickOnGpsImageButton() {
        worksheetsAdapterGpsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientAddress = (String) view.getTag();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + clientAddress);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                getContext().startActivity(mapIntent);
            }
        });
    }

    private void clickOnDoneButton() {

        worksheetsAdapterDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionId = (int) view.getTag();
                String clientId = worksheetsBeanList.get(positionId).getClientId();
                String clientName = worksheetsBeanList.get(positionId).getClientName();
                Integer weekWork = worksheetsBeanList.get(positionId).getClientWeekWork();
                Integer yearWork = worksheetsBeanList.get(positionId).getClientYearWork();
                Integer repeatWork = worksheetsBeanList.get(positionId).getClientWorkRepeat();
                Double clientPayment = worksheetsBeanList.get(positionId).getClientPayment();
                Double clientBalance = worksheetsBeanList.get(positionId).getClientBalance();
                Double clientTotal = worksheetsBeanList.get(positionId).getClientTotal();

                Intent showWorksheetsPaymentActivity = new Intent(getContext(), WorksheetsDoneActivity.class);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANID, clientId);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANNAME, clientName);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANWEEKWORK, weekWork);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANYEARWORK, yearWork);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANREPEAT, repeatWork);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANPAYMENT, clientPayment);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANBALANCE, clientBalance);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANTOTAL, clientTotal);
                getContext().startActivity(showWorksheetsPaymentActivity);
            }
        });
    }

    private void clickOnMissedButton() {
        worksheetsAdapterMissedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionId = (int) view.getTag();
                String clientsBeanId = worksheetsBeanList.get(positionId).getClientId();

                Intent showWorksheetsPaymentActivity = new Intent(getContext(), WorksheetsMissedActivity.class);
                showWorksheetsPaymentActivity.putExtra(IntentConstant.KEY_CLIENTBEANID, clientsBeanId);
                getContext().startActivity(showWorksheetsPaymentActivity);

            }
        });

    }
}
