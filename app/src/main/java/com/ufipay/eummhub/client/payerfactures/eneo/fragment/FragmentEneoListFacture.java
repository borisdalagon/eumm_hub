package com.ufipay.eummhub.client.payerfactures.eneo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.client.utils.UtilBottomDialog;
import com.ufipay.eummhub.core.adapters.AdapterFacture;
import com.ufipay.eummhub.core.classe.Facture;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.details.BillDetails;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.PBType;
import com.ufipay.eummhub.remote.billing.unpaids.Bill;
import com.ufipay.eummhub.remote.billing.unpaids.UnpaidBillsResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentEneoListFacture extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private BillingRestInterface billingRestInterface;
    private SharedPrefManager sharedPrefManagerEneoBills;

    private static final String TAG = "fgPyEneo2";
    FragmentEneoListFactureListener listener;

    private ProgressBar loadBills;

    LinearLayout blocAucuneFacture;
    TextView textHearder;

    AdapterFacture adapter;
    private RecyclerView recyclerView;
    List<Facture> listItems = new ArrayList<>();

    Bundle bundle = new Bundle();

    String stNumeroAbonnee;

    RapportRequest rapportRequest = new RapportRequest();

    //
    public FragmentEneoListFacture(){
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

    public interface FragmentEneoListFactureListener {
        void onInputEneoListFactureSent(CharSequence input);
    }

    public static FragmentEneoListFacture newInstance(){
        return new FragmentEneoListFacture();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPrefManagerEneoBills = SharedPrefManager.newInstance(getContext());

        billingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefManagerEneoBills.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);


        View eneo2View = inflater.inflate( R.layout.fragment_eneo_2, container, false );

        textHearder = eneo2View.findViewById(R.id.textHearder);
        blocAucuneFacture = eneo2View.findViewById(R.id.blocAucuneFacture);

        loadBills = eneo2View.findViewById(R.id.load_bills);

        recyclerView = eneo2View.findViewById(R.id.recyclerFacture);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        bundle = getArguments();
        if(bundle != null){
            stNumeroAbonnee = bundle.getString(Utils.NUMERO_ABONNEE);
            getCustomerBills(stNumeroAbonnee);
        }

        return eneo2View;
    }

    private void addFactureToList(String numero, String montant, String issue) {
        listItems.add(new Facture(numero, montant, issue));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEneoListFactureListener) {
            listener = (FragmentEneoListFactureListener) context;
        } else {
            Log.d(TAG, "onAttach: transfer account to cash  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********


    @Override
    public void onClick(final View v) {
        //nothing to do here for the moment
    }

    public void getCustomerBills(String customerID){

        rapportRequest.setOperation(Utils.ENEO_CUSTOMER_BILLS);
        rapportRequest.setTypeRequet(Utils.REQUEST_POUR_NON_AUTHENTICATION);
        rapportRequest.setStatut(Utils.OPERATION_ECHOUE);

        loadBills.setVisibility(View.VISIBLE);

        Call<UnpaidBillsResponse> bills = billingRestInterface.getCustomerUnpaidBills(PBType.ENEO.name(), customerID, sharedPrefManagerEneoBills.getAccountNumber() );

        bills.enqueue(new Callback<UnpaidBillsResponse>() {
            @Override
            public void onResponse(Call<UnpaidBillsResponse> call, Response<UnpaidBillsResponse> response) {
                loadBills.setVisibility(View.GONE);

                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        addBillsOnList(response.body().getData().getBills());
                        return;
                    }
                    rapportRequest.setMessage(response.body().getMessage());
                    UtilBottomDialog.bottomRapport(rapportRequest,getParentFragmentManager());

                }

            }

            @Override
            public void onFailure(Call<UnpaidBillsResponse> call, Throwable t) {
                loadBills.setVisibility(View.GONE);
                rapportRequest.setMessage(getString(R.string.network_error)+t.getLocalizedMessage());
                UtilBottomDialog.bottomRapport(rapportRequest,getParentFragmentManager());
            }
        });


    }

    public void addBillsOnList(List<Bill> bills){
        listItems.clear();

        //if there is no bill
        if (bills.isEmpty()){
            blocAucuneFacture.setVisibility(View.VISIBLE);
            return;
        }

        //adding bills to "listItems" arrayList
        for (int count=0; count<bills.size(); count++){
            addFactureToList(bills.get(count).getBillNumber(), bills.get(count).getAmount(), bills.get(count).getIssueDate());
        }

        //if there is at least one bill, hide the bloc "aucune facture". PS : if no bill found we won't reach this stef as we've the up return
        blocAucuneFacture.setVisibility(View.GONE);

        //setting an adapter for the click event (select one bill)
        adapter = new AdapterFacture(getContext(), listItems, new BaseListener() {
            @Override
            public void onClick(int position) {
                PBRequest payBillRequest = new PBRequest();
                payBillRequest.setType(PBType.ENEO.name());
                payBillRequest.setBillNumber(listItems.get(position).getNumero());
                payBillRequest.setAccountNumber(sharedPrefManagerEneoBills.getAccountNumber());

                BillDetails billDetails = new BillDetails();
                billDetails.setAmount(listItems.get(position).getMontant());
                billDetails.setIssueDate(listItems.get(position).getIssueDate());


                listener.onInputEneoListFactureSent("page_list_facture");
                CBillingNav.openEneoRecap(getParentFragmentManager(), payBillRequest, billDetails);
            }

            @Override
            public void onLongClick(int position) {
                //nothing to do here
            }
        });

        recyclerView.setAdapter(adapter);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
        getCustomerBills(stNumeroAbonnee);
    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


}
