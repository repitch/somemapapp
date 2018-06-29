package com.bobin.somemapapp.ui.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobin.somemapapp.R;

public class ExpandHeader extends FrameLayout {
    private static final int ANIMATION_DURATION = 200;
    private ImageView arrow;
    private ImageView icon;
    private TextView title;

    public ExpandHeader(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ExpandHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.item_expand_header, this);
        icon = view.findViewById(R.id.header_icon);
        title = view.findViewById(R.id.title);
        arrow = view.findViewById(R.id.arrow);
        title.setAlpha(0.5f);
        icon.setAlpha(0.5f);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ExpandHeader,
                0, 0);

        try {
            Drawable drawable = a.getDrawable(R.styleable.ExpandHeader_header_icon);
            icon.setImageDrawable(drawable);
            title.setText(a.getString(R.styleable.ExpandHeader_title));
        } finally {
            a.recycle();
        }
    }

    public boolean toggle() {
        boolean state = toggleArrow(arrow);
        title.animate().setDuration(ANIMATION_DURATION).alpha(state ? 1 : 0.5f);
        icon.animate().setDuration(ANIMATION_DURATION).alpha(state ? 1 : 0.5f);
        return state;
    }

    private boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(ANIMATION_DURATION).rotation(180);
            return true;
        } else {
            view.animate().setDuration(ANIMATION_DURATION).rotation(0);
            return false;
        }
    }
}
