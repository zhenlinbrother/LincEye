package com.linc.base.mvvm.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.linc.base.mvvm.view.IBaseView;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.base.web.BaseWebViewFragment;

import java.util.List;


public abstract class BaseMvvmActivity<VM extends IMvvmBaseViewModel, V extends ViewDataBinding>
        extends AppCompatActivity implements IBaseView {

    protected V mBinding;
    protected VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initViewModel();
        performDataBinding();
        initData();
    }

    private void performDataBinding(){
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        if (getBindingVariable() > 0){
            mBinding.setVariable(getBindingVariable(), mViewModel);
        }
        mBinding.executePendingBindings();
    }

    private  void initViewModel(){
        mViewModel = getViewModel();
        if (mViewModel != null){
            mViewModel.attachUi(this);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentActivity = this.getSupportFragmentManager();
        boolean isConsume = isConsumeBackEvent(fragmentActivity);

        if (isConsume){
            return;
        }
        super.onBackPressed();
    }

    private boolean isConsumeBackEvent(FragmentManager fragmentActivity) {
        if (fragmentActivity == null){
            return false;
        }

        List<Fragment> fragments = fragmentActivity.getFragments();
        if (fragments != null && fragments.size() > 0){
            int size = fragments.size();

            for (int i = size - 1; i >= 0; i--){
                Fragment fragment = fragments.get(i);
                if (fragment instanceof BaseWebViewFragment){
                    BaseWebViewFragment baseWebViewFragment = (BaseWebViewFragment) fragment;
                    boolean consume = baseWebViewFragment.onConsumeBackEvent(fragmentActivity);

                    if (consume){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 获取ViewModel
     * @return
     */
    protected abstract VM getViewModel();

    /**
     * 获取参数Variable
     */
    protected abstract int getBindingVariable();

    protected abstract void initData();

    protected abstract void initIntent();
}
