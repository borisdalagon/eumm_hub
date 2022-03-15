package com.ufipay.eummhub.client.transfertargent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.transfertargent.cashversmobile.EnvoisCashVersMobile;
import com.ufipay.eummhub.client.transfertargent.compteverscash.EnvoisCompteVersCash;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

public class TransfertArgent extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    CardView cardEnvoisCashVersMobil;
    CardView cardTransfertCompteVersCash;
    TextView titrePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert_argent);

        cardEnvoisCashVersMobil = (CardView) findViewById(R.id.cardEnvoisCashVersMobil);
        cardEnvoisCashVersMobil.setOnClickListener(this);

        cardTransfertCompteVersCash = (CardView) findViewById(R.id.cardTransfertCompteVersCash);
        cardTransfertCompteVersCash.setOnClickListener(this);

        titrePage = (TextView) findViewById(R.id.titrePage);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        viderSharedPreference();
    }

    private void viderSharedPreference(){
        SharedPrefManager.newInstance(this).setPhoneNumberTransfert(null);
        SharedPrefManager.newInstance(this).setAmountTransfert(null);
        SharedPrefManager.newInstance(this).setNomContactTransfert(null);
        SharedPrefManager.newInstance(this).setNomBeneficiaireTransfert(null);

    }

    @Override
    public void onBackPressed() {
        Utils.openNewActivity(TransfertArgent.this, MainActivity.class);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorMoneyTrans = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorMoneyTrans
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {

                            case R.id.back:
                                Utils.openNewActivity(TransfertArgent.this, MainActivity.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                            case R.id.cardEnvoisCashVersMobil:
                                Utils.openNewActivity(TransfertArgent.this, EnvoisCashVersMobile.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                            case R.id.cardTransfertCompteVersCash:
                                Utils.openNewActivity(TransfertArgent.this, EnvoisCompteVersCash.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
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
}