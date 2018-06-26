package com.bobin.somemapapp.ui.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointDetailBottomSheet extends BottomSheetDialog {
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

    public PointDetailBottomSheet(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PointDetailBottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected PointDetailBottomSheet(@NonNull Context context, boolean cancelable,
                                     OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_points_small_details, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        ((View) view.getParent()).setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
    }

    public void show(DepositionPoint point, String iconUrl) {
        Glide.with(icon).load(iconUrl).apply(new RequestOptions().circleCrop()).into(icon);
        name.setText(point.getPartnerName());
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
        show();
    }
}
