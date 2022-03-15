package com.ufipay.eummhub.client.transfertargent.compteverscash;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.transfertargent.TransfertArgent;
import com.ufipay.eummhub.client.transfertargent.compteverscash.fragment.FragmentTransfertCompteCash;
import com.ufipay.eummhub.client.transfertargent.compteverscash.fragment.FragmentTransfertCompteCashRecap;
import com.ufipay.eummhub.core.bottomsheet.ImportContact;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

public class EnvoisCompteVersCash extends AppCompatActivity implements
        View.OnClickListener,
        FragmentTransfertCompteCash.FragmentTransfertCompteCashListener,
        FragmentTransfertCompteCashRecap.FragmentTransfertCompteCashRecapListener,
        ImportContact.ImportContactListener {

    ImageView back;

    FrameLayout fragmentContainer;
    Fragment fragment;
    FragmentTransaction ft;

    String PAGE_TO_BACK = "";

    Fragment frag1 = new FragmentTransfertCompteCash();

    TextView titrePageCompteCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert_compte_vers_cash);

        titrePageCompteCash = (TextView) findViewById(R.id.titrePageCompteCash);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        fragment = new FragmentTransfertCompteCash();
        init(fragment);
    }

    public void init(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_transfert_compte_cash, frgmt);
        ft.commitNowAllowingStateLoss();

    }

    public void openBack(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
        ft.replace(R.id.fragment_container_transfert_compte_cash, frgmt);
        //ft.commitNowAllowingStateLoss();
        // ft.addToBackStack(null);
        ft.commit();

    }

    public void actionButtonBack(){

        if(PAGE_TO_BACK.equals("Page_formulaire")){
            openBack(frag1);
            PAGE_TO_BACK ="";
        }
        else {
            SharedPrefManager.newInstance(EnvoisCompteVersCash.this).setPhoneNumberTransfert(null);
            SharedPrefManager.newInstance(EnvoisCompteVersCash.this).setAmountTransfert(null);
            SharedPrefManager.newInstance(EnvoisCompteVersCash.this).setNomContactTransfert(null);
            Utils.openNewActivity(EnvoisCompteVersCash.this, TransfertArgent.class);
            overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        actionButtonBack();
    }

    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorSentCashToMob = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSentCashToMob
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {

                            case R.id.back:

                                actionButtonBack();

                                break;
                            case R.id.close:
                                //nothing to do for the moment
                                break;


                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do here
                    }
                } )
                .withLayer()
                .start();

    }


    @Override
    public void onInputTransfertCompteCashSent(CharSequence input) {
        if (input.equals("Page_formulaire")) {

            PAGE_TO_BACK = "Page_formulaire";

        }
    }

    @Override
    public void onInputTransfertCompteCashRecapSent(CharSequence input) {

    }

    @Override
    public void onInputImportContactSent(CharSequence input) {

    }
}