/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

public class Utils {

    public Utils() {
    }

    public boolean checkNetwork(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }

    public static void animateSunblind(RecyclerView.ViewHolder holder, boolean goesDown) {
        int holderHeight = holder.itemView.getHeight();
        holder.itemView.setPivotY(goesDown == true ? 0 : holderHeight);
        holder.itemView.setPivotX(holder.itemView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 300 : -300, 0);
        ObjectAnimator animatorRotation = ObjectAnimator.ofFloat(holder.itemView, "rotationX", goesDown == true ? -90f : 90, 0f);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.5f, 1f);
        animatorSet.playTogether(animatorTranslateY, animatorRotation, animatorScaleX);
        animatorSet.setInterpolator(new DecelerateInterpolator(1.1f));
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}