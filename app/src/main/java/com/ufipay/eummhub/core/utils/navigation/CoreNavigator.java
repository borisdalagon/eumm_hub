package com.ufipay.eummhub.core.utils.navigation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.fragments.FragmentOtp;
import com.ufipay.eummhub.core.fragments.FragmentQuestionReponse;

public class CoreNavigator {

    public static void openOtpFragment(FragmentManager fragmentManager) {

        Fragment fragmentOtp = new FragmentOtp();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container, fragmentOtp);
        ft.commit();
    }

    public static void openQuestionFragment(FragmentManager fragmentManager) {

        Fragment n = new FragmentQuestionReponse();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations( R.anim.enter, R.anim.exit );
        ft.replace( R.id.fragment_container, n );
        ft.commit();

    }
}
