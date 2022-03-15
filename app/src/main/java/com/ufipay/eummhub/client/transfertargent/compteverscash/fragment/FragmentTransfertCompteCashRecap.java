package com.ufipay.eummhub.client.transfertargent.compteverscash.fragment;

import android.content.Context;
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


public class FragmentTransfertCompteCashRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "accToCashRecap";
    private SharedPrefManager accountToCashRecapPrefMan;
    private RemittanceRestInterface accountToCashRemittanceRestInterface;


    FragmentTransfertCompteCashRecapListener listener;

    TransfertArgentTEST transfertArgentTEST;
    TextView numeroPhone;
    TextView nomContact;
    TextView montant;
    TextView lettre;
    TextView nomBeneficiaire;
    RelativeLayout liContact;
    TextView numeroCompte;
    RelativeLayout liPhone;
    RelativeLayout premierLoader;
    Button valider;



    ProgressBar progressBar;
    RapportRequest rapportRequest = new RapportRequest();

    //
    public FragmentTransfertCompteCashRecap() {
        //required an empty constructor
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //nothing to do here
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing to do here
    }

    public interface FragmentTransfertCompteCashRecapListener {
        void onInputTransfertCompteCashRecapSent(CharSequence input);
    }


    //
    public static FragmentTransfertCompteCashRecap newInstance(){
        return new FragmentTransfertCompteCashRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRecapAccountToCash = inflater.inflate( R.layout.fragment_tranfert_compte_cash_recap, container, false );

        accountToCashRecapPrefMan = SharedPrefManager.newInstance(getContext());
        accountToCashRemittanceRestInterface = new Retrofit.Builder().baseUrl(accountToCashRecapPrefMan.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(RemittanceRestInterface.class);

        liContact = viewRecapAccountToCash.findViewById(R.id.liContact);
        liPhone = viewRecapAccountToCash.findViewById(R.id.liPhone);
        nomContact = viewRecapAccountToCash.findViewById(R.id.nomContact);
        numeroPhone = viewRecapAccountToCash.findViewById(R.id.numeroPhone);
        montant = viewRecapAccountToCash.findViewById(R.id.montant);
        lettre = viewRecapAccountToCash.findViewById(R.id.lettre);
        nomBeneficiaire = viewRecapAccountToCash.findViewById(R.id.beneficiaire);
        numeroCompte = viewRecapAccountToCash.findViewById(R.id.numeroCompte);
        progressBar = viewRecapAccountToCash.findViewById(R.id.progressBar);

        //premier Loader
        premierLoader = viewRecapAccountToCash.findViewById(R.id.premierLoader);

        valider = viewRecapAccountToCash.findViewById(R.id.valider);
        valider.setOnClickListener(this);


        numeroCompte.setText(accountToCashRecapPrefMan.getPhoneNumberTransfert());
        montant.setText(accountToCashRecapPrefMan.getAmountTransfert());

        nomBeneficiaire.setText(accountToCashRecapPrefMan.getNomBeneficiaireTransfert());

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


        return viewRecapAccountToCash;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
    //Lancer la requêtte de validation ici

        valider.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String amount = accountToCashRecapPrefMan.getAmountTransfert().replace("-", "").
                replace(",", "").replace(".", "");
        amount = amount.replace(" ", "").trim();

        String phone = accountToCashRecapPrefMan.getPhoneNumberTransfert().replace("(", "").replace(")", "");
        phone = phone.replace("+", "").replace("-", "").trim();
        phone = accountToCashRecapPrefMan.getCodePaysTelephone()+phone.replace(" ", "");


        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(Integer.parseInt(amount));
        transferRequest.setBeneficiary(nomBeneficiaire.getText().toString());
        transferRequest.setAccountNumber(accountToCashRecapPrefMan.getAccountNumber());
        transferRequest.setMpin(accountToCashRecapPrefMan.getMpin());
        transferRequest.setAccountNumber(accountToCashRecapPrefMan.getAccountNumber());

        transferMoneyAccountToCash(transferRequest);


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
        if (context instanceof FragmentTransfertCompteCashRecapListener) {
            listener = (FragmentTransfertCompteCashRecapListener) context;
        } else {
            Log.d(TAG, "onAttach: account to cash recap must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorSendAccountToCashRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSendAccountToCashRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.valider) {//Pour gérer le KeyBoard
                            Bundle bundle = new Bundle();
                            bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_COMPTE_VERS_CASH);

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


    private void transferMoneyAccountToCash(TransferRequest transferRequest){

        //Pour test Espresso
        accountToCashRecapPrefMan.setJetonTest(Utils.NON_OK);

        Call<SimpleDataResponse> transferToAccount = accountToCashRemittanceRestInterface.transfers("CASH_TO_CASH", transferRequest);
        OperationReport operationReport = new OperationReport();
        rapportRequest.setOperation(Utils.TRANSFERT_COMPTE_VERS_CASH);
        rapportRequest.setTypeRequet(Utils.REQUEST_POUR_NON_AUTHENTICATION);

        Bundle bundleTrAccountToCash = new Bundle();

        transferToAccount.enqueue(new Callback<SimpleDataResponse>() {
            @Override
            public void onResponse(Call<SimpleDataResponse> call, Response<SimpleDataResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){

                        rapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        rapportRequest.setMessage(getString(R.string.you_will_receive_sms_confirmation)+"\n"+response.body().getMessage());
                        rapportRequest.setTypeRequet(Utils.REQUEST_POUR_NON_AUTHENTICATION);

                        bundleTrAccountToCash.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );

                        OperationReport operationReport = new OperationReport();
                        operationReport.setArguments(bundleTrAccountToCash);
                        operationReport.show(getParentFragmentManager(), Utils.TAG);
                        progressBar.setVisibility(View.GONE);

                        //Pour test Espresso
                        accountToCashRecapPrefMan.setJetonTest(Utils.OK);
                        accountToCashRecapPrefMan.setStatutsTest(Utils.OPERATION_REUSSIE);

                        return;
                    }
                    //non reussi
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(response.body().getMessage());
                    bundleTrAccountToCash.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrAccountToCash);
                    operationReport.show(getParentFragmentManager(), Utils.TAG);

                    //Pour test Espresso
                    accountToCashRecapPrefMan.setStatutsTest(Utils.OPERATION_ECHOUE);

                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(status+"\n"+message);
                    bundleTrAccountToCash.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrAccountToCash);
                    operationReport.show(getParentFragmentManager(), Utils.TAG);

                    //Pour test Espresso
                    accountToCashRecapPrefMan.setStatutsTest(Utils.OPERATION_ECHOUE);
                } catch (JSONException | IOException e) {
                    Log.d(TAG, "trying to get status and message from json response, : "+e.getLocalizedMessage(), e);
                }
            }

            @Override
            public void onFailure(Call<SimpleDataResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                rapportRequest.setMessage(getString(R.string.network_error)+"\n"+t.getLocalizedMessage());
                bundleTrAccountToCash.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                operationReport.setArguments(bundleTrAccountToCash);
                operationReport.show(getParentFragmentManager(), Utils.TAG);

                //Pour test Espresso
                accountToCashRecapPrefMan.setStatutsTest(Utils.OPERATION_ECHOUE);
            }
        });

    }



}
