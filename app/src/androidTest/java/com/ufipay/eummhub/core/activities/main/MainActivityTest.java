package com.ufipay.eummhub.core.activities.main;

import androidx.test.rule.ActivityTestRule;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.utils.Utils;

import junit.framework.TestCase;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(JUnit4.class)
public class MainActivityTest extends TestCase {

    private SharedPrefManager preferencesConnection;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();

    }

    private void init(){
        preferencesConnection = SharedPrefManager.newInstance(mActivityRule.getActivity());
        preferencesConnection.setRestUrl("http://192.168.2.8:8485");
        preferencesConnection.setCodePaysTelephone("237");
        preferencesConnection.setAccountNumber("237697240704");
        preferencesConnection.setMpin("2018");
        preferencesConnection.setJetonTest(Utils.NON_OK);
        preferencesConnection.setStatutsTest(Utils.OPERATION_REUSSIE);
    }

    private void valideDerniereRequette(int j){
        onView(withId(R.id.valider)).perform(click());
        onView(withId(R.id.titre_code_1)).check(matches(withText("Entrez votre code secrêt")));
        EventBus.getDefault().post(Utils.AUTHENFICATION_REUSSIE);
        onView(withId(R.id.titre_code)).perform(click());
        while (j>0){
            if(preferencesConnection.getJetonTest().equals(Utils.NON_OK)){
                j = j+1;
                if(preferencesConnection.getStatutsTest().equals(Utils.OPERATION_ECHOUE)){
                    j=0;
                    preferencesConnection.setStatutsTest(Utils.OPERATION_REUSSIE);
                }
            }else {
                j=0;
                preferencesConnection.setJetonTest(Utils.NON_OK);
            }
        }
        onView(withId(R.id.titreRapport)).check(matches(withText(Utils.OPERATION_REUSSIE)));
        onView(withId(R.id.fermer)).perform(click());
    }


    @Test
    //Test Transfert Compte à Compte
    public void testTransfertCompteCompte(){
        init();
        String pageName = "Transfert d'argent";
        String numero = "677515077";
        String montant = "500";
        String namePageMontant = "Montant de l'envois";
        String accountName = "test ufi drd";
        int i = 1;
        int j = 1;

        onView(withId(R.id.bloc_envois_compte_compte)).perform(click());
        onView(withId(R.id.pageName)).check(matches(withText(pageName)));
        onView(withId(R.id.edtNumeroPhone)).perform(typeText(numero), closeSoftKeyboard());
        onView(withId(R.id.suivant)).perform(click());
        onView(withId(R.id.namePageMontant)).check(matches(withText(namePageMontant)));
        onView(withId(R.id.edtMontant)).perform(typeText(montant), closeSoftKeyboard());
        onView(withId(R.id.suivant)).perform(click());
        while (i>0){
            if(preferencesConnection.getJetonTest().equals(Utils.NON_OK)){
                i = i+1;
                if(preferencesConnection.getStatutsTest().equals(Utils.OPERATION_ECHOUE)){
                    i=0;
                    preferencesConnection.setStatutsTest(Utils.OPERATION_REUSSIE);
                }
            }else {
                i=0;
                //Check le nom du beneficiaire si la reponse à la requette a été un succès
                onView(withId(R.id.beneficiary_account_name)).check(matches(withText(accountName)));
                preferencesConnection.setJetonTest(Utils.NON_OK);
            }
        }
        valideDerniereRequette(j);



    }


   @Test
   //Test de Transfert Compte vers CASH
    public void testTransfertCompteVersCash(){
        init();
        String titreInitial = "Envois d'argent";
        String titrePage = "Envois COMPTE vers CASH";
        String numero = "670080926";
        String montant = "1000";
        int j = 1;

        onView(withId(R.id.bloc_envois_argent)).perform(click());
        onView(withId(R.id.titrePage)).check(matches(withText(titreInitial)));
        onView(withId(R.id.cardTransfertCompteVersCash)).perform(click());

        onView(withId(R.id.titrePageCompteCash)).check(matches(withText(titrePage)));
        onView(withId(R.id.edtNomBeneficiaire)).perform(typeText(numero), closeSoftKeyboard());
        onView(withId(R.id.edtMontant)).perform(scrollTo()).perform(typeText(montant), closeSoftKeyboard());
        onView(withId(R.id.suivant)).perform(click());
        onView(withId(R.id.beneficiaire)).check(matches(withText(numero)));
        onView(withId(R.id.valider)).perform(click());
        onView(withId(R.id.titre_code_1)).check(matches(withText("Entrez votre code secrêt")));
        EventBus.getDefault().post(Utils.AUTHENFICATION_REUSSIE);
        onView(withId(R.id.titre_code)).perform(click());
       while (j>0){
           if(preferencesConnection.getJetonTest().equals(Utils.NON_OK)){
               j = j+1;
               if(preferencesConnection.getStatutsTest().equals(Utils.OPERATION_ECHOUE)){
                   j=0;
                   preferencesConnection.setStatutsTest(Utils.OPERATION_REUSSIE);
               }
           }else {
               j=0;
               preferencesConnection.setJetonTest(Utils.NON_OK);
           }
       }
       onView(withId(R.id.titreRapport)).check(matches(withText(Utils.OPERATION_REUSSIE)));
       onView(withId(R.id.fermer)).perform(click());



   }


    @Test
    //Test de Transfert CASH vers Mobile
    public void testTransfertCashVersMobile(){
        init();
        String titreInitial = "Envois d'argent";
        String titrePage = "Envois CASH vers Mobile";
        String numero = "670080926";
        String montant = "1500";
        String nomBeneficiaire = "NOUVEAU test FR DALAGON";
        int i = 1;
        int j = 1;

        onView(withId(R.id.bloc_envois_argent)).perform(click());
        onView(withId(R.id.titrePage)).check(matches(withText(titreInitial)));
        onView(withId(R.id.cardEnvoisCashVersMobil)).perform(click());
        onView(withId(R.id.titrePageCashMobile)).check(matches(withText(titrePage)));
        onView(withId(R.id.edtNumeroPhone)).perform(typeText(numero), closeSoftKeyboard());
        onView(withId(R.id.edtMontant)).perform(scrollTo()).perform(typeText(montant), closeSoftKeyboard());
        onView(withId(R.id.suivant)).perform(click());
        while (i>0){
            if(preferencesConnection.getJetonTest().equals(Utils.NON_OK)){
                i = i+1;
                if(preferencesConnection.getStatutsTest().equals(Utils.OPERATION_ECHOUE)){
                    i=0;
                    preferencesConnection.setStatutsTest(Utils.OPERATION_REUSSIE);
                }
            }else {
                i=0;
                onView(withId(R.id.beneficiary_name)).check(matches(withText(nomBeneficiaire)));
                preferencesConnection.setJetonTest(Utils.NON_OK);
            }
        }
        valideDerniereRequette(j);

    }





}