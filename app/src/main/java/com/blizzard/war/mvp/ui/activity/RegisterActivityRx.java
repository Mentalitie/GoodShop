package com.blizzard.war.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.utils.SystemBarHelper;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blizzard.war.utils.CommonUtil.GetColor;
import static com.blizzard.war.utils.CommonUtil.GetDrawable;
import static com.blizzard.war.utils.CommonUtil.JumpFinish;
import static com.blizzard.war.utils.CommonUtil.LoadingShow;
import static com.blizzard.war.utils.CommonUtil.getHandler;

/**
 * 功能描述:
 * 注册账号
 *
 * @auther: ma
 * @param: RegisterActivityRx
 * @date: 2019/4/17 18:36
 */
public class RegisterActivityRx extends RxBaseActivity {
    private Handler handler = getHandler();

    @BindView(R.id.layout_menu_icon)
    ImageView mMenuICon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackICon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_getCode)
    TextView et_getCode;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.phone_line)
    LinearLayout phone_line;
    @BindView(R.id.code_line)
    LinearLayout code_line;
    @BindView(R.id.password_line)
    LinearLayout password_line;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        et_phone.setOnFocusChangeListener((v, hasFocus) -> onFocus(v.getId()));
        et_code.setOnFocusChangeListener((v, hasFocus) -> onFocus(v.getId()));
        et_password.setOnFocusChangeListener((v, hasFocus) -> onFocus(v.getId()));
        et_getCode.setOnClickListener((v) -> onClick(v.getId()));
        SystemBarHelper.tintStatusBar(this, GetColor(R.color.window_background), 0);
    }

    @Override
    public void initToolBar() {
        mBackICon.setVisibility(View.VISIBLE);
        mMenuICon.setVisibility(View.GONE);
        mToolTitle.setText("注册");
        mBackICon.setOnClickListener(v -> finish());
    }

    @OnClick(R.id.btn_load_register)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_register:
                //注册并登录
                Register();
                break;
        }
    }

    private void onFocus(int id) {
        switch (id) {
            case R.id.et_phone:
                // 设置下划线颜色
                phone_line.setBackground(GetDrawable(R.drawable.db_edit_check));
                code_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                password_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                break;
            case R.id.et_code:
                // 设置下划线颜色
                phone_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                code_line.setBackground(GetDrawable(R.drawable.db_edit_check));
                password_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                break;
            case R.id.et_password:
                // 设置下划线颜色
                phone_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                code_line.setBackground(GetDrawable(R.drawable.db_edit_un_check));
                password_line.setBackground(GetDrawable(R.drawable.db_edit_check));
                break;
        }

    }

    private void onClick(int id) {
        String name = et_phone.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            et_code.requestFocus();
        }
    }

    public void Register() {
        String name = et_phone.getText().toString();
        String code = et_code.getText().toString();
        String password = et_password.getText().toString();
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.ator_edit_empty);
        if (TextUtils.isEmpty(name)) {
            animator.setTarget(phone_line);
            animator.start();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            animator.setTarget(code_line);
            animator.start();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            animator.setTarget(password_line);
            animator.start();
            return;
        }
        LoadingShow(this);
        handler.postDelayed(() -> JumpFinish(RegisterActivityRx.this, MainActivityRx.class, true), 2000);
    }

}