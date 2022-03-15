package com.ufipay.eummhub.client.transfertcomptecompte.fragment;

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
import android.widget.Toast;

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

public class FragmentTransfertCompteCompteRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgAccToAccRec";
    private SharedPrefManager sharedPrefAccToAcc;
    private RemittanceRestInterface accountToAccountRemittanceRestInterface;
    private UserRestInterface accountToAccountUserRestInterface;
    private String phone;

    FragmentTransfertCompteCompteRecapListener listener;

    TransfertArgentTEST transfertArgentTEST;
    TextView numeroPhone;
    TextView nomContact;
    TextView beneficiaryAccountName;
    TextView montant;
    TextView lettre;
    RelativeLayout liContact;
    TextView numeroCompte;
    RelativeLayout liPhone;
    RelativeLayout premierLoader;
    Button valider;
    int valueKeyBoard = 6;



    ProgressBar progressBar;
    RapportRequest rapportRequest = new RapportRequest();

    OperationReport operationReport = new OperationReport();


    //
    public FragmentTransfertCompteCompteRecap() {
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

    public interface FragmentTransfertCompteCompteRecapListener {
        void onInputTransfertCompteCompteRecapSent(CharSequence input);
    }


    //
    public static FragmentTransfertCompteCompteRecap newInstance(){
        return new FragmentTransfertCompteCompteRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate( R.layout.fragment_tranfert_compte_compte_recap, container, false );
        sharedPrefAccToAcc = SharedPrefManager.newInstance(getContext());

        rapportRequest.setOperation(Utils.TRANSFERT_COMPTE_COMPTE);

        liContact = v.findViewById(R.id.liContact);
        liPhone = v.findViewById(R.id.liPhone);
        nomContact = v.findViewById(R.id.nomContact);
        beneficiaryAccountName = v.findViewById(R.id.beneficiary_account_name);
        numeroPhone = v.findViewById(R.id.numeroPhone);
        montant = v.findViewById(R.id.montant);
        lettre = v.findViewById(R.id.lettre);
        numeroCompte = v.findViewById(R.id.numeroCompte);
        progressBar = v.findViewById(R.id.progressBar);
        premierLoader = v.findViewById(R.id.premierLoader);
        valider = v.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        accountToAccountRemittanceRestInterface = new Retrofit.Builder().baseUrl(sharedPrefAccToAcc.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(RemittanceRestInterface.class);

        accountToAccountUserRestInterface = new Retrofit.Builder().baseUrl(sharedPrefAccToAcc.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(UserRestInterface.class);

        valider.setOnClickListener(this);

        numeroCompte.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());


        Bundle bundle = this.getArguments();
        if (bundle != null) {

            transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.TRANSFERT_ARGENT);

            if(transfertArgentTEST.getNomContact() != null){
                liContact.setVisibility(View.VISIBLE);
                nomContact.setText(transfertArgentTEST.getNomContact());
                lettre.setText(transfertArgentTEST.getNomContact().substring(0,1));
            }else {
                liContact.setVisibility(View.GONE);
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
        }
        liPhone.setVisibility(View.VISIBLE);

        phone = transfertArgentTEST.getPhone().replace("(", "").replace(")", "");
        phone = phone.replace("+", "").replace("-", "").trim();
        phone = sharedPrefAccToAcc.getCodePaysTelephone()+phone.replace(" ", "");

        getBeneficiaryName(phone);


        return v;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {

        if(result.equals(Utils.REQUEST_POUR_NON_AUTHENTICATION)){

            getBeneficiaryName(phone);

        }else {
            valider.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            try {

                String amount = transfertArgentTEST.getMontant().replace("-", "").
                        replace(",", "").replace(".", "");
                amount = amount.replace(" ", "").trim();
                TransferRequest transferRequest = new TransferRequest();
                transferRequest.setAmount(Integer.parseInt(amount));
                transferRequest.setBeneficiary(phone);
                transferRequest.setAccountNumber(sharedPrefAccToAcc.getAccountNumber());
                transferRequest.setMpin(sharedPrefAccToAcc.getMpin());
                transferRequest.setAccountNumber(sharedPrefAccToAcc.getAccountNumber());


                //Lancer la requêtte de validation ici
                transferMoneyToAccount(transferRequest);

            }
            catch (NullPointerException exception){
                Log.d(TAG, "converting amount ? request ? : "+exception.getLocalizedMessage(), exception);
                Toast.makeText(getContext(), "Error while converting amount, "+exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
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
        if (context instanceof FragmentTransfertCompteCompteRecapListener) {
            listener = (FragmentTransfertCompteCompteRecapListener) context;
        } else {
            Log.d(TAG, "onAttach: recap must implement FragmentAListener ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //simply inherit method onActivityResult previously present here removed (Nothing implemented inside. Just calling super... reset if needed

    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.valider) {
                            valueKeyBoard = valueKeyBoard + 3;
                            //Pour gérer le KeyBoard
                            Bundle bundle = new Bundle();
                            bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_COMPTE_COMPTE);

                            CodeEmpreinte codeTransfertcCompteCompte = new CodeEmpreinte();
                            codeTransfertcCompteCompte.setArguments(bundle);
                            codeTransfertcCompteCompte.show(getActivity().getSupportFragmentManager(), Utils.TAG);
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


    private void transferMoneyToAccount(TransferRequest transferRequest){

        //Pour le test Espresso
        sharedPrefAccToAcc.setJetonTest(Utils.NON_OK);

        Call<SimpleDataResponse> transferToAccount = accountToAccountRemittanceRestInterface.transfers("PTOP", transferRequest);
        Bundle bundleTrToAccount = new Bundle();

        transferToAccount.enqueue(new Callback<SimpleDataResponse>() {
            @Override
            public void onResponse(Call<SimpleDataResponse> call, Response<SimpleDataResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){

                        rapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        rapportRequest.setMessage(getString(R.string.you_will_receive_sms_confirmation)+"\n"+response.body().getMessage());
                        rapportRequest.setMessage(response.body().getMessage());
                        bundleTrToAccount.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );

                        operationReport.setArguments(bundleTrToAccount);
                        operationReport.show(getActivity().getSupportFragmentManager(), Utils.TAG);
                        listener.onInputTransfertCompteCompteRecapSent(Utils.BACK_TO_MAIN);

                        //Pour le test Espresso
                        sharedPrefAccToAcc.setJetonTest(Utils.OK);
                        sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_REUSSIE);

                        return;
                    }
                    //non reussi
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(response.body().getMessage());
                    bundleTrToAccount.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrToAccount);
                    operationReport.show(getActivity().getSupportFragmentManager(), Utils.TAG);
                    listener.onInputTransfertCompteCompteRecapSent(Utils.BACK_TO_MAIN);
                    //Pour le test Espresso
                    sharedPrefAccToAcc.setJetonTest(Utils.OK);
                    sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);

                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);
                    rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    rapportRequest.setMessage(status+"\n"+message);
                    bundleTrToAccount.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                    operationReport.setArguments(bundleTrToAccount);
                    operationReport.show(getActivity().getSupportFragmentManager(), Utils.TAG);
                    listener.onInputTransfertCompteCompteRecapSent(Utils.BACK_TO_MAIN);
                    //Pour le test Espresso
                    sharedPrefAccToAcc.setJetonTest(Utils.OK);
                    sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);

                } catch (JSONException | IOException e) {
                    Log.d(TAG, "trying to get status and message from json response, : "+e.getLocalizedMessage(), e);
                }
            }

            @Override
            public void onFailure(Call<SimpleDataResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                rapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                rapportRequest.setMessage(getString(R.string.network_error)+"\n"+t.getLocalizedMessage());
                bundleTrToAccount.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
                operationReport.setArguments(bundleTrToAccount);
                operationReport.show(getActivity().getSupportFragmentManager(), Utils.TAG);
                listener.onInputTransfertCompteCompteRecapSent(Utils.BACK_TO_MAIN);
                //Pour le test Espresso
                sharedPrefAccToAcc.setJetonTest(Utils.OK);
                sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);
            }
        });

    }

    private void getBeneficiaryName(String benefPhone){

        //Pour le test Espresso
        sharedPrefAccToAcc.setJetonTest(Utils.NON_OK);

        valider.setClickable(false);
        valider.setBackgroundColor(Color.parseColor("#818181"));

        Call<SimpleDataResponse> getName = accountToAccountUserRestInterface.getUserName(benefPhone, sharedPrefAccToAcc.getAccountNumber());

        premierLoader.setVisibility(View.VISIBLE);
        getName.enqueue(new Callback<SimpleDataResponse>() {
            @Override
            public void onResponse(Call<SimpleDataResponse> call, Response<SimpleDataResponse> response) {
                premierLoader.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        beneficiaryAccountName.setText(response.body().getData());
                        valider.setClickable(true);
                        valider.setBackgroundColor(Color.parseColor("#1D5FA7"));

                        //Pour le test Espresso
                        sharedPrefAccToAcc.setJetonTest(Utils.OK);
                        sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_REUSSIE);
                        return;
                    }
                    bottomRapport(getString(R.string.impossible_to_connect)+response.body().getMessage());

                    //Pour le test Espresso
                    sharedPrefAccToAcc.setJetonTest(Utils.OK);
                    sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);
                    //reloadName(getString(R.string.impossible_to_connect)+response.body().getMessage(), benefPhone);
                }else {
                    try{
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString(Utils.STATUS);
                        String message = jsonObject.getString(Utils.MESSAGE);
                        bottomRapport(message);

                        //Pour le test Espresso
                        sharedPrefAccToAcc.setJetonTest(Utils.OK);
                        sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);

                    }catch (JSONException | IOException e){
                        Log.d("Requet_OK", "");
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleDataResponse> call, Throwable t) {
                premierLoader.setVisibility(View.GONE);
                bottomRapport(getString(R.string.impossible_to_connect)+t.getLocalizedMessage());
                //Pour le test Espresso
                sharedPrefAccToAcc.setJetonTest(Utils.OK);
                sharedPrefAccToAcc.setStatutsTest(Utils.OPERATION_ECHOUE);
                //reloadName(getString(R.string.impossible_to_connect)+t.getLocalizedMessage(), benefPhone);
            }
        });

    }

    private void reloadName(String nameAlertMessage, String beneficiaryPhone){
        Utils.getDialogBuilder(getContext(), "", nameAlertMessage, getString(R.string.retry), getString(R.string.close))
                .setPositiveButtonListener(()->getBeneficiaryName(beneficiaryPhone))
                .setNegativeButtonListener(()->{

                })
                .build();
    }

    private void bottomRapport(String message){
        Bundle bundle = new Bundle();
        rapportRequest.setOperation(Utils.TRANSFERT_COMPTE_COMPTE);
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
