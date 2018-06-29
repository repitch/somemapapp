package com.bobin.somemapapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.model.tables.Limit;
import com.bobin.somemapapp.presenter.DepositionPointDetailPresenter;
import com.bobin.somemapapp.ui.custom.ExpandHeader;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;
import com.bobin.somemapapp.utils.GoogleMapUtils;
import com.bobin.somemapapp.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DepositionPointDetailActivity
        extends MvpAppCompatActivity
        implements DepositionPointDetailView, AdapterView.OnItemSelectedListener {
    private static final String PARTNER_ID_KEY = "partnerId";
    private static final String USER_LATITUDE_KEY = "uLatitude";
    private static final String USER_LONGITUDE_KEY = "uLongitude";
    private static final String POINT_LATITUDE_KEY = "pLatitude";
    private static final String POINT_LONGITUDE_KEY = "pLongitude";
    private static final String POINT_ADDRESS_KEY = "pAddress";
    private static final String TRANSITION_NAME_KEY = "transitionName";

    @BindView(R.id.description_content)
    FrameLayout descriptionContent;
    @BindView(R.id.deposition_time_content)
    FrameLayout depositionTimeContent;
    @BindView(R.id.deposition_point_type_content)
    FrameLayout depositionPointTypeContent;
    @BindView(R.id.restrictions_content)
    View restrictionsContent;

    @BindView(R.id.description_text)
    TextView description;
    @BindView(R.id.deposition_time_text)
    TextView depositionTime;
    @BindView(R.id.deposition_point_type_text)
    TextView depositionPointType;

    @BindView(R.id.selector)
    AppCompatSpinner restrictionsSelector;
    @BindView(R.id.money_restrictions)
    TextView moneyRestrictions;
    @BindView(R.id.one_time_restrictions)
    TextView oneTimeRestrictions;
    @BindView(R.id.partner_icon)
    ImageView partnerIcon;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.distance)
    TextView distance;

    @InjectPresenter
    DepositionPointDetailPresenter presenter;

    private Unbinder unbinder;

    public static void start(@Nonnull Activity activity,
                             @Nonnull DepositionPoint point,
                             @Nullable MapCoordinates userPosition,
                             @Nullable View iconView) {
        ActivityOptions activityOptions = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iconView != null) {
            activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    activity, iconView, iconView.getTransitionName());
        }

        Intent intent = new Intent(activity, DepositionPointDetailActivity.class)
                .putExtra(PARTNER_ID_KEY, point.getPartnerName())
                .putExtra(POINT_LATITUDE_KEY, point.getLatitude())
                .putExtra(POINT_LONGITUDE_KEY, point.getLongitude())
                .putExtra(POINT_ADDRESS_KEY, point.getFullAddress());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iconView != null)
            intent.putExtra(TRANSITION_NAME_KEY, iconView.getTransitionName());


        if (userPosition != null) {
            intent.putExtra(USER_LATITUDE_KEY, userPosition.getLatitude())
                    .putExtra(USER_LONGITUDE_KEY, userPosition.getLongitude());
        }

        if (activityOptions == null)
            activity.startActivity(intent);
        else
            activity.startActivity(intent, activityOptions.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_point_detail);
        unbinder = ButterKnife.bind(this);
        restrictionsSelector.setOnItemSelectedListener(this);
        notifyPresenterStart(getIntent());
    }

    private void notifyPresenterStart(Intent intent) {
        String partnerId = intent.getStringExtra(PARTNER_ID_KEY);
        String pointAddress = intent.getStringExtra(POINT_ADDRESS_KEY);
        MapCoordinates pointLocation = new MapCoordinates(
                intent.getDoubleExtra(POINT_LATITUDE_KEY, 0),
                intent.getDoubleExtra(POINT_LONGITUDE_KEY, 0));

        MapCoordinates userPosition = null;
        if (intent.hasExtra(USER_LATITUDE_KEY) && intent.hasExtra(USER_LONGITUDE_KEY)) {
            userPosition = new MapCoordinates(
                    intent.getDoubleExtra(USER_LATITUDE_KEY, 0),
                    intent.getDoubleExtra(USER_LONGITUDE_KEY, 0));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent.hasExtra(TRANSITION_NAME_KEY))
            partnerIcon.setTransitionName(intent.getStringExtra(TRANSITION_NAME_KEY));

        address.setText(pointAddress);

        presenter.onStart(partnerId, pointLocation, userPosition);
    }

    @OnClick(R.id.go_to_web_site)
    public void onGoToWebSiteClick() {
        presenter.goToWebSite();
    }

    @OnClick(R.id.description_button)
    public void toggleDescription(View header) {
        if (header instanceof ExpandHeader)
            toggleSection((ExpandHeader) header, descriptionContent);
    }

    @OnClick(R.id.deposition_time_button)
    public void toggleDepositionTime(View header) {
        if (header instanceof ExpandHeader)
            toggleSection((ExpandHeader) header, depositionTimeContent);
    }

    @OnClick(R.id.deposition_point_type_button)
    public void toggleDepositionPointType(View header) {
        if (header instanceof ExpandHeader)
            toggleSection((ExpandHeader) header, depositionPointTypeContent);
    }

    @OnClick(R.id.restrictions_button)
    public void toggleRestrictions(View header) {
        if (header instanceof ExpandHeader)
            toggleSection((ExpandHeader) header, restrictionsContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void toggleSection(ExpandHeader header, final View content) {
        boolean show = header.toggle();
        if (show) {
            ViewUtils.expand(content);
        } else {
            ViewUtils.collapse(content);
        }
    }

    @Override
    public void showDistance(int meters) {
        distance.setVisibility(meters > 0 ? View.VISIBLE : View.GONE);
        distance.setText(GoogleMapUtils.distanceToString(this, meters));
    }

    @Override
    public void showPartner(DepositionPartner partner) {
        setTitle(partner.getName());
        description.setText(ViewUtils.toHtml(partner.getDescription()));
        depositionTime.setText(ViewUtils.toHtml(partner.getDepositionDuration()));
        depositionPointType.setText(ViewUtils.toHtml(partner.getPointType()));
        oneTimeRestrictions.setText(ViewUtils.toHtml(partner.getLimitations()));

        List<Limit> filtered = new ArrayList<>();
        for (Limit limit : partner.getLimits()) {
            if (!limit.isEmpty())
                filtered.add(limit);
        }

        restrictionsSelector.setAdapter(new ArrayAdapter<>(
                restrictionsSelector.getContext(),
                R.layout.item_spinner,
                R.id.text,
                filtered));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startPostponedEnterTransition();

        ViewUtils.glideRoundImage(partnerIcon, partner.getFullPictureUrl());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void openBrowser(Uri uri) {
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Limit limit = presenter.getLimit(position);
        moneyRestrictions.setText(getDescription(limit));
    }

    private String getDescription(Limit limit) {
        if (limit.getAmount() == 0)
            return getString(R.string.from) + " " + limit.getMin() + " " + getString(R.string.to) + " " + limit.getMax();
        return getString(R.string.amount) + ": " + limit.getAmount();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

