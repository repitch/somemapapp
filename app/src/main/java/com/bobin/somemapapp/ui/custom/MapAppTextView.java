package com.bobin.somemapapp.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.bobin.somemapapp.utils.ViewUtils;

public class MapAppTextView extends AppCompatTextView {
    public MapAppTextView(Context context) {
        super(context);
        init(context);
    }

    public MapAppTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapAppTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(ViewUtils.getCustomTypeface(context));
    }
}
