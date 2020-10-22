package com.linc.download.listener;
/**
 * <下载监听> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface DownloadListener {
    /**
     * 暂停
     */
    void onPause();

    /**
     * 等待下载
     */
    void onWaitting();

    /**
     * 初始化
     */
    void onInit();

    /**
     * 开始下载
     */
    void onDownloading();

    /**
     * 异常
     */
    void onTip();

    /**
     * 错误
     */
    void onError();

    /**
     * 完成
     */
    void onFinish();

    /**
     * 进度
     */
    void onProgress();
}
