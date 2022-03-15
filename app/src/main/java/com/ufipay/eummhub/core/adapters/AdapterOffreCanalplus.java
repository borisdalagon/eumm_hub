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
import com.ufipay.eummhub.core.classe.Offre;
import com.ufipay.eummhub.core.listener.BaseListener;
import com.ufipay.eummhub.remote.merchant.canal.Offer;

import java.util.ArrayList;
import java.util.List;

public class AdapterOffreCanalplus extends RecyclerView.Adapter<AdapterOffreCanalplus.ViewHolder> {


    BaseListener listener;
    private List<Offer> listItems ;
    private List<Offer> listItemsSearch;
    private Context context;

    private List<Integer> selectionnes;

    public AdapterOffreCanalplus(Context context, List<Offer> listItem, BaseListener listener) {
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
                .inflate( R.layout.item_offre_canalplus, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.nomOffre.setText( listItems.get( position ).getName() );
        holder.montant.setText( listItems.get( position ).getAmount() );
        holder.cardItemOffre.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView nomOffre;
        private TextView montant;
        private CardView cardItemOffre;

        ViewHolder(@NonNull View itemView) {

            super( itemView );

            nomOffre = itemView.findViewById( R.id.nomOffre );
            montant = itemView.findViewById( R.id.montant );
            cardItemOffre = itemView.findViewById( R.id.card_item_offre );



        }
    }








}