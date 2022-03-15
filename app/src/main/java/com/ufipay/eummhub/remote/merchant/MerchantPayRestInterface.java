package com.ufipay.eummhub.remote.merchant;

import com.ufipay.eummhub.remote.billing.pay.StringResponse;
import com.ufipay.eummhub.remote.merchant.canal.OffersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MerchantPayRestInterface {

    @GET("/hub-api/canal-plus")
    Call<OffersResponse> getCanalPlusOffers();

    @POST("/hub-api/pay")
    Call<StringResponse> merchantPay(@Body MerchantPayRequest merchantPayRequest);


}
