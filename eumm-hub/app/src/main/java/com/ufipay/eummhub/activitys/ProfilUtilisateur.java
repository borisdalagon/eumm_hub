package com.ufipay.eummhub.core.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.bottomsheet.ModifierCode;
import com.ufipay.eummhub.core.bottomsheet.SeDeconnecter;
import com.ufipay.eummhub.core.utils.Utils;

public class ProfilUtilisateur extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    CircularImageView imageProfil;

    LinearLayout liModifierCode;
    LinearLayout liSeDeconnecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_utilisateur);

        imageProfil = (CircularImageView) findViewById(R.id.imageProfil);

        liModifierCode = (LinearLayout) findViewById(R.id.li_modifier_code);
        liModifierCode.setOnClickListener(this);

        liSeDeconnecter = (LinearLayout) findViewById(R.id.li_se_deconnecter);
        liSeDeconnecter.setOnClickListener(this);

        Glide.with(this)
                .load(R.drawable.n)
                .into(imageProfil);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        ViewCompat.animate( v )
                .setDuration( 200 )
                .scaleX( 0.9f )
                .scaleY( 0.9f )
                .setInterpolator( new Utils.CycleInterpolator() )
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                        Log.d( "","" );
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {

                            case R.id.back:

                                Utils.openNewActivity(ProfilUtilisateur.this, MainActivity.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);

                                break;

                            case R.id.li_modifier_code:

                                ModifierCode modifierCode = new ModifierCode();
                                modifierCode.show(getSupportFragmentManager(), Utils.TAG);

                                break;

                            case R.id.li_se_deconnecter:

                                SeDeconnecter seDeconnecter = new SeDeconnecter();
                                seDeconnecter.show(getSupportFragmentManager(), Utils.TAG);

                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                        Log.d("","");
                    }
                } )
                .withLayer()
                .start();

    }



}