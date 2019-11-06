package com.polacik.david.cleanerplanner.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.polacik.david.cleanerplanner.activities.CalendarActivity;
import com.polacik.david.cleanerplanner.adapters.WorksheetsAdapter;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class WorksheetsFragment extends Fragment {

    private View worksheetsFragmentView;
    private ListView worksheetsFragmentListView;
    private WorksheetsAdapter worksheetsAdapter;
    private List<ClientsBean> dateClientsBean;
    private ProgressBar worksheetsFragmentProgressBar;
    private TextView worksheetsFragmentTodoTextView;
    private TextView worksheetsFragmentTotalPriceTextView;

    private int dateWeekInt;
    private int dateYearInt;

    private DatabaseReference databaseReferenceClients;
    private DatabaseReference databaseReferencePayments;
    private DatabaseReference databaseReferenceSettings;
    private FirebaseAuth firebaseAuth;

    private List<ClientsBean> clientsBeanList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        dateWeekInt = calendar.get(Calendar.WEEK_OF_YEAR);
        dateYearInt = calendar.get(Calendar.YEAR);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceClients = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");
        databaseReferencePayments = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("payments");
        databaseReferenceSettings = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("settings");

        worksheetsFragmentView = inflater.inflate(R.layout.fragment_worksheets, container, false);
        worksheetsFragmentListView = worksheetsFragmentView.findViewById(R.id.worksheets_fragment_list_view);

        worksheetsFragmentProgressBar = worksheetsFragmentView.findViewById(R.id.fragment_worksheets_progress_bar);

        worksheetsFragmentTodoTextView = worksheetsFragmentView.findViewById(R.id.worksheets_fragment_todo_text_view);
        worksheetsFragmentTotalPriceTextView = worksheetsFragmentView.findViewById(R.id.worksheets_fragment_total_price_text_view);

        clientsBeanList = new ArrayList<>();
        dateClientsBean = new ArrayList<>();

        databaseReferencePaymentsMethods();
        selectDateWorksheets();


        return worksheetsFragmentView;
    }

    private void databaseReferencePaymentsMethods() {
        databaseReferenceClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientsBeanList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    clientsBeanList.add(ds.getValue(ClientsBean.class));

                }

                selectDateWorksheets();
                if (getActivity() != null) {
                    worksheetsFragmentProgressBar.setVisibility(View.GONE);
                    worksheetsAdapter = new WorksheetsAdapter(getActivity(), R.layout.adapter_worksheets, dateClientsBean);
                    worksheetsFragmentListView.setAdapter(worksheetsAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                worksheetsAdapter = new WorksheetsAdapter(getActivity(), R.layout.adapter_worksheets, searchList);
                worksheetsFragmentListView.setAdapter(worksheetsAdapter);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_top_navidation_calendar_icon) {
            Intent showCalendarActivity = new Intent(getContext(), CalendarActivity.class);
            startActivityForResult(showCalendarActivity, 1);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                dateWeekInt = data.getIntExtra(IntentConstant.KEY_CALENDARWEEK, 0);
                dateYearInt = data.getIntExtra(IntentConstant.KEY_CALENDARYEAR, 0);

                selectDateWorksheets();
            }
        }
    }

    private void selectDateWorksheets() {

        dateClientsBean = new ArrayList<>();

        int reatDate = (dateYearInt * 100) + dateWeekInt;
        double totalPrice = 0;

        for (ClientsBean clientsBean : clientsBeanList) {

            int clientData = (clientsBean.getClientYearWork() * 100) + clientsBean.getClientWeekWork();
            if (reatDate >= clientData) {
                totalPrice = totalPrice + clientsBean.getClientPayment();
                dateClientsBean.add(clientsBean);
            }
        }

        if (getActivity() != null) {
            worksheetsAdapter = new WorksheetsAdapter(getActivity(), R.layout.adapter_worksheets, dateClientsBean);
            worksheetsFragmentListView.setAdapter(worksheetsAdapter);
            worksheetsFragmentTodoTextView.setText(String.valueOf(worksheetsAdapter.getCount()));
            worksheetsFragmentTotalPriceTextView.setText(("\u00A3" + totalPrice));
        }


    }

}
