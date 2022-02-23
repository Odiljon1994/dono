package com.cicenterprise.wellet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ItemTokensBinding;
import com.cicenterprise.wellet.models.TokensModel;

import java.util.HashMap;
import java.util.List;

public class TokensAdapter extends RecyclerView.Adapter<TokensAdapter.ViewHolder>{

    private List<TokensModel> items;
    private Context context;
    private ClickListener clickListener;
    private HashMap<String, TokensModel> map;
    private HashMap<Integer, String> tokenPosToContractAddr;
    private HashMap<String, Integer> tokenContractAddrToPos;

    public TokensAdapter(List<TokensModel> items, Context context, ClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
        this.map = new HashMap<>();
        this.tokenContractAddrToPos = new HashMap<>();
        this.tokenPosToContractAddr = new HashMap<>();
        for (int i = 0; i < this.items.size(); i++) {
            TokensModel model = this.items.get(i);

            this.tokenContractAddrToPos.put(model.getContractAddress().toLowerCase(),i);
            this.tokenPosToContractAddr.put(i, model.getContractAddress().toLowerCase());
            this.map.put(model.getContractAddress().toLowerCase(), model);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTokensBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_tokens, parent, false);
        return new TokensAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String contractAddress = tokenPosToContractAddr.get(position);
        tokenContractAddrToPos.put(contractAddress.toLowerCase(),position);
        TokensModel tokenModel = map.get(contractAddress);
        holder.bind(tokenModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTokensBinding binding;



        public ViewHolder(@NonNull ItemTokensBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
        void bind (TokensModel model) {
            binding.amount.setText(model.getTokenAmount());
            binding.tokenName.setText(model.getTokenName());
           // binding.tokenSymbol.setText(model.getTokenSymbol());
            binding.amountKrw.setText(model.getAmountInWon());
            binding.tokenLogo.setImageDrawable(model.getLogo());
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void clearItems() {
        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public void updateToken(TokensModel tokenModel){
        // update existing token model
        map.put(tokenModel.getContractAddress().toLowerCase(), tokenModel);
        int tokenPosition = tokenContractAddrToPos.get(tokenModel.getContractAddress().toLowerCase());
        notifyItemChanged(tokenPosition);
    }

    public void setItems(List<TokensModel> items) {
        this.map.clear();
        this.tokenContractAddrToPos.clear();
        this.tokenPosToContractAddr.clear();
        this.items = items;
        this.map = new HashMap<>();
        this.tokenContractAddrToPos = new HashMap<>();
        this.tokenPosToContractAddr = new HashMap<>();
        for (int i = 0; i < this.items.size(); i++) {
            TokensModel model = this.items.get(i);

            this.tokenContractAddrToPos.put(model.getContractAddress().toLowerCase(),i);
            this.tokenPosToContractAddr.put(i, model.getContractAddress().toLowerCase());
            this.map.put(model.getContractAddress().toLowerCase(), model);
        }

        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(TokensModel tokenModel);
    }
}
