package com.ufipay.eummhub.client.payerfactures.camwater;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.payerfactures.PayerFactures;
import com.ufipay.eummhub.client.payerfactures.camwater.bottomsheet.ListSaveCamwaterAbonnee;
import com.ufipay.eummhub.client.payerfactures.camwater.fragment.FragmentCamwaterFirstPage;
import com.ufipay.eummhub.client.payerfactures.camwater.fragment.FragmentCamwaterImpaye;
import com.ufipay.eummhub.client.payerfactures.camwater.fragment.FragmentCamwaterRecap;
import com.ufipay.eummhub.core.utils.Utils;

public class PayerFactureCamwater extends AppCompatActivity implements
        FragmentCamwaterFirstPage.FragmentCamwaterFirstPageListener,
        FragmentCamwaterRecap.FragmentCamwaterRecapListener,
        FragmentCamwaterImpaye.FragmentCamwaterImpayeListener,
        ListSaveCamwaterAbonnee.ListSaveCamwaterAbonneeListener,
        View.OnClickListener {

    ImageView back;

    Fragment fragmentFistPage;
    Fragment fragmentImpayeFacture;
    FragmentTransaction ft;

    String PAGE_TO_BACK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_facture_camwater);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        fragmentFistPage = new FragmentCamwaterFirstPage();
        fragmentImpayeFacture = new FragmentCamwaterImpaye();
        init(fragmentFistPage);

    }

    public void init(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_camwater, frgmt);
        ft.commitNowAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorMainPayCamwaterClick = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorMainPayCamwaterClick
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
    public void onBackPressed() {
        actionButtonBack();
    }

    public void openBack(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
        ft.replace(R.id.fragment_container_camwater, frgmt);
        ft.commit();

    }

    public void actionButtonBack(){

        if(PAGE_TO_BACK.equals("Page_formulaire")){
            openBack(fragmentFistPage);
            PAGE_TO_BACK ="";
        }else if(PAGE_TO_BACK.equals("page_impaye_facture")){
            openBack(fragmentImpayeFacture);
            PAGE_TO_BACK ="Page_formulaire";
        }
        else {

            Utils.openNewActivity(PayerFactureCamwater.this, PayerFactures.class);
            overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
            finish();
        }
    }



    @Override
    public void onInputCamwaterFirstPageSent(CharSequence input) {

        if(input.equals("Page_formulaire")){
            PAGE_TO_BACK = "Page_formulaire";
        }

    }

    @Override
    public void onInputCamwaterRecapSent(CharSequence input) {

    }

    @Override
    public void onInputCamwaterImpayeSent(CharSequence input) {

        if(input.equals("page_impaye_facture")){
            PAGE_TO_BACK = "page_impaye_facture";
        }
    }

    @Override
    public void onInputListSaveCamwaterAbonneeSent(CharSequence input) {
        if(input.equals("Page_formulaire")){
            PAGE_TO_BACK = "Page_formulaire";
        }

    }
}