package com.ufipay.eummhub.core.activities;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.appolica.flubber.Flubber;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.utils.Utils;


public class Launcher extends AppCompatActivity {


    CardView cardLogo;
    ImageView nomLogo;
    TashieLoader loader;
    ImageView courbure;
    ImageView logoExpressUnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        cardLogo = findViewById(R.id.card_logo);
        nomLogo = findViewById(R.id.nomLogo);
        loader = findViewById(R.id.loader);
        courbure = findViewById(R.id.courbure);
        logoExpressUnion = findViewById(R.id.logoExpressUnion);


        //Animation du LOGO
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_DOWN)
                .listener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        nomLogo.setImageResource(R.drawable.titrelogoblanc);

                        //Animation du NOM DU LOGO
                        Flubber.with()
                                .animation(Flubber.AnimationPreset.FADE_IN)
                                .listener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        loader.setVisibility(View.VISIBLE);
                                        courbure.setVisibility(View.VISIBLE);
                                        //Animation de la COURBURE
                                        Flubber.with()
                                                .animation(Flubber.AnimationPreset.FADE_IN)
                                                .listener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        logoExpressUnion.setVisibility(View.VISIBLE);
                                                        //Animation du LOGO EXPRESS UNION
                                                        Flubber.with()
                                                                .animation(Flubber.AnimationPreset.FADE_IN)
                                                                .listener(new Animator.AnimatorListener() {
                                                                    @Override
                                                                    public void onAnimationStart(Animator animator) {

                                                                    }

                                                                    @Override
                                                                    public void onAnimationEnd(Animator animator) {

                                                                        Utils.openNewActivity(Launcher.this, Authentification.class);

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
                                                                .createFor(logoExpressUnion)                             // Apply it to the view
                                                                .start();
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
                                                .createFor(courbure)                             // Apply it to the view
                                                .start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                })// Slide up animation FADE_IN_UP
                                .repeatCount(0)                              // Repeat once
                                .duration(1200)                              // Last for 1000 milliseconds(1 second)
                                .createFor(nomLogo)                             // Apply it to the view
                                .start();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })// Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(1500)                              // Last for 1000 milliseconds(1 second)
                .createFor(cardLogo)                             // Apply it to the view
                .start();



        TashieLoader tashie = new TashieLoader(
                this, 5,
                30, 10,
                ContextCompat.getColor(this, R.color.colorBlanche));

        tashie.setAnimDuration(500);
        tashie.setAnimDelay(100);
        tashie.setInterpolator(new LinearInterpolator());

        //containerLL.addView(tashie);



    }


}