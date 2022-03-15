package com.ufipay.eummhub.core.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.client.payerfactures.PayerFactures;
import com.ufipay.eummhub.client.transfertargent.TransfertArgent;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class OperationReport extends BottomSheetDialogFragment implements View.OnClickListener {

    Button close;
    Button reload;
    TextView titreRapport;
    TextView message;
    ImageView imgRapport;
    RapportRequest rapportRequest;

    public OperationReport() {
        //required an empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_operation_report, container, false);

        imgRapport = v.findViewById(R.id.imgRapport);
        titreRapport = v.findViewById(R.id.titreRapport);
        message = v.findViewById(R.id.message);

        reload = v.findViewById(R.id.relancer);
        reload.setOnClickListener(this);

        close = v.findViewById(R.id.fermer);
        close.setOnClickListener(this);
        this.setCancelable(false);


        Bundle bundle = getArguments();

        if(bundle != null){

            rapportRequest = (RapportRequest) bundle.getSerializable(Utils.RAPPORT_REQUEST);

            message.setText(rapportRequest.getMessage());
            titreRapport.setText(rapportRequest.getStatut());

            if(rapportRequest.getStatut().equals(Utils.OPERATION_REUSSIE)){
                reload.setVisibility(View.GONE);
                Glide.with(getContext())
                        .load(R.drawable.ic_done_deep)
                        .into(imgRapport);
            }
            else if(rapportRequest.getStatut().equals(Utils.OPERATION_ECHOUE)){
                Glide.with(getContext())
                        .load(R.drawable.ic_echec)
                        .into(imgRapport);
            }

        }

        return v;
    }


    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyForOpReport = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyForOpReport
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {

                            case R.id.fermer:

                                if(rapportRequest.getOperation().equals(Utils.DEMANDE_DE_SOLDE)){
                                   dismiss();
                                }
                                else if(rapportRequest.getOperation().equals(Utils.TRANSFERT_COMPTE_COMPTE)){
                                    Utils.openNewActivity(getContext(), MainActivity.class);
                                    getActivity().finish();
                                }
                                else if(rapportRequest.getOperation().equals(Utils.TRANSFERT_CASH_VERS_MOBILE)){
                                    Utils.openNewActivity(getContext(), TransfertArgent.class);
                                    getActivity().finish();
                                }else if(rapportRequest.getOperation().equals(Utils.TRANSFERT_COMPTE_VERS_CASH)){
                                    Utils.openNewActivity(getContext(), TransfertArgent.class);
                                    getActivity().finish();
                                }else if(rapportRequest.getOperation().equals(Utils.PAYER_FACTURE_ENEO)){
                                    Utils.openNewActivity(getContext(), PayerFactures.class);
                                    getActivity().finish();
                                }else if(rapportRequest.getOperation().equals(Utils.ENEO_CUSTOMER_BILLS)){

                                    CBillingNav.backEneoFistPage(getParentFragmentManager(),null);
                                    dismiss();

                                }else if(rapportRequest.getOperation().equals(Utils.PAYER_FACTURE_CAMWATER)){

                                    Utils.openNewActivity(getContext(), PayerFactures.class);
                                    getActivity().finish();
                                }
                                else if(rapportRequest.getOperation().equals(Utils.PAYER_FACTURE_CANALPLUS)){

                                    Utils.openNewActivity(getContext(), PayerFactures.class);
                                    getActivity().finish();

                                }

                                break;

                            case R.id.relancer:

                                if(rapportRequest.getTypeRequet().equals(Utils.REQUEST_POUR_NON_AUTHENTICATION)){
                                    EventBus.getDefault().post(Utils.REQUEST_POUR_NON_AUTHENTICATION);
                                }else {
                                    EventBus.getDefault().post(Utils.AUTHENFICATION_REUSSIE);
                                }
                                dismiss();


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
