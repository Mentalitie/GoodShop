package com.blizzard.war.mvp.model.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.utils.ClipboardUtil;
import com.blizzard.war.utils.ConstantUtil;
import com.blizzard.war.utils.ToastUtil;
import com.blizzard.war.mvp.ui.widget.CircleProgressView;

import butterknife.BindView;

/**
 * Created by hcc on 16/8/7 14:12
 * 100332338@qq.com
 * <p/>
 * 浏览器界面
 */
public class BrowserActivityRx extends RxBaseActivity {

    private Context context = BiliApplication.GetContext();

    @BindView(R.id.layout_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackIcon;
    @BindView(R.id.layout_close_icon)
    ImageView mCloseIcon;
    @BindView(R.id.circle_progress)
    CircleProgressView progressBar;
    @BindView(R.id.webView)
    WebView mWebView;

    private final Handler mHandler = new Handler();
    private String url, copyUrl, mTitle;
    private WebViewClientBase webViewClient = new WebViewClientBase();

    @Override
    public int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(ConstantUtil.EXTRA_URL);
            mTitle = intent.getStringExtra(ConstantUtil.EXTRA_TITLE);
        }
        setupWebView();
    }

    @Override
    public void initToolBar() {
        // 初始化导航菜单
        setSupportActionBar(mToolBar);
        mMenuIcon.setVisibility(View.GONE);
        mToolTitle.setVisibility(View.GONE);
        mToolBar.setNavigationIcon(R.drawable.ic_cancel);
        mToolBar.setNavigationOnClickListener(v -> finish());
        mToolBar.setTitle(TextUtils.isEmpty(mTitle) ? "详情" : mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_home:
                mWebView.loadUrl(url);
                mWebView.postDelayed(() -> mWebView.clearHistory(), 1000);
                break;
            case R.id.menu_share:
                share();
                break;
            case R.id.menu_open:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(copyUrl));
                startActivity(intent);
                break;
            case R.id.menu_copy:
                ClipboardUtil.setText(BrowserActivityRx.this, copyUrl);
                ToastUtil.show("已复制");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + copyUrl);
        startActivity(Intent.createChooser(intent, mTitle));
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && mWebView.copyBackForwardList().getSize() > 0
                && !mWebView.getUrl().equals(mWebView.copyBackForwardList()
                .getItemAtIndex(0).getOriginalUrl())) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, BrowserActivityRx.class);
        intent.putExtra(ConstantUtil.EXTRA_URL, url);
        intent.putExtra(ConstantUtil.EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        progressBar.spin();
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setSupportZoom(true); //是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(false); //是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true); //设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true); //和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true); //DOM Storage
        webSettings.setAppCacheEnabled(true); //是否使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setGeolocationEnabled(true);

        mWebView.getSettings().setBlockNetworkImage(true); // 本身含义阻止图片网络数据
        mWebView.setWebViewClient(webViewClient);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog
                        .Builder(BrowserActivityRx.this)
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("确定", (dialog, which) -> result.confirm());
                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

    public class WebViewClientBase extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            copyUrl = String.valueOf(url);
            progressBar.setVisibility(View.GONE);
            progressBar.stopSpinning();
            mWebView.getSettings().setBlockNetworkImage(false);
        }

//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            String errorHtml = "<html><body><h2>找不到网页</h2></body></html>";
//            view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
//        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            copyUrl = String.valueOf(request.getUrl());
            if (!copyUrl.startsWith("http")) {
                try {
                    // 以下固定写法,表示跳转到第三方应用
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(copyUrl));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onPause() {
        mWebView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
