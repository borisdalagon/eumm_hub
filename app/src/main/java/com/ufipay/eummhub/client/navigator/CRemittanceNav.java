package com.ufipay.eummhub.client.navigator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoListFacture;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoRecap;
import com.ufipay.eummhub.client.transfertargent.cashversmobile.fragment.FragmentTransfertCashMobileRecap;
import com.ufipay.eummhub.client.transfertargent.compteverscash.fragment.FragmentTransfertCompteCashRecap;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompte2;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompteRecap;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.utils.Utils;

public class CRemittanceNav {

    public static void openAccountToAccountFormAmountFragment(FragmentManager manager, TransfertArgentTEST transfertArgentTEST, SharedPrefManager prefManagerInstance, String phone) {

        prefManagerInstance.setPhoneNumberTransfert(phone);
        transfertArgentTEST.setPhone(phone);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.TRANSFERT_ARGENT, transfertArgentTEST);
        Fragment n = new FragmentTransfertCompteCompte2();

        FragmentTransaction ft = manager.beginTransaction();
        n.setArguments(bundle);
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_transfert_compte_compte, n );
        ft.commit();

    }

    public static void openAccountToAccountRecap(FragmentManager managerGoToRecap, TransfertArgentTEST transfertArgentTEST, SharedPrefManager sharedPrefManagerInstance, String amount){
        sharedPrefManagerInstance.setAmountTransfert(amount);
        transfertArgentTEST.setMontant(amount);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.TRANSFERT_ARGENT, transfertArgentTEST);
        Fragment accountToAccountTransferRecap = new FragmentTransfertCompteCompteRecap();
        FragmentTransaction ft = managerGoToRecap.beginTransaction();
        accountToAccountTransferRecap.setArguments(bundle);
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_transfert_compte_compte, accountToAccountTransferRecap);
        ft.commit();

    }

    public static void openCashToMobileRecap(FragmentManager managerGoToCashMobileRecap, SharedPrefManager sharedPrefCashToMobInstance, String phone, String amount){
        sharedPrefCashToMobInstance.setPhoneNumberTransfert(phone);
        sharedPrefCashToMobInstance.setAmountTransfert(amount);

        Fragment fragmentTransferCashMobileRecap = new FragmentTransfertCashMobileRecap();
        FragmentTransaction ft = managerGoToCashMobileRecap.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_transfert_cash_mobile, fragmentTransferCashMobileRecap);
        ft.commit();

    }

    public static void openAccountToCashRecap(FragmentManager managerGoToAccountToCashRecap){

        Fragment accountToCashTransferRecap = new FragmentTransfertCompteCashRecap();
        FragmentTransaction ft = managerGoToAccountToCashRecap.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container_transfert_compte_cash, accountToCashTransferRecap);
        ft.commit();
    }

}
