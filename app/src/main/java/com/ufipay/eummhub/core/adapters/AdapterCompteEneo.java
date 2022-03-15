package com.ufipay.eummhub.core.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ufipay.eummhub.R;
import com.ufipay.eummhub.core.classe.Abonnee;
import com.ufipay.eummhub.core.listener.BaseListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterCompteEneo extends RecyclerView.Adapter<AdapterCompteEneo.ViewHolder> {


    BaseListener listener;
    private List<Abonnee> listItems ;
    private List<Abonnee> listItemsSearch;
    private Context context;

    private List<Integer> selectionnes;

    public AdapterCompteEneo(Context context, List<Abonnee> listItem, BaseListener listener) {
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
                .inflate( R.layout.item_compte_eneo, parent, false );

        return new ViewHolder( view );

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.numeroCompte.setText( "N° "+listItems.get( position ).getNumero() );
        holder.nom.setText( listItems.get( position ).getNom() );
        holder.lettre.setText( listItems.get( position ).getNom().substring(0,1) );
        holder.cardItemCompte.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView numeroCompte;
        private TextView nom;
        private TextView lettre;
        private CardView cardItemCompte;

        ViewHolder(@NonNull View itemView) {

            super( itemView );

            numeroCompte = itemView.findViewById( R.id.numeroFacture );
            nom = itemView.findViewById( R.id.nom );
            lettre = itemView.findViewById( R.id.lettrePremiere );
            cardItemCompte = itemView.findViewById( R.id.card_item_compte );



        }
    }








}