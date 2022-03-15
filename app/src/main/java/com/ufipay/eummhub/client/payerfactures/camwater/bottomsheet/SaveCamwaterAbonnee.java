package com.ufipay.eummhub.client.payerfactures.camwater.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.navigator.CBillingNav;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.classe.AbonneesEneo;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.billing.BillingRestInterface;
import com.ufipay.eummhub.remote.billing.account.SubAccount;
import com.ufipay.eummhub.remote.billing.account.SubAccountResponse;
import com.ufipay.eummhub.remote.billing.pay.PBType;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveCamwaterAbonnee extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "SvPyWater1";
    private BillingRestInterface waterBillingRestInterface;
    private SharedPrefManager sharedPrefSaveWaterCustomer;

    TextView numeroCompte;
    TextInputEditText nomCompte;
    Button sauvegarder;
    Button refuser;

    String numero;
    String nom;

    Gson gson = new Gson();
    ArrayList<Abonnee> abonneeList = new ArrayList<>();
    AbonneesEneo abonneesEneo = new AbonneesEneo();
    Abonnee abonnee = new Abonnee();
    String stAbonneesCamwater;


    Bundle bundle = new Bundle();

    public SaveCamwaterAbonnee() {
        //required and empty Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPrefSaveWaterCustomer = SharedPrefManager.newInstance(getContext());

        View saveEneoAbonneeView = inflater.inflate(R.layout.bottomsheet_eneo_save_abonnee, container, false);

        numeroCompte = saveEneoAbonneeView.findViewById(R.id.numeroCompte);
        nomCompte = saveEneoAbonneeView.findViewById(R.id.nomCompte);

        sauvegarder = saveEneoAbonneeView.findViewById(R.id.sauvegarder);
        sauvegarder.setOnClickListener(this);

        refuser = saveEneoAbonneeView.findViewById(R.id.refuser);
        refuser.setOnClickListener(this);

        bundle = getArguments();

        if(bundle != null){
            numeroCompte.setText(String.format("N° %s", bundle.getString(Utils.NUMERO_ABONNEE)));
        }

        waterBillingRestInterface = new Retrofit.Builder().baseUrl(sharedPrefSaveWaterCustomer.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(BillingRestInterface.class);



        return saveEneoAbonneeView;
    }


    @Override
    public void onClick(View v) {

         numero = numeroCompte.getText().toString().trim();
         numero = numero.replace("N° ", "");
         nom = nomCompte.getText().toString().trim();

        ViewPropertyAnimatorCompat viewPropertyAnimatorSaveEneoCustomer = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorSaveEneoCustomer
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        if (v.getId() == R.id.sauvegarder) {
                            //test modified cuz' condition <<nomCompte.getText().toString().trim() == null>> no really needed. what do you really wanna test ? nomCompte just or ?? Modify it so it be suitable to what you really wanna test
                            if(nomCompte == null || nomCompte.getText().toString().trim().equals("")){
                                Toast.makeText(getContext(),"Renseignez le nom du compte",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //block of code for locally saving deleted as it is no more needed. We ain't save locally but use web service to save
                            addAbonneeToTheListAndOpenNewFragment();
                            return;

                        }
                        //if not "save" then refuse !
                        bundle.putString(Utils.NUMERO_ABONNEE, numero);
                        CBillingNav.openCamwaterImpaye(getParentFragmentManager(), numero);
                        dismiss();
                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do here
                    }
                } )
                .withLayer()
                .start();


    }

    private void addAbonneeToTheListAndOpenNewFragment(){
       // addSubAccount(numero);

        //no more need this block of code as we no more save locally but use web service to save.... you should just delete it
        abonnee.setNom(nom);
        abonnee.setNumero(numero);

        if(SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater().equals("")
           || SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater() == null){

            saveAbonneeAndOpenFragmentImpaye();

        }else {

            stAbonneesCamwater = SharedPrefManager.newInstance(getContext()).getListeAbonneCamwater();
            abonneesEneo = gson.fromJson(stAbonneesCamwater, AbonneesEneo.class);
            abonneeList = abonneesEneo.getListAbonnee();

            saveAbonneeAndOpenFragmentImpaye();

        }


    }

    private void saveAbonneeAndOpenFragmentImpaye(){
        abonneeList.add(abonnee);
        abonneesEneo.setListAbonnee(abonneeList);
        stAbonneesCamwater = gson.toJson(abonneesEneo);
        SharedPrefManager.newInstance(getContext()).setListeAbonneCamwater(stAbonneesCamwater);
        CBillingNav.openCamwaterImpaye(getParentFragmentManager(), numero);
        dismiss();
    }

    public void addSubAccount(String waterAccount){

        SubAccount newSubAccount = new SubAccount();
        newSubAccount.setOwner(sharedPrefSaveWaterCustomer.getAccountNumber());
        newSubAccount.setType(PBType.CAMWATER.name());
        newSubAccount.setValue(waterAccount);

        Call<SubAccountResponse> addNewWaterSubAccount = waterBillingRestInterface.addSubAccount(newSubAccount);

        Response<SubAccountResponse> newWaterSubAccountResponse;

        try {
            newWaterSubAccountResponse = addNewWaterSubAccount.execute();

            if (newWaterSubAccountResponse.isSuccessful()) {
                if (newWaterSubAccountResponse.body().getStatus().equals("201")){
                    Toast.makeText(getContext(),"Compte sauvegardé"
                            ,Toast.LENGTH_SHORT).show();
                    bundle.putString(Utils.NUMERO_ABONNEE, numero);
                    //CBillingNav.openEneoListFacture(getParentFragmentManager(), bundle);
                    //bundle.putString(Utils.NUMERO_ABONNEE, numero);
                    CBillingNav.openCamwaterImpaye(getParentFragmentManager(), numero);
                    dismiss();
                }
                return;
            }
            Toast.makeText(getContext(),newWaterSubAccountResponse.body().getMessage()
                    ,Toast.LENGTH_SHORT).show();

        } catch (IOException|NullPointerException e) {
            Log.d(TAG, "addSubAccount, water ...."+e.getLocalizedMessage(), e);
        }

        /*addNewWaterSubAccount.enqueue(new Callback<SubAccountResponse>() {
            @Override
            public void onResponse(Call<SubAccountResponse> call, Response<SubAccountResponse> response) {
                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("201")){
                        Toast.makeText(getContext(),"Compte sauvegardé"
                                ,Toast.LENGTH_SHORT).show();
                        bundle.putString(Utils.NUMERO_ABONNEE, numero);
                        CBillingNav.openEneoListFacture(getParentFragmentManager(), bundle);

                        dismiss();
                    }
                    else
                        Toast.makeText(getContext(),response.body().getMessage()
                                ,Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SubAccountResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Networking error : "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

}
