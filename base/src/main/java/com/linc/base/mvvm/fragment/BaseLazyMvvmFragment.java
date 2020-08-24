package com.linc.base.mvvm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.linc.base.base.listener.IStateListener;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;

import java.util.List;

/**
 * <基于MVVM模式的 带有上拉刷新和下拉加载 基类 Fragment> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseLazyMvvmFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
        extends BaseMvvmFragment<VM, V> implements IStateListener {

    protected View rootView = null;

    protected boolean isViewCreated = false;

    protected boolean currentVisibleState = false;

    protected boolean mIsFirstVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            rootView = mBinding.getRoot();
        }
        isViewCreated = true;
        Log.d(TAG, "onCreateView: ");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        //初始化的时候，判断当前fragment可见状态
        //isHidden在使用FragmentTransaction的show/hidden时会调用，可见返回false
        mViewModel = getViewModel();
        if (mViewModel != null){
            mViewModel.attachUi(this);
        }
        if (getBindingVariable() > 0){
            mBinding.setVariable(getBindingVariable(), mViewModel);
            mBinding.executePendingBindings();
        }
        if (!isHidden() && getUserVisibleHint()){
            //可见状态，进行时间分发
            dispatchUserVisibleHint(true);
        }

    }

    /**
     * 修改fragment的可见性 setUserVisibleHint 被调用有两种情况:
     * 1、在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数
     * 2、对于之前已经调用过setUserVisibleHint方法的fragment后，让fragment从可见到不可见状态的变化
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser);
        if (isViewCreated){
            //情况二需分情况考虑：1、可见  2、可见 -> 不可见
            //对于1，首先必须是可见的（isVisibleToUser == true) 而且只有当可见状态进行改变的时候才会切换，
            //否则则进行反复调用，从而导致时间分发的多次更新
            if (isVisibleToUser && !currentVisibleState){
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState){
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: " + hidden);
        super.onHiddenChanged(hidden);
        //这里的可见返回为false
        if (hidden){
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    private void dispatchUserVisibleHint(boolean isVisible){
        Log.d(TAG, "dispatchUserVisibleHint: " + isVisible);

        //首先考虑下fragment嵌套fragment的情况
        if (isVisible && isParentInvisible()){
            return;
        }
        //如果当前状态与需要设置的状态一致，则不处理
        if (currentVisibleState == isVisible){
            return;
        }
        currentVisibleState = isVisible;
        if (isVisible){
            if (mIsFirstVisible){
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            //分发事件给内嵌的fragment
            dispatchUserVisibleState(true);
        } else {

            onFragmentPause();
            dispatchUserVisibleState(false);
        }
    }

    /**
     * 在双重ViewPager嵌套的情况下，第一次滑到Fragment嵌套ViewPager(fragment)的场景时候
     * 此时只会加载外层fragment的数据，而不会加载内嵌viewpager中的fragment的数据
     * 因此，我们需要在此增加一个当外层fragment可见的时候，分发可见事件给自己内嵌的所有fragment显示
     * @param visible
     */
    private void dispatchUserVisibleState(boolean visible){
        Log.d(TAG, "dispatchUserVisibleHint: " + visible);
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null){
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseLazyMvvmFragment
                        && !fragment.isHidden()
                        && fragment.getUserVisibleHint()){

                    ((BaseLazyMvvmFragment) fragment).dispatchUserVisibleHint(true);
                }
            }
        }
    }

    /**
     * 第一次可见,根据业务进行初始化操作
     */
    protected  void onFragmentFirstVisible(){
        Log.d(TAG,"onFragmentFirstVisible  第一次可见");
    }

    /**
     * Fragment真正的Resume,开始处理网络加载等耗时操作
     */
    protected void onFragmentResume()
    {
        Log.d(TAG,"onFragmentResume" + " 真正的Resume 开始相关操作耗时");
    }

    /**
     * Fragment真正的Pause,暂停一切网络耗时操作
     */
    protected void onFragmentPause()
    {
        Log.d(TAG,"onFragmentPause " + " 真正的Pause 结束相关操作耗时");

    }


    private boolean isParentInvisible(){
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BaseLazyMvvmFragment){
            BaseLazyMvvmFragment fragment = (BaseLazyMvvmFragment) parentFragment;
            return !fragment.isSupportVisible();
        }
        return false;
    }

    private boolean isSupportVisible(){return  currentVisibleState;}
    /**
     * 第一次获取数据
     */
    protected abstract void getFirstData();

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible){
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()){
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用
     * dispatchUserVisibleHint currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前fragment 包含 子fragment 的时候 dispatchUserVisibleHint内部本身会通知子fragment不可见 子fragment走到这里时候自身又会调用一趟
     */
    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()){
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null && mViewModel.isUiAttach()){
            mViewModel.detachUi();
        }
        Log.d(TAG, "onDestroy: ");
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

    @Override
    public void onRetry() {
        getFirstData();
    }

    @Override
    public void onLoading() {
        getFirstData();
    }

    @Override
    public void onEmpty() {
        getFirstData();
    }
}
