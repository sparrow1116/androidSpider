package com.example.zyj.first;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by zyj on 2020/11/7.
 */

public class Uitls {

    private static final String TAG  = "Uitls";

    static GestureDescription.Builder wripe(Resources res,String derection){
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        final int downSideOfScreen = displayMetrics.heightPixels - 50;
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path path = new Path();
        switch (derection){
            case "up":
                path.moveTo(100 + getRandomInt(-10,10), downSideOfScreen + getRandomInt(-10,10));
                path.lineTo(100 + getRandomInt(-10,10), 100 + getRandomInt(-10,10));
                break;
        }

        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path,100,50));

        return gestureBuilder;

    }
    static GestureDescription.Builder click(Rect bound){
        Path path = new Path();
        int left = bound.left + getRandomInt(0, bound.right - bound.left);
        int top = bound.top + getRandomInt(0,bound.bottom - bound.top);
        path.moveTo(left, top );
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path,0,100));
        return gestureBuilder;
    }

    static int getRandomInt(int min, int max){
        int ran = (int) (Math.random()*(max-min)+min);
        return ran;
    }
}
