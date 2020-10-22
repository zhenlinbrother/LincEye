package com.linc.download.thread;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.abc.lib_log.JLogUtils;
import com.linc.download.bean.CurStatus;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.bean.Status;
import com.linc.download.constant.ReqHead;
import com.linc.download.jerry.JerryDownload;
import com.linc.download.listener.DownloadListener;
import com.linc.download.strategy.ContinueStrategy;
import com.linc.download.strategy.FirstStrategy;
import com.linc.download.strategy.IStrategy;
import com.linc.download.utils.CloseUtils;
import com.linc.download.utils.DownloadFileUtils;
import com.linc.download.utils.FileUtils;
import com.linc.download.utils.NetUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * <下载线程> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadThread implements IDownloadThread, Runnable{

    private final static String TAG = "DownloadThread.java";

    private final int mTaskId;

    private final DownloadInfo mDownloadInfo;

    private final JLogUtils mLog;

    private final static int DURATION = 2000;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private Call mCall;

    private Response mResponse;

    //是否运行
    private volatile boolean isRunning = true;

    //最后更新时间
    private long lastUpdateTime = -1;

    //策略
    private IStrategy mStrategy;


    public DownloadThread(int mTaskId, DownloadInfo mDownloadInfo) {
        this.mTaskId = mTaskId;
        this.mDownloadInfo = mDownloadInfo;
        this.mLog = JLogUtils.getDefault();
    }

    public boolean isRunning() { return isRunning; }

    @Override
    public void run() {
        if (!isRunning) {
            return;
        }

        //设置为开始状态
        mDownloadInfo.setCurStatus(CurStatus.INIT);
        onCallback();

        mLog.title("TASK NO: 【" + mTaskId + "】 线程开启");
        mDownloadInfo.log(mLog);

        if (!isRunning) {
            stopSelf("线程停止【1】");
            return;
        }

        // 存储文件夹没创建成功
        File createFile = DownloadFileUtils.getFolder();
        if (createFile == null){
            tip("文件异常", "文件创建失败...");
            return;
        }

        mLog.content("文件夹: " + createFile.getAbsolutePath());

        if (mDownloadInfo.isStatusContains(Status.INIT)) {
            mStrategy = new FirstStrategy(mDownloadInfo, mLog);
        } else {
            mStrategy = new ContinueStrategy(mDownloadInfo, mLog);
        }

        mDownloadInfo.setCurStatus(CurStatus.DOWNLOADING);
        onCallback();

        boolean isSuc = mStrategy.run();
        mLog.content("策略运行结果: " + isSuc);

        //是否被停止
        if(!isRunning) {
            stopSelf("线程停止【2】");
            return;
        }

        //策略结果
        if (!isSuc){
            onCallback();
            CloseUtils.close(mResponse);
            //移除
            JerryDownload.getInstance().removeDownloadInfo(mDownloadInfo);
            mDownloadInfo.log(mLog);
            mLog.showError();
            return;
        }

        //装载
        File sourceFile = DownloadFileUtils.getFile(mDownloadInfo.getTmpFileName());

        //文件是否存在
        boolean fileExist = DownloadFileUtils.isExist(sourceFile);
        mLog.content("文件是否存在：" + fileExist);

        if (!fileExist){
            error("文件异常", "本地文件异常");
            return;
        }

        long length = sourceFile.length();
        mLog.param("当前本地文件长度【已存储】：" + length);
        mLog.param("range：" + length);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put(ReqHead.RANGE, "bytes=" + length + "-");

        try {
            mCall = NetUtils.request(mDownloadInfo, headerMap);
            mResponse = mCall.execute();
        } catch (UnknownHostException e) {
            tip("网络异常", "EXCEPTION: " + e.getMessage());
            return;
        } catch (IOException e) {
            mLog.content("EXCEPTION: " + e.getMessage());
        }

        if (mResponse == null) {
            tip("服务器异常", "response 为空");
            return;
        }

        if (mResponse.code() == 416){
            error("资源异常", "response 响应码为416");
            return;
        }

        // 500 - 599 服务器异常，可以下次继续
        if (mResponse.code() >= 500 && mResponse.code() < 600) {
            tip("服务器异常", "mResponse 响应失败：" + mResponse.code());
            return;
        }

        if (!mResponse.isSuccessful()) {
            tip("资源异常", "mResponse 响应失败：" + mResponse.code());
            return;
        }

        ResponseBody body = mResponse.body();
        if (body == null) {
            tip("服务器异常", "body 为空");
            return;
        }

        OutputStream outputStream;

        //todo 判断是否需要删除

        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(sourceFile, true));
        }catch (FileNotFoundException e) {
            tip("文件异常", "文件流打开失败 " + e.getMessage());
            return;
        }

        onCallback();

        boolean isSucTransmit = transmitStream(body.byteStream(),
                outputStream,
                sourceFile);

        if (!isRunning) {
            stopSelf("线程停止【4】");
            return;
        }

        if (!isSucTransmit) {
            tip("服务器异常", "isSucTransmit 失败");
            return;
        }

        String realFileName = mDownloadInfo.getRealFileName();

        boolean is = FileUtils.renameFileAtTheSameFolder(sourceFile, realFileName);

        mLog.content("重命名结果：" + is);

        if (is){
            finish();
        } else {
            error("文件异常", "");
        }

        mLog.content("最终：");
        mDownloadInfo.log(mLog);
        mLog.showInfo();

    }


    /**
     * 线程内部停止
     * @param log
     */
    private void stopSelf(String log){
        mDownloadInfo.setCurStatus(CurStatus.PAUSE);
        // todo 停止自身线程，并且从线程池移除
        JerryDownload.getInstance().removeDownloadInfo(mDownloadInfo);
        //进行回调
        onCallback();
        //提示
        mLog.content(log).showWarn();
    }

    /**
     * 异常提示
     * @param msg
     * @param log
     */
    private void tip(String msg, String log) {

        CloseUtils.close(mResponse);

        JerryDownload.getInstance().removeDownloadInfo(mDownloadInfo);

        //如果是被暂停的，则不记录错误信息
        if (!isRunning) {
            mDownloadInfo.setCurStatus(CurStatus.PAUSE);
            onCallback();
            mLog.content("线程暂停【6】").showWarn();
            return;
        }

        mLog.content(log).showWarn();

        mDownloadInfo.setTip(msg);
        mDownloadInfo.addCurStatus(CurStatus.TIP);
        mDownloadInfo.addStatus(Status.TIP);
        mDownloadInfo.update();

        onCallback();
    }

    private void error(String msg, String log) {

        CloseUtils.close(mResponse);

        JerryDownload.getInstance().removeDownloadInfo(mDownloadInfo);

        //如果是被暂停的，则不记录错误信息
        if (!isRunning) {
            mDownloadInfo.setCurStatus(CurStatus.PAUSE);
            onCallback();
            mLog.content("线程暂停【5】").showWarn();
            return;
        }

        mLog.content(log).showError();

        mDownloadInfo.setErrorMsg(msg);
        mDownloadInfo.setCurStatus(CurStatus.ERROR);
        mDownloadInfo.setStatus(Status.ERROR);
        mDownloadInfo.update();

        onCallback();
    }

    private void finish() {

        CloseUtils.close(mResponse);

        JerryDownload.getInstance().removeDownloadInfo(mDownloadInfo);

        mDownloadInfo.setCurStatus(CurStatus.FINISH);
        mDownloadInfo.setStatus(Status.FINISH);
        mDownloadInfo.update();

        onCallback();
    }

    /**
     * 停止
     */
    public synchronized void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        if (mStrategy != null) {
            mStrategy.stop();
        }

        if (mCall != null) {
            mCall.cancel();
        }

        CloseUtils.close(mResponse);
    }

    /**
     * 转接流，将 输入流-->转给-->输出流
     * @param inputStream   输入流
     * @param outputStream  输出流
     * @param sourceFile    文件
     * @return              转送成功则返回true，否则则返回false
     */
    private boolean transmitStream(InputStream inputStream,
                                   OutputStream outputStream,
                                   File sourceFile) {

        mLog.content("开始流的逻辑");

        try {
            byte[] tempByte = new byte[2048];

            while (true) {
                int len = inputStream.read(tempByte);
                if (len == -1) {
                    outputStream.flush();
                    break;
                }

                outputStream.write(tempByte, 0, len);

                checkProgress(sourceFile, mDownloadInfo.getTotalSize());

                if (!isRunning) {
                    mLog.content("线程停止 【3】");
                    updateProgress(sourceFile.length(), mDownloadInfo.getTotalSize());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mLog.content(e.getMessage());
            return false;
        } finally {
            mLog.content("流的逻辑结束");
            CloseUtils.close(inputStream);
            CloseUtils.close(outputStream);
        }

        updateProgress(sourceFile.length(), mDownloadInfo.getTotalSize());
        return true;
    }

    /**
     * 检查进度是否需要更新
     * @param sourceFile    文件
     * @param totalSize     长度
     */
    private void checkProgress(File sourceFile, long totalSize) {
        if (lastUpdateTime == -1) {
            lastUpdateTime = System.currentTimeMillis();
            updateProgress(sourceFile.length(), totalSize);
            return;
        }

        long duration = System.currentTimeMillis() - lastUpdateTime;

        if (duration > DURATION) {
            lastUpdateTime = System.currentTimeMillis();
            updateProgress(sourceFile.length(), totalSize);
        }
    }

    /**
     * 更新进度
     * @param length    当前长度
     * @param totalSize 长度
     */
    private void updateProgress(long length, long totalSize) {

        final int percent = DownloadFileUtils.calculatePercent(length, totalSize);

        JLogUtils
                .getDefault()
                .title("Task ID: " + mTaskId + " process")
                .content("length: " + length)
                .content("totalSize: " + totalSize)
                .content("percent: " + percent)
                .showInfo();

        mDownloadInfo.setPercent(percent);

        mHandler.post(() -> {
            DownloadListener listener = mDownloadInfo.getListener();
            if (listener != null) {
                listener.onProgress();
            }
        });
    }

    /**
     * 进行回调
     */
    private void onCallback(){
        mHandler.post(() -> {
            DownloadListener listener = mDownloadInfo.getListener();
            Log.i("downloadThread",
                    mTaskId +
                    "onCallback: 【 curStatus: " + mDownloadInfo.getCurStatus()
                    + ";listener: " + mDownloadInfo.getListener() + "】");

            if (mDownloadInfo.isCurStatusContains(CurStatus.WAITING)) {
                Log.d("onCallback", mTaskId + "异常！！！含有waiting");
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.ERROR)){
                listener.onError();
                return;
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.FINISH)){
                listener.onFinish();
                return;
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.TIP)){
                listener.onTip();
                return;
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.PAUSE)){
                listener.onPause();
                return;
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.INIT)){
                listener.onInit();
                return;
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.DOWNLOADING)){
                listener.onDownloading();
                return;
            }
        });
    }
}
