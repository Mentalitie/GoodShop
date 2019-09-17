package com.blizzard.war.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.app.BiliApplication;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import static com.blizzard.war.utils.DisplayUtil.dp2px;

/**
 * 功能描述:
 * 通用工具类
 *
 * @auther: ma
 * @param: CommonUtil
 * @date: 2019/4/16 11:17
 */
public class CommonUtil {

    private static Context context = BiliApplication.GetContext();
    private static Intent in;
    private static ProgressDialog dialog;
    private static Handler handler = new Handler();

    /**
     * 检查SD卡是否存在
     */
    private static boolean checkSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取手机SD卡总空间
     */
    private static long getSDcardTotalSize() {
        if (checkSdCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSizeLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSizeLong = mStatFs.getBlockSizeLong();
            }
            long blockCountLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockCountLong = mStatFs.getBlockCountLong();
            }
            return blockSizeLong * blockCountLong;
        } else {
            return 0;
        }
    }


    /**
     * 获取SDk可用空间
     */
    private static long getSDcardAvailableSize() {
        if (checkSdCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSizeLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSizeLong = mStatFs.getBlockSizeLong();
            }
            long availableBlocksLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocksLong = mStatFs.getAvailableBlocksLong();
            }
            return blockSizeLong * availableBlocksLong;
        } else {
            return 0;
        }
    }


    /**
     * 获取手机内部存储总空间
     */
    public static long getPhoneTotalSize() {
        if (!checkSdCard()) {
            File path = Environment.getDataDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSizeLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSizeLong = mStatFs.getBlockSizeLong();
            }
            long blockCountLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockCountLong = mStatFs.getBlockCountLong();
            }
            return blockSizeLong * blockCountLong;
        } else {
            return getSDcardTotalSize();
        }
    }


    /**
     * 获取手机内存存储可用空间
     */
    public static long getPhoneAvailableSize() {
        if (!checkSdCard()) {
            File path = Environment.getDataDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSizeLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSizeLong = mStatFs.getBlockSizeLong();
            }
            long availableBlocksLong = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocksLong = mStatFs.getAvailableBlocksLong();
            }
            return blockSizeLong * availableBlocksLong;
        } else
            return getSDcardAvailableSize();
    }

    /**
     * Description: 获取颜色id
     *
     * @param color
     * @return int
     */
    public static int GetColor(int color) {
        int colors = ContextCompat.getColor(context, color);
        return colors;
    }

    /**
     * Description: 设置Drawable
     *
     * @param id
     * @param view
     * @return Drawable
     */
    public static TextView SetDrawable(int id, TextView view) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        return view;
    }

    /**
     * Description: 获取Drawable id
     *
     * @param draw
     * @return Drawable
     */
    public static Drawable GetDrawable(int draw) {
        Drawable colors = ContextCompat.getDrawable(context, draw);
        return colors;
    }

    /**
     * 跳转页面
     *
     * @return
     */

    public static void JumpTo(Class<?> cls) {
        in = new Intent(context, cls);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(in);
            }
        }, 50);
    }

    /**
     * 跳转并关闭所有页面
     *
     * @return
     */
    public static void JumpFinish(Activity activity, Class<?> cls) {
        JumpFinish(activity, cls, false);
    }

    /**
     * 跳转并关闭所有页面
     *
     * @return
     */
    public static void JumpFinish(Activity activity, Class<?> cls, Boolean bole) {
        in = new Intent(context, cls);
        if (bole) {
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  // 关闭所以activity 并把指定栈置顶
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(in);
                activity.finish();
            }
        }, 50);
    }

    /**
     * 带参数跳转页面
     *
     * @return
     */
    public static void JumpToIn(Intent ints) {
        in = ints;
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(in);
            }
        }, 50);
    }

    /**
     * 加载框
     */
    public static void LoadingShow(Activity activity) {
        LoadingShow(activity, false);
    }

    /**
     * 加载框
     */

    public static void LoadingShow(Activity activity, Boolean bool) {
        dialog = new ProgressDialog(activity);
        if (!bool) {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        } else {
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形式为水平的进度条
            dialog.setMax(100);
        }
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setIcon(R.mipmap.ic_launcher);//  设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
//        dialog.setTitle("提示");
        // dismiss监听
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        // 监听Key事件被传递给dialog
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode,
//                                 KeyEvent event) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//        });
//        // 监听cancel事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                handler.removeCallbacksAndMessages(null);
            }
        });
//        //设置可点击的按钮，最多有三个(默认情况下)
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "中立",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
        dialog.setMessage("正在注册并登录中");
        dialog.show();
    }

    /**
     * 功能描述:返回loading dialog对象
     *
     * @param: RxBaseActivity
     * @return: dialog
     * @auther: ma
     * @date: 2019/4/9 14:19
     */
    public static ProgressDialog getDialog() {
        return dialog;
    }

    /**
     * 功能描述:返回handler对象
     *
     * @param: RxBaseActivity
     * @return: handler
     * @auther: ma
     * @date: 2019/4/9 14:19
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 功能描述:
     * 隐藏loading框
     *
     * @param: RxBaseActivity
     * @return: null
     * @auther: ma
     * @date: 2019/4/9 13:26
     */
    public static void LoadingHide() {
        if (dialog != null && dialog.isShowing()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 2000);
        }
    }

    /**
     * glide 公共配置
     */
    public static RequestOptions GlideInfo(Context mContext) {
        return GlideInfo(mContext, false);
    }

    @SuppressLint("CheckResult")
    public static RequestOptions GlideInfo(Context mContext, boolean isCircle) {
        final RequestOptions options = new RequestOptions();
        if (!isCircle) {
            options.centerCrop().transform(new CenterCropRoundCornerTransform(dp2px(mContext, 2)));

        } else {
            options.transform(new CenterCrop());
        }
        options.placeholder(R.drawable.ic_bili_default_image_tv)
                .error(R.drawable.ic_bili_default_image_tv)
                .diskCacheStrategy(DiskCacheStrategy.ALL);//禁用掉Glide的缓存功能
        return options;
    }
}
