package com.polacik.david.cleanerplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.SettingsBean;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private View settingsFragmentView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceSettings;
    private FirebaseUser firebaseUser;

    private TextView settingsFragmentStringUserNameTextView;
    private TextView settingsFragmentStringUserEmailTextView;

    private EditText settingsFragmentDefaultEmailSubjectEditText;

    private MultiAutoCompleteTextView settingsFragmentDefaultSmsCompleteTextView;
    private MultiAutoCompleteTextView settingsFragmentDefaultEmailCompleteTextView;

    private Button settingsFragmentSignOutButton;
    private Button settingsFragmentDefaulTextSaveButtom;


    private String settingsFragmentId;
    private String settingsFragmentDefaultSms;
    private String settingsFragmentDefaultEmailSubject;
    private String settingsFragmentDefaultEmailText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        settingsFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceSettings = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("settings");

        settingsFragmentStringUserNameTextView = settingsFragmentView.findViewById(R.id.fragment_settings_user_name_text_view);
        settingsFragmentStringUserEmailTextView = settingsFragmentView.findViewById(R.id.fragment_settings_user_email_text_view);

        settingsFragmentDefaultSmsCompleteTextView = settingsFragmentView.findViewById(R.id.fragment_settings_defaul_sms_complete_text_view);
        settingsFragmentDefaultEmailCompleteTextView = settingsFragmentView.findViewById(R.id.fragment_settings_defaul_email_complete_text_view);

        settingsFragmentDefaultEmailSubjectEditText = settingsFragmentView.findViewById(R.id.fragment_settings_defaul_email_subject_edit_text);

        settingsFragmentSignOutButton = settingsFragmentView.findViewById(R.id.fragment_settings_sign_out_button);
        settingsFragmentDefaulTextSaveButtom = settingsFragmentView.findViewById(R.id.fragment_settings_save_button);

        if (firebaseUser != null) {
            settingsFragmentStringUserNameTextView.setText(firebaseUser.getDisplayName());
            settingsFragmentStringUserEmailTextView.setText(firebaseUser.getEmail());
        }

        databaseReferencePaymentsMethods();
        clickOnSignOutButton();
        clickOnSaveButton();

        return settingsFragmentView;
    }

    private void databaseReferencePaymentsMethods() {
        databaseReferenceSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SettingsBean settingsBean = dataSnapshot.getValue(SettingsBean.class);

                if (settingsBean != null) {
                    settingsFragmentDefaultSmsCompleteTextView.setText(settingsBean.getSettingsDefaultSms());
                    settingsFragmentDefaultEmailSubjectEditText.setText(settingsBean.getSettingsDefaultEmailSubject());
                    settingsFragmentDefaultEmailCompleteTextView.setText(settingsBean.getSettingsDefaultEmailText());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clickOnSignOutButton() {
        settingsFragmentSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                googleSignInClient.signOut();

                getActivity().finish();
            }
        });
    }

    private void clickOnSaveButton() {
        settingsFragmentDefaulTextSaveButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsFragmentDefaultSms = settingsFragmentDefaultSmsCompleteTextView.getText().toString();
                settingsFragmentDefaultEmailSubject = settingsFragmentDefaultEmailSubjectEditText.getText().toString();
                settingsFragmentDefaultEmailText = settingsFragmentDefaultEmailCompleteTextView.getText().toString();

                settingsFragmentId = databaseReferenceSettings.push().getKey();

                SettingsBean settingsBean = new SettingsBean(settingsFragmentId, settingsFragmentDefaultSms, settingsFragmentDefaultEmailSubject, settingsFragmentDefaultEmailText);
                databaseReferenceSettings.setValue(settingsBean);

                Toast.makeText(getContext(), "Save", Toast.LENGTH_LONG).show();
            }
        });
    }

}
