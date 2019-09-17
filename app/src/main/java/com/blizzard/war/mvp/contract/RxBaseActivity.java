package com.blizzard.war.mvp.contract;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.blizzard.war.app.BiliApplication;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能描述:
 * 公共Activity
 *
 * @auther: ma
 * @param: RxBaseActivity
 * @date: 2019/4/17 18:36
 */
public abstract class RxBaseActivity extends RxAppCompatActivity {


    private Unbinder bind;
    private AlertDialog alertDialog;
    private static Context context = BiliApplication.GetContext();
    private String contextString = this.toString();
    private String contextState = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));

    public Unbinder getBind() {
        return bind;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());

        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();
    }

    @Override
    public void onBackPressed() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        } else {
            finish();
        }
    }

    private void BackupDialog() {
        //创建AlertDialog的构造器的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置构造器标题
//        builder.setTitle("提示");
        //构造器对应的图标
//        builder.setIcon(R.mipmap.ic_launcher);
        //构造器内容,为对话框设置文本项(之后还有列表项的例子)
        builder.setMessage("您要离开吗？");

        //为构造器设置确定按钮,第一个参数为按钮显示的文本信息，第二个参数为点击后的监听事件，用匿名内部类实现
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //第一个参数dialog是点击的确定按钮所属的Dialog对象,第二个参数which是按钮的标示值
                Log.e("DialogInterface", "DialogInterface");
                finish();//结束当前Activity
            }
        });
        //为构造器设置取消按钮,若点击按钮后不需要做任何操作则直接为第二个参数赋值null
//        builder.setNegativeButton("不呀", null);
        //为构造器设置一个比较中性的按钮，比如忽略、稍后提醒等
        builder.setNeutralButton("取消", null);
        //利用构造器创建AlertDialog的对象,实现实例化
        alertDialog = builder.create();
    }

    /**
     * 设置布局layout
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化toolbar
     */
    public abstract void initToolBar();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    /**
     * 加载数据
     */
    public void loadData() {
    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    public void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    public void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    public void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    public void finishTask() {
    }


}
