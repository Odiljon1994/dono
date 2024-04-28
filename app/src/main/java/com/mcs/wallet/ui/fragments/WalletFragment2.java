package com.mcs.wallet.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.ui.AddTokenActivity;
import com.mcs.wallet.ui.TokenActivity;
import com.mcs.wallet.R;
import com.mcs.wallet.adapter.TokensAdapter;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.database.DatabaseMainnetToken;
import com.mcs.wallet.databinding.FragmentWallet2Binding;
import com.mcs.wallet.models.TokensModel;
import com.mcs.wallet.ui.viewmodel.CoinMarketCapVM;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WalletFragment2 extends Fragment {
    FragmentWallet2Binding binding;
    private String walletAddress;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    CoinMarketCapVM coinMarketCapVM;
    List<TokensModel> mainnetTokenList;
    TokensAdapter adapter;
    DatabaseMainnetToken databaseMainnetToken;
    List<TokensModel> tokensForConvertingKRW;
    int counterPfResponses = 0;
    private String ethBalance = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet2, container, false);
        coinMarketCapVM = ViewModelProviders.of(this, viewModelFactory).get(CoinMarketCapVM.class);
        coinMarketCapVM.item2().observe(getActivity(), this::setTokenInKRW);

        View view = binding.getRoot();

        binding.addToken.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTokenActivity.class)));

        binding.walletAddress.setText(preferencesUtil.getWalletAddress());
        binding.walletAddress.setText(preferencesUtil.getWalletAddress());
        binding.copyBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", binding.walletAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), R.string.copied, Toast.LENGTH_SHORT).show();
        });
        databaseMainnetToken = new DatabaseMainnetToken(getActivity());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainnetTokenList = new ArrayList<>();
        mainnetTokenList.add(new TokensModel(getActivity().getDrawable(R.drawable.matic), "MATIC", "0", "MATIC", "", preferencesUtil.getWalletAddress()));
        Cursor data;
        data = databaseMainnetToken.getData();

        while (data.moveToNext()) {
            String tokenSymbol = data.getString(1);
            String tokenName = data.getString(2);
            String contractAddress = data.getString(3);
            Drawable drawable = getActivity().getDrawable(R.drawable.dono_appicon);

            if (tokenSymbol.equals("USDT")) {
                drawable = getActivity().getDrawable(R.drawable.usdt);
            } else if (tokenSymbol.equals("DONOpia")) {
                drawable = getActivity().getDrawable(R.drawable.donpia_logo);
            }
            else if (tokenSymbol.equals("TTAP")) {
                drawable = getActivity().getDrawable(R.drawable.ttap_new_icon);
            }else if (tokenSymbol.equals("DAI")) {
                drawable = getActivity().getDrawable(R.drawable.dai);
            } else if (tokenSymbol.equals("LINK")) {
                drawable = getActivity().getDrawable(R.drawable.link);
            } else if (tokenSymbol.equals("UNI")) {
                drawable = getActivity().getDrawable(R.drawable.uni);
            } else if (tokenSymbol.equals("USDC")) {
                drawable = getActivity().getDrawable(R.drawable.usdc);
            } else if (tokenSymbol.equals("WBTC")) {
                drawable = getActivity().getDrawable(R.drawable.wbtc);
            } else if (tokenSymbol.equals("VEN")) {
                drawable = getActivity().getDrawable(R.drawable.ven);
            } else if (tokenSymbol.equals("THETA")) {
                drawable = getActivity().getDrawable(R.drawable.theta);
            } else if (tokenSymbol.equals("WFIL")) {
                drawable = getActivity().getDrawable(R.drawable.wfil);
            } else if (tokenSymbol.equals("BUSD")) {
                drawable = getActivity().getDrawable(R.drawable.busd);
            } else if (tokenSymbol.equals("OKB")) {
                drawable = getActivity().getDrawable(R.drawable.okb);
            } else if (tokenSymbol.equals("CRO")) {
                drawable = getActivity().getDrawable(R.drawable.cro);
            } else if (tokenSymbol.equals("cUSDC")) {
                drawable = getActivity().getDrawable(R.drawable.cusdc);
            } else if (tokenSymbol.equals("HT")) {
                drawable = getActivity().getDrawable(R.drawable.ht);
            } else if (tokenSymbol.equals("cETH")) {
                drawable = getActivity().getDrawable(R.drawable.ceth);
            } else if (tokenSymbol.equals("MKR")) {
                drawable = getActivity().getDrawable(R.drawable.mkr);
            } else if (tokenSymbol.equals("cDAI")) {
                drawable = getActivity().getDrawable(R.drawable.cdai);
            } else if (tokenSymbol.equals("COMP")) {
                drawable = getActivity().getDrawable(R.drawable.comp);
            } else if (tokenSymbol.equals("CHZ")) {
                drawable = getActivity().getDrawable(R.drawable.chz);
            } else if (tokenSymbol.equals("ADA")) {
                drawable = getActivity().getDrawable(R.drawable.ada);
            } else if (tokenSymbol.equals("DOGE")) {
                drawable = getActivity().getDrawable(R.drawable.doge);
            } else if (tokenSymbol.equals("CHZ")) {
                drawable = getActivity().getDrawable(R.drawable.chz);
            } else if (tokenSymbol.equals("XRP")) {
                drawable = getActivity().getDrawable(R.drawable.xrp);
            } else if (tokenSymbol.equals("BCH")) {
                drawable = getActivity().getDrawable(R.drawable.bch);
            } else if (tokenSymbol.equals("LTC")) {
                drawable = getActivity().getDrawable(R.drawable.ltc);
            } else if (tokenSymbol.equals("EOS")) {
                drawable = getActivity().getDrawable(R.drawable.eos);
            } else if (tokenSymbol.equals("CAKE")) {
                drawable = getActivity().getDrawable(R.drawable.cake);
            } else if (tokenSymbol.equals("BUSD-T")) {
                drawable = getActivity().getDrawable(R.drawable.busdt);
            } else if (tokenSymbol.equals("YFI")) {
                drawable = getActivity().getDrawable(R.drawable.yfi);
            }

            mainnetTokenList.add(new TokensModel(drawable, tokenName, "0", tokenSymbol, "", contractAddress));
        }

        adapter = new TokensAdapter(mainnetTokenList, getActivity(), clickListener -> {
//            Fragment someFragment = new TokensFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
//            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//
//            //String ethKrw = String.format("%,.2f", tokens.get(0).getAmountInKrw());
//            Bundle bundle = new Bundle();
//            bundle.putString("tokenName", clickListener.getTokenSymbol());
//            bundle.putString("KRW", clickListener.getAmountInWon());
//            bundle.putString("contractAddress", clickListener.getContractAddress());
//            someFragment.setArguments(bundle);
//
//            transaction.commit();


            Intent intent = new Intent(getActivity(), TokenActivity.class);

            intent.putExtra("tokenName", clickListener.getTokenSymbol());
            intent.putExtra("KRW", clickListener.getAmountInWon());
            intent.putExtra("contractAddress", clickListener.getContractAddress());

            startActivity(intent);
        });

        binding.recyclerview.setAdapter(adapter);
       // checkBalanaces(mainnetTokenList);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkBalanaces(mainnetTokenList);
            }
        });
        return view;
    }

    private void dismissRefreshLayout(int listCount) {
        try {
            if (counterPfResponses == listCount) {
                // binding.swipeRefreshLayout.setRefreshing(false);

                for (int i = 0; i < tokensForConvertingKRW.size(); i++) {
                    if (tokensForConvertingKRW.get(i).getTokenSymbol().equals("G3S") && i != 1) {
                        TokensModel model = tokensForConvertingKRW.get(i);
                        tokensForConvertingKRW.remove(i);
                        tokensForConvertingKRW.add(1, model);
                    }
                }

                coinMarketCapVM.getPrices2(tokensForConvertingKRW);
            }
        } catch (Throwable error) {
            System.out.println(error.getMessage());
        }

    }

    private void checkBalanaces(List<TokensModel> items) {

        tokensForConvertingKRW = new ArrayList<>();
        counterPfResponses = 0;

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());


        binding.swipeRefreshLayout.setRefreshing(true);
        for (TokensModel tokenModel : items) {

            boolean isCoin = tokenModel.getTokenSymbol().toUpperCase() == "ETH";

            if (isCoin) {

                ethManager.balanceInEth(tokenModel.getContractAddress(), getActivity())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(balance -> {
                            ethBalance = balance.toString();
                            counterPfResponses++;
                            dismissRefreshLayout(items.size());
                            String amountInBalance = balance.toString();
                            if (amountInBalance.length() > 6) {
                                amountInBalance = amountInBalance.substring(0, 5);
                            }
                            tokenModel.setTokenAmount(amountInBalance);
                            //tokenModel.setTokenAmount(balance.toString());
                            tokensForConvertingKRW.add(tokenModel);
                            adapter.updateToken(tokenModel);
                        }, error -> {
                            counterPfResponses++;
                            dismissRefreshLayout(items.size());
                        });


            } else {


//                ethManager.getTokenBalance(preferencesUtil.getWalletAddress(), "", tokenModel.getContractAddress(), getActivity())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(balance -> {
//                            counterPfResponses++;
//                            tokenModel.setTokenAmount(balance.toString());
//                            tokensForConvertingKRW.add(tokenModel);
//                            adapter.updateToken(tokenModel);
//                            dismissRefreshLayout(items.size());
//
//                        }, error -> {
//                            counterPfResponses++;
//                            /**
//                             * if function fails error can be caught in this block
//                             */
//
//                            System.out.println(error.getMessage());
//                            dismissRefreshLayout(items.size());
//
//                        });


                if (tokenModel.getTokenSymbol().equals("DONOpia")) {
                    ethManager.getTokenBalance(preferencesUtil.getWalletAddress(), "", ApiUtils.getContractAddress(), getActivity())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(balance -> {
                                counterPfResponses++;
                                String amountInBalance = balance.toString();
                                if (amountInBalance.length() > 6) {
                                    amountInBalance = amountInBalance.substring(0, 5);
                                }
                                tokenModel.setTokenAmount(amountInBalance);
                               // tokenModel.setTokenAmount(balance.toString());
                                tokensForConvertingKRW.add(tokenModel);
                                adapter.updateToken(tokenModel);
                                dismissRefreshLayout(items.size());

                            }, error -> {
                                counterPfResponses++;
                                /**
                                 * if function fails error can be caught in this block
                                 */

                                System.out.println(error.getMessage());
                                dismissRefreshLayout(items.size());

                            });
                } else {
                    ethManager.getTokenBalance(preferencesUtil.getWalletAddress(), "", tokenModel.getContractAddress(), getActivity())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(balance -> {
                                counterPfResponses++;
                                String amountInBalance = balance.toString();
                                if (amountInBalance.length() > 6) {
                                    amountInBalance = amountInBalance.substring(0, 5);
                                }
                                tokenModel.setTokenAmount(amountInBalance);
                                tokensForConvertingKRW.add(tokenModel);
                                adapter.updateToken(tokenModel);
                                dismissRefreshLayout(items.size());

                            }, error -> {
                                counterPfResponses++;
                                /**
                                 * if function fails error can be caught in this block
                                 */

                                System.out.println(error.getMessage());
                                dismissRefreshLayout(items.size());

                            });
                }

            }
        }
    }
    public void updateTokens() {
        mainnetTokenList = new ArrayList<>();
        mainnetTokenList.add(new TokensModel(getActivity().getDrawable(R.drawable.eth_icon), "Ethereum", "0", "ETH", "", preferencesUtil.getWalletAddress()));
        Cursor data;
        data = databaseMainnetToken.getData();

        while (data.moveToNext()) {
            String tokenSymbol = data.getString(1);
            String tokenName = data.getString(2);
            String contractAddress = data.getString(3);
            Drawable drawable = getActivity().getDrawable(R.drawable.ttap_coin_logo);

            if (tokenSymbol.equals("USDT")) {
                drawable = getActivity().getDrawable(R.drawable.usdt);
            } else if (tokenSymbol.equals("DONOpia")) {
                drawable = getActivity().getDrawable(R.drawable.donpia_logo);
            }
            else if (tokenSymbol.equals("DAI")) {
                drawable = getActivity().getDrawable(R.drawable.dai);
            } else if (tokenSymbol.equals("LINK")) {
                drawable = getActivity().getDrawable(R.drawable.link);
            } else if (tokenSymbol.equals("UNI")) {
                drawable = getActivity().getDrawable(R.drawable.uni);
            } else if (tokenSymbol.equals("USDC")) {
                drawable = getActivity().getDrawable(R.drawable.usdc);
            } else if (tokenSymbol.equals("WBTC")) {
                drawable = getActivity().getDrawable(R.drawable.wbtc);
            } else if (tokenSymbol.equals("VEN")) {
                drawable = getActivity().getDrawable(R.drawable.ven);
            } else if (tokenSymbol.equals("THETA")) {
                drawable = getActivity().getDrawable(R.drawable.theta);
            } else if (tokenSymbol.equals("WFIL")) {
                drawable = getActivity().getDrawable(R.drawable.wfil);
            } else if (tokenSymbol.equals("BUSD")) {
                drawable = getActivity().getDrawable(R.drawable.busd);
            } else if (tokenSymbol.equals("OKB")) {
                drawable = getActivity().getDrawable(R.drawable.okb);
            } else if (tokenSymbol.equals("CRO")) {
                drawable = getActivity().getDrawable(R.drawable.cro);
            } else if (tokenSymbol.equals("cUSDC")) {
                drawable = getActivity().getDrawable(R.drawable.cusdc);
            } else if (tokenSymbol.equals("HT")) {
                drawable = getActivity().getDrawable(R.drawable.ht);
            } else if (tokenSymbol.equals("cETH")) {
                drawable = getActivity().getDrawable(R.drawable.ceth);
            } else if (tokenSymbol.equals("MKR")) {
                drawable = getActivity().getDrawable(R.drawable.mkr);
            } else if (tokenSymbol.equals("cDAI")) {
                drawable = getActivity().getDrawable(R.drawable.cdai);
            } else if (tokenSymbol.equals("COMP")) {
                drawable = getActivity().getDrawable(R.drawable.comp);
            } else if (tokenSymbol.equals("CHZ")) {
                drawable = getActivity().getDrawable(R.drawable.chz);
            } else if (tokenSymbol.equals("ADA")) {
                drawable = getActivity().getDrawable(R.drawable.ada);
            } else if (tokenSymbol.equals("DOGE")) {
                drawable = getActivity().getDrawable(R.drawable.doge);
            } else if (tokenSymbol.equals("CHZ")) {
                drawable = getActivity().getDrawable(R.drawable.chz);
            } else if (tokenSymbol.equals("XRP")) {
                drawable = getActivity().getDrawable(R.drawable.xrp);
            } else if (tokenSymbol.equals("BCH")) {
                drawable = getActivity().getDrawable(R.drawable.bch);
            } else if (tokenSymbol.equals("LTC")) {
                drawable = getActivity().getDrawable(R.drawable.ltc);
            } else if (tokenSymbol.equals("EOS")) {
                drawable = getActivity().getDrawable(R.drawable.eos);
            } else if (tokenSymbol.equals("CAKE")) {
                drawable = getActivity().getDrawable(R.drawable.cake);
            } else if (tokenSymbol.equals("BUSD-T")) {
                drawable = getActivity().getDrawable(R.drawable.busdt);
            } else if (tokenSymbol.equals("YFI")) {
                drawable = getActivity().getDrawable(R.drawable.yfi);
            }
            mainnetTokenList.add(new TokensModel(drawable, tokenName, "0", tokenSymbol, "", contractAddress));
        }

        adapter.setItems(mainnetTokenList);

    }


    public void setTokenInKRW(List<TokensModel> tokenInKRW) {
        adapter.setItems(tokenInKRW);
        binding.swipeRefreshLayout.setRefreshing(false);
        // adapter.updateToken(tokenInKRW);

    }
}
