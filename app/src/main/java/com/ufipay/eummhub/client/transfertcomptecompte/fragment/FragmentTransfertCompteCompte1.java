package com.ufipay.eummhub.client.transfertcomptecompte.fragment;

import android.content.Context;
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
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.utils.Utils;


public class FragmentTransfertCompteCompte1 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentTransfertCompteCompte1Listener listener;

    private static  final String TAG = "fgTCC1";

    String codeISO2paysAccountToAccountBeneficiary;
    String paysAccountToAccountBeneficiary;
    String codePaysSelectAccountToAccountBeneficiary;
    String devisePaysSelectAccountToAccountBeneficiary;
    String codePaysTelephoneAccountToAccountBeneficiary;

    TextView codePaysAccountToAccountBeneficiary;

    CardView cardContact;
    Button suivant;

    TextInputEditText edtNumeroPhone;
    String numeroContact;

    TransfertArgentTEST transfertArgentTEST;

    Spinner typeMotif;
    String tmp;


    SharedPrefManager sharedPrefManager;


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edtNumeroPhone.length() >= 9) {
                tmp = edtNumeroPhone.getText().toString();
                suivant.setBackgroundColor(Color.parseColor("#1D5FA7"));
                suivant.setClickable(true);
                return;
            }
                suivant.setBackgroundColor(Color.parseColor("#818181"));
                suivant.setClickable(false);


        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };


    //
    public FragmentTransfertCompteCompte1(){
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

    public interface FragmentTransfertCompteCompte1Listener {
        void onInputTransfertCompteCompte1Sent(CharSequence input);
    }


    //
    public static FragmentTransfertCompteCompte1 newInstance(){
        return new FragmentTransfertCompteCompte1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate( R.layout.fragment_tranfert_compte_compte_1, container, false );
        sharedPrefManager = SharedPrefManager.newInstance(getContext());

        suivant = v.findViewById(R.id.suivant);
        suivant.setOnClickListener(this);
        suivant.setClickable(false);

        edtNumeroPhone = v.findViewById(R.id.edtNumeroPhone);
        edtNumeroPhone.addTextChangedListener(textWatcher);
        edtNumeroPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if(sharedPrefManager.getPhoneNumberTransfert() != null){
            if(!edtNumeroPhone.getText().toString().trim().isEmpty()){
                edtNumeroPhone.setText("");
            }
            edtNumeroPhone.setText(sharedPrefManager.getPhoneNumberTransfert());

        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
                transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.PHONE_TRANSFERT_ARGENT);
        }else {
            transfertArgentTEST = new TransfertArgentTEST();
            edtNumeroPhone.setText(sharedPrefManager.getPhoneNumberTransfert());

        }

        cardContact = v.findViewById(R.id.card_contact);
        cardContact.setOnClickListener(this);

        // Liste des Pays
        codePaysAccountToAccountBeneficiary = v.findViewById(R.id.codePays);
        Spinner spinner = v.findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);

        AdapterFlag customAdapter = new AdapterFlag(getContext(), Utils.getFlagCountriesEUMM(), Utils.getCountriesEUMM());
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                devisePaysSelectAccountToAccountBeneficiary = "XAF";

                switch (position) {
                    case 0:

                        codePaysAccountToAccountBeneficiary.setText("+237");
                        codePaysSelectAccountToAccountBeneficiary = "237";
                        codePaysTelephoneAccountToAccountBeneficiary = "237";
                        codeISO2paysAccountToAccountBeneficiary = "CM";
                        paysAccountToAccountBeneficiary = "CAMEROON";

                        break;
                    case 1:

                        codePaysAccountToAccountBeneficiary.setText("+241");
                        codePaysSelectAccountToAccountBeneficiary = "241";
                        codePaysTelephoneAccountToAccountBeneficiary = "241";
                        codeISO2paysAccountToAccountBeneficiary = "GA";
                        paysAccountToAccountBeneficiary = "GABON";
                        break;
                    case 2:

                        codePaysAccountToAccountBeneficiary.setText("+242");
                        codePaysSelectAccountToAccountBeneficiary = "242";
                        codePaysTelephoneAccountToAccountBeneficiary = "242";
                        codeISO2paysAccountToAccountBeneficiary = "CG";
                        paysAccountToAccountBeneficiary = "CONGO";
                        break;
                    case 3:

                        codePaysAccountToAccountBeneficiary.setText("+235");
                        codePaysSelectAccountToAccountBeneficiary = "235";
                        codePaysTelephoneAccountToAccountBeneficiary = "235";
                        codeISO2paysAccountToAccountBeneficiary = "TD";
                        paysAccountToAccountBeneficiary = "CHAD";
                        break;
                    case 4:

                        codePaysAccountToAccountBeneficiary.setText("+236");
                        codePaysSelectAccountToAccountBeneficiary = "236";
                        codePaysTelephoneAccountToAccountBeneficiary = "236";
                        codeISO2paysAccountToAccountBeneficiary = "CF";
                        paysAccountToAccountBeneficiary = "CAR";
                        break;
                    default:
                        codePaysAccountToAccountBeneficiary.setText("");
                        Log.d(TAG, "default switch position, what to do ?? Zarb");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing to do here
            }
        });


        return v;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransfertCompteCompte1Listener) {
            listener = (FragmentTransfertCompteCompte1Listener) context;
        } else {
            Log.d(TAG, "onAttach: tcc1 must implement FragmentAListener ");

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void ouvrirFragment() {
        String phone = edtNumeroPhone.getText().toString().trim();
        CRemittanceNav.openAccountToAccountFormAmountFragment(getParentFragmentManager(), transfertArgentTEST, sharedPrefManager, phone);
    }



    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCCPhone = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorCCPhone
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
                                bundle.putString(Utils.PAGE_POUR_CODE, Utils.TRANSFERT_COMPTE_COMPTE);

                                ImportContact importContact = new ImportContact();
                                importContact.setArguments(bundle);
                                importContact.show(getActivity().getSupportFragmentManager(), Utils.TAG);

                                break;

                            case R.id.suivant:

                                listener.onInputTransfertCompteCompte1Sent("Numero_destinataire");
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
