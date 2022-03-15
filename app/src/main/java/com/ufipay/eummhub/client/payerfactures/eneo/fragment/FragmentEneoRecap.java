package com.ufipay.eummhub.client.payerfactures.eneo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class FragmentEneoRecap extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPayEneoRcp";
    private BillDetails billDetails;
    private PBRequest eneoPayBillRequest;

    private BillingRestInterface billingRestInterface;

    //private OperationReport eneoPayBillOperationReport

    FragmentEneoRecapListener listener;

    Button eneoRecapButton;

    private TextView billNumber;
    private TextView billDueDate;
    private TextView billAccountName;
    private TextView billAmount;
    private TextView billFees;
    private TextView totalAmount;

    RadioButton rdNumerofactureEPF;
    RadioButton rdNumeroAbonneEPF;

    RelativeLayout rDonneeFactureEPF;

    EditText numeroFacture;
    EditText numeroAbonne;

    ProgressBar progressBar;

    //
    public FragmentEneoRecap(){
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

    public interface FragmentEneoRecapListener {
        void onInputEneoRecapSent(CharSequence input);
    }


    //
    public static FragmentEneoRecap newInstance(){
        return new FragmentEneoRecap();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments()!=null) {
            billDetails = (BillDetails) getArguments().getSerializable(CBillingNav.BILL_DETAILS);
            eneoPayBillRequest = (PBRequest) getArguments().getSerializable(CBillingNav.PAY_BILL_REQUEST);
        }

        SharedPrefManager payEneoRecapPrefManager = SharedPrefManager.newInstance(getContext());


        View eneoRecapView = inflater.inflate( R.layout.fragment_eneo_recap, container, false );

        billNumber = eneoRecapView.findViewById(R.id.bill_number);
        billDueDate = eneoRecapView.findViewById(R.id.bill_due_date);
        billAccountName = eneoRecapView.findViewById(R.id.beneficiary_account_name);
        billAmount = eneoRecapView.findViewById(R.id.montant);
        billFees = eneoRecapView.findViewById(R.id.frais);
        totalAmount = eneoRecapView.findViewById(R.id.montantTotal);

        try {
            billAmount.setText(billDetails.getAmount());
            billDueDate.setText(billDetails.getIssueDate());
            billNumber.setText(eneoPayBillRequest.getBillNumber());
            getEneoBillPayFees();
        }
        catch (NullPointerException exception){
            Toast.makeText(getContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, " "+exception.getLocalizedMessage(), exception);
        }

        eneoRecapButton = eneoRecapView.findViewById(R.id.valider);
        eneoRecapButton.setOnClickListener(this);

        progressBar = eneoRecapView.findViewById(R.id.progressBar);
        progressBar.setOnClickListener(this);

        billingRestInterface = new Retrofit.Builder().baseUrl(payEneoRecapPrefManager.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);


        return eneoRecapView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEneoRecapListener) {
            listener = (FragmentEneoRecapListener) context;
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

        eneoRecapButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        eneoPayBillRequest.setMpin(SharedPrefManager.newInstance(getContext()).getMpin());
        payEneoBill();

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
                            bundle.putString(Utils.PAGE_POUR_CODE, Utils.PAYER_FACTURE_ENEO);

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

    public void getEneoBillPayFees(){
        //TODO
    }

    public void payEneoBill(){

        OperationReport eneoPayBillOperationReport = new OperationReport();

        RapportRequest eneoRecapRapportRequest = new RapportRequest();
        eneoRecapRapportRequest.setOperation(Utils.PAYER_FACTURE_ENEO);

        Bundle bundleEneoRecap = new Bundle();


        Call<StringResponse> payEneo = billingRestInterface.payBill(eneoPayBillRequest);
        progressBar.setVisibility(View.VISIBLE);

        payEneo.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("200")){
                        eneoRecapRapportRequest.setStatut(Utils.OPERATION_REUSSIE);
                        eneoRecapRapportRequest.setMessage(getString(R.string.you_will_receive_sms_confirmation)+"\n"+response.body().getMessage());

                    }
                    else  {
                        eneoRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        eneoRecapRapportRequest.setMessage(response.body().getMessage());
                    }

                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString(Utils.STATUS);
                        String message = jsonObject.getString(Utils.MESSAGE);
                        eneoRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                        eneoRecapRapportRequest.setMessage(status+"\n"+message);
                    }
                    catch (Exception exception){
                        Log.d(TAG, "eneo recap trying to get status and message from json response, : "+exception.getLocalizedMessage(), exception);
                    }
                }

                bundleEneoRecap.putSerializable(Utils.RAPPORT_REQUEST, eneoRecapRapportRequest );
                eneoPayBillOperationReport.setArguments(bundleEneoRecap);
                eneoPayBillOperationReport.show(getParentFragmentManager(), Utils.TAG);
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                eneoRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                eneoRecapRapportRequest.setMessage(getString(R.string.networking_error)+t.getLocalizedMessage());

                bundleEneoRecap.putSerializable(Utils.RAPPORT_REQUEST, eneoRecapRapportRequest );
                eneoPayBillOperationReport.setArguments(bundleEneoRecap);
                eneoPayBillOperationReport.show(getParentFragmentManager(), Utils.TAG);
            }
        });

    }


}
