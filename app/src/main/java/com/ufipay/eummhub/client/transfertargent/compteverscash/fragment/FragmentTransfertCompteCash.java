package com.ufipay.eummhub.client.transfertargent.compteverscash.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CRemittanceNav;
import com.ufipay.eummhub.core.adapters.AdapterFlag;
import com.ufipay.eummhub.core.bottomsheet.ImportContact;
import com.ufipay.eummhub.core.classe.CurrencyEditText;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.data.Country;
import com.ufipay.eummhub.core.utils.Utils;


public class FragmentTransfertCompteCash extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgTrAccCash";
    private SharedPrefManager accountToCashPrefManager;
    FragmentTransfertCompteCashListener listener;

    String codePaysSelect;
    TextView codePays;

    CardView cardContact;
    Button openAccountToCashRecapButton;

    TextInputEditText edtNumeroPhone;
    TextInputEditText edtNomBeneficiaire;
    CurrencyEditText edtMontant;

    TransfertArgentTEST transfertArgentTEST;

    String tmp;


    private final TextWatcher accountToCashTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            String benefName = edtNomBeneficiaire.getText().toString().trim();
            if (!edtMontant.getText().toString().trim().isEmpty()
                    && !benefName.isEmpty()) {
                tmp = edtNumeroPhone.getText().toString();

                //edtNomBeneficiaire.setText(Utils.replaceSpecialCharacter(benefName));

                openAccountToCashRecapButton.setBackgroundColor(Color.parseColor("#1D5FA7"));
                openAccountToCashRecapButton.setClickable(true);
            }else {
                openAccountToCashRecapButton.setBackgroundColor(Color.parseColor("#818181"));
                openAccountToCashRecapButton.setClickable(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };


    //
    public FragmentTransfertCompteCash(){
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

    public interface FragmentTransfertCompteCashListener {
        void onInputTransfertCompteCashSent(CharSequence input);
    }


    //
    public static FragmentTransfertCompteCash newInstance(){
        return new FragmentTransfertCompteCash();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View accountToCashView = inflater.inflate( R.layout.fragment_tranfert_compte_cash, container, false );

        accountToCashPrefManager = SharedPrefManager.newInstance(getContext());

        openAccountToCashRecapButton = accountToCashView.findViewById(R.id.suivant);
        openAccountToCashRecapButton.setOnClickListener(this);
        openAccountToCashRecapButton.setClickable(false);

        edtNumeroPhone = accountToCashView.findViewById(R.id.edtNumeroPhone);
        edtNumeroPhone.addTextChangedListener(accountToCashTextWatcher);
        edtNumeroPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        edtNomBeneficiaire = accountToCashView.findViewById(R.id.edtNomBeneficiaire);
        edtNomBeneficiaire.addTextChangedListener(accountToCashTextWatcher);

        edtMontant = accountToCashView.findViewById(R.id.edtMontant);
        edtMontant.setRawInputType(Configuration.KEYBOARD_12KEY);
        edtMontant.addTextChangedListener(accountToCashTextWatcher);

        if(accountToCashPrefManager.getPhoneNumberTransfert() != null){
            if (!edtNumeroPhone.getText().toString().trim().equals("")) {
                edtNumeroPhone.setText("");
            }
            edtNumeroPhone.setText(accountToCashPrefManager.getPhoneNumberTransfert());
        }

        if(accountToCashPrefManager.getAmountTransfert() != null){
            edtMontant.setText(accountToCashPrefManager.getAmountTransfert());
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
                transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.PHONE_TRANSFERT_ARGENT);
        }else {
            transfertArgentTEST = new TransfertArgentTEST();
            edtNumeroPhone.setText(accountToCashPrefManager.getPhoneNumberTransfert());
        }

        cardContact = accountToCashView.findViewById(R.id.card_contact);
        cardContact.setOnClickListener(this);

        // Liste des Pays
        codePays = accountToCashView.findViewById(R.id.codePays);
        Spinner spinner = accountToCashView.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        AdapterFlag customAdapter = new AdapterFlag(getContext(), Utils.getFlagCountriesEUMM(), Utils.getCountriesEUMM());
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int frTrAccountCashCountriesSize = Utils.setEummCountriesArray(); //for been sure countries where initialized
                Log.d(TAG, "onItemSelected: countries number : "+frTrAccountCashCountriesSize);

                //get ze selected destination country for account to cash
                Country countryForAccountCash = Utils.getCountriesArrayEUMM().get(position);

                codePaysSelect = countryForAccountCash.getPhoneCode();
                codePays.setText(String.format("+%s", codePaysSelect));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing to do here
            }
        });


        return accountToCashView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransfertCompteCashListener) {
            listener = (FragmentTransfertCompteCashListener) context;
        } else {
            Log.d(TAG, "onAttach: transfer account to cash  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********




    public void ouvrirFragment() {

        String phoneNumber = edtNumeroPhone.getText().toString().trim();
        String amount = edtMontant.getText().toString().trim();
        String beneficiaryName = edtNomBeneficiaire.getText().toString().trim();
        beneficiaryName = Utils.replaceSpecialCharacter(beneficiaryName);

        if (!accountToCashFormIsValid(amount, beneficiaryName))
            return;

        accountToCashPrefManager.setPhoneNumberTransfert("");
        accountToCashPrefManager.setAmountTransfert(amount);
        accountToCashPrefManager.setNomBeneficiaireTransfert(beneficiaryName);

        CRemittanceNav.openAccountToCashRecap(getParentFragmentManager());

    }

    public boolean accountToCashFormIsValid( String accountToCashAmount, String accountToCashBeneficiaryName){

        if ( accountToCashAmount.isEmpty() || accountToCashBeneficiaryName.isEmpty()){

            if (accountToCashAmount.isEmpty())
                edtMontant.setError(getString(R.string.required_field));

            if (accountToCashBeneficiaryName.isEmpty())
                edtNomBeneficiaire.setError(getString(R.string.required_field));


            return false;
        }

        return true;
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
                        switch (v.getId()) {

                            case R.id.card_contact:
                                Bundle bundle = new Bundle();
                                bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_COMPTE_VERS_CASH);

                                ImportContact importContact = new ImportContact();
                                importContact.setArguments(bundle);
                                importContact.show(getActivity().getSupportFragmentManager(), Utils.TAG);

                                break;

                            case R.id.suivant:

                                listener.onInputTransfertCompteCashSent("Page_formulaire");
                                ouvrirFragment();
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
