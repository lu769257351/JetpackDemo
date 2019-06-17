package com.lwb.jetpack.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.lwb.jetpack.R;
import com.lwb.jetpack.widgets.premissions.PermissionsChecker;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class BaseV2AppActivity extends AppCompatActivity implements View.OnClickListener {
    protected AppCompatImageView backIV;
    protected LinearLayout titleLayout;
    protected TextView titleTV;
    protected TextView rightTV;
    protected AppCompatImageView rightIV;
    private LinearLayout rootLayout;
    protected LinearLayout netErrorRoot, noneDataRoot;
    protected TextView noneData;
    protected ImageView noneIcon;

    // 所需的全部权限
    protected PermissionsChecker mPermissionsChecker;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Facebook分享打印hash值
         */
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("com.addcn.newcar8891", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // 打印哈希值
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        setContentViewWithNetError();
        init();
    }


    protected void setContentViewWithNetError() {
        rootLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.root_layout, null);
        netErrorRoot = rootLayout.findViewById(R.id.network_layout);
        noneDataRoot = rootLayout.findViewById(R.id.nonedata_layout);
        noneIcon = rootLayout.findViewById(R.id.newcar_nonedata_icon);
        noneData = rootLayout.findViewById(R.id.newcar_nonedata_btn);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(this).inflate(getLayoutView(), null);
        rootLayout.addView(view, params);
        rootLayout.findViewById(R.id.network_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againRefresh();
            }
        });
        noneData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againRefresh();
            }
        });
        super.setContentView(rootLayout);
    }


    private void init() {
        titleLayout = findViewById(R.id.newcar_headview_titlelayout);
        backIV = findViewById(R.id.newcar_headview_back);
        titleTV = findViewById(R.id.newcar_headview_title);
        rightTV = findViewById(R.id.newcar_headview_right_text);
        rightIV = findViewById(R.id.newcar_headview_right);
        mPermissionsChecker = new PermissionsChecker(this);
        initView();
        initData();
        addListener();
    }

    /**
     * 是否滑动关闭
     *
     * @param isSlidr
     */
    protected void setSlidr(boolean isSlidr) {
        if (isSlidr) {
            SlidrConfig config = new SlidrConfig.Builder()
                    .position(SlidrPosition.LEFT)
                    .edge(true)
                    .edgeSize(1.0f)
                    .build();

            Slidr.attach(this, config);
        }
    }
    protected void setSlidr(boolean isSlidr,float ratio) {
        if (isSlidr) {
            SlidrConfig config = new SlidrConfig.Builder()
                    .position(SlidrPosition.LEFT)
                    .edge(true)
                    .edgeSize(ratio)
                    .build();

            Slidr.attach(this, config);
        }
    }

    protected void setSlidr(boolean isSlidr,SlidrPosition position,float ratio) {
        if (isSlidr) {
            SlidrConfig config = new SlidrConfig.Builder()
                    .position(position)
                    .edge(true)
                    .edgeSize(ratio)
                    .build();

            Slidr.attach(this, config);
        }
    }


    /**
     * 显示网络错误页面
     */
    protected void showNetErrorLayout() {
        if (rootLayout == null)
            return;
        netErrorRoot.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏网络错误页面
     */
    protected void hideNetErrorLayout() {
        if (rootLayout == null)
            return;
        netErrorRoot.setVisibility(View.GONE);
    }

    /**
     * 显示数据错误页面
     *
     * @param errorMsg
     */
    protected void showNoneDateLayout(String errorMsg) {
        if (rootLayout == null)
            return;
        noneDataRoot.setVisibility(View.VISIBLE);
        ((TextView) rootLayout.findViewById(R.id.newcar_nonedata_btn)).setText(errorMsg);
    }

    /**
     * 显示数据错误页面
     *
     * @param errorMsg
     */
    protected void showNoneDateLayout(int errorMsg) {
        if (rootLayout == null)
            return;
        noneDataRoot.setVisibility(View.VISIBLE);
        ((TextView) rootLayout.findViewById(R.id.newcar_nonedata_btn)).setText(errorMsg);
    }

    /**
     * 隐藏数据错误页面
     */
    protected void hideNoneDateLayout() {
        if (rootLayout == null)
            return;
        noneDataRoot.setVisibility(View.GONE);
    }

    protected void showBack() {
        backIV.setVisibility(View.VISIBLE);
        backIV.setOnClickListener(this);
    }

    protected void showTitle(String title) {
        titleTV.setVisibility(View.VISIBLE);
        titleTV.setText(title);
    }

    protected void showRightTV(String title) {
        rightTV.setVisibility(View.VISIBLE);
        rightTV.setText(title);
        rightTV.setOnClickListener(this);
        rightIV.setVisibility(View.GONE);
    }

    protected void showRightIV(int resource) {
        rightIV.setVisibility(View.VISIBLE);
        rightIV.setImageResource(resource);
        rightIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newcar_headview_back:
                closeAct();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeAct();
    }

    private void closeAct() {
        actQuit();

    }


    protected void setImmerseLayout(View view) {
        if (view != null) {
            try {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                Class<?> clazz = window.getClass();
                int darkModeFlag = 0;
                Class<?> layoutParams = null;
                layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                        Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                        field.setAccessible(true);
                        field.setInt(window.getDecorView(), Color.TRANSPARENT);  //改为透明
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * 检查是否存在虚拟按键栏
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    protected void hideBar() {
        if (hasNavBar(this)) {
            //隐藏虚拟按键，并且全屏
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    private void hideSoftInput(IBinder windowToken) {
        // TODO Auto-generated method stub
        if (windowToken != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @return
     */
    protected boolean isShoulHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] h =
                    {0, 0};
            v.getLocationInWindow(h);
            int left = h[0], top = h[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /***
     * 软键盘的显示及隐藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShoulHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        try {

            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }
    }



    /**
     * 退出页面
     */
    protected void actQuit() {
    }

    /**
     * 重新刷新
     */
    protected void againRefresh() {

    }

    protected abstract void addListener();

    protected abstract void initData();

    protected abstract void initView();

    public abstract int getLayoutView();


}
