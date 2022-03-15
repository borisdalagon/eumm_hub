package com.ufipay.eummhub.client.payerfactures.canalplus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.bottomsheet.CodeEmpreinte;
import com.ufipay.eummhub.core.bottomsheet.OperationReport;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.pay.StringResponse;
import com.ufipay.eummhub.remote.merchant.MerchantPayRequest;
import com.ufipay.eummhub.remote.merchant.MerchantPayRestInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentCanalplusRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPyCanalplusOffre";

    FragmentCanalplusRecapListener listener;

    Button valider;
    private ProgressBar progressBar;

    private MerchantPayRestInterface canalMerchantPayRestInterface;
    private MerchantPayRequest merchantCanalPayRequest;
    private TextView canalSubscriberAccountNumber;
    private TextView canalOfferName;

    private TextView amount;
    private TextView fee;
    private TextView totalAmount;

    private SharedPrefManager sharedPrefManagerCanalRecap;

    //
    public FragmentCanalplusRecap(){
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

    public interface FragmentCanalplusRecapListener {
        void onInputCanalplusRecapSent(CharSequence input);
    }

    //
    public static FragmentCanalplusRecap newInstance(){
        return new FragmentCanalplusRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPrefManagerCanalRecap = SharedPrefManager.newInstance(getContext());
        merchantCanalPayRequest = (MerchantPayRequest) getArguments().getSerializable(CBillingNav.CANAL_PAY_REQUEST);

        View canalPlusRecapView = inflater.inflate( R.layout.fragment_canalplus_recap, container, false );

        canalSubscriberAccountNumber = canalPlusRecapView.findViewById(R.id.numeroAbonnement);
        canalSubscriberAccountNumber.setText(merchantCanalPayRequest.getReference());

        canalOfferName = canalPlusRecapView.findViewById(R.id.nomOffre);
        canalOfferName.setText(merchantCanalPayRequest.getMerchant());

        amount = canalPlusRecapView.findViewById(R.id.montant);
        amount.setText(merchantCanalPayRequest.getAmount());

        fee = canalPlusRecapView.findViewById(R.id.frais);

        totalAmount = canalPlusRecapView.findViewById(R.id.montantTotal);

        progressBar = canalPlusRecapView.findViewById(R.id.progressBar);

        valider = canalPlusRecapView.findViewById(R.id.valider);
        valider.setOnClickListener(this);

        canalMerchantPayRestInterface = new Retrofit.Builder().baseUrl(sharedPrefManagerCanalRecap.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(MerchantPayRestInterface.class);

        return canalPlusRecapView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
        //Lancer la requêtte de validation ici
        progressBar.setVisibility(View.VISIBLE);
        valider.setVisibility(View.GONE);

        //pay canal here
        merchantCanalPayRequest.setAccountNumber(sharedPrefManagerCanalRecap.getAccountNumber());
        merchantCanalPayRequest.setMpin(sharedPrefManagerCanalRecap.getMpin());
        payCanal();
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
        if (context instanceof FragmentCanalplusRecapListener) {
            listener = (FragmentCanalplusRecapListener) context;
        } else {
            Log.d(TAG, "onAttach: transfer account to cash  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;{

        }
    }


    @Override
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorSendAccountToCash = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSendAccountToCash
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        //no need to switch as just one case
                        Bundle bundle = new Bundle();
                        bundle.putString(Utils.PAGE_POUR_CODE, Utils.PAYER_FACTURE_CANALPLUS);

                        CodeEmpreinte codeTransfertcCompteCompte = new CodeEmpreinte();
                        codeTransfertcCompteCompte.setArguments(bundle);
                        codeTransfertcCompteCompte.show(getParentFragmentManager(), Utils.TAG);

                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do here
                    }
                } )
                .withLayer()
                .start();

    }

    public void payCanal(){
        progressBar.setVisibility(View.VISIBLE);
        valider.setVisibility(View.GONE);

        OperationReport canalplusOperationReport = new OperationReport();
        Bundle bundleCanalplusRecap = new Bundle();
        RapportRequest canalplusRecapRapportRequest = new RapportRequest();
        canalplusRecapRapportRequest.setOperation(Utils.PAYER_FACTURE_CANALPLUS);
        canalplusRecapRapportRequest.setTypeRequet(Utils.REQUEST_POUR_AUTHENTICATION);

        Call<StringResponse> payingCanal = canalMerchantPayRestInterface.merchantPay(merchantCanalPayRequest);

        payingCanal.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                progressBar.setVisibility(View.GONE);
                valider.setVisibility(View.VISIBLE);

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("200")) {
                        canalplusRecapRapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        canalplusRecapRapportRequest.setMessage(response.body().getData());
                    }
                    else {
                        canalplusRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        canalplusRecapRapportRequest.setMessage(response.body().getMessage());
                    }

                }
                else  {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString(Utils.STATUS);
                        String message = jsonObject.getString(Utils.MESSAGE);
                        canalplusRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        canalplusRecapRapportRequest.setMessage(status+"\n"+message);
                    }
                    catch (Exception exception){
                        Log.d(TAG, "pay merchant canal recap trying to get status and message from json response, : "+exception.getLocalizedMessage(), exception);
                    }

                }


                bundleCanalplusRecap.putSerializable(Utils.RAPPORT_REQUEST, canalplusRecapRapportRequest );
                canalplusOperationReport.setArguments(bundleCanalplusRecap);
                canalplusOperationReport.show(getParentFragmentManager(), Utils.TAG);

            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                valider.setVisibility(View.VISIBLE);
                canalplusRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                canalplusRecapRapportRequest.setMessage(getString(R.string.network_error)+" : "+t.getLocalizedMessage());
                bundleCanalplusRecap.putSerializable(Utils.RAPPORT_REQUEST, canalplusRecapRapportRequest );
                canalplusOperationReport.setArguments(bundleCanalplusRecap);
                canalplusOperationReport.show(getParentFragmentManager(), Utils.TAG);


            }
        });

    }

}
