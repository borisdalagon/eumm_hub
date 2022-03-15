package com.ufipay.eummhub.core.activities.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rom4ek.arcnavigationview.ArcNavigationView;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.payerfactures.PayerFactures;
import com.ufipay.eummhub.client.payertransfert.PayerTransfert;
import com.ufipay.eummhub.client.transfertargent.TransfertArgent;
import com.ufipay.eummhub.client.transfertcomptecompte.TransfertCompteCompte;
import com.ufipay.eummhub.core.activities.Pager;
import com.ufipay.eummhub.core.activities.ProfilUtilisateur;
import com.ufipay.eummhub.core.bottomsheet.DetailSolde;
import com.ufipay.eummhub.core.bottomsheet.OperationReport;
import com.ufipay.eummhub.core.classe.RapportRequest;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;
import com.ufipay.eummhub.remote.UnsafeOkHttpClient;
import com.ufipay.eummhub.remote.user.BalanceData;
import com.ufipay.eummhub.remote.user.BalanceResponse;
import com.ufipay.eummhub.remote.user.UserRestInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,MainViewModel.MainListener {

    private BalanceData balanceData;
    private TextView mainBalance;
    private ProgressBar loaderMainBalance;
    private UserRestInterface mainUserRestInterface;
    private SharedPrefManager mainSharedPrefManager;
    private static final String TAG = "MainAct";
    public static final String MAIN_BALANCES_DATA = "MAIN_BALANCES_DATA";

    CircularImageView imageProfil;
    CircularImageView imageProfilNavigation;
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    ImageView profilUtilisateur;
    ImageView menu;

    RelativeLayout liDetailSolde;
    DrawerLayout drawer;

    TextView nomUtilisateur;

    LinearLayout blocEnvoisArgent;
    LinearLayout blocPayerTransfert;
    LinearLayout blocTransfertArgent;
    LinearLayout blocFacture;

    private MainViewModel mainViewModel;
    RapportRequest rapportRequest = new RapportRequest();

    OperationReport operationReport = new OperationReport();

    TextView nameTransfertArgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            isUserFirstTime = Boolean.parseBoolean(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

            if (isUserFirstTime) {
                Intent introIntent = new Intent(MainActivity.this, Pager.class);
                introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
                startActivity(introIntent);
            }

            setContentView(R.layout.activity_main);
        }

        //ViewModel
        mainViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MainViewModel(MainActivity.this);
            }
        }).get(MainViewModel.class);
        //fin

        //mainViewModel.getBalances(MainActivity.this);


        nameTransfertArgent = findViewById(R.id.nameTransfertArgent);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);

        profilUtilisateur = findViewById(R.id.profilUtilateur);
        profilUtilisateur.setOnClickListener(this);

        liDetailSolde = findViewById(R.id.li_detail_solde);
        liDetailSolde.setOnClickListener(this);

        blocTransfertArgent = findViewById(R.id.bloc_envois_compte_compte);
        blocTransfertArgent.setOnClickListener(this);

        blocEnvoisArgent = findViewById(R.id.bloc_envois_argent);
        blocEnvoisArgent.setOnClickListener(this);

        blocPayerTransfert = findViewById(R.id.bloc_payer_transfert);
        blocPayerTransfert.setOnClickListener(this);

        blocFacture = findViewById(R.id.bloc_payer_facture);
        blocFacture.setOnClickListener(this);


        imageProfil = findViewById(R.id.imageProfil);


        Glide.with(this)
                .load(R.drawable.d)
                .into(imageProfil);


          drawer = findViewById(R.id.drawer_layout);
          ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArcNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        nomUtilisateur = headerView.findViewById(R.id.nomUtilisateur);
        nomUtilisateur.setText("DALAGON II BORIS");

        mainBalance = findViewById(R.id.main_balance);
        loaderMainBalance = findViewById(R.id.loader_main_balance);

        mainSharedPrefManager = SharedPrefManager.newInstance(this);
        mainUserRestInterface = new Retrofit.Builder().baseUrl(mainSharedPrefManager.getRestUrl()).addConverterFactory(GsonConverterFactory.create()).
                client(UnsafeOkHttpClient.getUnsafeOkHttpClient()).build().create(UserRestInterface.class);

        loaderMainBalance.setVisibility(View.VISIBLE);
        viderSharedPreference();
        getBalances ();


    }

    private void viderSharedPreference(){
        SharedPrefManager.newInstance(this).setPhoneNumberTransfert(null);
        SharedPrefManager.newInstance(this).setAmountTransfert(null);
        SharedPrefManager.newInstance(this).setNomContactTransfert(null);
    }

    public void getBalances (){
        loaderMainBalance.setVisibility(View.VISIBLE);
        Call<BalanceResponse> balanceResponseCall = mainUserRestInterface.getBalance(mainSharedPrefManager.getAccountNumber());
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                if (response.isSuccessful()){
                    loaderMainBalance.setVisibility(View.GONE);
                    if (response.body().getStatus().equals("200")){
                        balanceData = response.body().getData();
                        String mainBal = String.valueOf(balanceData.getPrincipal());
                        mainBal = String.format(mainBal, "### ### ###");
                        mainBalance.setText(mainBal);
                        return;
                    }
                    openBottomSheetResult(null, Utils.OPERATION_ECHOUE,response.body().getMessage());
                    mainBalance.setText(". . .");

                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    String status = jsonObject.getString(Utils.STATUS);
                    String message = jsonObject.getString(Utils.MESSAGE);
                    openBottomSheetResult(null, Utils.OPERATION_ECHOUE,"Serveur Indisponible : ".concat(message));
                    mainBalance.setText(". . .");
                    loaderMainBalance.setVisibility(View.GONE);

                } catch (JSONException | IOException e) {
                    Log.d(TAG, "trying to get status and message from json response, : "+e.getLocalizedMessage(), e);
                }

            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
                loaderMainBalance.setVisibility(View.GONE);
                mainBalance.setText(". . .");
                openBottomSheetResult(null, Utils.OPERATION_ECHOUE,getString(R.string.impossible_to_connect)+t.getLocalizedMessage());
            }
        });
    }



    @Override
    public void onClick(View v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorMainClick = Utils.getViewPropertyAnimatorCompat(v);
        viewPropertyAnimatorMainClick
                .setListener( new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                        //nothing to do here
                    }

                    @Override
                    public void onAnimationEnd(final View v) {
                        switch (v.getId()) {


                            case R.id.profilUtilateur:

                                Utils.openNewActivity(MainActivity.this, ProfilUtilisateur.class);
                                overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                finish();
                                break;

                             case R.id.li_detail_solde:

                                 if (balanceData == null) {
                                     Toast.makeText(MainActivity.this, "Soldes non disponible, rechargez la page SVP", Toast.LENGTH_SHORT).show();
                                     break;
                                 }

                                 Bundle balancesBundle = new Bundle();
                                 balancesBundle.putSerializable(MAIN_BALANCES_DATA, balanceData);
                                 DetailSolde detailSolde = new DetailSolde();
                                 detailSolde.setArguments(balancesBundle);
                                 detailSolde.show(getSupportFragmentManager(), Utils.TAG);

                                break;

                             case R.id.menu:

                                 drawer.openDrawer(GravityCompat.START);

                                break;

                             case R.id.bloc_envois_argent:

                                 Utils.openNewActivity(MainActivity.this, TransfertArgent.class);
                                 overridePendingTransition(R.anim.enter, R.anim.exit);
                                 finish();
                                 break;

                             case R.id.bloc_envois_compte_compte:

                                 SharedPrefManager.newInstance(MainActivity.this).setPhoneNumberTransfert(null);
                                 SharedPrefManager.newInstance(MainActivity.this).setAmountTransfert(null);
                                 SharedPrefManager.newInstance(MainActivity.this).setNomContactTransfert(null);
                                 Utils.openNewActivity(MainActivity.this, TransfertCompteCompte.class);
                                 overridePendingTransition(R.anim.animated_dismissable_card_slide_up_anim, R.anim.animated_dismissable_card_stay_anim);
                                 finish();
                                 break;

                             case R.id.bloc_payer_transfert:

                                 Utils.openNewActivity(MainActivity.this, PayerTransfert.class);
                                 overridePendingTransition(R.anim.enter, R.anim.exit);
                                 finish();
                                 break;


                            case R.id.bloc_payer_facture:

                                 Utils.openNewActivity(MainActivity.this, PayerFactures.class);
                                 overridePendingTransition(R.anim.enter, R.anim.exit);
                                 finish();
                                 break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {
                        //nothing to do here
                    }
                } )
                .withLayer()
                .start();

    }


    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
            super.onBackPressed();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.autoriser) {
            // Handle the camera action
        } else if (id == R.id.eummPass) {
            //coming up
        } else if (id == R.id.miseAJour) {
            //coming up
        } else if (id == R.id.aPropos) {
            //coming up
        } else if (id == R.id.changeLangue) {
            //coming up
        } else if (id == R.id.seDeconnecter) {
            //coming up
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChangingConfigurations();
        operationReport.onDestroy();
        // super.onDestroy();
    }

    @Override
    public void responseOnView(BalanceData balanceData_, String mainBal, boolean success, String message, String status) {

        loaderMainBalance.setVisibility(View.GONE);

        if(balanceData_ != null){
            this.balanceData = balanceData_;
            mainBalance.setText(mainBal);
        }else if(!message.equals("")){
            mainBalance.setText(". . .");
            openBottomSheetResult(Utils.DEMANDE_DE_SOLDE, Utils.OPERATION_ECHOUE, message);
        }else {
            mainBalance.setText(". . .");
            openBottomSheetResult(Utils.DEMANDE_DE_SOLDE, Utils.OPERATION_ECHOUE, "Problème de connexion avec le serveur");
        }

    }

    public void openBottomSheetResult(String operation, String status,String message){
        Bundle bundle = new Bundle();
        rapportRequest.setOperation(Utils.DEMANDE_DE_SOLDE);
        rapportRequest.setStatut(status);
        rapportRequest.setTypeRequet(Utils.REQUEST_POUR_NON_AUTHENTICATION);
        rapportRequest.setMessage(message);
        bundle.putSerializable(Utils.RAPPORT_REQUEST, rapportRequest );

        operationReport.setCancelable(false);
        operationReport.setArguments(bundle);
        operationReport.show(getSupportFragmentManager(), Utils.TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
        loaderMainBalance.setVisibility(View.VISIBLE);
        mainBalance.setText("");
       // mainViewModel.getBalances(MainActivity.this);

        getBalances ();

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