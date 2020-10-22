package com.linc.download.bean;
/**
 * <当前状态> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface CurStatus {

    /**
     * 暂停
     */
    int PAUSE = 1;

    /**
     * 等待下载
     */
    int WAITING = 1 << 1;

    /**
     * 初始化下载
     */
    int INIT = 1 << 2;

    /**
     * 正在下载
     */
    int DOWNLOADING = 1 << 3;

    /**
     * 异常
     */
    int TIP = 1 << 4;

    /**
     * 错误
     */
    int ERROR = 1 << 5;

    /**
     * 完成
     */
    int FINISH = 1 << 6;

    /**
     * 获取状态值得解释
     * @param type 状态值
     * @return 解释
     */
    static String getStatus(int type) {
        switch (type) {
            case CurStatus.PAUSE:
                return "PAUSE";

            case CurStatus.WAITING:
                return "WAITING";

            case CurStatus.INIT:
                return "INIT";

            case CurStatus.DOWNLOADING:
                return "DOWNLOADING";

            case CurStatus.TIP:
                return "TIP";

            case CurStatus.ERROR:
                return "ERROR";

            case CurStatus.FINISH:
                return "FINISH";

            default:
                return "UNKNOW";
        }
    }

    /**
     * status 是否含有 type
     * @param status 状态值
     * @param type 类型
     * @return true 包含， false 不包含
     */
    static boolean contains(int status, int type) { return (status & type) == type; }
}
