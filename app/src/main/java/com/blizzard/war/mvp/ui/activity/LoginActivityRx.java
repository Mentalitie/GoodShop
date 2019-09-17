package com.blizzard.war.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Rational;
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
 * @param: LoginActivityRx
 * @date: 2019/4/17 18:36
 */
public class LoginActivityRx extends RxBaseActivity {

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
                JumpTo(RegisterActivityRx.class);
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
        JumpFinish(this, MainActivityRx.class);
    }
}