package com.ufipay.eummhub.remote.remittance;

import com.ufipay.eummhub.remote.BaseResponse;
import com.ufipay.eummhub.remote.user.SimpleDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemittanceRestInterface {

    /**
     * @param transferOperation : an enum of following elements : PTOP, CASH_TO_MOBILE, CASH_TO_CASH, CASH_TO_BANK
     * @param transferRequest : object for transfer request (for json!:-). must have following fields (or...) : accountNumber, amount, beneficiary and mpin;
     *                         accountNumber represent the account of connected user,
     *                         mpin is the pin of connected user (he has to provide his mpin to exec any op)
     *                         beneficiary is the beneficiary's account number of
     *                         amount is the amount to send
     * @return a baseResponse : status (int), message and data wich is just a string
     */

    @POST("/hub-api/transfers/{transferOperationEnum}")
    Call<SimpleDataResponse> transfers(@Path("transferOperationEnum") String transferOperation,
                                       @Body TransferRequest transferRequest);



    

}
