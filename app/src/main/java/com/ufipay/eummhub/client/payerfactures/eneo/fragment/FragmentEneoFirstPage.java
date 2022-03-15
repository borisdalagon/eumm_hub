package com.ufipay.eummhub.client.payerfactures.eneo.fragment;

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
import com.ufipay.eummhub.client.payerfactures.eneo.bottomsheet.ListSaveEneoAbonnee;
import com.ufipay.eummhub.client.payerfactures.eneo.bottomsheet.SaveEneoAbonnee;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.classe.AbonneesEneo;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.account.SubAccountsResponse;
import com.ufipay.eummhub.remote.billing.details.BDResponse;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.PBType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentEneoFirstPage extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPyEneo1";
    private BillingRestInterface billingRestInterface;
    private SharedPrefManager sharedPrefBilling;
    private ProgressBar progressBillDetails;

    public static final String SAVED_SUB_ACCOUNTS = "SAVED_SUB_ACCOUNTS";

    FragmentEneoFirstPageListener listener;

    Button eneo1Button;

    RadioButton rdNumerofactureEPF;
    RadioButton rdNumeroAbonneEPF;

    RelativeLayout rDonneeFactureEPF;

    EditText numeroFacture;
    EditText numeroAbonne;

    Gson gson = new Gson();
    AbonneesEneo abonneesEneo = new AbonneesEneo();
    ArrayList<Abonnee> listAbonneeEneo = new ArrayList<>();

    private final TextWatcher eneoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (!numeroFacture.getText().toString().trim().isEmpty()
                    || !numeroAbonne.getText().toString().trim().isEmpty()) {
                eneo1Button.setBackgroundColor(Color.parseColor("#1D5FA7"));
                eneo1Button.setClickable(true);
            }else {
                eneo1Button.setBackgroundColor(Color.parseColor("#818181"));
                eneo1Button.setClickable(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };


    //
    public FragmentEneoFirstPage(){
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

    public interface FragmentEneoFirstPageListener {
        void onInputEneoFirstPageSent(CharSequence input);
    }


    //
    public static FragmentEneoFirstPage newInstance(){
        return new FragmentEneoFirstPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View eneo1View = inflater.inflate( R.layout.fragment_eneo_1, container, false );

        sharedPrefBilling = SharedPrefManager.newInstance(getContext());

        progressBillDetails = eneo1View.findViewById(R.id.progress_bill_details);

        rdNumerofactureEPF = eneo1View.findViewById(R.id.rb_numero_facture);
        rdNumeroAbonneEPF = eneo1View.findViewById(R.id.rb_numero_abonne);
        rdNumerofactureEPF.setOnClickListener(this);
        rdNumeroAbonneEPF.setOnClickListener(this);

        rDonneeFactureEPF = eneo1View.findViewById(R.id.r_donnee_facture);

        numeroAbonne = eneo1View.findViewById(R.id.edt_numero_abonne);

        numeroAbonne.setOnKeyListener((v1, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                openNewFragment();

                return true;
            }
            return false;
        });

        numeroFacture = eneo1View.findViewById(R.id.edt_numero_facture);
        numeroFacture.setOnKeyListener((v12, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                openNewFragment();

                return true;
            }
            return false;
        });

        numeroFacture.addTextChangedListener(eneoTextWatcher);
        numeroAbonne.addTextChangedListener(eneoTextWatcher);

        eneo1Button = eneo1View.findViewById(R.id.suivant);
        eneo1Button.setOnClickListener(v -> {

            ViewPropertyAnimatorCompat viewPropertyAnimatorPayEneo1 = Utils.getViewPropertyAnimatorCompat(v);
            viewPropertyAnimatorPayEneo1
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

        billingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefBilling.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);

        // get saved customer's list
        getCustomerEneoSubAccounts();

        //Condition d'ouverture des comptes stoké en local


        return eneo1View;
    }


    private void openNewFragment(){
        listener.onInputEneoFirstPageSent("Page_formulaire");

        Bundle bundle = new Bundle();
        bundle.putString(Utils.NUMERO_ABONNEE, numeroAbonne.getText().toString().trim());

        //On test si le numero de facture n'est pas vide
        if(!numeroFacture.getText().toString().trim().isEmpty()){

            String bill = numeroFacture.getText().toString();

            getBillDetails(PBType.ENEO.name(), bill);

            return;
        }

        if(!numeroAbonne.getText().toString().trim().isEmpty()) {
            // A ce niveau, le numero de Facture est vide et le numero du compte d'abonnée
            // n'est pas vide
            // Ensuite on test si un compte est déjà enregistré en local
            if(SharedPrefManager.newInstance(getContext()).getListeAbonneEneo() == null ||
                    SharedPrefManager.newInstance(getContext()).getListeAbonneEneo().equals("")){

                // Ici aucun compte n'est pas enregistré en local, alors on lui propose d'enregistrer
                SaveEneoAbonnee saveEneoAbonnee = new SaveEneoAbonnee();
                saveEneoAbonnee.setArguments(bundle);
                saveEneoAbonnee.show(getParentFragmentManager(), Utils.TAG);

            }
            else {

                // Ici au moins un compte est enregistré en local
                // Alors nous recupérons la liste de compte enregistré en local
                abonneesEneo = gson.fromJson(SharedPrefManager.newInstance(getContext())
                        .getListeAbonneEneo(), AbonneesEneo.class);
                listAbonneeEneo = abonneesEneo.getListAbonnee();

                // Ensuite nous parcourons cette liste
                for(int i=0; i<listAbonneeEneo.size(); i++){

                    // A chaque élément, nous verifions si le numero de compte renseigné corespond
                    // à un compte enregistré en local
                    if(listAbonneeEneo.get(i).getNumero().equals(numeroAbonne.getText().toString().trim())){

                        // Ici, le compte renseigné corespond à un compte enregistré en local
                        // Alors nous ouvrons tout simplement le fragment suivant
                        CBillingNav.openEneoListFacture(getParentFragmentManager(),bundle);

                    }else if(i==listAbonneeEneo.size()-1){

                        // Ici Aucun des comptes enregistrés ne correspond au compte renseigné
                        // Alors on lui propose d'enregistrer ce compte
                        SaveEneoAbonnee saveEneoAbonnee = new SaveEneoAbonnee();
                        saveEneoAbonnee.setArguments(bundle);
                        saveEneoAbonnee.show(getParentFragmentManager(), Utils.TAG);

                    }

                }



            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEneoFirstPageListener) {
            listener = (FragmentEneoFirstPageListener) context;
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


    public void getBillDetails(String type, String billNumber){

        progressBillDetails.setVisibility(View.VISIBLE);
        eneo1Button.setVisibility(View.GONE);

        Call<BDResponse> billDetailsResponse = billingRestInterface.getBillDetails(type, billNumber, sharedPrefBilling.getAccountNumber());

        billDetailsResponse.enqueue(new Callback<BDResponse>() {
            @Override
            public void onResponse(Call<BDResponse> call, Response<BDResponse> response) {
                progressBillDetails.setVisibility(View.GONE);
                eneo1Button.setVisibility(View.VISIBLE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        PBRequest eneoPayBillRequest = new PBRequest();
                        eneoPayBillRequest.setBillNumber(billNumber);
                        eneoPayBillRequest.setType(type);
                        eneoPayBillRequest.setAccountNumber(sharedPrefBilling.getAccountNumber());

                        CBillingNav.openEneoRecap(getParentFragmentManager(), eneoPayBillRequest, response.body().getData());
                        return;
                    }
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //TODO : alert
                }

            }

            @Override
            public void onFailure(Call<BDResponse> call, Throwable t) {
                progressBillDetails.setVisibility(View.GONE);
                eneo1Button.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                //todo : alert
            }
        });


    }

    public void getCustomerEneoSubAccounts(){

       Call<SubAccountsResponse> getSubAccounts =  billingRestInterface.getSubAccounts(PBType.ENEO.toString(), sharedPrefBilling.getAccountNumber());

       getSubAccounts.enqueue(new Callback<SubAccountsResponse>() {
           @Override
           public void onResponse(Call<SubAccountsResponse> call, Response<SubAccountsResponse> response) {

               if (response.isSuccessful()){

                   if (response.body().getStatus().equals("200")){

                       if (!response.body().getData().isEmpty()){
                           Bundle savedSubAccountsBundle = new Bundle();
                           savedSubAccountsBundle.putSerializable(SAVED_SUB_ACCOUNTS, response.body());

                           ListSaveEneoAbonnee listSaveEneoAbonnee = new ListSaveEneoAbonnee();
                           listSaveEneoAbonnee.setArguments(savedSubAccountsBundle);
                           listSaveEneoAbonnee.show(getParentFragmentManager(), Utils.TAG);

                           return;
                       }
                       Log.d(TAG, "no sub-account found: ");
                       return;
                   }
                   Log.d(TAG, "onResponse error : ".concat(response.body().getMessage()));

               }

           }

           @Override
           public void onFailure(Call<SubAccountsResponse> call, Throwable t) {
               Log.d(TAG, "onFailure: "+t.getLocalizedMessage(), t);
           }
       });

    }


}
