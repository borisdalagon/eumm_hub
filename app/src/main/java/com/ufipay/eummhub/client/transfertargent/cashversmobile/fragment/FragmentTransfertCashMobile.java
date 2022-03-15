package com.ufipay.eummhub.client.transfertargent.cashversmobile.fragment;

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


public class FragmentTransfertCashMobile extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentTransfertCashMobileListener listener;
    private static final String TAG = "fgTrCashMob";
    private SharedPrefManager sharedPrefCashToMob;


    String codeISO2pays;
    String pays;
    String codePaysSelect;
    String devisePaysSelect;
    String codePaysTelephone;

    TextView codePays;

    CardView cardContact;
    Button suivant;

    TextInputEditText edtNumeroPhone;
    CurrencyEditText edtMontant;

    TransfertArgentTEST transfertArgentTEST;

    String tmp;




    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (edtNumeroPhone.length() >= 9 && !edtMontant.getText().toString().trim().isEmpty()) {
                Log.d("XXX_TEST_1", "OK");
                tmp = edtNumeroPhone.getText().toString();
                suivant.setBackgroundColor(Color.parseColor("#1D5FA7"));
                suivant.setClickable(true);
            }else {
                suivant.setBackgroundColor(Color.parseColor("#818181"));
                suivant.setClickable(false);
                Log.d("XXX_TEST_2", "OK");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d( "","" );
        }
    };


    //
    public FragmentTransfertCashMobile(){
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

    public interface FragmentTransfertCashMobileListener {
        void onInputTransfertCashMobileSent(CharSequence input);
    }


    //
    public static FragmentTransfertCashMobile newInstance(){
        return new FragmentTransfertCashMobile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate( R.layout.fragment_tranfert_cash_mobile, container, false );
        sharedPrefCashToMob = SharedPrefManager.newInstance(getContext());

        suivant = v.findViewById(R.id.suivant);
        suivant.setOnClickListener(this);
        suivant.setClickable(false);

        edtNumeroPhone = v.findViewById(R.id.edtNumeroPhone);
        edtNumeroPhone.addTextChangedListener(textWatcher);
        edtNumeroPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        edtMontant = v.findViewById(R.id.edtMontant);
        edtMontant.setRawInputType(Configuration.KEYBOARD_12KEY);
        edtMontant.addTextChangedListener(textWatcher);

        if(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert() != null){
            if (!edtNumeroPhone.getText().toString().trim().equals("")) {
                edtNumeroPhone.setText("");
            }
            edtNumeroPhone.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());
        }

        if(SharedPrefManager.newInstance(getContext()).getAmountTransfert() != null){
            edtMontant.setText(SharedPrefManager.newInstance(getContext()).getAmountTransfert());
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
                transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.PHONE_TRANSFERT_ARGENT);
        }else {
            transfertArgentTEST = new TransfertArgentTEST();
            edtNumeroPhone.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());

        }

        cardContact = v.findViewById(R.id.card_contact);
        cardContact.setOnClickListener(this);

        // Liste des Pays
        codePays = v.findViewById(R.id.codePays);
        Spinner spinner = v.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        AdapterFlag customAdapter = new AdapterFlag(getContext(), Utils.getFlagCountriesEUMM(), Utils.getCountriesEUMM());
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int countriesSize = Utils.setEummCountriesArray(); //for been sure countries where initialized
                Log.d(TAG, "onItemSelected: countries number : "+countriesSize);

                Country countryForCashMobile = Utils.getCountriesArrayEUMM().get(position);

                codePaysSelect = countryForCashMobile.getPhoneCode();
                codePaysTelephone = countryForCashMobile.getPhoneCode();
                codePays.setText(String.format("+%s", codePaysSelect));
                codeISO2pays = countryForCashMobile.getCodeISO2();
                pays = countryForCashMobile.getName();
                devisePaysSelect = countryForCashMobile.getDevise();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing to do
            }
        });



        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransfertCashMobileListener) {
            listener = (FragmentTransfertCashMobileListener) context;
        } else {
            Log.d(TAG, "onAttach:cash mobile must implement FragmentAListener ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********




    public void ouvrirFragment() {

        String phone = edtNumeroPhone.getText().toString().trim();
        String amount = edtMontant.getText().toString().trim();

        if (cashToMobileFormIsValid(phone, amount))
            CRemittanceNav.openCashToMobileRecap(getParentFragmentManager(), sharedPrefCashToMob, phone, amount);

    }


    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorSendCashToMob = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSendCashToMob
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
                                bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_CASH_VERS_MOBILE);

                                ImportContact importContact = new ImportContact();
                                importContact.setArguments(bundle);
                                importContact.show(getParentFragmentManager(), Utils.TAG);

                                break;

                            case R.id.suivant:

                                listener.onInputTransfertCashMobileSent("Page_formulaire");
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


    public boolean cashToMobileFormIsValid(String phone, String amount){
        if (phone.isEmpty() || amount.isEmpty()){
            if (phone.isEmpty())
                edtNumeroPhone.setError(getString(R.string.required_field));

            if (amount.isEmpty()){
                edtMontant.setError(getString(R.string.required_field));
            }

            return false;
        }


        //the below bloc code ain't necessary unless we add a return in the catch part
        try {
            int am = Integer.parseInt(amount);
            Log.d(TAG, "cashToMobileFormIsValid: amount filled "+am);
        }
        catch (NumberFormatException exception){
            Log.d(TAG, "cashToMobileFormIsValid: "+exception.getLocalizedMessage(), exception);
            edtMontant.setError(getString(R.string.number_format_invalid));
        }

        return true;
    }


}
