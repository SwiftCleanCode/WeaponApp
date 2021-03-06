package com.weapon.joker.lib.mvvm.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.weapon.joker.lib.mvvm.R;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.weapon.joker.lib.mvvm.common.PublicActivity
 *     e-mail : 1012126908@qq.com
 *     time   : 2017/10/13
 *     desc   : 公共 Activity,通过 {@link #startActivity(Activity, String, Intent, int)}
 *              跳转到指定的 Fragment
 * </pre>
 */

public class PublicActivity extends AppCompatActivity {

    /** 跳转的 fragment 详细的名字 */
    private String   mFragmentName;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentName = getIntent().getStringExtra("fragment_name");
        setContentView(R.layout.activity_public_empty);
        initFragment();
    }

    /**
     * 初始化 Fragment
     */
    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment            fragment            = getFragment();
        if (fragment == null) {
            finish();
            return;
        }
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    /**
     * @return 通过反射获取到对应的 Fragment 实例
     */
    private Fragment getFragment() {
        try {
            Class fragmentClass = Class.forName(mFragmentName);
            mFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFragment;
    }

    @Override
    public void finish() {
        if (mFragment != null && mFragment instanceof BaseFragment) {
            ((BaseFragment) mFragment).finish();
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 加入友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 加入友盟统计
        MobclickAgent.onResume(this);
    }

    /**
     * 跳转到指定的 Fragment
     *
     * @param context      activity
     * @param fragmentName 要跳转页面的 Fragment 名字，必须是全称包括包名
     */
    public static void startActivity(Activity context, String fragmentName) {
        Intent intent = new Intent(context, PublicActivity.class);
        startActivity(context, fragmentName, intent);
    }

    /**
     * 跳转到指定的 Fragment
     *
     * @param context      activity
     * @param fragmentName 要跳转页面的 Fragment 名字，必须是全称包括包名
     * @param intent       跳转的 Intent
     */
    public static void startActivity(Activity context, String fragmentName, Intent intent) {
        startActivity(context, fragmentName, intent, -1);
    }

    /**
     * 跳转到指定的 Fragment
     *
     * @param context      activity
     * @param fragmentName 要跳转页面的 Fragment 名字，必须是全称包括包名
     * @param intent       跳转的 Intent
     * @param resultCode   跳转返回的 resultCode
     */
    public static void startActivity(Activity context, String fragmentName, Intent intent, int resultCode) {
        if (context == null) {
            return;
        }

        if (!TextUtils.isEmpty(fragmentName)) {
            intent.putExtra("fragment_name", fragmentName);
        }

        if (resultCode != -1) {
            context.startActivityForResult(intent, resultCode);
        } else {
            context.startActivity(intent);
        }

    }
}
