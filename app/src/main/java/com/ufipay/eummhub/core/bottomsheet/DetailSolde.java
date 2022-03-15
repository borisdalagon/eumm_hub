package com.ufipay.eummhub.core.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.activities.main.MainActivity;
import com.ufipay.eummhub.remote.user.BalanceData;

public class DetailSolde extends BottomSheetDialogFragment {

    public DetailSolde() {
        //required and empty Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_detail_solde, container, false);
        BalanceData balances = (BalanceData) getArguments().get(MainActivity.MAIN_BALANCES_DATA);
        TextView principalBalance = view.findViewById(R.id.principal_balance);
        principalBalance.setText(balances.getPrincipal());
        TextView availableBalance = view.findViewById(R.id.available_balance);
        availableBalance.setText(balances.getAvailable());

        return view;
    }


}
