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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.activities.ClientsActivityAdd;
import com.polacik.david.cleanerplanner.adapters.ClientsAdapter;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ClientsFragment extends Fragment {

    private View clientsFragmentView;
    private ListView clientsFragmentListView;
    private ClientsAdapter clientsAdapter;

    private FloatingActionButton clientsFragmentAddFloatingActionButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private List<ClientsBean> clientsBeanList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");

        clientsFragmentView = inflater.inflate(R.layout.fragment_clients, container, false);
        clientsFragmentListView = clientsFragmentView.findViewById(R.id.clients_fragment_list_view);

        clientsFragmentAddFloatingActionButton = clientsFragmentView.findViewById(R.id.fragment_clients_add_floating_action_button);

        clientsBeanList = new ArrayList<>();
        clientsAdapter = new ClientsAdapter(getContext(), R.layout.adapter_clients, clientsBeanList);

        databaseReferencePaymentsMethods();
        clickOnAddFloatingButton();
        clickOnListViewItem();

        return clientsFragmentView;
    }

    private void databaseReferencePaymentsMethods() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsBeanList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    clientsBeanList.add(ds.getValue(ClientsBean.class));

                }
                if (getActivity() != null) {
                    clientsAdapter = new ClientsAdapter(getActivity(), R.layout.adapter_worksheets, clientsBeanList);
                    clientsFragmentListView.setAdapter(clientsAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clickOnListViewItem() {

        clientsFragmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ClientsBean idListView = (ClientsBean) adapterView.getAdapter().getItem(i);

                Intent showClientsActivityAdd = new Intent(getActivity(), ClientsActivityAdd.class);
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTLISTVIEWPOSITION, i);
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANID, idListView.getClientId());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANNAME, idListView.getClientName());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANADDRESS, idListView.getClientAddress());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANPHONE, idListView.getClientPhone());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANEMAIL, idListView.getClientEmail());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANPAYMENT, idListView.getClientPayment());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANBALANCE, idListView.getClientBalance());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANTOTAL, idListView.getClientTotal());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANREPEAT, idListView.getClientWorkRepeat());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEASTARTWORK, idListView.getClientWorkStart());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANWEEKWORK, idListView.getClientWeekWork());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANYEARWORK, idListView.getClientYearWork());
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEANDESCRIPTION, idListView.getClientDescription());

                startActivityForResult(showClientsActivityAdd, 0);
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
                clientsAdapter = new ClientsAdapter(getActivity(), R.layout.adapter_worksheets, searchList);
                clientsFragmentListView.setAdapter(clientsAdapter);

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    private void clickOnAddFloatingButton() {
        clientsFragmentAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatDate = new SimpleDateFormat("d.M.yyyy");
                String todaysDate = formatDate.format(calendar.getTime());

                Intent showClientsActivityAdd = new Intent(getActivity(), ClientsActivityAdd.class);
                showClientsActivityAdd.putExtra(IntentConstant.KEY_CLIENTBEASTARTWORK, todaysDate);
                startActivity(showClientsActivityAdd);
            }
        });
    }

}
