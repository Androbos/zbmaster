package com.ohdroid.zbmaster.base.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.ohdroid.zbmaster.application.BaseView;
import com.ohdroid.zbmaster.application.data.api.QiniuApi;

/**
 * Created by ohdroid on 2016/2/23.
 * 我是把activity与fragment规划在mvp中view层
 * 所以base activity 主要作用是实现BaseView的功能，以及绑定presenter等通用操作
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

}
