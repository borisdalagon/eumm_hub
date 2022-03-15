package com.ufipay.eummhub.client.payerfactures.eneo.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.client.payerfactures.eneo.fragment.FragmentEneoFirstPage;
import com.ufipay.eummhub.core.adapters.AdapterCompteEneo;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.billing.account.SubAccount;
import com.ufipay.eummhub.remote.billing.account.SubAccountsResponse;

import java.util.ArrayList;
import java.util.List;

import static com.ufipay.eummhub.core.utils.Utils.TAG;

public class ListSaveEneoAbonnee extends BottomSheetDialogFragment {

    private SubAccountsResponse subAccountSaved;
    private List<SubAccount> subAccounts;

    AdapterCompteEneo adapter;
    private RecyclerView recyclerView;
    List<Abonnee> listItems = new ArrayList<>();
    ListSaveEneoAbonneeListener listener;

    //this part is no more used as we ain't save is locally, we get saved account from web service
    //PS : even if we had to save it locally, isn't it a bad idea to save it into sharedPref ? I get a better idea is to use database for this kind of data instead shared pref... But this is just my opinion
    //String stListAbonnee       Abonnee abonnee      AbonneesEneo abonneesEneo   ArrayList<Abonnee> listAbonnee     Gson gson = new Gson()

    public ListSaveEneoAbonnee() {
        //nothing to do here
    }

    public interface ListSaveEneoAbonneeListener {
        void onInputListSaveEneoAbonneeSent(CharSequence input);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        subAccountSaved = (SubAccountsResponse) getArguments().getSerializable(FragmentEneoFirstPage.SAVED_SUB_ACCOUNTS);

        View viewEneoCompteList = inflater.inflate(R.layout.bottomsheet_eneo_list_abonnee, container, false);

        recyclerView = viewEneoCompteList.findViewById(R.id.recyclerCompte);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        getCompte();

        return viewEneoCompteList;
    }

    private void getCompte(){

        listItems.clear();

       subAccounts = subAccountSaved.getData();


       if(subAccounts.isEmpty()){
           dismiss();
           Toast.makeText(getContext(), "No sub-account found", Toast.LENGTH_SHORT).show();
           return;
       }

        for(int i=0; i<subAccounts.size(); i++){
            addCompteToList(subAccounts.get(i).getOwner(), subAccounts.get(i).getValue());
        }

        adapter = new AdapterCompteEneo(getContext(), listItems, new BaseListener() {
            @Override
            public void onClick(int position) {
                listener.onInputListSaveEneoAbonneeSent("Page_formulaire");

                Bundle bundle = new Bundle();
                String numeroAbonneeSelectionne = listItems.get(position).getNumero();
                bundle.putString(Utils.NUMERO_ABONNEE, numeroAbonneeSelectionne);
                CBillingNav.openEneoListFacture(getParentFragmentManager(),bundle);

                dismiss();
            }

            @Override
            public void onLongClick(int position) {
                //nothing to do here
            }
        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListSaveEneoAbonneeListener) {
            listener = (ListSaveEneoAbonneeListener) context;
        } else {
            Log.d(TAG, "onAttach: saved eneo customer  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********

    private void addCompteToList(String nom, String numero) {
        listItems.add(new Abonnee(nom, numero));
    }


}
