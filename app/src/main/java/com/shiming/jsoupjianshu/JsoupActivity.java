package com.shiming.jsoupjianshu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author shiming
 * @version v1.0 create at 2017/9/25
 * @des
 */
public class JsoupActivity extends AppCompatActivity {

    private Document mDocument;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jsoup_activity_layout);
        initData();

    }

    private void initData() {
        try {
            //我个人的简书的地址
            mDocument = Jsoup.connect("http://www.jianshu.com/u/a58eb984bda4").timeout(10000).get();
            mDocument.firstElementSibling();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
