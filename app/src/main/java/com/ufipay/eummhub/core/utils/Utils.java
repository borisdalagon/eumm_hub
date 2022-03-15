package com.ufipay.eummhub.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.data.Country;

import java.util.ArrayList;
import java.util.List;

import umairayub.madialog.MaDialog;


public class Utils {

    public static final String BACK_TO_MAIN = "BACK_MAIN";
    public static final String COLOR_APPROX_BLUE = "#213783";
    public static final String COLOR_BLACK = "#000000";

    private static final String[] countriesEUMM = {"Cameroun","Gabon","Congo", "Tchad", "RDC"};
    private static final int[] flagsCountriesEUMM = {R.drawable.flag_cm,R.drawable.flag_ga,R.drawable.flag_cg,R.drawable.flag_td, R.drawable.flag_cf};
    private static List<Country> countriesArrayEUMM;


    private static final String PREFERENCES_FILE = "materialsample_settings";
    public static final String TAG = "TAG";
    public static final String NUMERO_CONTACT = "NUMERO_CONTACT";
    public static final String OK = "OK";
    public static final String NON_OK = "NON_OK";
    public static final String PHONE_TRANSFERT_ARGENT = "PHONE_TRANSFERT_ARGENT";
    public static final String MONTANT_TRANSFERT_ARGENT = "MONTANT_TRANSFERT_ARGENT";
    public static final String MOTIF_TRANSFERT_ARGENT = "MOTIF_TRANSFERT_ARGENT";
    public static final String TRANSFERT_ARGENT = "TRANSFERT_ARGENT";
    public static final String PAGE_POUR_CODE = "PAGE_POUR_CODE";
    public static final String DEMANDE_DE_SOLDE = "DEMANDE_DE_SOLDE";
    public static final String TRANSFERT_COMPTE_COMPTE = "TRANSFERT_COMPTE_COMPTE";
    public static final String TRANSFERT_CASH_VERS_MOBILE = "TRANSFERT_CASH_VERS_MOBILE";
    public static final String ENEO_CUSTOMER_BILLS = "ENEO_CUSTOMER_BILLS";
    public static final String TRANSFERT_COMPTE_VERS_CASH = "TRANSFERT_COMPTE_VERS_CASH";
    public static final String PAYER_FACTURE_ENEO = "PAYER_FACTURE_ENEO";
    public static final String PAYER_FACTURE_CAMWATER = "PAYER_FACTURE_CAMWATER";
    public static final String PAYER_FACTURE_CANALPLUS = "PAYER_FACTURE_CANALPLUS";
    public static final String RAPPORT_REQUEST = "RAPPORT_REQUEST";
    public static final String NUMERO_ABONNEE = "NUMERO_ABONNEE";
    public static final String OPERATION_REUSSIE = "OPERATION_REUSSIE";
    public static final String OPERATION_ECHOUE = "OPERATION_ECHOUE";
    public static final String REQUEST_POUR_AUTHENTICATION = "REQUEST_POUR_AUTHENTICATION";
    public static final String REQUEST_POUR_NON_AUTHENTICATION = "REQUEST_POUR_NON_AUTHENTICATION";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";

    public static final String AUTHENFICATION_REUSSIE = "AUTHENFICATION_REUSSIE";

    private static final String[] motifs = {"( Motif du transfert)","Approvisionnement compte",
            "Envoi / Reception de fonds","Dons fournis / Reçus",
            "Envoi des fonds des travailleurs","Frais de mission",
            "Frais de scolarité",
            "Frais de voyages médicaux",
            "Frais de voyages touristiques",
            "Autres..."};

    private static final int[] figurantMotifs = {R.drawable.ic_back_left_0,R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0,R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0,R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0,R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0,R.drawable.ic_back_left_0};


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

    public static ViewPropertyAnimatorCompat getViewPropertyAnimatorCompat(View v){
        return ViewCompat.animate(v).setDuration(200).scaleX(0.9f).scaleX(0.9f).setInterpolator( new CycleInterpolator() );
    }

    public static int[] getFlagCountriesEUMM(){
        return flagsCountriesEUMM;
    }

    public static String[] getCountriesEUMM(){
        return countriesEUMM;
    }

    public static MaDialog.Builder getDialogBuilder(Context context, String title, String message, String positiveB, String negativeB){
        return getPositiveOnlyDialogBuilder(context, title, message, positiveB)
                .setNegativeButtonText(negativeB);
    }

