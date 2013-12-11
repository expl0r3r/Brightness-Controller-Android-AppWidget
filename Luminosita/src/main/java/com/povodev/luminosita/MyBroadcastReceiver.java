package com.povodev.luminosita;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

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


public class MyBroadcastReceiver extends android.content.BroadcastReceiver {

    // the brightness increment/decrement for every
    // intent received with actions MINUS or PLUS
    private int increment=25;

    // current brightness value
    private int brightness;

    // mode -> 0 manual / 1 auto
    private int mode;

    // appwidget src
    private int src;

    //Log TAG
    private String TAG = Utils.TAG;

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;

    String action;
    String percent;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Get the content resolver
        cResolver = context.getContentResolver();

        action = intent.getAction();

        try {
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
            mode = Settings.System.getInt(cResolver,Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system mode & brightness");
            e.printStackTrace();
        }

        Log.d(TAG,"mode [0manual|1auto]: " + mode);
        if (mode==0)
            Log.d(TAG,"Brightness before changes: " + brightness);

        // PLUS button has been pressed. Process it!
        if (action.equals(Utils.PLUS)){

            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
                setSrc(brightness);
                // change mode to manual before setting brightness
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
            if (brightness<=235){
                percent = Utils.calculatePercent(brightness+increment) + "%";
                setBrightness(brightness+increment);
                setSrc(brightness);
                Utils.updateWidget(context, brightness, src, percent);
            } else {
                setBrightness(255);
                percent = "100%";
                setSrc(brightness);
                Utils.updateWidget(context, brightness, src, percent);
            }


        }
        // MINUS button has been pressed. Process it!
        else if (action.equals(Utils.MINUS)){

            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
                setSrc(brightness);
                // change mode to manual before setting brightness
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }

            if (brightness>=20){
                percent = Utils.calculatePercent(brightness-increment) + "%";
                setBrightness(brightness-increment);
                setSrc(brightness);
                Utils.updateWidget(context, brightness, src, percent);
            } else {
                setBrightness(0);
                percent = Utils.calculatePercent(brightness) + "%";
                setSrc(brightness);
                Utils.updateWidget(context, brightness, src, percent);
            }

        }
        // CHANGE button has been pressed. Process it!
        else if (action.equals(Utils.CHANGE)) {

            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
                setSrc(brightness);
                percent = Utils.calculatePercent(brightness) + "%";
                // change mode to manual
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            } else {
                Toast toast = Toast.makeText(context,context.getResources().getString(R.string.toast_text),Toast.LENGTH_LONG);
                toast.show();
                src = R.drawable.brightness_auto;
                percent = "AUTO";
                // change mode to automatic
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
            Utils.updateWidget(context, brightness, src, percent);
        }
    }

    

    /*
     * Set system brightness
     * @param: brightness value
     */
    private void setBrightness(int brightness) {
        this.brightness=brightness;
        this.brightness = Utils.calculateExactBrightness(brightness);
        //Set the system brightness using the brightness variable value
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        Log.d(TAG,"Brightness after changes: " + this.brightness);
    }

    /*
     *  Set the src that will displayed by the appwidget
     *  @param: brightness value
     */
    public void setSrc(int brightness){
        if (brightness<=85)
            src = R.drawable.low;
        else if (brightness>85 && brightness<=170)
            src = R.drawable.medium;
        else
            src = R.drawable.high;
    }
}
