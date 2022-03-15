package com.ufipay.eummhub.client.payerfactures.camwater.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.adapters.AdapterCompteEneo;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.classe.AbonneesEneo;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.billing.account.SubAccountsResponse;

import java.util.ArrayList;
import java.util.List;

import static com.ufipay.eummhub.core.utils.Utils.TAG;

public class ListSaveCamwaterAbonnee extends BottomSheetDialogFragment {

    private SubAccountsResponse waterSavedSubAccountResponse;

    AdapterCompteEneo adapter;
    private RecyclerView recyclerView;
    List<Abonnee> listItems = new ArrayList<>();
    ListSaveCamwaterAbonneeListener listener;

    String stListAbonnee;
    Abonnee abonnee;
    AbonneesEneo abonneesEneo;
    ArrayList<Abonnee> listAbonnee;

    Gson gson = new Gson();

    //this part is no more used as we ain't save is locally, we get saved account from web service
    //PS : even if we had to save it locally, isn't it a bad idea to save it into sharedPref ? I get a better idea is to use database for this kind of data instead shared pref... But this is just my opinion
    //String stListAbonnee       Abonnee abonnee      AbonneesEneo abonneesEneo   ArrayList<Abonnee> listAbonnee     Gson gson = new Gson()

    public ListSaveCamwaterAbonnee() {
        //nothing to do here
    }

    public interface ListSaveCamwaterAbonneeListener {
        void onInputListSaveCamwaterAbonneeSent(CharSequence input);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       // waterSavedSubAccountResponse = (SubAccountsResponse) getArguments().getSerializable(FragmentEneoFirstPage.SAVED_SUB_ACCOUNTS);

        View viewCamwaterCompteList = inflater.inflate(R.layout.bottomsheet_eneo_list_abonnee, container, false);

        recyclerView = viewCamwaterCompteList.findViewById(R.id.recyclerCompte);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        getCompte();

        return viewCamwaterCompteList;
    }

    private void getCompte(){

        listItems.clear();

       // List<SubAccount> waterSubAccounts = waterSavedSubAccountResponse.getData();

        stListAbonnee = SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater();
        abonneesEneo = gson.fromJson(stListAbonnee, AbonneesEneo.class);
        listAbonnee = abonneesEneo.getListAbonnee();


        if(listAbonnee.size() == 0){
            dismiss();
        }else {

            for (int i = 0; i < listAbonnee.size(); i++) {

                addCompteToList(listAbonnee.get(i).getNom(), listAbonnee.get(i).getNumero());
            }

            adapter = new AdapterCompteEneo(getContext(), listItems, new BaseListener() {
                @Override
                public void onClick(int position) {
                    listener.onInputListSaveCamwaterAbonneeSent("Page_formulaire");

                    Bundle bundle = new Bundle();
                    String numeroAbonneeSelectionne = listAbonnee.get(position).getNumero();
                    bundle.putString(Utils.NUMERO_ABONNEE, numeroAbonneeSelectionne);

                    CBillingNav.openCamwaterImpaye(getParentFragmentManager(),numeroAbonneeSelectionne);
                    dismiss();
                }

                @Override
                public void onLongClick(int position) {

                }
            });

            recyclerView.setAdapter(adapter);


        /*
       if(waterSubAccounts.isEmpty()){
           dismiss();
           Toast.makeText(getContext(), "No sub-account found", Toast.LENGTH_SHORT).show();
           return;
       }

        for(int i = 0; i< waterSubAccounts.size(); i++){
            addCompteToList(waterSubAccounts.get(i).getOwner(), waterSubAccounts.get(i).getValue());
        }

        adapter = new AdapterCompteEneo(getContext(), listItems, new BaseListener() {
            @Override
            public void onClick(int position) {
                listener.onInputListSaveCamwaterAbonneeSent("Page_formulaire");

                //TODO : replace this with opening camwater list ?
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

         */

        }

    }


        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListSaveCamwaterAbonneeListener) {
            listener = (ListSaveCamwaterAbonneeListener) context;
        } else {
            Log.d(TAG, "onAttach: saved eneo customer  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private void addCompteToList(String nom, String numero) {
        listItems.add(new Abonnee(nom, numero));
    }

}