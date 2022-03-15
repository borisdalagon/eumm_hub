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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.adapters.AdapterFlag2;
import com.ufipay.eummhub.core.utils.Utils;

import java.util.Locale;


public class FragmentQuestionReponse extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentQuestionReponseListener listener;

    Button valider;
    TextView titreCode;

    Spinner mSpinner;

    // Liste des motifs
    private String[] listQuestion = {"( Choisir une question  )",
            "Quel est votre fruit préféré ?",
            "Quel est votre animal préféré ?",
            "Quel est votre boisson préféré ?",
            "Quel est votre plat préféré ?",
            "Quel est votre sport préféré ?",
            "Qui est votre chanteur préféré ?",
            "Qui est votre serie préféré ?",
            "Qui est votre chanson préféré ?"};

    private int figurant2[] = {R.drawable.ic_back_left_0, R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0, R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0, R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0, R.drawable.ic_back_left_0,
            R.drawable.ic_back_left_0};
    private String motifSelect = "";


    TextView question_text;

    String stCustomerType;

    private String selectedQuestion = "";
    private String toQuestionFromOTP; // if not null then open main menu on next, else go OTP fragment



    //
    public FragmentQuestionReponse(){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface FragmentQuestionReponseListener {
        void onInputQuestionReponseSent(CharSequence input);
    }


    //
    public static FragmentQuestionReponse newInstance(){

        return new FragmentQuestionReponse();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate( R.layout.fragment_question_secrete, container, false );

        valider = (Button)v.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        titreCode = (TextView) v.findViewById(R.id.titre_code);
        titreCode.setOnClickListener(this);

        mSpinner = (Spinner)v.findViewById( R.id.spinner_question );

        spinnerQuestion();

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


    public void spinnerQuestion(){

        //Liste de questions
        AdapterFlag2 customAdapterMotif = new AdapterFlag2(getContext(), figurant2, listQuestion);
        mSpinner.setAdapter(customAdapterMotif);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //METHOD 1: Get text from selected item's position & set it to TextView
                //METHOD 2: Get the position of item selected, & perform specific task

                if (position == 1)
                    selectedQuestion = "Quel est votre fruit préféré ?";
                else if (position == 2)
                    selectedQuestion =  "Quel est votre animal préféré ?";
                else if (position == 3)
                    selectedQuestion = "Quel est votre boisson préféré ?";
                else if (position == 4)
                    selectedQuestion = "Quel est votre plat préféré ?";
                else if (position == 5)
                    selectedQuestion = "Quel est votre sport préféré ?";
                else if (position == 6)
                    selectedQuestion = "Qui est votre chanteur préféré ?";
                else if (position == 7)
                    selectedQuestion = "Qui est votre serie préféré ?";
                else if (position == 8)
                    selectedQuestion = "Qui est votre chanson préféré ?";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    // Gestion des écoutes

    public void updateEditText(CharSequence newText) {
        // editText.setText(newText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentQuestionReponseListener) {
            listener = (FragmentQuestionReponseListener) context;
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

        Fragment n = new FragmentOtp();
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
            ft.replace( R.id.fragment_container, n );
            ft.commit();
        }
    }

    public void ouvrirFragmentPayerFactureEneo() {

       /* Fragment n = new FragmentEneoPayerFacture();
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.enter, R.anim.exit );
            ft.replace( R.id.fragment_container, n );
            ft.commit();
        } */
    }



    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimator = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimator
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {


                            case R.id.valider:

                                listener.onInputQuestionReponseSent( "fragment_Question_reponse" );

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
                        //nothing to do
                    }
                } )
                .withLayer()
                .start();

    }



}
