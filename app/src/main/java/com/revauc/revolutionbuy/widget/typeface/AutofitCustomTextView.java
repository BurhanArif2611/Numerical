package com.revauc.revolutionbuy.widget.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.revauc.revolutionbuy.R;
import com.revauc.revolutionbuy.widget.AutofitTextView;

import java.util.HashMap;
import java.util.Map;

public class AutofitCustomTextView extends AutofitTextView {
    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created
     * every time when they are referenced.
     */
    public static Map<String, Typeface> mTypefaces;

    public AutofitCustomTextView(Context context) {
        super(context);
    }

    public AutofitCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public AutofitCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        if (mTypefaces == null)
            mTypefaces = new HashMap<>();

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont;
        if (a.hasValue(R.styleable.CustomTextView_customFont))
            customFont = a.getString(R.styleable.CustomTextView_customFont);
        else
            customFont = ctx.getString(R.string.font_avenir_regular);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String path) {
        Typeface typeface;
        if (mTypefaces.containsKey(path)) {
            typeface = mTypefaces.get(path);
        } else {
            typeface = Typeface.createFromAsset(ctx.getAssets(), path);
            mTypefaces.put(path, typeface);
        }
        setTypeface(typeface);
    }
}