    public static MaDialog.Builder getPositiveOnlyDialogBuilder(Context context, String title, String message, String positiveB){
        return new MaDialog.Builder(context)
                .setCancelableOnOutsideTouch(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText(positiveB)
                .setBackgroundColor( R.color.colorBlancheS)
                .setButtonTextColor(Color.parseColor(COLOR_APPROX_BLUE) )
                .setTitleTextColor( Color.parseColor(COLOR_APPROX_BLUE) )
                .setMessageTextColor( Color.parseColor(COLOR_BLACK) )
                .setButtonOrientation( LinearLayout.HORIZONTAL);
    }

    public static String getMotifSelectedAt(int position){
        if (position==0 || position==1){
            return motifs [1];
        }
        return motifs [position];
    }

    public static String[] getMotifs(){
        return motifs;
    }

    public  static int[] getFigurantMotifs(){
        return figurantMotifs;
    }

    public static int setEummCountriesArray(){

        //if countries contains data ==> we ain't need to add something, as we're initializing it one !
        if (countriesArrayEUMM!=null && !countriesArrayEUMM.isEmpty())
            return countriesArrayEUMM.size();

        countriesArrayEUMM = new ArrayList<>();

        Country cm = new Country();
        Country gabon = new Country();
        Country congo = new Country();
        Country chad = new Country();
        Country car = new Country();


        cm.setCodeISO2("CM");
        cm.setDevise("XAF");
        cm.setName("CAMEROON");
        cm.setPhoneCode("237");
        countriesArrayEUMM.add(cm);

        gabon.setCodeISO2("GA");
        gabon.setDevise("XAF");
        gabon.setName("GABON");
        gabon.setPhoneCode("241");
        countriesArrayEUMM.add(gabon);

        congo.setCodeISO2("CG");
        congo.setDevise("XAF");
        congo.setName("CONGO");
        congo.setPhoneCode("242");
        countriesArrayEUMM.add(congo);

        chad.setName("TCHAD");
        chad.setPhoneCode("235");
        chad.setDevise("XAF");
        chad.setCodeISO2("TD");
        countriesArrayEUMM.add(chad);

        car.setName("RCA");
        car.setPhoneCode("236");
        car.setDevise("XAF");
        car.setCodeISO2("CF");
        countriesArrayEUMM.add(car);


        return countriesArrayEUMM.size();
    }

    public static List<Country> getCountriesArrayEUMM(){
        return countriesArrayEUMM;
    }

    public static Country getCountrySelectedAt(int position){
        return countriesArrayEUMM.get(position);
    }

    /**
     * Useful for the like account to name, where the name shouldn't contains special characters
     * @param stringToCompute a string that may containing special characters
     * @return a string without special character
     */

    public static String replaceSpecialCharacter(String stringToCompute){
        String computedString = stringToCompute.toLowerCase().replace("¨", "");

        computedString = computedString.replace("^", "");
        computedString = computedString.replace("$", "");
        computedString = computedString.replace("£", "");
        computedString = computedString.replace("¤", "");
        computedString = computedString.replace("%", "");
        computedString = computedString.replace("µ", "");
        computedString = computedString.replace("*", "");
        computedString = computedString.replace("§", "");
        computedString = computedString.replace("!", "");
        computedString = computedString.replace("/", "");
        computedString = computedString.replace(":", "");
        computedString = computedString.replace(";", "");
        computedString = computedString.replace("?", "");
        computedString = computedString.replace(",", "");
        computedString = computedString.replace("<", "");
        computedString = computedString.replace(">", "");
        computedString = computedString.replace("&", "");
        computedString = computedString.replace("~", "");
        computedString = computedString.replace("#", "");
        computedString = computedString.replace("'", "");
        computedString = computedString.replace("\"", "");
        computedString = computedString.replace("{", "");
        computedString = computedString.replace("(", "");
        computedString = computedString.replace("[", "");
        computedString = computedString.replace("|", "");
        computedString = computedString.replace("-", "");
        computedString = computedString.replace("`", "");
        computedString = computedString.replace("°", "");
        computedString = computedString.replace("\\", "");
        computedString = computedString.replace("_", "");
        computedString = computedString.replace("^", "");
        computedString = computedString.replace("+", "");
        computedString = computedString.replace("=", "");

        computedString = computedString.replace(")", "");
        computedString = computedString.replace("}", "");
        computedString = computedString.replace("]", "");
        computedString = computedString.replace("ç", "c");

        computedString = computedString.replace("ù", "u");

        computedString = computedString.replace("€", "e");
        computedString = computedString.replace("ê", "e");
        computedString = computedString.replace("ë", "e");
        computedString = computedString.replace("é", "e");
        computedString = computedString.replace("è", "e");

        computedString = computedString.replace("â", "a");
        computedString = computedString.replace("ä", "a");
        computedString = computedString.replace("@", "a");
        computedString = computedString.replace("à", "a");

        computedString = computedString.replace("î", "i");
        computedString = computedString.replace("ï", "i");

        computedString = computedString.replace("û", "u");
        computedString = computedString.replace("ü", "u");

        computedString = computedString.replace("ô", "o");
        computedString = computedString.replace("ö", "o");

        computedString = computedString.replace("ÿ", "y");

        computedString = computedString.replace(" ", ".");

        return  computedString.toUpperCase();
    }


}
