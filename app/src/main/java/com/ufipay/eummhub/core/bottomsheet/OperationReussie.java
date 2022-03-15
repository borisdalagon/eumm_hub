package com.ufipay.eummhub.core.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.utils.Utils;

public class OperationReussie extends BottomSheetDialogFragment implements View.OnClickListener {

    Button fermer;

    public OperationReussie() {
        //required an empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_operation_report, container, false);

        fermer = v.findViewById(R.id.fermer);
        fermer.setOnClickListener(this);
        this.setCancelable(false);


        return v;
    }


    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorCompat
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {

                            case R.id.fermer:

                                Utils.openNewActivity(getContext(), MainActivity.class);

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
