package com.ufipay.eummhub.client.utils;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.ufipay.eummhub.core.bottomsheet.OperationReport;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.utils.Utils;

public class UtilBottomDialog {

    public static void bottomRapport(RapportRequest rapportRequest, FragmentManager fragmentManager){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );
        OperationReport operationReport = new OperationReport();
        operationReport.setArguments(bundle);
        operationReport.show(fragmentManager, Utils.TAG);
    }

}
