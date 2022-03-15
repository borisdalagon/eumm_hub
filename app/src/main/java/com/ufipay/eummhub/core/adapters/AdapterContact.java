package com.ufipay.eummhub.core.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.client.transfertcomptecompte.TransfertCompteCompte;
import com.ufipay.eummhub.core.classe.Contact;
import com.ufipay.eummhub.core.listener.BaseListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ViewHolder> implements Filterable {


    BaseListener listener;
    private List<Contact> listItems ;
    private List<Contact> listItemsSearch;
    private Context context;

    private List<Integer> selectionnes;

    public AdapterContact(Context context, List<Contact> listItem, BaseListener listener) {
        super();
        this.listItems = listItem;
        this.context = context;
        this.listener = listener;
        selectionnes = new ArrayList<>(  );
        listItemsSearch = new ArrayList<>( listItem );
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( context )
                .inflate( R.layout.item_contact, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.nomContact.setText( listItems.get( position ).getNom() );
        holder.numero.setText( listItems.get( position ).getNumero() );
        holder.lettre.setText( listItems.get( position ).getNom().substring(0,1) );


        if(selectionnes.contains( position )) {
            // todo: afficher l'icone de sélection
            Log.d("","");

        }

        holder.card.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<Contact> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listItemsSearch);
                } else {

                    String charSequenceString = constraint.toString();

                    for (Contact item : listItemsSearch) {
                        if (item.getNom().toLowerCase().contains(charSequenceString.toLowerCase().trim())) {
                            filteredList.add(item);
                        }
                        listItems = filteredList;
                    }



                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                listItems = (List<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public List<Contact> liste(){

        return listItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView nomContact;
        private TextView numero;
        private TextView lettre;
        private LinearLayout card;


        ViewHolder(@NonNull View itemView) {

            super( itemView );

            nomContact = itemView.findViewById( R.id.nomContact );
            numero = itemView.findViewById( R.id.numeroPhone );
            lettre = itemView.findViewById( R.id.lettre );
            card = itemView.findViewById( R.id.item_card );



        }
    }








}