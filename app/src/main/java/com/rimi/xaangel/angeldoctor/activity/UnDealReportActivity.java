package com.rimi.xaangel.angeldoctor.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rimi.xaangel.angeldoctor.BaseActivity;
import com.rimi.xaangel.angeldoctor.R;
import com.rimi.xaangel.angeldoctor.adapter.MainListAdapter;
import com.rimi.xaangel.angeldoctor.bean.Report;
import com.rimi.xaangel.angeldoctor.http.HttpUrls;
import com.rimi.xaangel.angeldoctor.http.MyCallBack;
import com.rimi.xaangel.angeldoctor.http.XHttpRequest;
import com.rimi.xaangel.angeldoctor.utils.CommonUtils;
import com.rimi.xaangel.angeldoctor.utils.GsonUtils;
import com.rimi.xaangel.angeldoctor.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/6/1.
 * 今日待办
 */
public class UnDealReportActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.back_btn)
    ImageButton backBtn;
    @Bind(R.id.mListview)
    ListView mListView;
    @Bind(R.id.tishi_tv)
    TextView tishiTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undeal_report);
        ButterKnife.bind(this);
        CommonUtils.getLoading(this, "加载中.....");
        loadData();
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.REPORT);
        params.addBodyParameter("method", "dList");
        params.addBodyParameter("doctor_code", (String) SPUtils.get(this,DOC_CODE,CODE,""));
        params.addBodyParameter("start_date", CommonUtils.getNowDate() + " 0:00:00");
        params.addBodyParameter("end_date", CommonUtils.getNowDate() + " 23:59:59");
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    List<Report> list ;
                    Gson gson = GsonUtils.getGson();
                    list = gson.fromJson(response.getJSONArray("list").toString(), new TypeToken<List<Report>>(){}.getType());
                    if (list == null || list.size() == 0){
                        tishiTv.setVisibility(View.VISIBLE);
                    }
                    MainListAdapter adapter = new MainListAdapter(UnDealReportActivity.this,list);
                    mListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.back_btn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
