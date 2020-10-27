package com.linc.linceye.mvvm.download;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.download.bean.DownloadInfo;

import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/26
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IDownloadView extends IBasePagingView {
    /**
     * 数据加载成功
     * @param viewModels
     * @param isFirst
     */
    void onDataLoadFinish(List<DownloadInfo> viewModels, boolean isFirst);
}
