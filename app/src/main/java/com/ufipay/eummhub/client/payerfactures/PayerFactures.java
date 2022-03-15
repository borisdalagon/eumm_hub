package com.ufipay.eummhub.client.payerfactures;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.appolica.flubber.Flubber;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.payerfactures.camwater.PayerFactureCamwater;
import com.ufipay.eummhub.client.payerfactures.canalplus.PayerFactureCanalPlus;
import com.ufipay.eummhub.client.payerfactures.eneo.PayerFactureEneo;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.utils.Utils;

public class PayerFactures extends AppCompatActivity implements View.OnClickListener {

    CardView cardElectricite;
    CardView cardEau;
    CardView cardTv;
    CardView cardAutres;

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_facture);

        init();

    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        cardElectricite = (CardView)findViewById(R.id.card_electricite);
        cardElectricite.setOnClickListener(this);

        cardEau = (CardView)findViewById(R.id.card_eau);
        cardEau.setOnClickListener(this);

        cardTv = (CardView)findViewById(R.id.card_television);
        cardTv.setOnClickListener(this);

        cardAutres = (CardView)findViewById(R.id.card_autres);
        cardAutres.setOnClickListener(this);

        animation();
    }

    private void animation(){

        //Animation card Electricité
        Flubber.with()
                .animation(Flubber.AnimationPreset.FADE_IN)
                .listener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                       // Utils.openNewActivity(PayerFactures.this, Authentification.class);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })// Slide up animation FADE_IN_UP
                .repeatCount(0)                              // Repeat once
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(cardElectricite)                             // Apply it to the view
                .start();

        //Animation card Eau
        Flubber.with()
                .animation(Flubber.AnimationPreset.FADE_IN)
                .listener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        // Utils.openNewActivity(PayerFactures.this, Authentification.class);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })// Slide up animation FADE_IN_UP
                .repeatCount(0)                              // Repeat once
                .duration(1500)                              // Last for 1000 milliseconds(1 second)
                .createFor(cardEau)                             // Apply it to the view
                .start();

        //Animation card Tv
        Flubber.with()
                .animation(Flubber.AnimationPreset.FADE_IN)
                .listener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        // Utils.openNewActivity(PayerFactures.this, Authentification.class);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })// Slide up animation FADE_IN_UP
                .repeatCount(0)                              // Repeat once
                .duration(2000)                              // Last for 1000 milliseconds(1 second)
                .createFor(cardTv)                             // Apply it to the view
                .start();

        //Animation card Autres
        Flubber.with()
                .animation(Flubber.AnimationPreset.FADE_IN)
                .listener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        // Utils.openNewActivity(PayerFactures.this, Authentification.class);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })// Slide up animation FADE_IN_UP
                .repeatCount(0)                              // Repeat once
                .duration(2500)                              // Last for 1000 milliseconds(1 second)
                .createFor(cardAutres)                             // Apply it to the view
                .start();



    }

    private void backAction(){
        Utils.openNewActivity(PayerFactures.this, MainActivity.class);
        overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
        finish();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorMainClick = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorMainClick
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {

                            case R.id.back:

                                backAction();

                                break;

                            case R.id.card_electricite:

                                Utils.openNewActivity(PayerFactures.this, PayerFactureEneo.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                            case R.id.card_eau:

                                Utils.openNewActivity(PayerFactures.this, PayerFactureCamwater.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                            case R.id.card_television:

                                Utils.openNewActivity(PayerFactures.this, PayerFactureCanalPlus.class);
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