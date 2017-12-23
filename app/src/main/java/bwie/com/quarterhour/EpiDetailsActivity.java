package bwie.com.quarterhour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import api.Api;
import api.Common;
import bwie.com.basemodule.NetAval;
import bwie.com.basemodule.RetrofitHelper;
import entity.BaseEntity;
import entity.EpiDetails;
import entity.UserInfo;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

public class EpiDetailsActivity extends AppCompatActivity {

    private PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private ProgressSubscriber epiDetailsSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = getIntent().getIntExtra("flag", -1);
        if (flag==0){
            setContentView(R.layout.episode_item);
        }else if (flag==1){
            setContentView(R.layout.episode_item_one);
        }else if (flag==2){
            setContentView(R.layout.episode_item_two);
        }else if (flag==3){
            setContentView(R.layout.episode_item_more);
        }else {
            finishAfterTransition();
        }
        int jid = getIntent().getIntExtra("jid", 0);
        if (jid!=0 && NetAval.NetAvailable(App.AppContext)){
            Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
            Observable<BaseEntity<EpiDetails>> epiDetails = apiService.getEpiDetails(jid + "");
            epiDetailsSub = new ProgressSubscriber<EpiDetails>(this) {
                @Override
                public void _Next(EpiDetails o) {

                }

                @Override
                public void _OnError(String msg) {

                }
            };
            HttpUtils.getInstace().toSubscribe(epiDetails,epiDetailsSub,"epiDetailsCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
        }else {
            setContentView(R.layout.not_net_layout);
        }
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        super.onDestroy();
    }



}