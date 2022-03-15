package com.ufipay.eummhub.core.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.utils.Utils;

import java.util.Locale;


public class FragmentOtp extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentOtpListener listener;

    Button valider;
    TextView titreCode;

    //
    public FragmentOtp(){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface FragmentOtpListener {
        void onInputOtpSent(CharSequence input);
    }


    //
    public static FragmentOtp newInstance(){

        return new FragmentOtp();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate( R.layout.fragment_otp, container, false );

        valider = (Button)v.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        titreCode = (TextView) v.findViewById(R.id.titre_code);
        titreCode.setOnClickListener(this);

        return v;

    }

    public void setLocale(String lang, Context context){

        Locale locale = new Locale(lang);
        locale.setDefault(locale);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(locale);

            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }else {
            Configuration config = new Configuration();
            config.locale = locale;

            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }


    }


    // Gestion des écoutes

    public void updateEditText(CharSequence newText) {
        // editText.setText(newText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentOtpListener) {
            listener = (FragmentOtpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********


    public void rentreFragment() {

        Fragment n = new FragmentConnexion();
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
            ft.replace( R.id.fragment_container, n );
            ft.commit();
        }
    }

    public void ouvrirFragment() {

        Fragment n = new FragmentQuestionReponse();
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.enter, R.anim.exit );
            ft.replace( R.id.fragment_container, n );
            ft.commit();
        }

    }



    @Override
    public void onClick(final View v) {

        ViewCompat.animate( v )
                .setDuration( 200 )
                .scaleX( 0.9f )
                .scaleY( 0.9f )
                .setInterpolator( new Utils.CycleInterpolator() )
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                        Log.d( "","" );

                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {


                            case R.id.valider:

                                //listener.onInputOtpSent( "fragment_otp" );

                                ouvrirFragment();

                                break;

                             case R.id.titre_code:

                                 rentreFragment();

                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                        Log.d("","");
                    }
                } )
                .withLayer()
                .start();

    }




}
