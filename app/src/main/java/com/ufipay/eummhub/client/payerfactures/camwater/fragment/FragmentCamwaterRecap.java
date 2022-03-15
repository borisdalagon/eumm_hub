package com.ufipay.eummhub.client.payerfactures.camwater.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

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
import com.ufipay.eummhub.databinding.FragmentCamwaterRecapBinding;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.details.BillDetails;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.StringResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentCamwaterRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPayWaterRcp";
    private PBRequest waterPayBillRequest;

    private BillingRestInterface waterBillingRestInterface;
    private SharedPrefManager sharedPrefWaterRecap;


    FragmentCamwaterRecapListener listener;

    Button waterRecapButton;

    private FragmentCamwaterRecapBinding waterRecapBinding;

    ProgressBar progressBar;

    //
    public FragmentCamwaterRecap(){
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

    public interface FragmentCamwaterRecapListener {
        void onInputCamwaterRecapSent(CharSequence input);
    }


    //
    public static FragmentCamwaterRecap newInstance(){
        return new FragmentCamwaterRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        waterRecapBinding = FragmentCamwaterRecapBinding.inflate(inflater, container, false);
        View eneoRecapView = waterRecapBinding.getRoot(); // no more using eneoRecapView = inflater inflate( R.layout.fragment_camwater_recap, container, false ), let's use view binding instead
        //billAccountName eneoRecapView.findViewById(R.id.beneficiary_account_name)


        waterRecapButton = eneoRecapView.findViewById(R.id.valider);
        waterRecapButton.setOnClickListener(this);

        progressBar = eneoRecapView.findViewById(R.id.progressBar);
        progressBar.setOnClickListener(this);

        Bundle waterRecapBundle = getArguments();
        BillDetails billDetails = (BillDetails) waterRecapBundle.getSerializable(CBillingNav.BILL_DETAILS);
        waterPayBillRequest = (PBRequest) waterRecapBundle.getSerializable(CBillingNav.PAY_BILL_REQUEST);

        waterRecapBinding.billDueDate.setText(billDetails.getIssueDate());
        waterRecapBinding.billNumber.setText(waterPayBillRequest.getBillNumber());
        waterRecapBinding.montant.setText(billDetails.getAmount());

        sharedPrefWaterRecap = SharedPrefManager.newInstance(getContext());
        waterBillingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefWaterRecap.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);


        return eneoRecapView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCamwaterRecapListener) {
            listener = (FragmentCamwaterRecapListener) context;
        } else {
            Log.d(TAG, "onAttach: pay eneo recap  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
        //Lancer la requêtte de validation ici
        payWaterBill();
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
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorPayEneoRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorPayEneoRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.valider) {//Pour gérer le KeyBoard
                            Bundle bundle = new Bundle();
                            bundle.putString(Utils.PAGE_POUR_CODE, Utils.PAYER_FACTURE_CAMWATER);

                            CodeEmpreinte codeTransfertcCompteCompte = new CodeEmpreinte();
                            codeTransfertcCompteCompte.setArguments(bundle);
                            codeTransfertcCompteCompte.show(getParentFragmentManager(), Utils.TAG);
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

    public void payWaterBill(){

        OperationReport waterPayBillOperationReport = new OperationReport();

        RapportRequest waterRecapRapportRequest = new RapportRequest();
        waterRecapRapportRequest.setOperation(Utils.PAYER_FACTURE_CAMWATER);

        Bundle bundleWaterRecap = new Bundle();


        waterPayBillRequest.setMpin(sharedPrefWaterRecap.getMpin());
        Call<StringResponse> payEneo = waterBillingRestInterface.payBill(waterPayBillRequest);
        waterRecapButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        payEneo.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                progressBar.setVisibility(View.GONE);
                waterRecapButton.setVisibility(View.VISIBLE);

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("200")){
                        waterRecapRapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        waterRecapRapportRequest.setMessage(getString(R.string.you_will_receive_sms_confirmation)+"\n"+response.body().getMessage());

                    }
                    else  {
                        waterRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        waterRecapRapportRequest.setMessage(response.body().getMessage());
                    }

                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString(Utils.STATUS);
                        String message = jsonObject.getString(Utils.MESSAGE);
                        waterRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        waterRecapRapportRequest.setMessage(status+"\n"+message);
                    }
                    catch (Exception exception){
                        Log.d(TAG, "eneo recap trying to get status and message from json response, : "+exception.getLocalizedMessage(), exception);
                    }
                }

                bundleWaterRecap.putSerializable(Utils.RAPPORT_REQUEST, waterRecapRapportRequest );
                waterPayBillOperationReport.setArguments(bundleWaterRecap);
                waterPayBillOperationReport.show(getParentFragmentManager(), Utils.TAG);
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                waterRecapButton.setVisibility(View.VISIBLE);

                waterRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                waterRecapRapportRequest.setMessage(getString(R.string.networking_error)+t.getLocalizedMessage());

                bundleWaterRecap.putSerializable(Utils.RAPPORT_REQUEST, waterRecapRapportRequest );
                waterPayBillOperationReport.setArguments(bundleWaterRecap);
                waterPayBillOperationReport.show(getParentFragmentManager(), Utils.TAG);
                //18T212.20.0

                /*
                Bundle bundleCamwaterRecap = new Bundle();
        OperationReport camwaterPayBillOperationReport = new OperationReport();
        RapportRequest camwaterRecapRapportRequest = new RapportRequest();
        camwaterRecapRapportRequest.setOperation(Utils.PAYER_FACTURE_CAMWATER);
        camwaterRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);

        bundleCamwaterRecap.putSerializable(Utils.RAPPORT_REQUEST, camwaterRecapRapportRequest );
        camwaterPayBillOperationReport.setArguments(bundleCamwaterRecap);
        camwaterPayBillOperationReport.show(getParentFragmentManager(), Utils.TAG);

                 */
            }
        });

    }


}
