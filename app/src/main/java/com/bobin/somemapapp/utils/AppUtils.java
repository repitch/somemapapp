package com.bobin.somemapapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;

import java.util.List;

public final class AppUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T findFragment(FragmentManager fragmentManager, Class<T> clazz) {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments == null)
            return null;
        for (Fragment fragment : fragments) {
            if (fragment == null)
                continue;
            if (fragment.getClass().equals(clazz)) {
                return (T) fragment;
            }
        }
        return null;
    }

    public static float dpToPixels(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static Typeface getCustomTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "SourceSansPro-Regular.ttf");
    }

    public static Spanned toHtml(String html) {
        return Html.fromHtml(html);
    }
}
