package com.linc.linceye;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.util.Log;

import com.gyf.immersionbar.ImmersionBar;
import com.linc.base.mvvm.activity.BaseMvvmActivity;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;

import com.linc.linceye.base.adapter.BasePageAdapter;
import com.linc.linceye.databinding.ActivityMainBinding;
import com.linc.linceye.mvvm.community.CommunityFragment;
import com.linc.linceye.mvvm.home.HomeFragment;
import com.linc.linceye.mvvm.view.TestView;
import com.linc.linceye.mvvm.viewmodel.TestViewModel;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/13
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MainActivity extends BaseMvvmActivity<IMvvmBaseViewModel, ActivityMainBinding> {

    private List<Fragment> fragments;

    private BasePageAdapter adapter;

    private NavigationController mNavigationController;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected TestViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.main_color_bar)
                .navigationBarColor(R.color.main_color_bar)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CommunityFragment());
        initView();
    }

    private void initView(){
        mNavigationController = mBinding.bottomView.material()
                .addItem(R.drawable.main_home,
                        "首页",
                        ContextCompat.getColor(this, R.color.main_bottom_check_color))
                .addItem(R.drawable.main_community,
                        "社区",
                        ContextCompat.getColor(this, R.color.main_bottom_check_color))
                .addItem(R.drawable.main_notify,
                        "通知",
                        ContextCompat.getColor(this, R.color.main_bottom_check_color))
                .addItem(R.drawable.main_user,
                        "我的",
                        ContextCompat.getColor(this, R.color.main_bottom_check_color))
                .setDefaultColor(ContextCompat.getColor(this, R.color.main_bottom_default_color))
                .enableAnimateLayoutChanges()
                .build();
        adapter = new BasePageAdapter(getSupportFragmentManager(), fragments);
        mBinding.viewPage.setOffscreenPageLimit(1);
        mBinding.viewPage.setAdapter(adapter);
        mNavigationController.setupWithViewPager(mBinding.viewPage);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showFailure(String message) {

    }
}