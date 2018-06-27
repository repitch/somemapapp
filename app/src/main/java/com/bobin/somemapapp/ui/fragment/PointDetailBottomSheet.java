package com.bobin.somemapapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointDetailBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.hours)
    TextView hours;
    @BindView(R.id.hours_label)
    TextView hoursLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_small_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((View) view.getParent()).setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
    }

    public void show(FragmentManager fragmentManager, DepositionPoint point, String name, String iconUrl) {
        Glide.with(icon).load(iconUrl).apply(new RequestOptions().circleCrop()).into(icon);
        this.name.setText(name);
        address.setText(point.getFullAddress());
        String hours = point.getWorkHours();
        if (hours == null || hours.length() == 0) {
            this.hours.setVisibility(View.GONE);
            hoursLabel.setVisibility(View.GONE);
        } else {
            this.hours.setVisibility(View.VISIBLE);
            hoursLabel.setVisibility(View.VISIBLE);
            this.hours.setText(hours);
        }
        show(fragmentManager, point.getPartnerName() + "bottom_sheet");
    }
}
