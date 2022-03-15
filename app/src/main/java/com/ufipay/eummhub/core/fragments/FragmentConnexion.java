package com.ufipay.eummhub.core.fragments;

import android.content.Context;
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
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.google.android.material.textfield.TextInputEditText;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.adapters.AdapterFlag;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.core.utils.navigation.CoreNavigator;


public class FragmentConnexion extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentConnexionListener listener;

    Button valider;

    private final String[] countries = {"Cameroun","Gabon","Congo", "Tchad", "RDC"};

    private final int[] flags = {R.drawable.flag_cm,R.drawable.flag_ga,R.drawable.flag_cg,R.drawable.flag_td, R.drawable.flag_cf};


    String codeISO2pays;
    String pays;
    String codePaysSelect;
    String devisePaysSelect;
    String codePaysTelephone;

    RelativeLayout blocContenu;

    LinearLayout blocLoader;
    TashieLoader loader;

    TextView txtAppelSav;
    private TextInputEditText accountNumberEditor;
    private TextInputEditText accountPinEditor;
    private SharedPrefManager preferencesConnection;
    private static final String TAG = "fgConn";

    //
    public FragmentConnexion(){
        //required an empty constructor
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//nothing to do
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//nothing to do
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

        preferencesConnection = SharedPrefManager.newInstance(getContext());

        accountNumberEditor = v.findViewById(R.id.account_number);
        accountPinEditor = v.findViewById(R.id.account_pin_code);

        blocContenu = v.findViewById(R.id.bloc_contenu);
        blocLoader = v.findViewById(R.id.bloc_loader);
        loader = v.findViewById(R.id.loader);
        loader.setOnClickListener(this);
        txtAppelSav = v.findViewById(R.id.txt_appel_sav);
        txtAppelSav.setOnClickListener(this);

        valider = v.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        Spinner spinner = v.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        AdapterFlag customAdapter = new AdapterFlag(getContext(), flags, countries);
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){

                    codePaysSelect = "237";
                    codePaysTelephone = "237";
                    codeISO2pays = "CM";
                    pays = "CAMEROON";
                    devisePaysSelect = "XAF";

                }
                if (position==1){

                    codePaysSelect = "241";
                    codePaysTelephone = "241";
                    codeISO2pays = "GA";
                    pays = "GABON";
                    devisePaysSelect = "XAF";
                }
                if (position==2){

                    codePaysSelect = "242";
                    codePaysTelephone = "242";
                    codeISO2pays = "CG";
                    pays = "CONGO";
                    devisePaysSelect = "XAF";

                }
                if (position==3){

                    codePaysSelect = "235";
                    codePaysTelephone = "235";
                    codeISO2pays = "TD";
                    pays = "CHAD";
                    devisePaysSelect = "XAF";
                }
                if (position==4){

                    codePaysSelect = "236";
                    codePaysTelephone = "236";
                    codeISO2pays = "CF";
                    pays = "CAR";
                    devisePaysSelect = "XAF";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//nothing to do
            }
        });

        preferencesConnection.setRestUrl("http://192.168.2.8:8485");

        return v;

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


    @Override
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorConn = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorConn
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {


                            case R.id.valider:
                                String accNum = accountNumberEditor.getText().toString().trim();
                                String accPin = accountPinEditor.getText().toString().trim();

                                if (connectionFormValid(accNum, accPin)){
                                    preferencesConnection.setCodePaysTelephone(codePaysTelephone);
                                    preferencesConnection.setAccountNumber(codePaysTelephone.concat(accNum));
                                    preferencesConnection.setMpin(accPin);

                                    //Pour le test Espresso
                                    preferencesConnection.setJetonTest(Utils.NON_OK);
                                    CoreNavigator.openOtpFragment(getParentFragmentManager());
                                }


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
                        //nothing to do
                    }
                } )
                .withLayer()
                .start();

    }

    public  boolean connectionFormValid(String accountNum, String accountPin){
        if (accountNum.isEmpty()) {
            setConnectionAccountNumberError();
            return  false;
        }

        if (accountPin.length() != 4){
            setConnectionPinError();
            return false;
        }

        try {
            int testPin = Integer.parseInt(accountPin);
            Log.d(TAG, "valid integer 4 size as test "+testPin);
        }catch (NumberFormatException exception){
            Log.d(TAG, "connectionFormValid: "+exception.getLocalizedMessage(), exception);
            return false;
        }

        return true;
    }

    public void setConnectionAccountNumberError(){
        accountNumberEditor.setError("Numero de compte requis");
    }

    public void setConnectionPinError(){
        accountPinEditor.setError("Format du MPIN : 4 Chiffres");
    }


}
