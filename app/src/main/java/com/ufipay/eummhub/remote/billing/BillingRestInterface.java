package com.ufipay.eummhub.remote.billing;

import com.ufipay.eummhub.remote.billing.account.SubAccount;
import com.ufipay.eummhub.remote.billing.account.SubAccountResponse;
import com.ufipay.eummhub.remote.billing.account.SubAccountsResponse;
import com.ufipay.eummhub.remote.billing.details.BDResponse;
import com.ufipay.eummhub.remote.billing.pay.PBRequest;
import com.ufipay.eummhub.remote.billing.pay.StringResponse;
import com.ufipay.eummhub.remote.billing.unpaids.UnpaidBillsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BillingRestInterface {


    @GET("hub-api/bills/{type}/unpaid/{customerID}")
    Call<UnpaidBillsResponse> getCustomerUnpaidBills(@Path("type") String type, @Path("customerID") String customerID, @Query("accountNumber") String accountNumber);


    @GET("hub-api/bills/{type}/details/{billNumber}")
    Call<BDResponse> getBillDetails(@Path("type") String type,
                                    @Path("billNumber") String billNumber,
                                    @Query("accountNumber") String accountNumber);



    @POST("hub-api/bills")
    Call<StringResponse> payBill(@Body PBRequest payBillRequest);


    @POST("hub-api/bills/sub-accounts")
    Call<SubAccountResponse> addSubAccount(@Body SubAccount addSubAccountRequest);


    @GET("hub-api/bills/{type}/sub-accounts")
    Call<SubAccountsResponse> getSubAccounts(@Path("type") String billType,
                                             @Query("accountNumber") String  accountNumber);



    @DELETE("/bills/sub-accounts/{id}")
    Call<SubAccount> deleteSubAccount(@Path("id") String subAccountId);


}
