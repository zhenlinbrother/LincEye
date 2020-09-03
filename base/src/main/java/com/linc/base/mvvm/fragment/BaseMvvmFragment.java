package com.linc.base.mvvm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.linc.base.mvvm.view.IBaseView;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;


public abstract class BaseMvvmFragment<VM extends IMvvmBaseViewModel, V extends ViewDataBinding>
        extends Fragment implements IBaseView {

    protected String TAG = this.getClass().getSimpleName();
    
    protected V mBinding;
    
    protected VM mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        Log.d(TAG, "onCreateView: ");
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = (VM) getViewModel();
        if (mViewModel != null){
            mViewModel.attachUi(this);
        }
        
        if (getBindingVariable() > 0){
            mBinding.setVariable(getBindingVariable(), mViewModel);
            mBinding.executePendingBindings();
        }

        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");

        Bundle arguments = getArguments();
        if (arguments != null){
            this.initArgs(arguments);
        }
    }

    /**
     * 初始化参数
     * @param arguments
     */
    protected void initArgs(Bundle arguments){

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onDetach() {
        super.onDetach();
        if (mViewModel != null && mViewModel.isUiAttach()){
            mViewModel.detachUi();
        }
        Log.d(TAG, "onDetach: ");
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
