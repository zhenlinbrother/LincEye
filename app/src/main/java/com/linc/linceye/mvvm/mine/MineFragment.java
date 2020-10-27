package com.linc.linceye.mvvm.mine;

import android.content.Intent;
import android.view.View;

import com.linc.base.mvvm.fragment.BaseLazyMvvmFragment;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.linceye.BuildConfig;
import com.linc.linceye.R;
import com.linc.linceye.databinding.FragmentMineBinding;
import com.linc.linceye.mvvm.download.DownloadActivity;

public class MineFragment extends BaseLazyMvvmFragment<FragmentMineBinding, IMvvmBaseViewModel> implements View.OnClickListener {
    @Override
    protected void getFirstData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        mBinding.tvVersion.setText(String.format(getString(R.string.version_name), BuildConfig.VERSION_NAME));
        mBinding.tvCache.setOnClickListener(this);
    }

    @Override
    protected IMvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cache:
                Intent intent = new Intent(getActivity(), DownloadActivity.class);
                startActivity(intent);
                break;
        }
    }
}
