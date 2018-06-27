package com.bobin.somemapapp.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobin.somemapapp.R;

public class ExpandHeader extends FrameLayout {
    private ImageView arrow;

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
        ImageView icon = view.findViewById(R.id.header_icon);
        TextView title = view.findViewById(R.id.title);
        arrow = view.findViewById(R.id.arrow);

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

    private boolean state;

    public boolean toggle() {
        state = !state;
        return state;
    }
}
