package com.martenumberto.smartcar;

import android.os.Handler;
import android.service.dreams.DreamService;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.Random;


public class DayDream extends DreamService {
    TranslateAnimation anim;
    private Handler handler;
    private RelativeLayout moveable;
    private int x1 = 0;
    private int x2;
    private int y1 = 0;
    private int y2;
    private int dauer = 7000;
    private int pause = 1000;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setScreenBright(false);
        setContentView(R.layout.layout_daydream);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        anim.cancel();
        handler.removeCallbacks(animation);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        moveable = (RelativeLayout) findViewById(R.id.moveable);
        handler = new Handler();
        handler.postDelayed(animation, 1000);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
        x2 = dm.widthPixels - moveable.getWidth();
        y2 = dm.heightPixels - statusBarOffset - moveable.getHeight();
    }


    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
    }

    private Runnable animation = new Runnable() {
        @Override
        public void run() {
            Random r = new Random();
            int rx = r.nextInt(x2 - x1 + 1) + x1;
            int ry = r.nextInt(y2 - y1 + 1) + y1;
            moveViewTo(moveable, rx, ry);
            handler.postDelayed(this, dauer);
        }
    };

    private void moveViewTo(final View view, final int xDest, final int yDest) {
        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);
        anim = new TranslateAnimation(0, xDest - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(dauer - pause);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setX((float) xDest);
                view.setY((float) yDest);
                view.clearAnimation();
            }
        });
        view.startAnimation(anim);
    }
}