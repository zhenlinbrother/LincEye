package com.linc.linceye.mvvm.viewmodel;

import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;
import com.linc.linceye.mvvm.model.TestModel;
import com.linc.linceye.mvvm.view.TestView;

public class TestViewModel extends MvvmBaseViewModel<TestView, TestModel> {

    @Override
    public void initModel() {
        model = new TestModel();
        model.load();
    }
}
