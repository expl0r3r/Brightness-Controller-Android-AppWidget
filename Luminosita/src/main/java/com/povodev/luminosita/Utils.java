package com.povodev.luminosita;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * (( Open-Source License ))
 *
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Stefano Munarini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *
 * Brightness Controller - Android AppWidget Project
 *
 * Developed by Stefano Munarini on 10/11/13.
 */


public class Utils {

    public static final String TAG = "MyActivity";

    // Intent actions
    public static final String MINUS = "com.povodev.Provider.minus";
    public static final String PLUS = "com.povodev.Provider.plus";
    public static final String CHANGE = "com.povodev.Provider.change";

    public static int calculatePercent(int brightness) {

        double result = brightness*10/255;
        Math.round(result);
        return (int)result*10;
    }

    public static int calculateExactBrightness(int brightness) {

        int percent = calculatePercent(brightness);
        double result = percent*255/100;
        //Math.round(result);
        return (int)result;
    }

    /*
     *  Create all the intents for the appWidget, add 'em actions and attach 'em to
     *  their buttons; update progressbar progress, src and text.
     *
     */
    public static void updateWidget(Context context, int brightness, int src, String percent) {

        Intent intentPlus = new Intent();
        intentPlus.setAction(Utils.PLUS);
        PendingIntent pendingIntentPlus = PendingIntent.getBroadcast(context, 0, intentPlus, 0);

        Intent intentMinus = new Intent();
        intentMinus.setAction(Utils.MINUS);
        PendingIntent pendingIntentMinus = PendingIntent.getBroadcast(context, 0, intentMinus, 0);

        // Create an Intent to launch ExampleActivity
        Intent intentChange = new Intent();
        intentChange.setAction(Utils.CHANGE);
        PendingIntent pendingIntentChange = PendingIntent.getBroadcast(context, 0, intentChange, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.wwidget);
        rv.setOnClickPendingIntent(R.id.plus, pendingIntentPlus);
        rv.setOnClickPendingIntent(R.id.minus, pendingIntentMinus);
        rv.setOnClickPendingIntent(R.id.auto,pendingIntentChange);
        rv.setProgressBar(R.id.progress,255,brightness,false);
        rv.setImageViewResource(R.id.auto,src);
        rv.setTextViewText(R.id.percent,percent);

        pushWidgetUpdate(context, rv);
    }

    /*
     * Send notification to appWidget telling it to update
     */
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, Provider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
