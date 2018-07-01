package com.bobin.somemapapp.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private static final String POINT_ADDRESS_KEY = "pAddress";
    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String POINT_ID_KEY = "pointId";
    private static final String EXPANDED_BUTTONS_KEY = "expandedButtons";
    private static final String SELECTED_RESTRICTION_KEY = "selectedRestriction";
    private static final String POINT_LATITUDE_KEY = "pLatitude";
    private static final String POINT_LONGITUDE_KEY = "pLongitude";
    private static final String SHOW_POINT_ON_MAP_KEY = "showOnMap";

    @BindView(R.id.description_button)
    ExpandHeader descriptionButton;
    @BindView(R.id.deposition_time_button)
    ExpandHeader depositionTimeButton;
    @BindView(R.id.deposition_point_type_button)
    ExpandHeader depositionPointTypeButton;
    @BindView(R.id.restrictions_button)
    ExpandHeader restrictionsButton;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @InjectPresenter
    DepositionPointDetailPresenter presenter;

    private Unbinder unbinder;
    private int selectedLimit;
    private MapCoordinates pointLocation;

    public static MapCoordinates tryExtractActivityResult(Intent intent) {
        if (!intent.getBooleanExtra(SHOW_POINT_ON_MAP_KEY, false))
            return null;
        double latitude = intent.getDoubleExtra(POINT_LATITUDE_KEY, 0);
        double longitude = intent.getDoubleExtra(POINT_LONGITUDE_KEY, 0);
        return new MapCoordinates(latitude, longitude);
    }

    public static void start(@Nonnull Activity activity,
                             @Nonnull DepositionPoint point,
                             @Nullable MapCoordinates userPosition,
                             @Nullable View iconView) {
        start(activity, point, userPosition, iconView, 0);
    }

    public static void start(@Nonnull Activity activity,
                             @Nonnull DepositionPoint point,
                             @Nullable MapCoordinates userPosition,
                             @Nullable View iconView,
                             int requestCode) {
        ActivityOptions activityOptions = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iconView != null) {
            activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    activity, iconView, iconView.getTransitionName());
        }

        Intent intent = new Intent(activity, DepositionPointDetailActivity.class)
                .putExtra(POINT_ID_KEY, point.getExternalId())
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
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivityForResult(intent, requestCode, activityOptions.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_point_detail);
        unbinder = ButterKnife.bind(this);
        restrictionsSelector.setOnItemSelectedListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewUtils.changeAllTextViewsToCustomFont(toolbar);
        notifyPresenterStart(getIntent());
        if (savedInstanceState != null)
            loadState(savedInstanceState);
    }

    private void loadState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(EXPANDED_BUTTONS_KEY)) {
            boolean[] expanded = savedInstanceState.getBooleanArray(EXPANDED_BUTTONS_KEY);
            if (expanded == null || expanded.length != 4)
                return;
            if (expanded[0]) toggleSection(descriptionButton, descriptionContent);
            if (expanded[1]) toggleSection(depositionTimeButton, depositionTimeContent);
            if (expanded[2]) toggleSection(depositionPointTypeButton, depositionPointTypeContent);
            if (expanded[3]) toggleSection(restrictionsButton, restrictionsContent);
        }

        selectedLimit = savedInstanceState.getInt(SELECTED_RESTRICTION_KEY, 0);
    }

    private void notifyPresenterStart(Intent intent) {
        String partnerId = intent.getStringExtra(PARTNER_ID_KEY);
        String pointId = intent.getStringExtra(POINT_ID_KEY);
        String pointAddress = intent.getStringExtra(POINT_ADDRESS_KEY);
        pointLocation = new MapCoordinates(
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
        presenter.setPointWatched(pointId);
    }

    @OnClick(R.id.show_on_map)
    public void showOnMap() {
        Intent intent = new Intent();
        intent.putExtra(SHOW_POINT_ON_MAP_KEY, true)
                .putExtra(POINT_LATITUDE_KEY, pointLocation.getLatitude())
                .putExtra(POINT_LONGITUDE_KEY, pointLocation.getLongitude());
        setResult(RESULT_OK, intent);
        finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public void showPartner(DepositionPartner partner, String icon) {
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
        restrictionsSelector.setSelection(selectedLimit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startPostponedEnterTransition();

        ViewUtils.glideRoundImage(partnerIcon, icon);
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
        selectedLimit = position;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean[] expanded = new boolean[4];
        expanded[0] = descriptionButton.isExpanded();
        expanded[1] = depositionTimeButton.isExpanded();
        expanded[2] = depositionPointTypeButton.isExpanded();
        expanded[3] = restrictionsButton.isExpanded();
        outState.putBooleanArray(EXPANDED_BUTTONS_KEY, expanded);
        outState.putInt(SELECTED_RESTRICTION_KEY, selectedLimit);
    }
}

