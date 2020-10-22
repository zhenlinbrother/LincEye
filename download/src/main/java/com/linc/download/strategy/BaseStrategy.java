package com.linc.download.strategy;

import android.os.Handler;
import android.os.Looper;

import com.abc.lib_log.JLogUtils;
import com.linc.download.bean.CurStatus;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.bean.Status;
import com.linc.download.utils.CloseUtils;

import okhttp3.Call;
import okhttp3.Response;


/**
 * <传输策略 基类> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseStrategy implements IStrategy{

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    protected final DownloadInfo mDownloadInfo;

    protected final String mStrategyName;

    protected final JLogUtils mLog;

    protected boolean mIsRunning;

    protected Response mResponse;

    protected Call mCall;

    public BaseStrategy(DownloadInfo mDownloadInfo,
                        String mStrategyName,
                        JLogUtils logUtils) {
        this.mDownloadInfo = mDownloadInfo;
        this.mStrategyName = mStrategyName;
        this.mLog = logUtils;
        this.mIsRunning = true;
    }

    /**
     * 设置错误状态
     * @param errorMsg
     */
    protected void setErrorStatus(String errorMsg) {
        //如果是被暂停的，则不记录错误信息
        if (!mIsRunning) {
            mDownloadInfo.setCurStatus(CurStatus.PAUSE);
            return;
        }

        mDownloadInfo.setErrorMsg(errorMsg);
        mDownloadInfo.setCurStatus(CurStatus.ERROR);
        mDownloadInfo.setStatus(Status.ERROR);
        mDownloadInfo.update();

        CloseUtils.close(mResponse);
    }

    /**
     * 保存异常信息
     * @param tipMsg
     */
    protected void setTipStatus(String tipMsg) {
        // 如果是被暂停的，则不记录异常信息
        if (!mIsRunning) {
            mDownloadInfo.setCurStatus(CurStatus.PAUSE);
            return;
        }

        mDownloadInfo.setTip(tipMsg);
        mDownloadInfo.addCurStatus(CurStatus.TIP);
        mDownloadInfo.addStatus(Status.TIP);
        mDownloadInfo.update();

        CloseUtils.close(mResponse);
    }

    @Override
    public void stop() {
        mIsRunning = false;
        if (mCall != null) {
            mCall.cancel();
        }
        CloseUtils.close(mResponse);
    }
}
