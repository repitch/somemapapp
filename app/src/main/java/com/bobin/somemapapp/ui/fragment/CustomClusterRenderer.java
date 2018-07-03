package com.bobin.somemapapp.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.model.DepositionPointClusterItem;
import com.bobin.somemapapp.utils.AppUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<DepositionPointClusterItem> {
    private final Context context;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<DepositionPointClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(DepositionPointClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.map_marker);
        int h = ((int) AppUtils.dpToPixels(context, 41));
        int w = ((int) AppUtils.dpToPixels(context, 30));
        drawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        drawable.draw(canvas);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bm);
        // все что выше - в конструктор, здесь только выставление иконки
        markerOptions.icon(bitmapDescriptor);
    }
}
