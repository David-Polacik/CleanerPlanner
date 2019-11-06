package com.polacik.david.cleanerplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.ClientsBean;

import java.util.List;

public class PaymentsAdapter extends ArrayAdapter<ClientsBean> {

    private View historyDoneAdapterView;
    private List<ClientsBean> clientsBeanList;

    private TextView paymentsAdapterNameTextView;
    private TextView paymentsAdapterAddressTextView;
    private TextView paymentsAdapterChargedTextView;
    private TextView paymentsAdapterBalanceTextView;
    private TextView balanceTextView;
    private TextView paymentsAdapterTotalTextView;


    public PaymentsAdapter(@NonNull Context context, int resource, @NonNull List<ClientsBean> objects) {
        super(context, resource, objects);
        clientsBeanList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        historyDoneAdapterView = convertView;
        ClientsBean clientsBean = clientsBeanList.get(position);

        if (historyDoneAdapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            historyDoneAdapterView = layoutInflater.inflate(R.layout.adapter_payments, null);
        }

        if (clientsBean != null) {
            paymentsAdapterNameTextView = historyDoneAdapterView.findViewById(R.id.adapter_payments_name_client_text_view);
            paymentsAdapterAddressTextView = historyDoneAdapterView.findViewById(R.id.adapter_payments_address_client_text_view);
            paymentsAdapterChargedTextView = historyDoneAdapterView.findViewById(R.id.adapter_payments_charged_client_text_view);
            paymentsAdapterBalanceTextView = historyDoneAdapterView.findViewById(R.id.adapter_payments_balance_client_text_view);
            balanceTextView = historyDoneAdapterView.findViewById(R.id.balance_client_text_view);
            paymentsAdapterTotalTextView = historyDoneAdapterView.findViewById(R.id.adapter_payments_total_payments_client_text_view);

            if (clientsBean.getClientBalance() <= 0) {
                balanceTextView.setTextColor(Color.parseColor("#439C45"));
                paymentsAdapterBalanceTextView.setTextColor(Color.parseColor("#439C45"));

            }else {
                balanceTextView.setTextColor(Color.parseColor("#9B2E45"));
                paymentsAdapterBalanceTextView.setTextColor(Color.parseColor("#9B2E45"));
            }

            paymentsAdapterNameTextView.setText(clientsBean.getClientName());
            paymentsAdapterAddressTextView.setText(clientsBean.getClientAddress());
            paymentsAdapterChargedTextView.setText(("\u00A3" + clientsBean.getClientPayment()));
            paymentsAdapterBalanceTextView.setText(("\u00A3" + clientsBean.getClientBalance()));
            paymentsAdapterTotalTextView.setText(("\u00A3" + clientsBean.getClientTotal()));

        }

        return historyDoneAdapterView;
    }

}
