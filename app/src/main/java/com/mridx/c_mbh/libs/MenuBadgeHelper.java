/*
 * Copyright (c) 2020. Code by MriDx
 */

package com.mridx.c_mbh.libs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Menu;
import android.view.MenuItem;

import com.mridx.c_mbh.R;


public class MenuBadgeHelper {

    public static void addBadge(Context context, Menu menu, String badgeText) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.badgeCount);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(badgeText);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.badgeCount, badge);
    }

}
