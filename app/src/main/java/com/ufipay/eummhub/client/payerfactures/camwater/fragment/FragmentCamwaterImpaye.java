package com.ufipay.eummhub.client.payerfactures.camwater.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.classe.CurrencyEditText;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.databinding.FragmentCamwaterImpayeBinding;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.details.BillDetails;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.PBType;
import com.ufipay.eummhub.remote.billing.unpaids.Bill;
import com.ufipay.eummhub.remote.billing.unpaids.UnpaidBillsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentCamwaterImpaye extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPayCamwaterRcp";
    private BillDetails billDetails;
    private PBRequest waterPayBillRequest;
    private Bill waterBill = new Bill();

    private BillingRestInterface waterBillingRestInterface;
    private SharedPrefManager sharedPrefWaterUnpaid;
    private ProgressBar progressWaterAccountDetails;

    //private OperationReport eneoPayBillOperationReport

    FragmentCamwaterImpayeListener listener;

    Button camwaterImpayeButton;

    private TextView impaye;
    private TextView penalite;
    private TextView minimumPaye;
    private TextView montantTotal;

    private TextView billIssueDate;
    private TextView billAccountNumber;

    private FragmentCamwaterImpayeBinding waterUnpaidBinding;

    RadioButton rbPayerTotal;
    RadioButton rbPayerPartiel;

    LinearLayout reEdtMontant;
    CurrencyEditText amountToPay;

    Bundle bundle = new Bundle();
    String numeroAbonne;

    //
    public FragmentCamwaterImpaye(){
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

    public interface FragmentCamwaterImpayeListener {
        void onInputCamwaterImpayeSent(CharSequence input);
    }

    private final TextWatcher camwaterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (!amountToPay.getText().toString().trim().isEmpty()
                    || !amountToPay.getText().toString().trim().isEmpty()) {

              String stMinimumPaye =  minimumPaye.getText().toString().trim();
              Double dbMinimuimPaye = Double.parseDouble(stMinimumPaye);

              String stAmountToPay =  amountToPay.getText().toString().trim();
              stAmountToPay = stAmountToPay.replace(",","");
              Double dbAmountToPay = Double.parseDouble(stAmountToPay);

              if( dbAmountToPay >= dbMinimuimPaye ){

                  camwaterImpayeButton.setBackgroundColor(Color.parseColor("#1D5FA7"));
                  camwaterImpayeButton.setClickable(true);

              }


            }else {
                camwaterImpayeButton.setBackgroundColor(Color.parseColor("#818181"));
                camwaterImpayeButton.setClickable(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };

    //
    public static FragmentCamwaterImpaye newInstance(){
        return new FragmentCamwaterImpaye();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View waterAccountView = inflater.inflate( R.layout.fragment_camwater_impaye, container, false );

        bundle = getArguments();
        waterUnpaidBinding = FragmentCamwaterImpayeBinding.inflate(inflater, container, false); //let's use view binding nor ?
        waterUnpaidBinding.loaderAmountTotalToPay.setVisibility(View.VISIBLE);
        waterUnpaidBinding.loaderMinimumPayable.setVisibility(View.VISIBLE);
        waterUnpaidBinding.loaderPenalties.setVisibility(View.VISIBLE);
        waterUnpaidBinding.loaderUnpaids.setVisibility(View.VISIBLE);

        sharedPrefWaterUnpaid = SharedPrefManager.newInstance(getContext());

        View waterAccountView = waterUnpaidBinding.getRoot();

        progressWaterAccountDetails = waterAccountView.findViewById(R.id.progressBar);

        waterBillingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefWaterUnpaid.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);

        impaye = waterAccountView.findViewById(R.id.impaye);
        penalite = waterAccountView.findViewById(R.id.penalite);
        minimumPaye = waterAccountView.findViewById(R.id.minimumPaye);
        montantTotal = waterAccountView.findViewById(R.id.montantTotal);

        billIssueDate = waterAccountView.findViewById(R.id.bill_due_date);

        billAccountNumber = waterAccountView.findViewById(R.id.bill_number);

        rbPayerTotal = waterAccountView.findViewById(R.id.rb_payer_total);
        rbPayerPartiel = waterAccountView.findViewById(R.id.rb_payer_partiel);
        rbPayerTotal.setOnClickListener(this);
        rbPayerPartiel.setOnClickListener(this);

        reEdtMontant =  waterAccountView.findViewById(R.id.re_edt_montant);
        amountToPay = waterAccountView.findViewById(R.id.amountToPay);
        amountToPay.addTextChangedListener(camwaterTextWatcher);
        amountToPay.setActivated(false);

        camwaterImpayeButton =  waterAccountView.findViewById(R.id.valider);

        camwaterImpayeButton.setOnClickListener(v -> {

            ViewPropertyAnimatorCompat viewPropertyAnimatorPayEneoRecap = Utils.getViewPropertyAnimatorCompat(v);
            viewPropertyAnimatorPayEneoRecap
                    .setListener( new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(final View view) {
                            //nothing to do here
                        }

                        @Override
                        public void onAnimationEnd(final View view) {
                            PBRequest waterPayBillRequest = new PBRequest();
                            waterPayBillRequest.setType(PBType.CAMWATER.name());
                            waterPayBillRequest.setBillNumber(waterBill.getBillNumber());
                            waterPayBillRequest.setAccountNumber(sharedPrefWaterUnpaid.getAccountNumber());

                            BillDetails billDetails = new BillDetails();
                            billDetails.setAmount(waterBill.getAmount());
                            billDetails.setIssueDate(waterBill.getIssueDate());


                            listener.onInputCamwaterImpayeSent("page_impaye_facture");
                            CBillingNav.openCamwaterRecap(getParentFragmentManager(), waterPayBillRequest, billDetails);
                        }

                        @Override
                        public void onAnimationCancel(final View view) {
                            //nothing to do here
                        }
                    } )
                    .withLayer()
                    .start();

        });

        if (bundle != null){

            numeroAbonne = bundle.getString(CBillingNav.BILL_NUMBER);
            billAccountNumber.setText(numeroAbonne);

            getWaterAccountDetails(numeroAbonne);

        }

        Log.d(TAG, "onCreateView: "+numeroAbonne);


        return waterAccountView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCamwaterImpayeListener) {
            listener = (FragmentCamwaterImpayeListener) context;
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

   @Override
    public void onClick(final View v) {

       final boolean checked = ((RadioButton)v).isChecked();
       ViewPropertyAnimatorCompat viewPropertyAnimatorPayEneoRecap = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorPayEneoRecap
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.rb_payer_total) {
                            if(checked){
                                reEdtMontant.setVisibility(View.GONE);
                                //TODO : Validation pour payement total
                            }

                        }
                        else if (v.getId() == R.id.rb_payer_partiel){
                            if(checked){
                                reEdtMontant.setVisibility(View.VISIBLE);
                            }
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

    public void getWaterAccountDetails(String customerID){
        camwaterImpayeButton.setVisibility(View.GONE);
        progressWaterAccountDetails.setVisibility(View.VISIBLE);

        Call<UnpaidBillsResponse> getWaterUnpaid =waterBillingRestInterface.getCustomerUnpaidBills(PBType.CAMWATER.name(), customerID, sharedPrefWaterUnpaid.getAccountNumber());

        getWaterUnpaid.enqueue(new Callback<UnpaidBillsResponse>() {
            @Override
            public void onResponse(Call<UnpaidBillsResponse> call, Response<UnpaidBillsResponse> response) {
                camwaterImpayeButton.setVisibility(View.VISIBLE);
                progressWaterAccountDetails.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")){

                        waterBill = response.body().getData().getBills().get(0);
                        impaye.setText(waterBill.getAmount());
                        minimumPaye.setText(waterBill.getAmount());
                        montantTotal.setText(waterBill.getAmount());
                        amountToPay.setText(waterBill.getAmount());

                        billIssueDate.setText(waterBill.getIssueDate());

                        waterUnpaidBinding.loaderAmountTotalToPay.setVisibility(View.GONE);
                        waterUnpaidBinding.loaderMinimumPayable.setVisibility(View.GONE);
                        waterUnpaidBinding.loaderPenalties.setVisibility(View.GONE);
                        waterUnpaidBinding.loaderUnpaids.setVisibility(View.GONE);


                        return;
                    }
                    Snackbar.make(camwaterImpayeButton.getRootView(), response.body().getMessage(), Snackbar.LENGTH_LONG).setAction("Ok", null).show();
                }
                
            }

            @Override
            public void onFailure(Call<UnpaidBillsResponse> call, Throwable t) {
                camwaterImpayeButton.setVisibility(View.VISIBLE);
                progressWaterAccountDetails.setVisibility(View.GONE);

                Snackbar.make(camwaterImpayeButton.getRootView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).setAction("Ok", null).show();
            }
        });

    }


}
