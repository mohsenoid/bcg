package com.mirhoseini.bcg.util;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mirhoseini.bcg.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohsen on 07/01/2017.
 */

public class CustomBindingAdapter {

    @BindingAdapter("bind:avatarUrl")
    public static void loadImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_avatar);
            ViewGroup parent = (ViewGroup) imageView.getParent();
            parent.setBackgroundColor(Color.GRAY);
        } else {
            Picasso.with(imageView.getContext())
                    .load(url)
                    .error(R.drawable.ic_avatar)
                    .transform(new CircleTransform())
                    .into(imageView, new PaletteCallback(imageView) {
                        @Override
                        public void onPalette(Palette palette) {
                            if (null != palette) {
                                ViewGroup parent = (ViewGroup) imageView.getParent();
                                parent.setBackgroundColor(palette.getDominantColor(Color.GRAY));
                            }
                        }
                    });
        }
    }

    static abstract class PaletteCallback implements com.squareup.picasso.Callback {
        private final ImageView target;

        PaletteCallback(final ImageView t) {
            target = t;
        }

        @Override
        public void onSuccess() {
            onPalette(Palette.from(((BitmapDrawable) target.getDrawable()).getBitmap()).generate());
        }

        @Override
        public void onError() {
            onPalette(null);
        }

        public abstract void onPalette(final Palette palette);
    }
}
