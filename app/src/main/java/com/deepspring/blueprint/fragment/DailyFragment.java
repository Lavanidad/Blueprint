package com.deepspring.blueprint.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.deepspring.blueprint.R;
import com.deepspring.blueprint.activity.ContentActivity;
import com.deepspring.blueprint.adapter.TitleAdapter;
import com.deepspring.blueprint.base.BaseFragment;
import com.deepspring.blueprint.bean.NewsBean;
import com.deepspring.blueprint.bean.NewsList;
import com.deepspring.blueprint.bean.Title;
import com.deepspring.blueprint.utils.GsonUtil;
import com.deepspring.blueprint.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * todo list 1、内容页标题栏红色 2、banner样式设计 3、加载速度优化（fragment重复创建问题
 */
public class DailyFragment extends BaseFragment {

    private List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private static final int  ITEM_SPORT= 0;

    public DailyFragment() {
        // Required empty public constructor
    }


    public static DailyFragment newInstance(String title) {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initViews(View rootView) {
        refreshLayout = rootView.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = rootView.findViewById(R.id.list_view);
        adapter = new TitleAdapter(getActivity(),R.layout.news_list_item, titleList);
        listView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        initViews(rootView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(getActivity(), ContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("uri",title.getUri());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestNew(ITEM_SPORT);
            }
        });
        requestNew(ITEM_SPORT);
        return rootView;
    }

    public void requestNew(int itemName){
        // 根据返回到的 URL 链接进行申请和返回数据
        String address = response(itemName);    // key
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "新闻加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final NewsList newlist = GsonUtil.parseJsonWithGson(responseText);
                final int code = newlist.code;
                final String msg = newlist.msg;
                if (code == 200){
                    titleList.clear();
                    for (NewsBean news:newlist.newsList){
                        Title title = new Title(news.title,news.description,news.picUrl, news.url);
                        titleList.add(title);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        };
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "数据错误返回",Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    private String response(int itemName){
        String address = "https://api.tianapi.com/tiyu/?key=\n" +
                "575d99bd18252fe3a99c653a2f03a0bb&num=50&rand=1";
        return address;
    }

}
