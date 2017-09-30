package com.shiming.jsoupjianshu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * @author shiming
 * @version v1.0 create at 2017/9/25
 * @des
 */
public class JsoupActivity extends AppCompatActivity {

    private Document mDocument;
    private ArrayList<JianShuDataBean> mDataBeanArray;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private View mRlBackground;
    private TextView tvTitle;
    private JianShuAdapter mJianShuAdapter;
    private String mAvatarImg;
    private String mLikeMe;
    private String mAllNum;
    private String mArtical;
    private String mFanNum;
    private String mLookOther;
    private String mDesAll;
    private String mMyName;
    private String mPersonalIntroduction;
    private JianShuDataBean mBean;
    private TextView mHarvestLike;
    private TextView mFan;
    private TextView mFollow;
    private TextView mDes;
    private ImageView mAvater;
    private TextView mArticle;
    private TextView mNumberOfWords;
    private Toolbar toolbar;
    private JianShuDataBean mJianShuDataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jsoup_activity_layout);
        mDataBeanArray = new ArrayList<>();
        findView();
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJianShuAdapter = new JianShuAdapter(this, null);
        mRecyclerView.setAdapter(mJianShuAdapter);

        initListener();

    }

    private void initListener() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.common_green));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void findView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mImageView = (ImageView) findViewById(R.id.ivBackground);
        mRlBackground = findViewById(R.id.rlBackground);
        mAvater = (ImageView) findViewById(R.id.img_avatar);
        mDes = (TextView) findViewById(R.id.des);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        mFollow = (TextView) findViewById(R.id.follow);
        mFan = (TextView) findViewById(R.id.fan);
        mHarvestLike = (TextView) findViewById(R.id.harvest_like);
        mArticle = (TextView) findViewById(R.id.article);
        mNumberOfWords = (TextView) findViewById(R.id.number_of_words);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.day_menu);
        appBar.addOnOffsetChangedListener(mAppBarStateChangeListener);


    }
    AppBarStateChangeListener mAppBarStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (state == State.EXPANDED) {
                toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back_white));
                tvTitle.setTextColor(getResources().getColor(R.color.common_btn_background));
                setOffsetView(View.VISIBLE);
            } else if (state == State.COLLAPSED) {
                toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back_green));
                toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_toolbar));
                tvTitle.setTextColor(getResources().getColor(R.color.common_txt_color));
                setOffsetView(View.GONE);
            } else if (state == State.IDLE) {
                setOffsetView(View.VISIBLE);
            }
        }
    };
    private void setOffsetView(int visibility) {
        mRlBackground.setVisibility(visibility);
    }
    private void initData() {
        mJianShuDataBean = new JianShuDataBean();
        mSwipeRefreshLayout.setRefreshing(true);
        mDataBeanArray.clear();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //我个人的简书的地址
                    mDocument = Jsoup.connect("http://www.jianshu.com/u/a58eb984bda4").timeout(10000).get();
                    //Personal introduction
                    mPersonalIntroduction = mDocument.select("div.js-intro").text();
                    //工作稳定，没有压力，看天吃饭，下雨的话，一般在桥洞下睡觉，平时，在各大小街小巷活动！！！ 个人测试博客：https://shimingli.github.io/ 职业：要饭
                    System.out.println("shiming====+text" + mPersonalIntroduction);
                    mMyName = mDocument.select("div.title").text();
                    mMyName = mMyName.split(" ")[0];
                    //豌豆射手_BiuBiu 个人介绍
                    System.out.println("shiming text1"+ mMyName);
                    mDesAll = mDocument.select("div.meta-block").text();
                    //  5 关注 19 粉丝 12 文章 26745 字数 54 收获喜欢
                    String[] split = mDesAll.split(" ");
                    mLookOther = split[0];
                    mFanNum = split[2];
                    mArtical = split[4];
                    mAllNum = split[6];
                    mLikeMe = split[8];

                    //无序列表标签
                    Elements select = mDocument.select("ul.note-list");
                    Elements li = select.select("li");
                    for (Element element : li) {
                        mBean = new JianShuDataBean();
                        /**<div class="name">
                         <a class="blue-link" target="_blank" href="/u/a58eb984bda4">豌豆射手_BiuBiu</a>
                         <span class="time" data-shared-at="2017-09-28T13:42:20+08:00">前天 13:42</span>
                         </div>**/
                        mBean.mAuthorName = element.select("div.name").text(); // 作者姓名
                        //<a class="blue-link" target="_blank" href="/u/a58eb984bda4">豌豆射手_BiuBiu</a>
                        //个 html 文档中经常有很多链接，而这些链接可能包含主机地址，可能不包含，也可能是一个相对的地址。
                        // 一般我们从 html 文档中解析出这些链接，最终还要转成第一种形式的链接地址，这个转链接的过程还挺复杂的，
                        // 需要根据所解析的文档url地址来计算。可如果使用 jsoup 的话，就非常简单了，jsoup 的 attr 方法提供了一个 abs: 的操作
                        mBean.mAuthorLink = element.select("a.blue-link").attr("abs:href"); // 作者首页链接
                        //<span class="time" data-shared-at="2017-09-28T13:42:20+08:00">前天 13:42</span>
                        //这样获取不到时间
                        //mBean.mTime=element.select("span.time").first().text();   // 发表时间
                        mBean.mTime = timeChange(element.select("span.time").attr("data-shared-at"));   // 发表时间
                        mBean.mPrimaryImg = (element.select("img").attr("src"));  // 主图
                        /**<a class="avatar" target="_blank" href="/u/a58eb984bda4">
                         <img src="//upload.jianshu.io/users/upload_avatars/5363507/e00df149-1ed7-49b1-a9c2-6a6f8cbd1841.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/64/h/64" alt="64">
                         </a>**/
                        mBean.mAvatarImg = element.select("a.avatar").select("img").attr("src"); // 头像
                        if (TextUtils.isEmpty(mAvatarImg)) {
                            mAvatarImg = element.select("a.avatar").select("img").attr("src");
                        }
                        //<a class="title" target="_blank" href="/p/ac85724da2f6">主要是学习，转自GitHub上</a>
                        mBean.mTitle = element.select("a.title").text();    // 标题

                        mBean.mTitleLink = element.select("a.title").attr("abs:href"); // 标题链接
                        /**
                         * <p class="abstract">
                         项目案例 十分钟人人能学会开发开源中国 整个项目视频如下： 一行代码开发开源中国资讯页面 一分钟给RecyView添加head 一行代码实现轮播图 一分钟实现下拉刷新和加载更...
                         </p>
                         */
                        mBean.mContent = element.select("p.abstract").text();       // 内容
                        /**
                         * <div class="meta">
                         <a target="_blank" href="/p/ac85724da2f6">
                         <i class="iconfont ic-list-read"></i> 6
                         </a>        <a target="_blank" href="/p/ac85724da2f6#comments">
                         <i class="iconfont ic-list-comments"></i> 0
                         </a>      <span><i class="iconfont ic-list-like"></i> 1</span>
                         </div>
                         */
                        String[] arr = element.select("div.meta").text().split(" ");
                        try {
                            //已经读了
                            mBean.mReadNum = arr[0];
                            //留言多少人
                            mBean.mTalkNum = arr[1];
                            //喜欢多少人
                            mBean.mLikeNum = arr[2];
                        } catch (Exception e) {

                        }
                        mDataBeanArray.add(mBean);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mJianShuAdapter.addData(mDataBeanArray);

                            Glide.with(getApplicationContext())
                                    .load("http:"+mAvatarImg)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(mImageView);

                            Glide.with(getApplicationContext())
                                    .load("http:"+mAvatarImg)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(mAvater);

                            tvTitle.setText(mMyName);

                            mFan.setText("粉丝："+mFanNum);
                            mFollow.setText("关注："+mLookOther);
                            mArticle.setText("粉丝："+mArtical);
                            mNumberOfWords.setText("字数："+mAllNum);
                            mHarvestLike.setText("收获喜欢："+mLikeMe);

                            mDes.setText(mPersonalIntroduction);

                            mJianShuAdapter.setClick(new JianShuAdapter.onClick() {
                                @Override
                                public void onClick(View view, JianShuDataBean bean) {
                                    Intent title = new Intent(JsoupActivity.this, WebViewActivity.class);
                                    title.putExtra("link", bean.mTitleLink);
                                    startActivity(title);
                                }
                            });


                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String timeChange(String time) {
        String[] ts = time.split("T");
        String[] split = ts[1].split("\\+");
        return ts[0] + "    " + split[0];
    }
}
