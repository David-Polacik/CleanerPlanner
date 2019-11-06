package com.polacik.david.cleanerplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.bean.ClientsBean;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends ArrayAdapter<ClientsBean> implements Filterable {

    private View clientsAdapterView;
    private List<ClientsBean> clientsBeanList;

    private TextView clientsAdapterNameTextView;
    private TextView clientsAdapterAddressTextView;
    private TextView clientsAdapterPhoneTextView;
    private TextView clientsAdapterEmailTextView;
    private TextView clientsAdapterDateTextView;
    private TextView clientsAdapterChargedTextView;
    private TextView clientsAdapterRepeatTextView;
    private TextView clientsAdapterDescriptionTextView;


    public ClientsAdapter(@NonNull Context context, int resource, @NonNull List<ClientsBean> objects) {
        super(context, resource, objects);
        clientsBeanList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        clientsAdapterView = convertView;
        ClientsBean clientsBean = clientsBeanList.get(position);

        if (clientsAdapterView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            clientsAdapterView = layoutInflater.inflate(R.layout.adapter_clients, null);
        }

        if (clientsBean != null) {

            clientsAdapterNameTextView = clientsAdapterView.findViewById(R.id.adapter_clients_name_text_view);
            clientsAdapterAddressTextView = clientsAdapterView.findViewById(R.id.adapter_clients_address_text_view);
            clientsAdapterPhoneTextView = clientsAdapterView.findViewById(R.id.adapter_clients_phone_text_view);
            clientsAdapterEmailTextView = clientsAdapterView.findViewById(R.id.adapter_clients_email_text_view);
            clientsAdapterDateTextView = clientsAdapterView.findViewById(R.id.adapter_clients_date_text_view);
            clientsAdapterChargedTextView = clientsAdapterView.findViewById(R.id.adapter_clients_charged_text_view);
            clientsAdapterRepeatTextView = clientsAdapterView.findViewById(R.id.adapter_clients_repeat_text_view);
            clientsAdapterDescriptionTextView = clientsAdapterView.findViewById(R.id.adapter_clients_description_text_view);


            clientsAdapterNameTextView.setText(clientsBean.getClientName());
            clientsAdapterAddressTextView.setText(clientsBean.getClientAddress());
            clientsAdapterPhoneTextView.setText(clientsBean.getClientPhone());
            clientsAdapterEmailTextView.setText(clientsBean.getClientEmail());
            clientsAdapterDateTextView.setText(clientsBean.getClientWorkStart());
            clientsAdapterChargedTextView.setText(("\u00A3" + clientsBean.getClientPayment()));
            clientsAdapterRepeatTextView.setText(String.valueOf(clientsBean.getClientWorkRepeat()));
            clientsAdapterDescriptionTextView.setText(clientsBean.getClientDescription());
        }

        return clientsAdapterView;
    }
}
