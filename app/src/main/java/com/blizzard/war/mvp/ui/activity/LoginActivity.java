package com.blizzard.war.mvp.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.utils.CommonUtil;
import com.blizzard.war.utils.SystemBarHelper;
import com.blizzard.war.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blizzard.war.utils.CommonUtil.GetColor;
import static com.blizzard.war.utils.CommonUtil.GetDrawable;
import static com.blizzard.war.utils.CommonUtil.JumpFinish;
import static com.blizzard.war.utils.CommonUtil.JumpTo;
import static com.blizzard.war.utils.CommonUtil.SetDrawable;

/**
 * 功能描述:
 * 登录页面
 *
 * @auther: ma
 * @param: LoginActivity
 * @date: 2019/4/17 18:36
 */
public class LoginActivity extends RxBaseActivity {

    @BindView(R.id.layout_menu_icon)
    ImageView mMenuICon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackICon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.iv_icon_left)
    ImageView mLeftLogo;
    @BindView(R.id.iv_icon_right)
    ImageView mRightLogo;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.username_line)
    LinearLayout username_line;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.password_line)
    LinearLayout password_line;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //绑定聚焦事件
        et_username.setOnFocusChangeListener((v, hasFocus) -> onFocus(v.getId()));
        et_password.setOnFocusChangeListener((v, hasFocus) -> onFocus(v.getId()));
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_password.setOnEditorActionListener((textView, i, keyEvent) -> {
            login();
            return false;
        });
        SystemBarHelper.tintStatusBar(this, GetColor(R.color.window_background), 0);
//        LoadingShow(this);
    }

    @Override
    public void initToolBar() {
        mBackICon.setVisibility(View.VISIBLE);
        mMenuICon.setVisibility(View.GONE);
        mBackICon.setImageDrawable(CommonUtil.GetDrawable(R.drawable.ic_cancel));
        mToolTitle.setText("登录");
        mBackICon.setOnClickListener(v -> finish());
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.btn_register:
                //注册
                JumpTo(RegisterActivity.class);
                break;
        }
    }

    private void onFocus(int id) {
        switch (id) {
            case R.id.et_username:
                // 设置图片
                mLeftLogo.setImageResource(R.drawable.ic_22);
                mRightLogo.setImageResource(R.drawable.ic_33);
                // 设置左侧图标
                SetDrawable(R.drawable.ic_login_username_on, et_username);
                SetDrawable(R.drawable.ic_login_password_default, et_password);
                // 设置下划线颜色
                username_line.setBackground(GetDrawable(R.drawable.db_edit_check));
                password_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                break;
            case R.id.et_password:
                // 设置图片
                mLeftLogo.setImageResource(R.drawable.ic_22_hide);
                mRightLogo.setImageResource(R.drawable.ic_33_hide);
                // 设置左侧图标
                SetDrawable(R.drawable.ic_login_username_default, et_username);
                SetDrawable(R.drawable.ic_login_password_on, et_password);
                // 设置下划线颜色
                username_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                password_line.setBackground(GetDrawable(R.drawable.db_edit_check));

                break;
        }

    }

    private void login() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.ator_edit_empty);
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("用户名不能为空");
            animator.setTarget(username_line);
            animator.start();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show("密码不能为空");
            animator.setTarget(password_line);
            animator.start();
            return;
        }
        showContacts();
    }

    private static final int READ_PHONE_STATE = 100;

    //请求权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                    Manifest.permission.CHANGE_WIFI_STATE
            }, READ_PHONE_STATE);
        } else {
            JumpFinish(this, MainActivity.class);
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case READ_PHONE_STATE:
                if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                
                } else {
                    JumpFinish(this, MainActivity.class);
                }
                break;
            default:
                break;
        }
    }
}