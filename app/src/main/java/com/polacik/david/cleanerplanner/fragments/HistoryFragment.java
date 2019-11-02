package com.polacik.david.cleanerplanner.fragments;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.adapters.HistoryAdapter;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.bean.PaymentBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private View historyFragmentView;
    private ListView historyFragmentListView;
    private HistoryAdapter historyAdapter;
    private List<ClientsBean> historyClientBeanList;
    private List<PaymentBean> historyPaymentsBeanList;
    private List<ClientsBean> historyClientSelectBeanList;

    private TabLayout historyFragmentTabLayout;

    private DatabaseReference databaseReferenceClients;
    private DatabaseReference databaseReferencePayments;
    private FirebaseAuth firebaseAuth;

    private boolean isMenuActionMode;
    private List<PaymentBean> paymentsSelection = new ArrayList<>();
    private ActionMode actionMode = null;
    private AbsListView.MultiChoiceModeListener modeListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceClients = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("clients");
        databaseReferencePayments = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(firebaseAuth.getUid())).child("payments");

        historyFragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        historyFragmentListView = historyFragmentView.findViewById(R.id.history_fragment_list_view);

        multiChoiceSelect();
        historyFragmentListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        historyFragmentListView.setMultiChoiceModeListener(modeListener);

        historyFragmentTabLayout = historyFragmentView.findViewById(R.id.fragment_history_tab_layout);

        historyClientBeanList = new ArrayList<>();
        historyClientSelectBeanList = new ArrayList<>();
        historyPaymentsBeanList = new ArrayList<>();


        databaseReferencePaymentsMethods();
        clickOnTabLayoutItem();


        return historyFragmentView;
    }

    private void multiChoiceSelect() {
        modeListener = new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_top_navigation, menu);
                isMenuActionMode = true;
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                menu.findItem(R.id.menu_top_navidation_calendar_icon).setVisible(false);
                menu.findItem(R.id.menu_top_navidation_search_icon).setVisible(false);
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_top_navidation_delete_icon:

                        for (PaymentBean bean : paymentsSelection) {
                            String id = bean.getPaymentId();
                            databaseReferencePayments.child(id).removeValue();
                        }

                        actionMode.finish();
                        return true;

                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                isMenuActionMode = false;
                actionMode = null;
                historyAdapter.notifyDataSetChanged();
                paymentsSelection.clear();
            }
        };
    }

    public boolean isMenuActionMode() {
        return isMenuActionMode;
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public List<PaymentBean> getPaymentsSelection() {
        return paymentsSelection;
    }

    private void databaseReferencePaymentsMethods() {
        databaseReferencePayments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyPaymentsBeanList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    historyPaymentsBeanList.add(ds.getValue(PaymentBean.class));
                }
                databaseReferenceClients.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        historyClientBeanList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            historyClientBeanList.add(ds.getValue(ClientsBean.class));
                        }

                        if (getActivity() != null) {
                            historyAdapter = new HistoryAdapter(getActivity(), R.layout.adapter_history, historyPaymentsBeanList,
                                    historyClientBeanList, HistoryFragment.this);
                            historyFragmentListView.setAdapter(historyAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clickOnTabLayoutItem() {

        historyFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        databaseReferencePaymentsMethods();
                        break;
                    case 1:
                        databaseReferencePayments.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                historyPaymentsBeanList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    PaymentBean paymentBean = ds.getValue(PaymentBean.class);
                                    {
                                        if (Objects.requireNonNull(paymentBean).getPaymentStatus() == 1) {
                                            historyPaymentsBeanList.add(ds.getValue(PaymentBean.class));
                                        }
                                    }
                                }
                                databaseReferenceClients.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        historyClientBeanList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            historyClientBeanList.add(ds.getValue(ClientsBean.class));
                                        }

                                        if (getActivity() != null) {
                                            historyAdapter = new HistoryAdapter(getActivity(), R.layout.adapter_history, historyPaymentsBeanList,
                                                    historyClientBeanList, HistoryFragment.this);
                                            historyFragmentListView.setAdapter(historyAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    case 2:
                        databaseReferencePayments.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                historyPaymentsBeanList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    PaymentBean paymentBean = ds.getValue(PaymentBean.class);
                                    {
                                        if (Objects.requireNonNull(paymentBean).getPaymentStatus() == 2) {
                                            historyPaymentsBeanList.add(ds.getValue(PaymentBean.class));
                                        }
                                    }
                                }
                                databaseReferenceClients.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        historyClientBeanList.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            historyClientBeanList.add(ds.getValue(ClientsBean.class));
                                        }

                                        if (getActivity() != null) {
                                            historyAdapter = new HistoryAdapter(getActivity(), R.layout.adapter_history, historyPaymentsBeanList,
                                                    historyClientBeanList, HistoryFragment.this);
                                            historyFragmentListView.setAdapter(historyAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

                List<PaymentBean> searchList = new ArrayList<>();
                List<ClientsBean> searchClientList = new ArrayList<>();
                for (PaymentBean paymentBean : historyPaymentsBeanList) {
                    for (ClientsBean clientsBean : historyClientBeanList) {
                        if (clientsBean.getClientName().toUpperCase().contains(s.toUpperCase()))
                            if (paymentBean.getClientId().contains(clientsBean.getClientId())) {
                                searchClientList.add(clientsBean);
                                searchList.add(paymentBean);
                            }
                    }
                }
                if (getActivity() != null) {
                    historyAdapter = new HistoryAdapter(getActivity(), R.layout.adapter_history, searchList,
                            searchClientList, HistoryFragment.this);
                    historyFragmentListView.setAdapter(historyAdapter);

                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
