package com.ufipay.eummhub.client.payerfactures.canalplus.fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.merchant.MerchantPayRequest;


public class FragmentCanalplusFirstPage extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPyCanalplus1";

    FragmentCanalplusFirstPageListener listener;

    Button canalplus1Button;

    TextInputEditText edtNumeroAbonnement;

    private MerchantPayRequest merchantCanalPayRequest;


    private final TextWatcher canalplusTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //EditText
            if (!edtNumeroAbonnement.getText().toString().trim().isEmpty()) {
                canalplus1Button.setBackgroundColor(Color.parseColor("#1D5FA7"));
                canalplus1Button.setClickable(true);
            }else {
                canalplus1Button.setBackgroundColor(Color.parseColor("#818181"));
                canalplus1Button.setClickable(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //nothing to do
        }
    };


    //
    public FragmentCanalplusFirstPage(){
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

    public interface FragmentCanalplusFirstPageListener {
        void onInputCanalplusFirstPageSent(CharSequence input);
    }


    //
    public static FragmentCanalplusFirstPage newInstance(){
        return new FragmentCanalplusFirstPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View canalplus1View = inflater.inflate( R.layout.fragment_canalplus_1, container, false );

        edtNumeroAbonnement = canalplus1View.findViewById(R.id.edtNumeroAbonnement);
        edtNumeroAbonnement.addTextChangedListener(canalplusTextWatcher);

        canalplus1Button = canalplus1View.findViewById(R.id.suivant);
        canalplus1Button.setOnClickListener(this);

        if (getArguments()==null)
            merchantCanalPayRequest = new MerchantPayRequest();
        else
            merchantCanalPayRequest = (MerchantPayRequest) getArguments().getSerializable(CBillingNav.CANAL_PAY_REQUEST);


        return canalplus1View;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCanalplusFirstPageListener) {
            listener = (FragmentCanalplusFirstPageListener) context;
        } else {
            Log.d(TAG, "onAttach: transfer account to cash  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorPayCanal1 = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorPayCanal1
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        //we ain't need to use the switch case here as we've only one case : case R.id.suivant:

                        String canalAccountNumber = edtNumeroAbonnement.getText().toString().trim();
                        merchantCanalPayRequest.setReference(canalAccountNumber);

                        listener.onInputCanalplusFirstPageSent("Page_formulaire");
                        CBillingNav.openCanalplusOffre(getParentFragmentManager(), merchantCanalPayRequest);

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
