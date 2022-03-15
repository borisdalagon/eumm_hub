package com.ufipay.eummhub.core.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.bottomsheet.DetailSolde;
import com.ufipay.eummhub.core.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CircularImageView imageProfil;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    ImageView profilUtilisateur;

    RelativeLayout liDetailSolde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // clear FLAG_TRANSLUCENT_STATUS flag:
       // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

       // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // finally change the color
      //  getWindow().setStatusBarColor(ContextCompat.getColor(getWindow().getContext(),R.color.colorEU));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            isUserFirstTime = Boolean.parseBoolean(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

            if (isUserFirstTime) {
                Intent introIntent = new Intent(MainActivity.this, Pager.class);
                introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
                startActivity(introIntent);
            }

            setContentView(R.layout.activity_main);
        }


        profilUtilisateur = (ImageView) findViewById(R.id.profilUtilateur);
        profilUtilisateur.setOnClickListener(this);

        liDetailSolde = (RelativeLayout) findViewById(R.id.li_detail_solde);
        liDetailSolde.setOnClickListener(this);

        imageProfil = (CircularImageView)findViewById(R.id.imageProfil);

        Glide.with(this)
                .load(R.drawable.d)
                .into(imageProfil);

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


                            case R.id.profilUtilateur:

                                Utils.openNewActivity(MainActivity.this, ProfilUtilisateur.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);

                                break;

                             case R.id.li_detail_solde:

                                 DetailSolde detailSolde = new DetailSolde();
                                 detailSolde.show(getSupportFragmentManager(), Utils.TAG);

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