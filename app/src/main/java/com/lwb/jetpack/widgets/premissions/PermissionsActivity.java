package com.lwb.jetpack.widgets.premissions;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.lwb.jetpack.R;
import com.lwb.jetpack.base.BaseV2AppActivity;

/**
 * Created by 10464 on 2017/6/15.
 */

public class PermissionsActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS =
            "com.addcn.newcar8891.permission"; // 权限参数
    private static final String PACKAGE_URL_SCHEME = "package:";
    public static final int PERMISSIONS_GRANTED = 5; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private PermissionsChecker mChecker; // 权限检测器
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠
    public static int PERMISSONS_REQUEST_CODE = 0;

    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (mChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions); // 请求权限
            } else {
                allPermissionsGranted(permissions); // 全部权限都已获取
            }
        } else {
            isRequireCheck = true;
        }
    }



    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    // 全部权限均已获取
    private void allPermissionsGranted(String[] permissions) {
       // if (shouldShowRequestPermissionRationale(permissions)){
            setResult(PERMISSIONS_GRANTED);
       // }
        finish();
    }


    // 请求权限兼容低版本
    private void requestPermissions(final String... permissions) {
        ActivityCompat.requestPermissions(PermissionsActivity.this, permissions, PERMISSION_REQUEST_CODE);

    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted(permissions);
        } else {
            isRequireCheck = false;
            if (shouldShowRequestPermissionRationale(permissions)) {
                requestPermissions(permissions);
            } else {
                showMissingPermissionDialog();
            }

        }
    }


    private boolean shouldShowRequestPermissionRationale(String[] permission) {
        boolean flag = false;
        for (String per : permission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, per)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle(R.string.newcar_help);
        builder.setMessage(R.string.newcar_help_hint);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.newcar_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });

        builder.setPositiveButton(R.string.newcar_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

}
