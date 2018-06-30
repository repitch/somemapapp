package com.bobin.somemapapp.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.infrastructure.PointWatchedService;
import com.bobin.somemapapp.infrastructure.PointWatchedServiceImpl;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.utils.ViewUtils;
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
    @BindView(R.id.watched)
    View eye;

    private ClickListener clickListener;
    private DepositionPoint currentPoint;
    private String pointName;
    private String pointIcon;
    private PointWatchedService watchedService;

    private static final String NAME_KEY = "name";
    private static final String ICON_KEY = "icon";
    private static final String POINT_KEY = "point";
    private boolean onStartWatched;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points_small_details, container, false);
    }

    private void onClick(View view) {
        if (clickListener != null)
            clickListener.onSheetClick(currentPoint, icon);

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
        watchedService = new PointWatchedServiceImpl();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setExitTransition(new Fade());
        onStartWatched = watchedService.isWatched(currentPoint.getExternalId());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this::onClick);
        initData();
        eye.setVisibility(onStartWatched ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((View) getView().getParent()).setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        boolean watched = watchedService.isWatched(currentPoint.getExternalId());
        int visibilityFlag = watched ? View.VISIBLE : View.INVISIBLE;
        if (watched != onStartWatched) {
            eye.setAlpha(0);
            eye.setVisibility(visibilityFlag);
            eye.animate().setDuration(200).alpha(0.6f);
        }
    }

    private void initData() {
        ViewUtils.glideRoundImage(icon, pointIcon);
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
        void onSheetClick(DepositionPoint point, View iconView);
    }
}
