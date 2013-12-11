package com.povodev.luminosita;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

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


public class Provider extends AppWidgetProvider{

    // current brightness value
    private int brightness;

    // mode -> 0 manual / 1 auto
    private int mode;

    // appwidget src
    private int src;

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;

    private String percent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;

        //Get the content resolver
        cResolver = context.getContentResolver();
        try {
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
            // get mode
            mode = Settings.System.getInt(cResolver,Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        setResources();

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            Utils.updateWidget(context, brightness, src, percent);
        }
    }

    /*
     * Set src and percent String.
     */
    private void setResources() {
        if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            src = R.drawable.brightness_auto;
            percent = "AUTO";
        } else {
            percent = Utils.calculatePercent(brightness) + "%";
            if (brightness<=85)
                src = R.drawable.low;
            else if (brightness>85 && brightness<=170)
                src = R.drawable.medium;
            else
                src = R.drawable.high;
        }
    }
}
