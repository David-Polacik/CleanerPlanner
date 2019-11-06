package com.polacik.david.cleanerplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.activities.PaymentsActivityListItem;
import com.polacik.david.cleanerplanner.adapters.PaymentsAdapter;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentsFragment extends Fragment {

    private View paymentsFragmentView;
    private ListView paymentsFragmentListView;
    private PaymentsAdapter paymentsAdapter;

    private TextView paymentsFragmentWorkTotalTextView;
    private TextView paymentsFragmentBalanceTextView;
    private TextView paymentsFragmentPaidTotalTextView;

    private List<ClientsBean> clientsBeanList;

    private DatabaseReference databaseReferenceClients;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceClients = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");

        paymentsFragmentView = inflater.inflate(R.layout.fragment_payments, container, false);
        paymentsFragmentListView = paymentsFragmentView.findViewById(R.id.payments_fragment_list_view);

        paymentsFragmentWorkTotalTextView = paymentsFragmentView.findViewById(R.id.fragment_payments_work_total_text_view);
        paymentsFragmentBalanceTextView = paymentsFragmentView.findViewById(R.id.fragment_payments_balance_text_view);
        paymentsFragmentPaidTotalTextView = paymentsFragmentView.findViewById(R.id.fragment_payments_paid_total_text_view);

        clientsBeanList = new ArrayList<>();

        databaseReferencePaymentsMethods();
        clickOnListViewItem();


        return paymentsFragmentView;
    }

    private void databaseReferencePaymentsMethods() {
        databaseReferenceClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsBeanList.clear();

                double workTotal = 0;
                double balance = 0;
                double paidTotal = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ClientsBean clientsBean = ds.getValue(ClientsBean.class);
                    if (clientsBean != null) {
                        clientsBeanList.add(clientsBean);
                        workTotal = workTotal + clientsBean.getClientPayment();
                        balance = balance + clientsBean.getClientBalance();
                        paidTotal = paidTotal + clientsBean.getClientTotal();
                    }


                }

                if (getActivity() != null) {
                    paymentsAdapter = new PaymentsAdapter(getActivity(), R.layout.adapter_payments, clientsBeanList);
                    paymentsFragmentListView.setAdapter(paymentsAdapter);
                    paymentsFragmentWorkTotalTextView.setText(("\u00A3" + workTotal));
                    paymentsFragmentBalanceTextView.setText(("\u00A3" + balance));
                    paymentsFragmentPaidTotalTextView.setText(("\u00A3" + paidTotal));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clickOnListViewItem() {
        paymentsFragmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ClientsBean idListView = (ClientsBean) parent.getAdapter().getItem(position);

                Intent showPaymentsActivityListItem = new Intent(getActivity(), PaymentsActivityListItem.class);
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_CLIENTBEANID, idListView.getClientId());
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_CLIENTBEANNAME, idListView.getClientName());
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_CLIENTBEANADDRESS, idListView.getClientAddress());
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_CLIENTBEANPAYMENT, idListView.getClientPayment());
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_PAYMENTSBALANCE, idListView.getClientBalance());
                showPaymentsActivityListItem.putExtra(IntentConstant.KEY_PAYMENTSTOTAL, idListView.getClientTotal());
                startActivity(showPaymentsActivityListItem);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_navigation, menu);
        menu.findItem(R.id.menu_top_navidation_calendar_icon).setVisible(false);
        menu.findItem(R.id.menu_top_navidation_delete_icon).setVisible(false);

        MenuItem item = menu.findItem(R.id.menu_top_navidation_search_icon);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                List<ClientsBean> searchList = new ArrayList<>();
                for (ClientsBean bean : clientsBeanList) {
                    if (bean.getClientName().toUpperCase().contains(s.toUpperCase())) {
                        searchList.add(bean);

                    }
                }
                paymentsAdapter = new PaymentsAdapter(getActivity(), R.layout.adapter_worksheets, searchList);
                paymentsFragmentListView.setAdapter(paymentsAdapter);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
