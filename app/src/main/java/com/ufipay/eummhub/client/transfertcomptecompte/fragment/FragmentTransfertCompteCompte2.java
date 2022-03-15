package com.ufipay.eummhub.client.transfertcomptecompte.fragment;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CRemittanceNav;
import com.ufipay.eummhub.core.adapters.AdapterFlag2;
import com.ufipay.eummhub.core.classe.CurrencyEditText;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.utils.Utils;


public class FragmentTransfertCompteCompte2 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    FragmentTransfertCompteCompte2Listener listener;

    private SharedPrefManager sharedPrefManager;
    private static final String TAG = "fgAccToAccAmt";

    Button suivant;

    CurrencyEditText edtMontant;


    // Liste des motifs
    Spinner spinnerMotif;
    private String  motifSelectedForAccountToAccount = "approvisionnement compte";
    LinearLayout liCommentaire;
    TextInputEditText edtMotif;

    TransfertArgentTEST transfertArgentTEST;
    TextView numeroPhone;
    TextView nomContact;
    TextView lettre;
    TextView numeroCompte;
    RelativeLayout liContact;
    RelativeLayout liPhone;
    TextView namePageMontant;



    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (edtMontant.getText().toString().trim().isEmpty()) {
                suivant.setBackgroundColor(Color.parseColor("#818181"));
                suivant.setClickable(false);
            }else {
                suivant.setBackgroundColor(Color.parseColor("#1D5FA7"));
                suivant.setClickable(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d( "","" );
        }
    };


    //
    public FragmentTransfertCompteCompte2(){
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

    public interface FragmentTransfertCompteCompte2Listener {
        void onInputTransfertCompteCompte2Sent(CharSequence input);
    }


    //
    public static FragmentTransfertCompteCompte2 newInstance(){
        return new FragmentTransfertCompteCompte2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate( R.layout.fragment_tranfert_compte_compte_2, container, false );
        sharedPrefManager = SharedPrefManager.newInstance(getContext());

        transfertArgentTEST = new TransfertArgentTEST();

        namePageMontant = v.findViewById(R.id.namePageMontant);

        suivant = v.findViewById(R.id.suivant);
        suivant.setOnClickListener(this);
        suivant.setClickable(false);

        liContact = v.findViewById(R.id.liContact);
        liPhone = v.findViewById(R.id.liPhone);
        nomContact = v.findViewById(R.id.nomContact);
        numeroPhone = v.findViewById(R.id.numeroPhone);
        lettre = v.findViewById(R.id.lettre);
        numeroCompte = v.findViewById(R.id.numeroCompte);

        edtMontant = v.findViewById(R.id.edtMontant);
        edtMontant.setRawInputType(Configuration.KEYBOARD_12KEY);
        edtMontant.addTextChangedListener(textWatcher);

        //----------------------------------Gestion de l'allé---------------------------------------
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            transfertArgentTEST = (TransfertArgentTEST) bundle.getSerializable(Utils.TRANSFERT_ARGENT);

            if(transfertArgentTEST.getNomContact() != null){
                liContact.setVisibility(View.VISIBLE);
                liPhone.setVisibility(View.GONE);
                nomContact.setText(transfertArgentTEST.getNomContact());
                lettre.setText(transfertArgentTEST.getNomContact().substring(0,1));
                listener.onInputTransfertCompteCompte2Sent("Numero_destinataire");

            }else {
                liContact.setVisibility(View.GONE);
                liPhone.setVisibility(View.VISIBLE);
            }

            if(transfertArgentTEST.getPhone() != null){
                numeroPhone.setText(transfertArgentTEST.getPhone());
            }

            if(transfertArgentTEST.getMontant() != null) {
                edtMontant.setText(transfertArgentTEST.getMontant());
            }

            if(sharedPrefManager.getAmountTransfert() != null) {
                edtMontant.setText(SharedPrefManager.newInstance(getContext())
                        .getAmountTransfert());
            }

            if(sharedPrefManager.getPhoneNumberTransfert() != null) {
                numeroCompte.setText(SharedPrefManager.newInstance(getContext())
                        .getPhoneNumberTransfert());
            }



        }else {

            retourDesDonnees();
        }



        liCommentaire = v.findViewById(R.id.li_commentaire);
        edtMotif = v.findViewById(R.id.edtMotif);
        spinnerMotif = v.findViewById(R.id.simpleSpinner);
        AdapterFlag2 customAdapterMotif = new AdapterFlag2(getContext(), Utils.getFigurantMotifs(), Utils.getMotifs() );
        spinnerMotif.setAdapter(customAdapterMotif);
        spinnerMotif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //METHOD 1: Get text from selected item's position & set it to TextView
                //METHOD 2: Get the position of item selected, & perform specific task
                if (position==9){
                    motifSelectedForAccountToAccount = "Autres...";
                    liCommentaire.setVisibility(View.VISIBLE);
                    return;
                }

                liCommentaire.setVisibility(View.GONE);
                edtMotif.setText(null);
                motifSelectedForAccountToAccount = Utils.getMotifSelectedAt(position);
                Log.d(TAG, "onItemSelectedMontif: ".concat(motifSelectedForAccountToAccount));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//nothing to do here
            }
        });


        return v;

    }

    public void retourDesDonnees(){

        if(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert() != null
                && SharedPrefManager.newInstance(getContext()).getAmountTransfert() != null
                && SharedPrefManager.newInstance(getContext()).getNomContactTransfert().equals("")){

            liContact.setVisibility(View.GONE);
            liPhone.setVisibility(View.VISIBLE);

            numeroPhone.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());
            numeroCompte.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());
            edtMontant.setText(SharedPrefManager.newInstance(getContext()).getAmountTransfert());
        }
        else if(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert() != null
                && SharedPrefManager.newInstance(getContext()).getAmountTransfert() != null
                && SharedPrefManager.newInstance(getContext()).getNomContactTransfert() != null
                && !SharedPrefManager.newInstance(getContext()).getNomContactTransfert().equals("")){

            liContact.setVisibility(View.VISIBLE);
            liPhone.setVisibility(View.GONE);

            try{
                nomContact.setText(SharedPrefManager.newInstance(getContext()).getNomContactTransfert());
                lettre.setText(SharedPrefManager.newInstance(getContext()).getNomContactTransfert().substring(0,1));

            }catch (StringIndexOutOfBoundsException s){
                Log.d(TAG,s.getLocalizedMessage(), s);
            }

            numeroPhone.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());
            edtMontant.setText(SharedPrefManager.newInstance(getContext()).getAmountTransfert());
            numeroCompte.setText(SharedPrefManager.newInstance(getContext()).getPhoneNumberTransfert());

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTransfertCompteCompte2Listener) {
            listener = (FragmentTransfertCompteCompte2Listener) context;
        } else {
            Log.d(TAG, "onAttach: account to account 2 must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void ouvrirFragment() {

        String amount = edtMontant.getText().toString().trim();
        CRemittanceNav.openAccountToAccountRecap(getParentFragmentManager(), transfertArgentTEST, sharedPrefManager,amount);

    }



    @Override
    public void onClick(final View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorAccountToAccountAmountForm = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorAccountToAccountAmountForm
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.suivant) {
                            ouvrirFragment();
                            listener.onInputTransfertCompteCompte2Sent("Montant_transfert");
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
