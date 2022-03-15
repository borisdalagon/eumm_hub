package com.ufipay.eummhub.remote.user;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserRestInterface {

    /**
     * @param accountNumber the account number of the user.
     * @return the balance of provided user. Response base format is status (int), message and data. Data elements are : available, commission, principal.
     * principal represent the main balance. commission, the commission balance. and available is the utilisable part of main balance (main - reserved)
     */
    @GET("/hub-api/users/{accountNumber}/balance")
    Call<BalanceResponse> getBalance(@Path("accountNumber") String accountNumber);


    /**
     *
     * @param accountNumber the account number of the user.
     * @return information about ze provided user. Response base format is status (int), message and data. Data elements are : category, name, phone, status.
     *      *Category
     *      *name
     *      *phone
     *      *status
     */
    @GET("/hub-api/users/{accountNumber}/info")
    Call<AccountInfoResponse> getAccountInfo(@Path("accountNumber") String accountNumber);


    /**
     * @param beneficiaryPhone : the account number than we wanna lookup (find name)
     * @param accountNumber : the user's (connected user) account number
     * @return the beneficiary's phone in an base response object with status, message and data. data here is a String, corresponding to the name we want
     */
    @GET("/hub-api/users/{beneficiary}/name")
    Call<SimpleDataResponse> getUserName(@Path("beneficiary") String beneficiaryPhone, @Query("accountNumber") String accountNumber);



}
