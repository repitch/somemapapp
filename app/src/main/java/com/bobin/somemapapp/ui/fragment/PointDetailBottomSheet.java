package com.bobin.somemapapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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
    @BindView(R.id.point_icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.hours)
    TextView hours;
    @BindView(R.id.hours_label)
    TextView hoursLabel;
    private ClickListener clickListener;
    private DepositionPoint currentPoint;
    private String pointName;
    private String pointIcon;

    private static final String NAME_KEY = "name";
    private static final String ICON_KEY = "icon";
    private static final String POINT_KEY = "point";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_small_details, container, false);
    }

    private void onClick(View view) {
        if (clickListener != null)
            clickListener.onSheetClick(currentPoint);
    }

    public PointDetailBottomSheet setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public static PointDetailBottomSheet newInstance(DepositionPoint point, String name, String iconUrl) {
        Bundle args = new Bundle();
        args.putSerializable(POINT_KEY, point);
        args.putString(NAME_KEY, name);
        args.putString(ICON_KEY, iconUrl);
        PointDetailBottomSheet fragment = new PointDetailBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null)
            return;

        currentPoint = (DepositionPoint) arguments.getSerializable(POINT_KEY);
        pointName = arguments.getString(NAME_KEY);
        pointIcon = arguments.getString(ICON_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this::onClick);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((View) getView().getParent()).setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
    }

    private void initData() {
        Glide.with(icon).load(pointIcon).apply(new RequestOptions().circleCrop()).into(icon);
        this.name.setText(pointName);
        address.setText(currentPoint.getFullAddress());
        String hours = currentPoint.getWorkHours();
        if (hours == null || hours.length() == 0) {
            this.hours.setVisibility(View.GONE);
            hoursLabel.setVisibility(View.GONE);
        } else {
            this.hours.setVisibility(View.VISIBLE);
            hoursLabel.setVisibility(View.VISIBLE);
            this.hours.setText(hours);
        }
    }

    public interface ClickListener {
        void onSheetClick(DepositionPoint point);
    }
}
