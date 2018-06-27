package com.bobin.somemapapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.presenter.DepositionPointDetailPresenter;
import com.bobin.somemapapp.ui.custom.ExpandHeader;
import com.bobin.somemapapp.ui.view.DepositionPointDetailView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DepositionPointDetailActivity
        extends MvpAppCompatActivity
        implements DepositionPointDetailView {
    private static final String PARTNER_ID_KEY = "partnerId";
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";

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


    @InjectPresenter
    DepositionPointDetailPresenter presenter;

    private Unbinder unbinder;

    public static void start(@Nonnull Context context,
                             @Nonnull String partnerId,
                             @Nullable MapCoordinates userPosition) {
        Intent intent = new Intent(context, DepositionPointDetailActivity.class)
                .putExtra(PARTNER_ID_KEY, partnerId);
        if (userPosition != null) {
            intent.putExtra(LATITUDE_KEY, userPosition.getLatitude())
                    .putExtra(LONGITUDE_KEY, userPosition.getLongitude());
        }

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_point_detail);
        unbinder = ButterKnife.bind(this);

        notifyPresenterStart(getIntent());
    }

    private void notifyPresenterStart(Intent intent) {
        String partnerId = intent.getStringExtra(PARTNER_ID_KEY);

        MapCoordinates userPosition = null;
        if (intent.hasExtra(LATITUDE_KEY) && intent.hasExtra(LONGITUDE_KEY)) {
            userPosition = new MapCoordinates(
                    intent.getDoubleExtra(LATITUDE_KEY, 0),
                    intent.getDoubleExtra(LONGITUDE_KEY, 0));
        }

        presenter.onStart(partnerId, userPosition);
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
            ViewHelpers.expand(content);
        } else {
            ViewHelpers.collapse(content);
        }
    }

    @Override
    public void showDistance(int meters) {

    }

    @Override
    public void showPartner(DepositionPartner partner) {
        description.setText(partner.getDescription());
        depositionTime.setText(partner.getDepositionDuration());
        depositionPointType.setText(partner.getPointType());
        oneTimeRestrictions.setText(partner.getLimitations());
    }
}

