package com.bobin.somemapapp.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.tables.DepositionPoint;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class DepositionPointViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.partner_name)
    TextView partnerName;
    @BindView(R.id.full_address)
    TextView fullAddress;

    public DepositionPointViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static int layoutId = R.layout.item_deposition_point;

    public void bind(DepositionPoint point) {
        partnerName.setText(point.getPartnerName());
        fullAddress.setText(point.getFullAddress());
    }
}
