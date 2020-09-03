package com.linc.linceye.mvvm.more.themes;

import com.google.android.material.tabs.TabLayout;
import com.linc.linceye.base.listener.IModelListener;
import com.linc.linceye.base.model.BaseModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;
import com.linc.linceye.mvvm.more.themes.bean.Tabs;

import java.util.List;

/**
 * <通知-社区> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemeFragmentViewModel extends MvvmBaseViewModel<IThemeView, ThemeModel>
        implements IModelListener<List<Tabs>> {
    @Override
    public void onLoadFinish(BaseModel model, List<Tabs> data) {
        if (getPageView() != null){
            getPageView().onDataLoaded(data);
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String prompt) {
        if (getPageView() != null){
            getPageView().showFailure(prompt);
        }
    }

    @Override
    public void initModel() {
        model = new ThemeModel();
        model.register(this);
        model.load();
    }
}
