package com.ufipay.eummhub.core.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.adapters.AdapterFlag;
import com.ufipay.eummhub.core.utils.Utils;

import java.util.Locale;


public class FragmentConnexion extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentConnexionListener listener;

    Button valider;

    private String[] countries = {"Cameroun","Gabon","Congo", "Tchad", "RDC"};

    private int flags[] = {R.drawable.flag_cm,R.drawable.flag_ga,R.drawable.flag_cg,R.drawable.flag_td, R.drawable.flag_cf};


    String codeISO2pays;
    String pays;
    String codePaysSelect;
    String devisePaysSelect;
    String codePaysTelephone;

    RelativeLayout blocContenu;

    LinearLayout blocLoader;
    TashieLoader loader;

    TextView txtAppelSav;

    //
    public FragmentConnexion(){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface FragmentConnexionListener {
        void onInputConnexionSent(CharSequence input);
    }


    //
    public static FragmentConnexion newInstance(){

        return new FragmentConnexion();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate( R.layout.fragment_connexion, container, false );

        blocContenu = (RelativeLayout) v.findViewById(R.id.bloc_contenu);
        blocLoader = (LinearLayout) v.findViewById(R.id.bloc_loader);
        loader = (TashieLoader) v.findViewById(R.id.loader);
        loader.setOnClickListener(this);
        txtAppelSav = (TextView) v.findViewById(R.id.txt_appel_sav);
        txtAppelSav.setOnClickListener(this);

        valider = (Button)v.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        Spinner spinner = (Spinner)v.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        AdapterFlag customAdapter = new AdapterFlag(getContext(), flags, countries);
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){

                    //  codePays.setText("+237");
                    codePaysSelect = "237";
                    codePaysTelephone = "237";
                    codeISO2pays = "CM";
                    pays = "CAMEROON";
                    devisePaysSelect = "XAF";

                }
                if (position==1){

                    //codePays.setText("+241");
                    codePaysSelect = "241";
                    codePaysTelephone = "241";
                    codeISO2pays = "GA";
                    pays = "GABON";
                    devisePaysSelect = "XAF";
                }
                if (position==2){

                    //codePays.setText("+242");
                    codePaysSelect = "242";
                    codePaysTelephone = "242";
                    codeISO2pays = "CG";
                    pays = "CONGO";
                    devisePaysSelect = "XAF";

                }
                if (position==3){

                    // codePays.setText("+235");
                    codePaysSelect = "235";
                    codePaysTelephone = "235";
                    codeISO2pays = "TD";
                    pays = "CHAD";
                    devisePaysSelect = "XAF";
                }
                if (position==4){

                    //codePays.setText("+236");
                    codePaysSelect = "236";
                    codePaysTelephone = "236";
                    codeISO2pays = "CF";
                    pays = "CAR";
                    devisePaysSelect = "XAF";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        if (context instanceof FragmentConnexionListener) {
            listener = (FragmentConnexionListener) context;
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


    public void rentreFragmentEnvoiCashMobile() {

      /*  Fragment n = new FragmentEnvoiCashCashh();
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
            ft.replace( R.id.fragment_container, n );
            ft.commit();
        } */
    }

    public void ouvrirFragmentOtp() {

        Fragment n = new FragmentOtp();
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

                               // listener.onInputConnexionSent( "fragment_connexion" );

                                /*
                                blocContenu.setVisibility(View.GONE);
                                blocLoader.setVisibility(View.VISIBLE);

                                TashieLoader tashie = new TashieLoader(
                                        getContext(), 5,
                                        30, 10,
                                        ContextCompat.getColor(getContext(), R.color.colorEU));

                                tashie.setAnimDuration(500);
                                tashie.setAnimDelay(100);
                                tashie.setInterpolator(new LinearInterpolator());
                                */

                                ouvrirFragmentOtp();


                                break;
                            case R.id.txt_appel_sav:

                                   blocContenu.setVisibility(View.GONE);
                                   blocLoader.setVisibility(View.VISIBLE);

                                   TashieLoader tashie = new TashieLoader(
                                           getContext(), 5,
                                           30, 10,
                                           ContextCompat.getColor(getContext(), R.color.colorEU));

                                   tashie.setAnimDuration(500);
                                   tashie.setAnimDelay(100);
                                   tashie.setInterpolator(new LinearInterpolator());

                                break;


                               case R.id.loader:

                                   blocContenu.setVisibility(View.VISIBLE);
                                   blocLoader.setVisibility(View.GONE);






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
