package com.ufipay.eummhub.core.bottomsheet;

import android.content.Context;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.bumptech.glide.Glide;
import com.dev21.fingerprintassistant.FingerprintAuthListener;
import com.dev21.fingerprintassistant.FingerprintHelper;
import com.dev21.fingerprintassistant.FingerprintResultsHandler;
import com.dev21.fingerprintassistant.ResponseCode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kevalpatel.passcodeview.PinView;
import com.kevalpatel.passcodeview.interfaces.AuthenticationListener;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.classe.CustomerMpin;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static android.view.View.GONE;

public class CodeEmpreinte extends BottomSheetDialogFragment implements FingerprintAuthListener, View.OnClickListener {

    private static final String TAG = "code";
    //PassCodeView
    private static final String ARG_CURRENT_PIN = "current_pin";
    private PinView mPinView;
    RelativeLayout mpinFingerPrint;
    CustomerMpin customerMpin = new CustomerMpin();
    ImageView close;

    int valueKeyBoard = 6;

    private FingerprintHelper fingerPrintHelper;
    private FingerprintResultsHandler fingerprintResultsHandler;

    ImageView imgFinger;
    TextView txtEmpreinte;
    LinearLayout blocEmpreinte;

    String operation;

    TextView titreCode;
    TextView titreCode1;



