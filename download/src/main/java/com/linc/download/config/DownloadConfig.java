package com.linc.download.config;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadConfig {

    private static volatile DownloadConfig instance = null;

    private String downloadFile = "jerry/download";

    private int threadCount = 3;

    private DownloadConfig() {}

    public static DownloadConfig getInstance() {

        if (instance == null) {
            synchronized (DownloadConfig.class) {
                if (instance == null) {
                    instance = new DownloadConfig();
                }
            }
        }

        return instance;

    }

    public void setDownloadFile(String downloadFile) {
        this.downloadFile = downloadFile;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getDownloadFile() {
        return downloadFile;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
