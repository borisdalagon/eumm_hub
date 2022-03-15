package com.ufipay.eummhub.core.activities.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.user.BalanceData;
import com.ufipay.eummhub.remote.user.BalanceResponse;
import com.ufipay.eummhub.remote.user.UserRestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ufipay.eummhub.core.utils.Utils.TAG;

public class MainViewModel extends ViewModel {


    MainListener mainListener;
    RapportRequest rapportRequest;
    private BalanceData balanceData;
    private UserRestInterface mainUserRestInterface;

    public MainViewModel(@NonNull MainListener mainListener) {
        this.mainListener = mainListener;

    }

    public void initRetrofit(Context context){
        mainUserRestInterface = new Retrofit.Builder().baseUrl(SharedPrefManager.newInstance(context).getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(UserRestInterface.class);

    }


    public void getBalances (Context context){
        //loaderMainBalance.setVisibility(View.VISIBLE);
        initRetrofit(context);
        Call<BalanceResponse> balanceResponseCall = mainUserRestInterface.getBalance(SharedPrefManager.newInstance(context).getAccountNumber());
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                if (response.isSuccessful()){
                    //loaderMainBalance.setVisibility(View.GONE);
                    if (response.body().getStatus().equals("200")){
                        balanceData = response.body().getData();
                        String mainBal = String.valueOf(balanceData.getPrincipal());
                        mainBal = String.format(mainBal, "### ### ###");
                        //mainBalance.setText(mainBal);
                        mainListener.responseOnView(balanceData, mainBal,true, "", "");
                        return;
                    }
                    //reloadBalances(response.body().getMessage());
                    mainListener.responseOnView(null, null,false,
                            "reload", "");
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);

                    mainListener.responseOnView(null, null,false,
                            message, status);

                    // reloadBalances("Serveur Indisponible : ".concat(message));
                } catch (JSONException | IOException e) {
                    Log.d(TAG, "trying to get status and message from json response, : "+e.getLocalizedMessage(), e);
                }

            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
                mainListener.responseOnView(null, null,false,
                        "", "");
                //loaderMainBalance.setVisibility(View.GONE);
               // reloadBalances(getString(R.string.impossible_to_connect)+t.getLocalizedMessage());
            }
        });
    }


    /*
    private void reloadBalances(String message){
        Utils.getDialogBuilder(this, "", message, getString(R.string.retry), getString(R.string.close))
                .setPositiveButtonListener(this::getBalances)
                .setNegativeButtonListener(()->{

                })
                .build();
    }

     */


    public interface MainListener{

       void responseOnView(BalanceData balanceData, String mainBal, boolean success,
                           String message, String status);

    }

}
