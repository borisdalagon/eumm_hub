package com.ufipay.eummhub.client.payerfactures.canalplus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.Snackbar;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.adapters.AdapterOffreCanalplus;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.merchant.MerchantPayRequest;
import com.ufipay.eummhub.remote.merchant.MerchantPayRestInterface;
import com.ufipay.eummhub.remote.merchant.canal.Offer;
import com.ufipay.eummhub.remote.merchant.canal.OffersResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentCanalplusOffres extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "fgPyCanalplusOffre";

    private MerchantPayRestInterface canalMerchantPayRestInterface;
    private MerchantPayRequest merchantCanalPayRequest;

    FragmentCanalplusOffreListener listener;

    AdapterOffreCanalplus adapter;
    private RecyclerView recyclerView;
    List<Offer> listItems = new ArrayList<>();

    //
    public FragmentCanalplusOffres(){
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

    public interface FragmentCanalplusOffreListener {
        void onInputCanalplusOffreSent(CharSequence input);
    }

    //
    public static FragmentCanalplusOffres newInstance(){
        return new FragmentCanalplusOffres();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPrefManager sharedPrefManagerCanal = SharedPrefManager.newInstance(getContext());

        View canalplusOffreView = inflater.inflate( R.layout.fragment_canalplus_offre, container, false );

        recyclerView = canalplusOffreView.findViewById(R.id.recyclerOffres);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        canalMerchantPayRestInterface = new Retrofit.Builder().baseUrl(sharedPrefManagerCanal.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(MerchantPayRestInterface.class);

        merchantCanalPayRequest = (MerchantPayRequest) getArguments().getSerializable(CBillingNav.CANAL_PAY_REQUEST);

        getCanalOffers();
       //ain't more used  listeOffre, replaced by getCanalOffers

        return canalplusOffreView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCanalplusOffreListener) {
            listener = (FragmentCanalplusOffreListener) context;
        } else {
            Log.d(TAG, "onAttach: transfer account to cash  must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;{

        }
    }


    @Override
    public void onClick(final View v) {

        ViewPropertyAnimatorCompat viewPropertyAnimatorCanalOffers = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorCanalOffers
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        //no used... deleted as we'd only one empty case bloc (case R.id.suivant)
                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do here
                    }
                } )
                .withLayer()
                .start();

    }

    public void getCanalOffers(){

        Call<OffersResponse> getOffers = canalMerchantPayRestInterface.getCanalPlusOffers();

        getOffers.enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("200")) {

                        listItems.clear();

                        //adding all gotten offers to the list
                        listItems.addAll(response.body().getData());

                        //setting an adapter for the click event (select one bill)
                        adapter = new AdapterOffreCanalplus(getContext(), listItems, new BaseListener() {
                            @Override
                            public void onClick(int position) {

                                Offer selectedOffer = listItems.get(position);

                                merchantCanalPayRequest.setAmount(selectedOffer.getAmount());
                                merchantCanalPayRequest.setMerchant(selectedOffer.getName());

                                listener.onInputCanalplusOffreSent("Page_offre");
                                CBillingNav.openCanalPlusRecap(getParentFragmentManager(),merchantCanalPayRequest);
                            }

                            @Override
                            public void onLongClick(int position) {
                                //nothing to do here
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        return;

                    }

                    Snackbar.make(recyclerView.getRootView(), response.body().getMessage(), Snackbar.LENGTH_SHORT).setAction("Ok", null).show();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);
                    //waterRecapRapportRequest.setStatut(Utils.OPERATION_ECHOUE);
                    //waterRecapRapportRequest.setMessage(status+"\n"+message);
                    Snackbar.make(recyclerView.getRootView(), status+":"+message, Snackbar.LENGTH_SHORT).setAction("Ok", null).show();
                }
                catch (Exception exception){
                    Log.d(TAG, "eneo recap trying to get status and message from json response, : "+exception.getLocalizedMessage(), exception);
                }


            }

            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Snackbar.make(recyclerView.getRootView(), t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).setAction("Ok", null).show();

            }
        });

    }

}
