package com.ufipay.eummhub.client.payerfactures.canalplus;

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
import com.ufipay.eummhub.client.payerfactures.canalplus.fragment.FragmentCanalplusFirstPage;
import com.ufipay.eummhub.client.payerfactures.canalplus.fragment.FragmentCanalplusOffres;
import com.ufipay.eummhub.core.utils.Utils;

public class PayerFactureCanalPlus extends AppCompatActivity implements
        FragmentCanalplusFirstPage.FragmentCanalplusFirstPageListener,
        FragmentCanalplusOffres.FragmentCanalplusOffreListener,
        View.OnClickListener{

    ImageView back;
    FragmentTransaction ft;

    Fragment fragmentFistPage;
    Fragment fragmentOffre;
    String PAGE_TO_BACK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_facture_canal_plus);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        fragmentFistPage = new FragmentCanalplusFirstPage();
        fragmentOffre = new FragmentCanalplusOffres();
        init(fragmentFistPage);

    }

    public void init(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_canalplus, frgmt);
        ft.commitNowAllowingStateLoss();
    }

    public void openBack(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
        ft.replace(R.id.fragment_container_canalplus, frgmt);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        actionButtonBack();
    }

    public void actionButtonBack(){

        if(PAGE_TO_BACK.equals("Page_formulaire")){
            openBack(fragmentFistPage);
            PAGE_TO_BACK ="";
        }else if(PAGE_TO_BACK.equals("Page_offre")){
            openBack(fragmentOffre);
            PAGE_TO_BACK ="Page_formulaire";
        }
        else {

            Utils.openNewActivity(PayerFactureCanalPlus.this, PayerFactures.class);
            overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorPayCanal = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorPayCanal
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
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
    public void onInputCanalplusFirstPageSent(CharSequence input) {

        if(input.equals("Page_formulaire")){

            PAGE_TO_BACK = "Page_formulaire";

        }

    }

    @Override
    public void onInputCanalplusOffreSent(CharSequence input) {

        if(input.equals("Page_offre")){

            PAGE_TO_BACK = "Page_offre";

        }

    }
}