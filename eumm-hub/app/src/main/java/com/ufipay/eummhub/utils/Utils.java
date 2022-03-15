package com.ufipay.eummhub.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.graphics.drawable.DrawableCompat;

import com.ufipay.eummhub.R;


public class Utils {


    private static final String PREFERENCES_FILE = "materialsample_settings";
    public static final String TAG = "TAG";

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query( contentUri, proj, null, null, null );


            int columnIndex = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cursor.moveToFirst();
            return cursor.getString( columnIndex );

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public static int getToolbarHeight(Context context) {
        int height = (int) context.getResources().getDimension( R.dimen.abc_action_bar_default_height_material );
        return height;
    }

    public static int getStatusBarHeight(Context context) {
        int height = (int) context.getResources().getDimension( R.dimen.statusbar_size );
        return height;
    }


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap( drawable );
        DrawableCompat.setTint( drawable, color );
        DrawableCompat.setTintMode( drawable, PorterDuff.Mode.SRC_IN );
        return drawable;
    }


    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences( PREFERENCES_FILE, Context.MODE_PRIVATE );
        return sharedPref.getString( settingName, defaultValue );
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences( PREFERENCES_FILE, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString( settingName, settingValue );
        editor.apply();
    }



    public static boolean isValidSecretCode(String code){
        return code.matches("[0-9]{4}");
    }


    public static void openNewActivity(Context context, Class<?> cls){

        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }

    public static class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin( 2.0f * mCycles * Math.PI * input );
        }
    }



}
