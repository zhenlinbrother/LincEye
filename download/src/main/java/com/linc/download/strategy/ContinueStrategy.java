package com.linc.download.strategy;

import android.util.Log;

import com.abc.lib_log.JLogUtils;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.constant.ReqHead;
import com.linc.download.utils.DownloadFileUtils;
import com.linc.download.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.Call;

/**
 * <续传策略> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ContinueStrategy extends BaseStrategy{

    private static final String TAG = "ContinueStrategy.java";

    public ContinueStrategy(DownloadInfo mDownloadInfo, JLogUtils jLogUtils) {
        super(mDownloadInfo, "ContinueStrategy", jLogUtils);
    }

    @Override
    public boolean run() {

        mLog.content("下载策略: " + mStrategyName);

        HashMap<String, String> headMap = new HashMap<>();
        headMap.put(ReqHead.If_None_Match, mDownloadInfo.getEtag());

        try {
            Call request = NetUtils.request(mDownloadInfo, headMap);
            mResponse = request.execute();
        } catch (UnknownHostException e){
            setTipStatus("网络异常");
            return false;
        } catch (IOException e){
            mLog.content("EXCEPTION: " + e.getMessage());
        }

        if (mResponse == null){
            setTipStatus("服务器异常");
            return false;
        }

        //说明之前的资源没变动
        if (mResponse.code() == 304) {
            return true;
        }

        if (!mResponse.isSuccessful()) {
            setTipStatus("服务器异常");
            return false;
        }

        boolean isDel = DownloadFileUtils.deleteFile(mDownloadInfo.getRealFileName());
        if (!isDel) {
            setErrorStatus("文件异常");
            return false;
        }

        File file = DownloadFileUtils.createFile(mDownloadInfo.getRealFileName());
        if (file == null) {
            setErrorStatus("文件异常");
            return false;
        }
        return true;
    }
}
