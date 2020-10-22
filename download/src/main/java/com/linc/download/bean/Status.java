package com.linc.download.bean;
/**
 * <状态> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface Status {
    /**
     * 初始化
     */
    int INIT = 1;
    /**
     * 开始下载
     */
    int DOWNLOAD = 1 << 1;
    /**
     * 完成
     */
    int FINISH = 1 << 2;
    /**
     * 错误
     */
    int ERROR = 1 << 3;
    /**
     * 异常
     */
    int TIP = 1 << 4;

    /**
     * 获取状态值的解释
     * @param type 状态值
     * @return 解释
     */
    static String getStatus(int type) {
        switch (type) {
            case Status.INIT:
                return "INIT";

            case Status.DOWNLOAD:
                return "DOWNLOAD";

            case Status.FINISH:
                return "FINISH";

            case Status.ERROR:
                return "ERROR";

            case Status.TIP:
                return "TIP";

            default:
                return "UNKOWN";
        }
    }

    /**
     * status 是否含有type
     * @param status
     * @param type
     * @return
     */
    static boolean contains(int status, int type) { return (status & type) == type; }
}
