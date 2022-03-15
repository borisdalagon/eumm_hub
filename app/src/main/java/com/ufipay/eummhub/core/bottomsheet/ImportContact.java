package com.ufipay.eummhub.core.bottomsheet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.transfertargent.cashversmobile.fragment.FragmentTransfertCashMobile;
import com.ufipay.eummhub.client.transfertargent.cashversmobile.fragment.FragmentTransfertCashMobileRecap;
import com.ufipay.eummhub.client.transfertcomptecompte.fragment.FragmentTransfertCompteCompte2;
import com.ufipay.eummhub.core.adapters.AdapterContact;
import com.ufipay.eummhub.core.classe.Contact;
import com.ufipay.eummhub.core.classe.SharedPrefManager;
import com.ufipay.eummhub.core.classe.TransfertArgentTEST;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.core.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ImportContact extends BottomSheetDialogFragment {

    ImportContactListener importContactListener;

    ImportContactListener listener;
    AdapterContact adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    List<Contact> listItems = new ArrayList<>();

    private static int REQUEST_CODE = 1;

    ProgressBar progressBar;
    View imgInvisible;
    String operation;

    TransfertArgentTEST transfertArgentTEST;

    public ImportContact() {

    }

    public interface ImportContactListener {
        void onInputImportContactSent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_contact, container, false);

        Bundle bundle = getArguments();

        if (bundle != null){

            operation = bundle.getString(Utils.PAGE_POUR_CODE);

        }

        // Pour éttendre le BottomSheetDialog
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();

                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });



        transfertArgentTEST = new TransfertArgentTEST();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imgInvisible = (View) view.findViewById(R.id.img_invisible);

        // Liste des contacts
        searchView = (SearchView) view.findViewById(R.id.search_view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerContact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //request the permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);

        } else {

            //for lower than marsmallow version
            getContact();

        }




        return view;
    }


    private void getContact() {
        // pass all phonebook to cursor
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //to fetch all the contact from cursor
        while (cursor.moveToNext()) {
            //pass the data into string from cursor
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            addContactToList(name, mobile);
        }

        progressBar.setVisibility(View.GONE);
        imgInvisible.setVisibility(View.GONE);

        adapter = new AdapterContact(getContext(), listItems, new BaseListener() {
            @Override
            public void onClick(int position) {

                Log.d("XXX_CONTACT", "Onclick OK");

               String nom = adapter.liste().get(position).getNom();
               String numero = adapter.liste().get(position).getNumero();

                if(numero.startsWith("+")){

                    if(numero.substring(4).startsWith(" ")){
                        parseContact(nom, numero.substring(5));
                    }else {
                        parseContact(nom, numero.substring(4));
                    }

                }
                else if(numero.startsWith("00")){

                    if(numero.substring(4).startsWith(" ")){
                        parseContact(nom, numero.substring(6));
                    }else {
                        parseContact(nom, numero.substring(5));
                    }

                }else {
                    parseContact(nom, numero);
                }


            }

            @Override
            public void onLongClick(int position) {

            }
        });

        recyclerView.setAdapter(adapter);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d("XXX_CONTACT", "Onclick Filtre 1");
                return false;
            }
        });


        //
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission is granted call funtion again
                getContact();
            }
        }
    }

    // Gestion des écoutes

    public void updateEditText(CharSequence newText) {
        // editText.setText(newText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImportContactListener) {
            listener = (ImportContactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //**********


    public void parseContact(String nom, String numero){

        transfertArgentTEST.setNomContact(nom);
        transfertArgentTEST.setPhone(numero);

        SharedPrefManager.newInstance(getContext())
                .setPhoneNumberTransfert(numero);

        SharedPrefManager.newInstance(getContext())
                .setNomContactTransfert(nom);

        if(operation.equals(Utils.TRANSFERT_COMPTE_COMPTE)){

            listener.onInputImportContactSent("Numero_destinataire");
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.TRANSFERT_ARGENT, transfertArgentTEST);
            Fragment n = new FragmentTransfertCompteCompte2();
            ouvrirFragment(n, bundle);
            dismiss();

        }
        else if(operation.equals(Utils.TRANSFERT_CASH_VERS_MOBILE)){

            Log.d("XXX_CONTACT000", "ok");
            Fragment fragment = new FragmentTransfertCashMobile();
            if( getFragmentManager() != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace( R.id.fragment_container_transfert_cash_mobile, fragment );
                ft.commit();
            }
            dismiss();

        }


    }

    public void ouvrirFragment(Fragment fragment, Bundle bundle) {

        fragment.setArguments(bundle);
        if( getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations( R.anim.enter, R.anim.exit );
            ft.replace( R.id.fragment_container_transfert_compte_compte, fragment );
            ft.commit();
        }

    }


    private void addContactToList(String nom, String numero) {

        listItems.add(new Contact(nom, numero));

        Collections.sort(listItems, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getNom().compareTo(o2.getNom());
            }
        });

    }

}
