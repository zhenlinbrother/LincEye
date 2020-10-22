package com.linc.download.constant;

import com.linc.download.bean.Status;

/**
 * <下载的常量> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface DownloadConstant {

    /**
     * 默认下载状态
     */
    int DEFAULT_STATUS = Status.INIT;
    /**
     * 默认的总长度
     */
    int DEFAULT_TOTAL_SIZE = -1;

    String TMP = ".tmp";
}
