package com.richland.wallet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.adapter.FaqAdapter;
import com.richland.wallet.databinding.ActivityFaqBinding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.FaqModel;
import com.richland.wallet.ui.viewmodel.FaqVM;
import com.richland.wallet.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FaqActivity extends AppCompatActivity {

    private ActivityFaqBinding binding;
    private FaqAdapter adapter;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ProgressDialog progressDialog;
    FaqVM faqVM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        faqVM = ViewModelProviders.of(this, viewModelFactory).get(FaqVM.class);
        faqVM.item().observe(this, this::items);
        faqVM.errorMessage().observe(this, this::onError);

//        faqVM.getFaq();
//        progressDialog = ProgressDialog.show(this, "", "데이터 불러오는 중…", true);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("FAQ");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new FaqAdapter(this, item -> {

            Intent intent = new Intent(FaqActivity.this, FaqBodyActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getCreated_at());
            intent.putExtra("content", item.getContent());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        List<FaqModel.FaqData> list = new ArrayList<>();
        list.add(new FaqModel.FaqData(1, "G3S Wallet 생성은 어떻게 하나요?", "그들의 보내는 앞이 쓸쓸하랴? 소담스러운 청춘에서만 아니더면, 충분히 전인\n" +
                "방지하는 것이다. 끓는 있을 품고 이상의 철환하였는가? 구할 얼마나 과실이 이상\n" +
                "따뜻한 이상의 우리 때문이다. 못할 충분히 더운지라 그것을 영원히 되려니와,\n" +
                "밝은 것이다. 끝에 않는 위하여서, 있는 소담스러운 작고 밝은 청춘에서만 가치를 끓는다.\n" +
                "예가 보이는 우리의 우리 예수는 청춘의 봄날의 운다. 가치를 가는 피가 이상이\n" +
                "그들의 우리의 이것이다. 만천하의 황금시대의 설산에서 쓸쓸한 부패뿐이다. 거친\n" +
                "봄바람을 안고, 웅대한 날카로우나 듣기만 이상을 앞이 많이 철환하였는가?\n전인 주는 청춘 싶이 군영과 만천하의 듣는다. 동력은 되려니와, 끝에 쓸쓸하랴?\n" +
                "두손을 열락의 천지는 천하를 때문이다. 위하여, 힘차게 뛰노는 용기가 설산에서\n" +
                "자신과 아니다. 실현에 그들은 별과 그들은 같지 되는 황금시대다. 열락의 힘차게 든\n" +
                "행복스럽고 아름다우냐? 생의 대고, 위하여 발휘하기 얼마나 사랑의 쓸쓸하랴?\n" +
                "실현에 커다란 방지하는 그림자는 힘차게 약동하다. 품고 피가 속에서 원질이 속잎나고,\n" +
                "무엇을 것이다. 영원히 따뜻한 공자는 열락의 우리의 귀는 영락과 약동하다.\n같이, 길을 속잎나고, 그들의 얼음과 미묘한 것이다. ", "2022.06.18T10:00 ", "2022.06.18T10:00 "));
        list.add(new FaqModel.FaqData(2, "よくあるご質問のタイトル例)G3SWalletの生成方法は何ですか？", "よくあるご質問の内容の例) 私は十一月すこぶるその相当院についてのの\n" +
                "るませあっ。同時に今が説明方もよくその相当たましばかりを向いてつける\n" +
                "たには尊重打ち壊すですまして、ずいぶんには進んらしくなけれたた。秋のあ\n" +
                "ろだのはようやく十月にようやくんたた。同時に大森さんを納得床なぜ落第よ\n" +
                "り云っな未成同じ年私かお断りにとしてお力説たたないませて、その場合も私\n" +
                "か心持落語からなって、嘉納さんのものに個性の私がついご講演と思うばここ\n" +
                "評語にお使用が忘れようにできるだけお観察を違えうですが、いやしくもまあ\n" +
                "返事をありですておきですのをしなけれです。それならそれでご方向をしよ訳\n" +
                "はあまり面倒とせましが、同じ人格では食わせろなてという教場が着るけれど\n" +
                "も合うですで。そのため次のためこの朋党は私ごろがしですかと三宅さんがあ\n" +
                "るななかっ、他の元来でしょというご発見たないですて、学校のうちの防に結\n" +
                "果ともの秋刀魚を時間忘れとしまいが、もともとのほかがあっばその一方がよ\n" +
                "うやくなりでうときめですつもりまして、ないますですてあいにくお秋刀魚し\n" +
                "ですのましうだ。ただ所々か不幸か学問の思うんから、前末信念がありてえだ\n" +
                "時がお学習の今日のなるべきで。前をはいくらするて計らうんありないて、と\n" +
                "もかく何しろ威張って活動はどう淋しいでものな。するとお＃「をしからは切ら\n" +
                "です方たので、様子には、すでに私か発してするられませな思わせななくっと\n" +
                "あるて、腹の中は読んばいるんます。\n", "2022.06.18T10:00 ", "2022.06.18T10:00 "));

        adapter.setItems(list);

    }

    public void items(FaqModel model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {
            adapter.setItems(model.getData());
        }


    }
    public void onError(String error) {

        progressDialog.dismiss();
    }
}
