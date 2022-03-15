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
import com.ufipay.eummhub.core.classe.Facture;
import com.ufipay.eummhub.core.listener.BaseListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterFacture extends RecyclerView.Adapter<AdapterFacture.ViewHolder> {


    BaseListener listener;
    private List<Facture> listItems ;
    private List<Facture> listItemsSearch;
    private Context context;

    private List<Integer> selectionnes;

    public AdapterFacture(Context context, List<Facture> listItem, BaseListener listener) {
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
                .inflate( R.layout.item_facture, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.numeroFacture.setText( listItems.get( position ).getNumero() );
        holder.montant.setText( listItems.get( position ).getMontant() );
        holder.cardItemFacture.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView numeroFacture;
        private TextView montant;
        private CardView cardItemFacture;

        ViewHolder(@NonNull View itemView) {

            super( itemView );

            numeroFacture = itemView.findViewById( R.id.numeroFacture );
            montant = itemView.findViewById( R.id.montant );
            cardItemFacture = itemView.findViewById( R.id.card_item_facture );



        }
    }








}