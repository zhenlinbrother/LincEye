package com.linc.download.strategy;

import android.util.Log;

import com.abc.lib_log.JLogUtils;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.bean.Status;
import com.linc.download.constant.DownloadConstant;
import com.linc.download.constant.RespHead;
import com.linc.download.utils.CloseUtils;
import com.linc.download.utils.DownloadFileUtils;
import com.linc.download.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * <首次传输策略> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FirstStrategy extends BaseStrategy{

    private static final String TAG = "FirstStrategy.java";

    private final static Object LOCK = new Object();

    public FirstStrategy(DownloadInfo mDownloadInfo, JLogUtils logUtils) {
        super(mDownloadInfo, "FirstStrategy", logUtils);
    }

    @Override
    public boolean run() {

        mLog.content("下载策略：" + mStrategyName);

        try {
            mCall = NetUtils.request(mDownloadInfo, null);
            mResponse = mCall.execute();
        } catch (UnknownHostException e) {
            setTipStatus("网络异常");
            return false;
        } catch (IOException e) {
            mLog.content("EXCEPTION: " + e.getMessage());
        }

        if (mResponse == null) {
            setTipStatus("服务器异常");
            return false;
        }

        if (!mResponse.isSuccessful()) {
            setTipStatus("服务器异常");
            return false;
        }

        ResponseBody body = mResponse.body();

        if (body == null) {
            setTipStatus("服务器异常");
            return false;
        }

        long contentLength = body.contentLength();
        if (contentLength <= 0) {
            setTipStatus("服务器异常");
            return false;
        }

        MediaType mediaType = body.contentType();

        if (mediaType == null) {
            setTipStatus("服务器异常");
            return false;
        }

        String etag = mResponse.header(RespHead.ETAG);

        mDownloadInfo.setTotalSize(contentLength);
        mDownloadInfo.setType(mediaType.subtype());
        mDownloadInfo.setMimeType(mediaType.toString());
        mDownloadInfo.setEtag(etag);

        String fileName = calculateFileName();
        if (fileName == null) {
            setTipStatus("文件创建失败");
            return false;
        }

        mDownloadInfo.setStatus(Status.DOWNLOAD);
        mDownloadInfo.update();

        CloseUtils.close(mResponse);
        return true;
    }

    /**
     * 获取文件名称
     * @return 成功则返回文件名称，否则返回null
     */
    private String calculateFileName() {

        String fileName = mDownloadInfo.getFileName();

        int i = 1;

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }

        String tmp = fileName;
        String tmpFileName = tmp + DownloadConstant.TMP;
        String realFileName = tmp + "." + mDownloadInfo.getType();

        File result;
        while (true) {
            synchronized (LOCK) {
                boolean tmpFileExist = DownloadFileUtils.isExist(tmpFileName);
                boolean realFileExist = DownloadFileUtils.isExist(realFileName);
                if (!tmpFileExist && !realFileExist) {
                    result = DownloadFileUtils.createFile(tmpFileName);

                    mDownloadInfo.setTmpFileName(tmpFileName);
                    mDownloadInfo.setRealFileName(realFileName);

                    break;
                }

                tmp = fileName + "(" + i + ")";
                tmpFileName = tmp + DownloadConstant.TMP;
                realFileName = tmp + "." + mDownloadInfo.getType();

                ++i;
            }
        }

        if (result == null){
            return null;
        }

        return tmpFileName;
    }
}
