package com.linc.download.jerry;

import com.linc.download.bean.DownloadInfo;

import java.util.List;

/**
 * <下载接口> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/20
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IJerryDownload {

    /**
     * 添加下载
     * @param url 下载地址
     * @param fileName 文件名
     * @param cover 封面
     * @return 下载信息
     */
    DownloadInfo download(String url,
                           String fileName,
                           String cover);

    /**
     * 添加下载
     * @param url 下载地址
     * @param fileName 文件名
     * @return 下载信息
     */
    DownloadInfo download(String url,
                          String fileName);

    /**
     * 添加下载
     * @param downloadInfo 下载信息
     * @return 下载信息
     */
    DownloadInfo download(DownloadInfo downloadInfo);

    /**
     * 停止下载
     * @param downloadInfo 下载信息
     */
    void stopTask(DownloadInfo downloadInfo);

    /**
     * 移除下载信息
     * @param downloadInfo 下载信息
     */
    void removeDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 删除
     * @param downloadInfo 下载信息
     */
    void delete(DownloadInfo downloadInfo);

    /**
     * 获取下载信息
     * @return
     */
    List<DownloadInfo> getDownloadInfos(int start, int size);
}
