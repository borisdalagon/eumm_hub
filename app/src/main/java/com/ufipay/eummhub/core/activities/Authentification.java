package com.ufipay.eummhub.core.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.fragments.FragmentConnexion;
import com.ufipay.eummhub.core.fragments.FragmentOtp;
import com.ufipay.eummhub.core.fragments.FragmentQuestionReponse;
import com.ufipay.eummhub.core.utils.Utils;

public class Authentification extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        FragmentConnexion.FragmentConnexionListener,
        FragmentOtp.FragmentOtpListener,
        FragmentQuestionReponse.FragmentQuestionReponseListener {


    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    FrameLayout fragmentContainer;
    Fragment fragment;
    FragmentTransaction ft;

    ImageView titreLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            isUserFirstTime = Boolean.parseBoolean(Utils.readSharedSetting(Authentification.this, PREF_USER_FIRST_TIME, "true"));

            if (isUserFirstTime) {
                Intent introIntent = new Intent(Authentification.this, Pager.class);
                introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
                startActivity(introIntent);

            }

            setContentView(R.layout.activity_authentification);
        }

        fragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);


        //Getting the instance of Spinner and applying OnItemSelectedListener on it



        init();


    }


    public void init(){

        fragment = new FragmentConnexion();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commitNowAllowingStateLoss();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onInputConnexionSent(CharSequence input) {

        if (input.equals("fragment_connexion")) {

            //fragmentContainer.setVisibility(View.GONE);


        }

    }


    @Override
    public void onInputOtpSent(CharSequence input) {


    }

    @Override
    public void onInputQuestionReponseSent(CharSequence input) {
        if (input.equals("fragment_Question_reponse")) {

       Utils.openNewActivity(Authentification.this, MainActivity.class);
       finish();

        }
    }
}