package com.ufipay.eummhub.client.navigator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.payerfactures.camwater.fragment.FragmentCamwaterImpaye;
import com.ufipay.eummhub.client.payerfactures.camwater.fragment.FragmentCamwaterRecap;
import com.ufipay.eummhub.client.payerfactures.canalplus.fragment.FragmentCanalplusOffres;
import com.ufipay.eummhub.client.payerfactures.canalplus.fragment.FragmentCanalplusRecap;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoFirstPage;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoListFacture;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoRecap;
import com.ufipay.eummhub.remote.billing.details.BillDetails;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.merchant.MerchantPayRequest;
import com.ufipay.eummhub.remote.merchant.canal.Offer;

public class CBillingNav {
    public static final String BILL_NUMBER = "BILL_NUMBER";
    public static final String BILL_DETAILS = "BILL_DETAILS";
    public static final String PAY_BILL_REQUEST = "PAY_BILL_REQUEST";
    // ain't needed for ze moment public static final String BILL_NUMBER "BILL_NUMBER"
    public static final String SELECTED_CANAL_OFFER = "SELECTED_CANAL_OFFER";
    public static final String CANAL_PAY_REQUEST = "CANAL_PAY_REQUEST";

    public static void openEneoRecap(FragmentManager managerGoToEneoRecap, PBRequest eneoPayBillRequest, BillDetails billDetails){

        Bundle eneoRecapBundle = new Bundle();
        eneoRecapBundle.putSerializable(BILL_DETAILS, billDetails);
        eneoRecapBundle.putSerializable(PAY_BILL_REQUEST, eneoPayBillRequest);

        Fragment toEneoRecap = new FragmentEneoRecap();
        toEneoRecap.setArguments(eneoRecapBundle);
        FragmentTransaction ft = managerGoToEneoRecap.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_eneo, toEneoRecap);
        ft.commit();
    }

    public static void openEneoListFacture(FragmentManager managerGoToEneoListFacture, Bundle bundle){


        Fragment toEneoListFacture = new FragmentEneoListFacture();
        FragmentTransaction ft = managerGoToEneoListFacture.beginTransaction();
        toEneoListFacture.setArguments(bundle);
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_eneo, toEneoListFacture);
        ft.commit();
    }


    public static void backEneoFistPage(FragmentManager managerGoToEneoFirstPage, Bundle bundle){


        Fragment toEneoEneoFirstPage = new FragmentEneoFirstPage();
        FragmentTransaction ft = managerGoToEneoFirstPage.beginTransaction();
        toEneoEneoFirstPage.setArguments(bundle);
        ft.setCustomAnimations( R.anim.left_to_right, R.anim.right_to_left );
        ft.replace( R.id.fragment_container_eneo, toEneoEneoFirstPage);
        ft.commit();
    }


    public static void openCamwaterRecap(FragmentManager goToWaterRecap, PBRequest waterPayBillRequest, BillDetails billDetails){

        Bundle waterRecapBundle = new Bundle();
        waterRecapBundle.putSerializable(BILL_DETAILS, billDetails);
        waterRecapBundle.putSerializable(PAY_BILL_REQUEST, waterPayBillRequest);

        Fragment toCamwaterRecap = new FragmentCamwaterRecap();
        toCamwaterRecap.setArguments(waterRecapBundle);

        FragmentTransaction ft = goToWaterRecap.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_camwater, toCamwaterRecap);
        ft.commit();
    }

    public static void openCamwaterImpaye(FragmentManager managerGoToCamwaterRecap, String numeroAbonnee){

        Bundle camwaterRecapBundle = new Bundle();
        camwaterRecapBundle.putString(BILL_NUMBER, numeroAbonnee);

        Fragment toCamwaterRecap = new FragmentCamwaterImpaye();
        toCamwaterRecap.setArguments(camwaterRecapBundle);
        FragmentTransaction ft = managerGoToCamwaterRecap.beginTransaction();
        toCamwaterRecap.setArguments(camwaterRecapBundle);
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_camwater, toCamwaterRecap);
        ft.commit();
    }

    public static void openCanalplusOffre(FragmentManager managerGoToOffre, MerchantPayRequest merchantCanalPayRequest){

        Bundle merchantCanalPayRequestBundle = new Bundle();
        merchantCanalPayRequestBundle.putSerializable(CANAL_PAY_REQUEST, merchantCanalPayRequest);

        Fragment toCanalPlusOffers = new FragmentCanalplusOffres();
        toCanalPlusOffers.setArguments(merchantCanalPayRequestBundle);

        FragmentTransaction ft = managerGoToOffre.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_canalplus, toCanalPlusOffers);
        ft.commit();
    }

    public static void openCanalPlusRecap(FragmentManager managerGoToRecap, MerchantPayRequest canalPayRequest){
        Bundle canalRecapBundle = new Bundle();
        canalRecapBundle.putSerializable(CANAL_PAY_REQUEST, canalPayRequest);


        Fragment toCanalPlusRecap = new FragmentCanalplusRecap();
        toCanalPlusRecap.setArguments(canalRecapBundle);
        FragmentTransaction ft = managerGoToRecap.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_canalplus, toCanalPlusRecap);
        ft.commit();
    }

}
