package com.ufipay.eummhub.client.transfertcomptecompte;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.bottomsheet.ImportContact;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompte1;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompte2;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompteRecap;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

public class TransfertCompteCompte extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        FragmentTransfertCompteCompte1.FragmentTransfertCompteCompte1Listener,
        FragmentTransfertCompteCompte2.FragmentTransfertCompteCompte2Listener,
        FragmentTransfertCompteCompteRecap.FragmentTransfertCompteCompteRecapListener,
        ImportContact.ImportContactListener{

    ImageView back;

    FrameLayout fragmentContainer;
    Fragment fragment;
    FragmentTransaction ft;

    String PAGE_TO_BACK = "";

    Fragment frag1 = new FragmentTransfertCompteCompte1();
    Fragment frag2 = new FragmentTransfertCompteCompte2();

    TextView pageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert_compte_compte);

        pageName = findViewById(R.id.pageName);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        fragment = new FragmentTransfertCompteCompte1();
        init(fragment);

    }

    public void init(Fragment frgmt){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_transfert_compte_compte, frgmt);
        ft.commitNowAllowingStateLoss();

    }

    public void openBack(Fragment frgmt){
        Log.d("XXX_Test_2_1", PAGE_TO_BACK);
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
        ft.replace(R.id.fragment_container_transfert_compte_compte, frgmt);
        ft.commit();
        Log.d("XXX_Test_2_2", PAGE_TO_BACK);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //nothing to do here
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //nothing to do here
    }

    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorTransferClick = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorTransferClick
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
                                //nothing to do here
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

    public void actionButtonBack(){
        switch (PAGE_TO_BACK) {
            case "Numero_destinataire":
                openBack(frag1);
                PAGE_TO_BACK = "";
                break;
            case "Montant_transfert":
                openBack(frag2);
                PAGE_TO_BACK = "Numero_destinataire";
                break;
            default:
                SharedPrefManager.newInstance(TransfertCompteCompte.this).setPhoneNumberTransfert(null);
                SharedPrefManager.newInstance(TransfertCompteCompte.this).setAmountTransfert(null);
                SharedPrefManager.newInstance(TransfertCompteCompte.this).setNomContactTransfert(null);
                Utils.openNewActivity(TransfertCompteCompte.this, MainActivity.class);
                overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
                finish();
                break;

                //case "BACK_MAIN" is identical to the default case ==> delete it
        }
    }

    @Override
    public void onBackPressed() {
        actionButtonBack();
    }

    @Override
    public void onInputTransfertCompteCompte1Sent(CharSequence input) {

        if (input.equals("Numero_destinataire")) {
            PAGE_TO_BACK = "Numero_destinataire";
        }

    }

    @Override
    public void onInputTransfertCompteCompte2Sent(CharSequence input) {
       /* if input equals "Montant_transfert"   PAGE_TO_BACK = "Montant_transfert"
        else if input equals "Numero_destinataire" PAGE_TO_BACK = "Numero_destinataire" */
        PAGE_TO_BACK = input.toString();
    }

    @Override
    public void onInputImportContactSent(CharSequence input) {
        //unneeded statement input equals Numero_destinataire
        // comment code block ? PAGE_TO_BACK = "Numero_destinataire"
    }

    @Override
    public void onInputTransfertCompteCompteRecapSent(CharSequence input) {
        //no need statement if input equals "BACK_MAIN"  PAGE_TO_BACK "BACK_MAIN"
        //block is identical to onInputTransfertCompteCompte2Sent
        onInputTransfertCompteCompte2Sent(input);

    }
}