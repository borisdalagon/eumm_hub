package com.ufipay.eummhub.client.payerfactures.eneo;

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
import com.ufipay.eummhub.client.payerfactures.eneo.bottomsheet.ListSaveEneoAbonnee;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoFirstPage;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoListFacture;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoRecap;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

public class PayerFactureEneo extends AppCompatActivity implements
        View.OnClickListener,
        FragmentEneoFirstPage.FragmentEneoFirstPageListener,
        FragmentEneoRecap.FragmentEneoRecapListener,
        FragmentEneoListFacture.FragmentEneoListFactureListener,
        ListSaveEneoAbonnee.ListSaveEneoAbonneeListener {

    ImageView back;

    Fragment fragmentFistPage;
    Fragment fragmentListFacture;
    FragmentTransaction ft;

    String PAGE_TO_BACK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_facture_eneo);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        fragmentFistPage = new FragmentEneoFirstPage();
        fragmentListFacture = new FragmentEneoListFacture();
        init(fragmentFistPage);
    }

    public void init(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_eneo, frgmt);
        ft.commitNowAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorMainPayEneoClick = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorMainPayEneoClick
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
        ft.replace(R.id.fragment_container_eneo, frgmt);
        ft.commit();

    }

    public void actionButtonBack(){

        if(PAGE_TO_BACK.equals("Page_formulaire")){
            openBack(fragmentFistPage);
            PAGE_TO_BACK ="";
        }else if(PAGE_TO_BACK.equals("page_list_facture")){
            openBack(fragmentListFacture);
            PAGE_TO_BACK ="Page_formulaire";
        }
        else {
            SharedPrefManager.newInstance(PayerFactureEneo.this).setPhoneNumberTransfert(null);
            SharedPrefManager.newInstance(PayerFactureEneo.this).setAmountTransfert(null);
            SharedPrefManager.newInstance(PayerFactureEneo.this).setNomContactTransfert(null);
            Utils.openNewActivity(PayerFactureEneo.this, PayerFactures.class);
            overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
            finish();
        }
    }

    @Override
    public void onInputEneoFirstPageSent(CharSequence input) {

        if(input.equals("Page_formulaire")){
            PAGE_TO_BACK = "Page_formulaire";
        }

    }

    @Override
    public void onInputEneoRecapSent(CharSequence input) {
        //nothing to do here
    }

    @Override
    public void onInputEneoListFactureSent(CharSequence input) {
        if(input.equals("page_list_facture")){
            PAGE_TO_BACK = "page_list_facture";
        }
    }

    @Override
    public void onInputListSaveEneoAbonneeSent(CharSequence input) {
        if(input.equals("Page_formulaire")){
            PAGE_TO_BACK = "Page_formulaire";
        }

    }
}