    public CodeEmpreinte() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_code, container, false);
        SharedPrefManager prefManagerCode = SharedPrefManager.newInstance(getContext());

        titreCode1 = v.findViewById(R.id.titre_code_1);
        titreCode = v.findViewById(R.id.titre_code);
        titreCode.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            operation = bundle.getString(Utils.PAGE_POUR_CODE);
        }

            // Pour éttendre le BottomSheetDialog
        v.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();

            FrameLayout bottomSheet = (FrameLayout)
                    dialog.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(0);
        });


        imgFinger = v.findViewById(R.id.finger);
        txtEmpreinte = v.findViewById(R.id.txt_empreinte);
        blocEmpreinte = v.findViewById(R.id.bloc_empreinte);


        //---------------------------------------PassCodeView---------------------------------------
        mpinFingerPrint = (RelativeLayout) v.findViewById(R.id.mpinFingerPrint);
        mpinFingerPrint.setVisibility(View.VISIBLE);
        mPinView = v.findViewById(R.id.pattern_view);

        close = (ImageView)v.findViewById(R.id.close);
        close.setOnClickListener(this);
        //Set the correct pin code.
        //REQUIRED
        String pin = prefManagerCode.getMpin();
        int[] correctPinPattern;
        if (pin == null || pin.length()!=4) {
            correctPinPattern = new int[]{};
            Log.d(TAG, "correctPin null : ");
        }
        else {
            int first = Integer.parseInt(""+pin.charAt(0));
            int second = Integer.parseInt(""+pin.charAt(1));
            int third = Integer.parseInt(""+pin.charAt(2));
            int four = Integer.parseInt(""+pin.charAt(3));

            correctPinPattern = new int[]{first, second, third, four};
        }

        customerMpin.correctMpin(mPinView, correctPinPattern);
        customerMpin.keyMethod(mPinView);
        customerMpin.indicatorMethod(mPinView);
        customerMpin.keyBoard(mPinView, getContext(), valueKeyBoard);

        mPinView.setAuthenticationListener(new AuthenticationListener() {
            @Override
            public void onAuthenticationSuccessful() {

                EventBus.getDefault().post(Utils.AUTHENFICATION_REUSSIE);

                dismiss();

            }

            @Override
            public void onAuthenticationFailed() {
                //Calls whenever authentication is failed or user is unauthorized.
                //Do something
                showToast("Code incorrect");
            }
        });


        //-------------------------------------Gestion d'empreinte----------------------------------

        // Check if we're running on Android 6.0 (M) or higher


        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Fingerprint API only available on from Android 6.0 (M)
                FingerprintManager fingerprintManager = (FingerprintManager) getContext().getSystemService( Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    blocEmpreinte.setVisibility(GONE);
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    blocEmpreinte.setVisibility(GONE);
                } else {
                    // Everything is ready for fingerprint authentication
                    registerForFingerprintService();
                }
            }

        }catch (NullPointerException n){
            blocEmpreinte.setVisibility(GONE);
        }

        return v;
    }


    // Methode utilisées pour le PassCodeView
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntArray(ARG_CURRENT_PIN, mPinView.getCurrentTypedPin());
        super.onSaveInstanceState(outState);    }


    @Override
    public void onClick(View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorCompat
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (v.getId()) {

                            case R.id.titre_code:

                                dismiss();

                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                        Log.d("","");
                    }
                } )
                .withLayer()
                .start();


    }



    //-------------------------------------Gestion d'empreinte--------------------------------------
    private void registerForFingerprintService() {

        fingerPrintHelper = new FingerprintHelper(getActivity(), "Test");

        switch (fingerPrintHelper.checkAndEnableFingerPrintService()) {
            case ResponseCode.FINGERPRINT_SERVICE_INITIALISATION_SUCCESS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintResultsHandler = new FingerprintResultsHandler(getContext());
                    fingerprintResultsHandler.setFingerprintAuthListener(this);
                    fingerprintResultsHandler.startListening(fingerPrintHelper.getFingerprintManager(), fingerPrintHelper.getCryptoObject());
                }

                break;
            case ResponseCode.OS_NOT_SUPPORTED:
                Log.d("XXX_3", "ok");
                showToast("Le système d'exploitation ne prend pas en charge les API d'empreintes digitales");
                blocEmpreinte.setVisibility(GONE);
                break;
            case ResponseCode.FINGER_PRINT_SENSOR_UNAVAILABLE:
                showToast("Capteur d'empreintes digitales introuvable");
                blocEmpreinte.setVisibility(GONE);
                break;
            case ResponseCode.ENABLE_FINGER_PRINT_SENSOR_ACCESS:
                showToast("Donner accès à l'utilisation du capteur d'empreintes digitales.");
                blocEmpreinte.setVisibility(GONE);
                break;
            case ResponseCode.NO_FINGER_PRINTS_ARE_ENROLLED:
                showToast("Aucune empreinte digitale trouvée.");
                blocEmpreinte.setVisibility(GONE);
                break;
            case ResponseCode.FINGERPRINT_SERVICE_INITIALISATION_FAILED:
                showToast("L'initialisation du service d'empreintes digitales a échoué");
                blocEmpreinte.setVisibility(GONE);
                break;
            case ResponseCode.DEVICE_NOT_KEY_GUARD_SECURED:
                showToast("L'appareil n'est pas protégé par un garde-clé");
                blocEmpreinte.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start fingerprint scanning and listen for fingerprint callbacks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintResultsHandler != null && !fingerprintResultsHandler.isAlreadyListening()) {
                fingerprintResultsHandler.startListening(fingerPrintHelper.getFingerprintManager(),
                        fingerPrintHelper.getCryptoObject());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop fingerprint scanning and listening for fingerprint callbacks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintResultsHandler != null) {
                fingerprintResultsHandler.stopListening();
            }
        }
    }

    @Override
    public void onAuthentication(int helpOrErrorCode, CharSequence infoString,
                                 FingerprintManager.AuthenticationResult authenticationResult,
                                 int authCode) {
        switch (authCode) {
            case ResponseCode.AUTH_HELP:
                // Show appropriate message
            case ResponseCode.AUTH_ERROR:
                // Show appropriate message
                break;

            case ResponseCode.AUTH_FAILED:
                // Show appropriate message
                txtEmpreinte.setText(getString(R.string.authentication_failed));
                txtEmpreinte.setTextColor(Color.parseColor("#C90044"));
                Glide.with(getActivity())
                        .load(R.drawable.ic__fingerprint_rouge)
                        .into(imgFinger);
                break;

            case ResponseCode.AUTH_SUCCESS:
                // Do whatever you want
                Glide.with(getActivity())
                        .load(R.drawable.ic_done_deep)
                        .into(imgFinger);
                txtEmpreinte.setText(R.string.authentication_succeeded);
                txtEmpreinte.setTextColor(Color.parseColor("#00B806"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintResultsHandler.restartListening(fingerPrintHelper.getFingerprintManager(),
                            fingerPrintHelper.getCryptoObject());
                }

                EventBus.getDefault().post(Utils.AUTHENFICATION_REUSSIE);

                dismiss();

                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
