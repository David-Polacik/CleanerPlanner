package com.polacik.david.cleanerplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.ClientsBean;
import com.polacik.david.cleanerplanner.bean.PaymentBean;
import com.polacik.david.cleanerplanner.fragments.HistoryFragment;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<PaymentBean> {

    private View historyDoneAdapterView;
    private HistoryFragment historyFragment;
    private List<PaymentBean> paymentClientsBeanList;
    private List<ClientsBean> clientsBeanList;

    private TextView historyPaymentAdapterNameTextView;
    private TextView historyPaymentAdapterChargedTextView;
    private TextView historyPaymentAdapterPaidTextView;
    private TextView historyPaymentAdapterMethodTextView;
    private TextView historyPaymentAdapterDateTextView;
    private TextView historyPaymentAdapterReasonTextView;

    private LinearLayout historyPaymentAdapterChargedPaidLinearLayout;
    private LinearLayout historyPaymentAdapterMethodLinearLayout;
    private LinearLayout historyPaymentAdapterReasonPaidLinearLayout;

    private CheckBox historyPaymentAdapterCheckBox;


    private ClientsBean clientsBean = new ClientsBean();


    public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<PaymentBean> objects, List<ClientsBean> clientsBeanList, HistoryFragment historyFragment) {
        super(context, resource, objects);
        paymentClientsBeanList = objects;
        this.clientsBeanList = clientsBeanList;
        this.historyFragment = historyFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        historyDoneAdapterView = convertView;
        PaymentBean paymentBean = paymentClientsBeanList.get(position);

        if (historyDoneAdapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            historyDoneAdapterView = layoutInflater.inflate(R.layout.adapter_history, null);
        }

        if (paymentBean != null) {
            historyPaymentAdapterNameTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_name_client_text_view);
            historyPaymentAdapterChargedTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_charged_client_text_view);
            historyPaymentAdapterPaidTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_paid_client_text_view);
            historyPaymentAdapterMethodTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_method_client_text_view);
            historyPaymentAdapterDateTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_date_text_view);
            historyPaymentAdapterReasonTextView = historyDoneAdapterView.findViewById(R.id.adapter_history_reason_client_text_view);
            historyPaymentAdapterCheckBox = historyDoneAdapterView.findViewById(R.id.adapter_history_checkBox);

            historyPaymentAdapterChargedPaidLinearLayout = historyDoneAdapterView.findViewById(R.id.history_adapter_charged_paid_linear_layout);
            historyPaymentAdapterMethodLinearLayout = historyDoneAdapterView.findViewById(R.id.history_adapter_method_linear_layout);
            historyPaymentAdapterReasonPaidLinearLayout = historyDoneAdapterView.findViewById(R.id.history_adapter_reason_linear_layout);

            historyPaymentAdapterChargedPaidLinearLayout.setVisibility(View.VISIBLE);
            historyPaymentAdapterMethodLinearLayout.setVisibility(View.VISIBLE);
            historyPaymentAdapterReasonPaidLinearLayout.setVisibility(View.VISIBLE);

            historyPaymentAdapterNameTextView.setTextColor(Color.BLACK);
            historyPaymentAdapterDateTextView.setTextColor(Color.BLACK);

            for (ClientsBean bean : clientsBeanList) {
                if (paymentBean.getClientId().contains(bean.getClientId())) {
                    clientsBean.setClientName(bean.getClientName());
                }
            }


            if (paymentBean.getPaymentStatus() == 1) {
                historyPaymentAdapterNameTextView.setTextColor(Color.parseColor("#439C45"));
                historyPaymentAdapterDateTextView.setTextColor(Color.parseColor("#439C45"));
                historyPaymentAdapterReasonPaidLinearLayout.setVisibility(View.GONE);
            }

            if (paymentBean.getPaymentStatus() == 2) {
                historyPaymentAdapterNameTextView.setTextColor(Color.parseColor("#9B2E45"));
                historyPaymentAdapterDateTextView.setTextColor(Color.parseColor("#9B2E45"));
                historyPaymentAdapterChargedPaidLinearLayout.setVisibility(View.GONE);
                historyPaymentAdapterMethodLinearLayout.setVisibility(View.GONE);

            }

            historyPaymentAdapterNameTextView.setText(clientsBean.getClientName());
            historyPaymentAdapterChargedTextView.setText(("\u00A3" + paymentBean.getPaymentCharged()));
            historyPaymentAdapterPaidTextView.setText((" \u00A3" + paymentBean.getPaymentPaid()));
            historyPaymentAdapterMethodTextView.setText(paymentBean.getPaymentMethod());
            historyPaymentAdapterDateTextView.setText(paymentBean.getPaymentDate());
            historyPaymentAdapterReasonTextView.setText(paymentBean.getPaymentReason());

        }

        historyPaymentAdapterCheckBox.setTag(position);
        checkBoxSelected(historyPaymentAdapterCheckBox);
        return historyDoneAdapterView;
    }

    private void checkBoxSelected(CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(false);

        if (historyFragment.isMenuActionMode()) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = (int) compoundButton.getTag();
                    if (historyFragment.getPaymentsSelection().contains(paymentClientsBeanList.get(position))) {
                        historyFragment.getPaymentsSelection().remove(paymentClientsBeanList.get(position));
                    } else {
                        historyFragment.getPaymentsSelection().add(paymentClientsBeanList.get(position));
                    }
                    historyFragment.getActionMode().setTitle(historyFragment.getPaymentsSelection().size() + " items selected");
                }
            });
        } else {
            checkBox.setVisibility(View.GONE);
        }

    }
}
