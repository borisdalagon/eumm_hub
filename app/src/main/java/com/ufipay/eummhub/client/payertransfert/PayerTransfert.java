package com.ufipay.eummhub.client.payertransfert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.utils.Utils;

public class PayerTransfert extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_transfert);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        Utils.openNewActivity(PayerTransfert.this, MainActivity.class);
        overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorAccountToAccountRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorAccountToAccountRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {

                            case R.id.back:

                                Utils.openNewActivity(PayerTransfert.this, MainActivity.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_stay_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do
                    }
                } )
                .withLayer()
                .start();

    }
}