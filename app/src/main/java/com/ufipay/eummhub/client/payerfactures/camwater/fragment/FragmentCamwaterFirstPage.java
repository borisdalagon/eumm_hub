package com.ufipay.eummhub.client.payerfactures.camwater.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.client.payerfactures.camwater.bottomsheet.ListSaveCamwaterAbonnee;
import com.ufipay.eummhub.client.payerfactures.camwater.bottomsheet.SaveCamwaterAbonnee;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoFirstPage;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.classe.AbonneesCamwater;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.account.SubAccountsResponse;
import com.ufipay.eummhub.remote.billing.details.BDResponse;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.PBType;
import com.ufipay.eummhub.remote.billing.unpaids.UnpaidBillsResponse;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentCamwaterFirstPage extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPyWater1";
    private BillingRestInterface billingRestInterface;
    private SharedPrefManager sharedPrefPayWater;
    private ProgressBar progressWaterBillDetails;


    FragmentCamwaterFirstPageListener listener;

    Button camwater1Button;

    RadioButton rdNumerofactureEPF;
    RadioButton rdNumeroAbonneEPF;

    RelativeLayout rDonneeFactureEPF;

    EditText numeroFacture;
    EditText numeroAbonne;

    Gson gson = new Gson();
    AbonneesCamwater abonneesCamwater = new AbonneesCamwater();
    ArrayList<Abonnee> listAbonneeCamwater = new ArrayList<>();

    private final TextWatcher camwaterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (!numeroFacture.getText().toString().trim().isEmpty()
                    || !numeroAbonne.getText().toString().trim().isEmpty()) {
                camwater1Button.setBackgroundColor(Color.parseColor("#1D5FA7"));
                camwater1Button.setClickable(true);
            }else {
                camwater1Button.setBackgroundColor(Color.parseColor("#818181"));
                camwater1Button.setClickable(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };


    //
    public FragmentCamwaterFirstPage(){
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

    public interface FragmentCamwaterFirstPageListener {
        void onInputCamwaterFirstPageSent(CharSequence input);
    }


    //
    public static FragmentCamwaterFirstPage newInstance(){
        return new FragmentCamwaterFirstPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View camwater1View = inflater.inflate( R.layout.fragment_camwater_1, container, false );

        sharedPrefPayWater = SharedPrefManager.newInstance(getContext());

        progressWaterBillDetails = camwater1View.findViewById(R.id.progress_bill_details);

        rdNumerofactureEPF = camwater1View.findViewById(R.id.rb_numero_facture);
        rdNumeroAbonneEPF = camwater1View.findViewById(R.id.rb_numero_abonne);
        rdNumerofactureEPF.setOnClickListener(this);
        rdNumeroAbonneEPF.setOnClickListener(this);

        rDonneeFactureEPF = camwater1View.findViewById(R.id.r_donnee_facture);

        numeroAbonne = camwater1View.findViewById(R.id.edt_numero_abonne);

        numeroAbonne.setOnKeyListener((v1, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                openNewFragment();
                return true;
            }
            return false;
        });

        numeroFacture = camwater1View.findViewById(R.id.edt_numero_facture);
        numeroFacture.setOnKeyListener((v12, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                openNewFragment();
                return true;
            }
            return false;
        });

        numeroFacture.addTextChangedListener(camwaterTextWatcher);
        numeroAbonne.addTextChangedListener(camwaterTextWatcher);

        camwater1Button = camwater1View.findViewById(R.id.suivant);
        camwater1Button.setOnClickListener(v -> {

            ViewPropertyAnimatorCompat viewPropertyAnimatorPayCamwater1 = Utils.getViewPropertyAnimatorCompat(v);
            viewPropertyAnimatorPayCamwater1
                    .setListener( new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(final View view) {
                            //nothing to do here
                        }

                        @Override
                        public void onAnimationEnd(final View view) {
                            openNewFragment();
                        }

                        @Override
                        public void onAnimationCancel(final View view) {
                            //nothing to do here
                        }
                    } )
                    .withLayer()
                    .start();


        });


        billingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefPayWater.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);

        // get saved customer's list
        getCustomerWaterSubAccounts();
        /*if(SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater().equals("")
           || SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater() == null){

        }else {
            ListSaveCamwaterAbonnee saveWaterCustomer = new ListSaveCamwaterAbonnee();
            saveWaterCustomer.show(getParentFragmentManager(), Utils.TAG);

        }*/

        return camwater1View;
    }


    private void openNewFragment(){
        listener.onInputCamwaterFirstPageSent("Page_formulaire");

        Bundle bundle = new Bundle();
        bundle.putString(Utils.NUMERO_ABONNEE, numeroAbonne.getText().toString().trim());

        //On test si le numero de facture n'est pas vide
        if(!numeroFacture.getText().toString().trim().isEmpty()){

            String bill = numeroFacture.getText().toString();
            getWaterBillDetails(bill);

            return;
        }

        if(!numeroAbonne.getText().toString().trim().isEmpty()) {
            // A ce niveau, le numero de Facture est vide et le numero du compte d'abonnée
            // n'est pas vide
            // Ensuite on test si un compte est déjà enregistré en local
            //String waterAccount = numeroAbonne.getText().toString().trim();
            //getCustomerWaterUnpaid(waterAccount);
            if(SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater() == null ||
                    SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater().equals("")){

                // Ici aucun compte n'est pas enregistré en local, alors on lui propose d'enregistrer
                SaveCamwaterAbonnee saveCamwaterAbonnee = new SaveCamwaterAbonnee();
                saveCamwaterAbonnee.setArguments(bundle);
                saveCamwaterAbonnee.show(getParentFragmentManager(), Utils.TAG);

            }
            else {

                // Ici au moins un compte est enregistré en local
                // Alors nous recupérons la liste de compte enregistré en local
                abonneesCamwater = gson.fromJson(SharedPrefManager.newInstance(getContext())
                        .getListeAbonneCamwater(), AbonneesCamwater.class);
                listAbonneeCamwater = abonneesCamwater.getListAbonnee();

                // Ensuite nous parcourons cette liste
                for(int i=0; i<listAbonneeCamwater.size(); i++){

                    // A chaque élément, nous verifions si le numero de compte renseigné corespond
                    // à un compte enregistré en local
                    if(listAbonneeCamwater.get(i).getNumero().equals(numeroAbonne.getText().toString().trim())){

                        // Ici, le compte renseigné corespond à un compte enregistré en local
                        // Alors nous ouvrons tout simplement le fragment suivant
                        CBillingNav.openCamwaterImpaye(getParentFragmentManager(), numeroAbonne.getText().toString().trim());

                    }else if(i==listAbonneeCamwater.size()-1){

                        // Ici Aucun des comptes enregistrés ne correspond au compte renseigné
                        // Alors on lui propose d'enregistrer ce compte
                        SaveCamwaterAbonnee saveCamwaterAbonnee = new SaveCamwaterAbonnee();
                        saveCamwaterAbonnee.setArguments(bundle);
                        saveCamwaterAbonnee.show(getParentFragmentManager(), Utils.TAG);

                    }

                }




            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCamwaterFirstPageListener) {
            listener = (FragmentCamwaterFirstPageListener) context;
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

        final boolean checked = ((RadioButton)v).isChecked();
        ViewPropertyAnimatorCompat viewPropertyAnimatorSendAccountToCash = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSendAccountToCash
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {

                            case R.id.rb_numero_facture:

                                if(checked) {
                                    suivantNumeroFacture();
                                }
                                break;

                            case R.id.rb_numero_abonne:
                                if(checked) {
                                    suivantNumeroAbonne();
                                }
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

    private void suivantNumeroFacture() {
        numeroFacture.setVisibility(View.VISIBLE);
        numeroAbonne.setVisibility(View.GONE);
        numeroAbonne.setText("");
    }

    private void suivantNumeroAbonne() {
        numeroFacture.setVisibility(View.GONE);
        numeroAbonne.setVisibility(View.VISIBLE);
        numeroFacture.setText("");
    }

    public void getCustomerWaterSubAccounts(){

        Call<SubAccountsResponse> getWaterSubAccounts =  billingRestInterface.getSubAccounts(PBType.CAMWATER.name(), sharedPrefPayWater.getAccountNumber());
        getWaterSubAccounts.enqueue(new Callback<SubAccountsResponse>() {
            @Override
            public void onResponse(Call<SubAccountsResponse> call, Response<SubAccountsResponse> response) {
                Response<SubAccountsResponse> waterSubAccountsResponse = response;
                if (waterSubAccountsResponse.isSuccessful()) {
                    if (waterSubAccountsResponse.body().getStatus().equals("200")){

                        if (!waterSubAccountsResponse.body().getData().isEmpty()){
                            Bundle savedWaterSubAccounts= new Bundle();
                            savedWaterSubAccounts.putSerializable(FragmentEneoFirstPage.SAVED_SUB_ACCOUNTS, waterSubAccountsResponse.body());

                            ListSaveCamwaterAbonnee saveWaterCustomer = new ListSaveCamwaterAbonnee();
                            saveWaterCustomer.setArguments(savedWaterSubAccounts);
                            saveWaterCustomer.show(getParentFragmentManager(), Utils.TAG);

                            return;
                        }
                        Log.d(TAG, "no sub-account found: ");

                        return;
                    }
                    Log.d(TAG, "onResponse error : ".concat(waterSubAccountsResponse.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SubAccountsResponse> call, Throwable t) {
                Log.d(TAG, "getCustomerWaterSubAccounts IOException, "+t.getLocalizedMessage(), t);

            }
        });
    }

    public void getWaterBillDetails(String billNumber){

        progressWaterBillDetails.setVisibility(View.VISIBLE);
        camwater1Button.setVisibility(View.GONE);

        Call<BDResponse> billDetailsResponse = billingRestInterface.getBillDetails(PBType.CAMWATER.name(), billNumber, sharedPrefPayWater.getAccountNumber());

        billDetailsResponse.enqueue(new Callback<BDResponse>() {
            @Override
            public void onResponse(Call<BDResponse> call, Response<BDResponse> response) {
                progressWaterBillDetails.setVisibility(View.GONE);
                camwater1Button.setVisibility(View.VISIBLE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        PBRequest waterPayBillRequest = new PBRequest();
                        waterPayBillRequest.setBillNumber(billNumber);
                        waterPayBillRequest.setType(PBType.CAMWATER.name());
                        waterPayBillRequest.setAccountNumber(sharedPrefPayWater.getAccountNumber());

                        CBillingNav.openCamwaterRecap(getParentFragmentManager(), waterPayBillRequest, response.body().getData());
                        return;
                    }
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //TODO : alert ?
                }

            }

            @Override
            public void onFailure(Call<BDResponse> call, Throwable t) {
                progressWaterBillDetails.setVisibility(View.GONE);
                camwater1Button.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                //todo : alert ??
            }
        });


    }

    public void getCustomerWaterUnpaid(String customerID){

        progressWaterBillDetails.setVisibility(View.VISIBLE);
        Call<UnpaidBillsResponse> getCustomerUnpaid = billingRestInterface.getCustomerUnpaidBills(PBType.CAMWATER.name(), customerID, sharedPrefPayWater.getAccountNumber() );

        getCustomerUnpaid.enqueue(new Callback<UnpaidBillsResponse>() {
            @Override
            public void onResponse(Call<UnpaidBillsResponse> call, Response<UnpaidBillsResponse> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("200")){
                        //
                        Bundle bundle = new Bundle();
                        bundle.putString(Utils.NUMERO_ABONNEE, numeroAbonne.getText().toString().trim());

                        SaveCamwaterAbonnee saveCamwaterAbonnee = new SaveCamwaterAbonnee();
                        saveCamwaterAbonnee.setArguments(bundle);
                        saveCamwaterAbonnee.show(getParentFragmentManager(), Utils.TAG);
                    }

                    Toast.makeText(requireActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<UnpaidBillsResponse> call, Throwable t) {
                Toast.makeText(requireActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


}
