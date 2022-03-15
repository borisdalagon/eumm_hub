package com.ufipay.eummhub.client.transfertargent.cashversmobile.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.bottomsheet.CodeEmpreinte;
import com.ufipay.eummhub.core.bottomsheet.OperationReport;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.remittance.RemittanceRestInterface;
import com.ufipay.eummhub.remote.remittance.TransferRequest;
import com.ufipay.eummhub.remote.user.SimpleDataResponse;
import com.ufipay.eummhub.remote.user.UserRestInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentTransfertCashMobileRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgTrCsMobRecap";
    private SharedPrefManager sharedPrefCashToMob;
    private RemittanceRestInterface accountToAccountRemittanceRestInterface;
    private String phone;
    private UserRestInterface accountToAccountUserRestInterface;

    FragmentTransfertCashMobileRecapListener listener;

    TransfertArgentTEST transfertArgentTEST;
    TextView numeroPhone;
    TextView nomContact;
    TextView montant;
    TextView lettre;
    RelativeLayout liContact;
    TextView numeroCompte;
    RelativeLayout liPhone;
    RelativeLayout premierLoader;
    Button valider;

    private TextView beneficiaryName;
    private ProgressBar progressBeneficiaryName;


    ProgressBar progressBar;
    RapportRequest rapportRequest = new RapportRequest();

    //
    public FragmentTransfertCashMobileRecap() {
        //required an empty constructor
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //nothing to do
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing to do
    }

    public interface FragmentTransfertCashMobileRecapListener {
        void onInputTransfertCashMobileRecapSent(CharSequence input);
    }


    //
    public static FragmentTransfertCashMobileRecap newInstance(){
        return new FragmentTransfertCashMobileRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate( R.layout.fragment_tranfert_cash_mobile_recap, container, false );
        sharedPrefCashToMob = SharedPrefManager.newInstance(getContext());

        liContact = v.findViewById(R.id.liContact);
        liPhone = v.findViewById(R.id.liPhone);
        nomContact = v.findViewById(R.id.nomContact);
        numeroPhone = v.findViewById(R.id.numeroPhone);
        montant = v.findViewById(R.id.montant);
        lettre = v.findViewById(R.id.lettre);
        numeroCompte = v.findViewById(R.id.numeroCompte);
        progressBar = v.findViewById(R.id.progressBar);

        //premier Loader
        premierLoader = v.findViewById(R.id.premierLoader);

        progressBeneficiaryName = v.findViewById(R.id.progress_beneficiary_name);
        beneficiaryName = v.findViewById(R.id.beneficiary_name);

        valider = v.findViewById(R.id.valider);
        valider.setOnClickListener(this);


        numeroCompte.setText(sharedPrefCashToMob.getPhoneNumberTransfert());
        montant.setText(sharedPrefCashToMob.getAmountTransfert());

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.TRANSFERT_ARGENT);

            if(transfertArgentTEST.getNomContact() != null){
                liContact.setVisibility(View.VISIBLE);
                liPhone.setVisibility(View.GONE);
                nomContact.setText(transfertArgentTEST.getNomContact());
                lettre.setText(transfertArgentTEST.getNomContact().substring(0,1));
            }else {
                liContact.setVisibility(View.GONE);
                liPhone.setVisibility(View.VISIBLE);
            }

            if(transfertArgentTEST.getPhone() != null){
                numeroPhone.setText(transfertArgentTEST.getPhone());
                numeroCompte.setText(transfertArgentTEST.getPhone());
            }

            if(transfertArgentTEST.getMontant() != null) {
                montant.setText(transfertArgentTEST.getMontant());
            }

        }else {
            liContact.setVisibility(View.GONE);
            liPhone.setVisibility(View.VISIBLE);
        }

        phone = sharedPrefCashToMob.getPhoneNumberTransfert().replace("(", "").replace(")", "");
        phone = phone.replace("+", "").replace("-", "").trim();
        phone = sharedPrefCashToMob.getCodePaysTelephone()+phone.replace(" ", "");

        accountToAccountRemittanceRestInterface = new Retrofit.Builder().baseUrl(sharedPrefCashToMob.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(RemittanceRestInterface.class);

        accountToAccountUserRestInterface = new Retrofit.Builder().baseUrl(sharedPrefCashToMob.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(UserRestInterface.class);

        getCashToMobileBeneficiaryName(phone);

        return v;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {

       if(result.equals(Utils.REQUEST_POUR_NON_AUTHENTICATION)){
           getCashToMobileBeneficiaryName(phone);

       }else {

           valider.setVisibility(View.GONE);
           progressBar.setVisibility(View.VISIBLE);

           String amount = sharedPrefCashToMob.getAmountTransfert().replace("-", "").
                   replace(",", "").replace(".", "");
           amount = amount.replace(" ", "").trim();
           TransferRequest transferRequest = new TransferRequest();
           transferRequest.setAmount(Integer.parseInt(amount));
           transferRequest.setBeneficiary(phone);
           transferRequest.setAccountNumber(sharedPrefCashToMob.getAccountNumber());
           transferRequest.setMpin(sharedPrefCashToMob.getMpin());
           transferRequest.setAccountNumber(sharedPrefCashToMob.getAccountNumber());

           transferMoneyCashToMobile(transferRequest);


       }

    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransfertCashMobileRecapListener) {
            listener = (FragmentTransfertCashMobileRecapListener) context;
        } else {
            Log.d(TAG, "onAttach: trsf cash mobile recap must implement FragmentAListener ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompatCashMobileRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorCompatCashMobileRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.valider) {//Pour gérer le KeyBoard
                            Bundle bundle = new Bundle();
                            bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_CASH_VERS_MOBILE);

                            CodeEmpreinte codeTransfertcCompteCompte = new CodeEmpreinte();
                            codeTransfertcCompteCompte.setArguments(bundle);
                            codeTransfertcCompteCompte.show(getParentFragmentManager(), Utils.TAG);
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


    private void transferMoneyCashToMobile(TransferRequest transferRequest){

        //pour les test Espresso
        sharedPrefCashToMob.setJetonTest(Utils.NON_OK);

        Call<SimpleDataResponse> transferToAccount = accountToAccountRemittanceRestInterface.transfers("CASH_TO_MOBILE", transferRequest);
        OperationReport operationReport = new OperationReport();
        Bundle bundleTrCashToMobile = new Bundle();

        transferToAccount.enqueue(new Callback<SimpleDataResponse>() {
            @Override
            public void onResponse(Call<SimpleDataResponse> call, Response<SimpleDataResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){

                        rapportRequest.setOperation(Utils.TRANSFERT_CASH_VERS_MOBILE);
                        rapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        rapportRequest.setMessage(getString(R.string.you_will_receive_sms_confirmation)+"\n"+response.body().getMessage());
                        bundleTrCashToMobile.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );

                        OperationReport operationReport = new OperationReport();
                        operationReport.setArguments(bundleTrCashToMobile);
                        operationReport.show(getParentFragmentManager(), Utils.TAG);
                        progressBar.setVisibility(View.GONE);

                        //pour les test Espresso
                        sharedPrefCashToMob.setJetonTest(Utils.OK);
                        sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_REUSSIE);


                        return;
                    }
                    //non reussi
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(response.body().getMessage());
                    bundleTrCashToMobile.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrCashToMobile);
                    operationReport.show(getParentFragmentManager(), Utils.TAG);

                    //pour les test Espresso
                    sharedPrefCashToMob.setJetonTest(Utils.OK);
                    sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);

                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(status+"\n"+message);
                    bundleTrCashToMobile.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrCashToMobile);
                    operationReport.show(getParentFragmentManager(), Utils.TAG);

                    //pour les test Espresso
                    sharedPrefCashToMob.setJetonTest(Utils.OK);
                    sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);

                } catch (JSONException | IOException e) {
                    Log.d(TAG, "trying to get status and message from json response, : "+e.getLocalizedMessage(), e);
                }
            }

            @Override
            public void onFailure(Call<SimpleDataResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                rapportRequest.setMessage(getString(R.string.network_error)+"\n"+t.getLocalizedMessage());
                bundleTrCashToMobile.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                operationReport.setArguments(bundleTrCashToMobile);
                operationReport.show(getParentFragmentManager(), Utils.TAG);

                //pour les test Espresso
                sharedPrefCashToMob.setJetonTest(Utils.OK);
                sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);
            }
        });

    }

    private void getCashToMobileBeneficiaryName(String benefPhone){

        //pour Espresso
        sharedPrefCashToMob.setJetonTest(Utils.NON_OK);

        valider.setBackgroundColor(Color.parseColor("#818181"));
        valider.setClickable(false);

        Call<SimpleDataResponse> getName = accountToAccountUserRestInterface.getUserName(benefPhone, sharedPrefCashToMob.getAccountNumber());

        progressBeneficiaryName.setVisibility(View.VISIBLE);
        getName.enqueue(new Callback<SimpleDataResponse>() {
            @Override
            public void onResponse(Call<SimpleDataResponse> call, Response<SimpleDataResponse> response) {
                progressBeneficiaryName.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        beneficiaryName.setText(response.body().getData());

                        valider.setBackgroundColor(Color.parseColor("#1D5FA7"));
                        valider.setClickable(true);

                        //pour Espresso
                        sharedPrefCashToMob.setJetonTest(Utils.OK);
                        sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_REUSSIE);
                        return;
                    }
                    bottomRapport(getString(R.string.impossible_to_connect)+response.body().getMessage());

                    //pour les test Espresso
                    sharedPrefCashToMob.setJetonTest(Utils.OK);
                    sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);
                }else {

                    try{
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString(Utils.STATUS);
                        String message = jsonObject.getString(Utils.MESSAGE);
                        bottomRapport(message);

                        //pour les test Espresso
                        sharedPrefCashToMob.setJetonTest(Utils.OK);
                        sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);

                    }catch (JSONException | IOException e){
                        Log.d("Requet_OK", "");
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleDataResponse> call, Throwable t) {
                progressBeneficiaryName.setVisibility(View.GONE);
                bottomRapport(getString(R.string.impossible_to_connect)+t.getLocalizedMessage());

                //pour les test Espresso
                sharedPrefCashToMob.setJetonTest(Utils.OK);
                sharedPrefCashToMob.setStatutsTest(Utils.OPERATION_ECHOUE);
            }
        });

    }

    private void reloadCashToMobileName(String nameAlertMessage, String beneficiaryPhone){
        Utils.getDialogBuilder(getContext(), "", nameAlertMessage, getString(R.string.retry), getString(R.string.close))
                .setPositiveButtonListener(()->getCashToMobileBeneficiaryName(beneficiaryPhone))
                .setNegativeButtonListener(()->{

                })
                .build();
    }

    private void bottomRapport(String message){
        Bundle bundle = new Bundle();
        rapportRequest.setOperation(Utils.TRANSFERT_CASH_VERS_MOBILE);
        rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
        rapportRequest.setMessage(message);
        rapportRequest.setTypeRequet(Utils.REQUEST_POUR_NON_AUTHENTICATION);
        bundle.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );

        OperationReport operationReport = new OperationReport();
        operationReport.setArguments(bundle);
        operationReport.show(getParentFragmentManager(), Utils.TAG);
        progressBar.setVisibility(View.GONE);
    }


}
