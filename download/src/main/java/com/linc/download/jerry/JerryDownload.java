package com.linc.download.jerry;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.abc.lib_log.JLogUtils;
import com.linc.download.bean.CurStatus;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.bean.DownloadInfo_Table;
import com.linc.download.bean.Status;
import com.linc.download.config.DownloadConfig;
import com.linc.download.listener.DownloadListener;
import com.linc.download.thread.DownloadThread;
import com.linc.download.utils.DownloadFileUtils;
import com.linc.download.utils.EncryptionUtils;
import com.quanten.framework.core.utils.EncryptUtils;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <下载> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/20
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JerryDownload implements IJerryDownload{

    public static final String TAG = "JerryDownload";

    private static final int BOUND = 100000;

    private final Download mDownload;

    private static volatile JerryDownload instance = null;

    private JerryDownload() { mDownload = new Download(); }

    public static JerryDownload getInstance() {
        if (instance == null) {
            synchronized (JerryDownload.class) {
                if (instance == null) {
                    instance = new JerryDownload();
                }
            }
        }

        return instance;
    }

    /**
     * 添加下载任务
     * @param url 下载地址
     * @param fileName 文件名
     * @param cover 封面
     * @return
     */
    public DownloadInfo download(String url,
                                 String fileName,
                                 String cover){
        DownloadInfo downloadInfo = new DownloadInfo();

        // 下载路径
        downloadInfo.setUrl(url);

        //设置名字
        if (TextUtils.isEmpty(fileName)) {
            fileName = obtainRandomName(fileName);
        }
        downloadInfo.setFileName(fileName);

        //设置时间
        downloadInfo.setCreateTime(System.currentTimeMillis());

        //下载状态
        downloadInfo.setStatus(Status.INIT);

        downloadInfo.setCover(cover);

        return download(downloadInfo);
    }

    public DownloadInfo initDownload(String url,
                                 String fileName,
                                 String cover){
        DownloadInfo downloadInfo = new DownloadInfo();

        // 下载路径
        downloadInfo.setUrl(url);

        //设置名字
        if (TextUtils.isEmpty(fileName)) {
            fileName = obtainRandomName(fileName);
        }
        downloadInfo.setFileName(fileName);

        //设置时间
        downloadInfo.setCreateTime(System.currentTimeMillis());

        //下载状态
        downloadInfo.setStatus(Status.INIT);

        downloadInfo.setCover(cover);

        return downloadInfo;
    }

   /**
     * 添加下载任务
     * @param url 下载地址
     * @param fileName 文件名
     * @return
     */
    @Override
    public DownloadInfo download(String url, String fileName) {
        return download(url,
                fileName,
                "");
    }

    /**
     * 添加下载任务
     * @param downloadInfo 下载信息
     * @return
     */
    @Override
    public DownloadInfo download(DownloadInfo downloadInfo) {
        return mDownload.submitTask(downloadInfo);
    }

    /**
     * 停止任务
     * @param downloadInfo 下载信息
     */
    @Override
    public void stopTask(DownloadInfo downloadInfo) {
        mDownload.stopTask(downloadInfo);
    }

    @Override
    public void removeDownloadInfo(DownloadInfo downloadInfo) {
        mDownload.removeThread(downloadInfo);
    }

    @Override
    public void delete(DownloadInfo downloadInfo) {
        mDownload.delete(downloadInfo);
    }

    /**
     * 获取 对应用户 的下载信息
     * @return
     */
    @Override
    public List<DownloadInfo> getDownloadInfos() {
        synchronized (mDownload.LOCK) {

            //按照添加时间，由 近期 到 久远的时间获取
            OrderBy orderBy = OrderBy
                    .fromProperty(DownloadInfo_Table.createTime)
                    .descending();

            List<DownloadInfo> downloadInfos = SQLite.select()
                    .from(DownloadInfo.class)
                    .orderBy(orderBy)
                    .queryList();


            //按照 status 填充 运行时 curStatus 的值
            for (int i = 0; i < downloadInfos.size(); i++) {

                DownloadInfo downloadInfo = downloadInfos.get(i);

                //错误状态
                if (downloadInfo.isStatusContains(Status.ERROR)){
                    downloadInfo.setCurStatus(CurStatus.ERROR);
                    continue;
                }

                //完成的状态
                if (downloadInfo.isStatusContains(Status.FINISH)) {
                    downloadInfo.setCurStatus(CurStatus.FINISH);
                    continue;
                }

                //是否存在运行的线程中
                boolean isExist = false;

                //获取 本地临时 文件的信息，并设置其进度
                String tmpFileName = downloadInfo.getTmpFileName();
                if (!TextUtils.isEmpty(tmpFileName)) {
                    File tempFile = DownloadFileUtils.getFile(tmpFileName);

                    //临时文件存在，则计算其进度
                    if (tempFile.exists()) {
                        int percent = DownloadFileUtils.calculatePercent(tempFile.length(),
                                downloadInfo.getTotalSize());
                        downloadInfo.setPercent(percent);
                    }
                }

                //将从 数据库中查找 的对象替换成 线程池中的对象
                for (Map.Entry<DownloadInfo, DownloadThread> entry : mDownload.mDownloadInfoMap.entrySet()) {

                    DownloadInfo key = entry.getKey();

                    if (key.getId() == downloadInfo.getId()) {     // 在线程池中

                        downloadInfos.remove(downloadInfo);
                        downloadInfos.add(i, key);

                        isExist = true;
                        break;
                    }
                }

                //不存在，则添加为暂停
                if (!isExist) {
                    downloadInfo.setCurStatus(CurStatus.PAUSE);
                }

                // 如果存在异常，则将异常状态 “添加”
                if (downloadInfo.isStatusContains(Status.TIP)){
                    downloadInfo.addCurStatus(CurStatus.TIP);
                }
            }

            return downloadInfos;
        }
    }

    /**
     * 随机文件名字
     * @param url
     * @return
     */
    private String obtainRandomName(String url) {
        return EncryptionUtils.md5(url)
                + "-"
                + System.currentTimeMillis();
    }

    static class Download {

        private final ExecutorService mThreadPool;
        private final HashMap<DownloadInfo, DownloadThread> mDownloadInfoMap;

        private final Object LOCK;

        private final Random mRandom;

        private final Handler mHandler;

        Download() {
            LOCK = new Object();
            mThreadPool = Executors.newFixedThreadPool(DownloadConfig.getInstance().getThreadCount());
            mDownloadInfoMap = new HashMap<>();
            mRandom = new Random();
            mHandler = new Handler(Looper.getMainLooper());
        }

        DownloadInfo submitTask(DownloadInfo downloadInfo) {

            synchronized (LOCK) {
                JLogUtils log = JLogUtils.getDefault();
                log.title("添加下载任务");

                Set<DownloadInfo> downloadInfoKeySet = mDownloadInfoMap.keySet();
                // 如果 id 大于 0，说明需要检测一下之前是否开启过
                if (downloadInfo.getId() > 0) {

                    for (DownloadInfo item : downloadInfoKeySet) {

                        if (item.getId() == downloadInfo.getId()) {

                            log.content("线程池已经存在有相同的任务：");

                            DownloadThread thread = mDownloadInfoMap.get(item);

                            if (thread.isRunning()) {
                                downloadInfo.log(log);
                                log.showError();
                                return null;
                            } else {
                                mDownloadInfoMap.remove(item);
                                log.content("移除对应的线程");
                                break;
                            }

                        }

                    }

                    log.content("线程池中没有相同的任务：" + downloadInfo.getId());

                } else {
                    log.content("新增任务");
                }

                //如果添加了错误状态的任务，则终止
                if (downloadInfo.isStatusContains(Status.ERROR)){
                    log.content("错误状态").showError();
                    return null;
                }

                //如果添加了完成状态的任务，则终止
                if (downloadInfo.getStatus() == Status.FINISH){
                    log.content("成功状态").showError();
                    return null;
                }

                if (downloadInfo.getId() <= 0) {
                    //添加进数据库
                    boolean save = downloadInfo.save();

                    log.content("插入数据库结果：" + save);

                    if (!save) {
                        log.showError();
                        return null;
                    }

                    downloadInfo.log(log);
                }

                if (downloadInfo.isCurStatusContains(CurStatus.TIP)) {
                    downloadInfo.removeCurStatus(CurStatus.TIP);
                }

                if (downloadInfo.isStatusContains(Status.TIP)) {
                    downloadInfo.removeStatus(Status.TIP);
                    downloadInfo.update();
                }

                int taskId = mRandom.nextInt(BOUND);
                downloadInfo.setCurStatus(CurStatus.WAITING);

                mHandler.post(() -> {
                    DownloadListener listener = downloadInfo.getListener();
                    if (listener == null){
                        return;
                    }
                    listener.onWaitting();
                });

                DownloadThread downloadThread = new DownloadThread(taskId, downloadInfo);
                mDownloadInfoMap.put(downloadInfo, downloadThread);

                mThreadPool.submit(downloadThread);

                log.content("线程编号：" + taskId + " 提交进线程池");
                downloadInfo.log(log);
                log.showInfo();

                return downloadInfo;
            }
        }

        /**
         * 停止下载任务
         * @param downloadInfo 下载信息
         */
        void stopTask(DownloadInfo downloadInfo) {

            synchronized (LOCK) {
                DownloadInfo removeModel = null;

                //通过 downloadInfo 获取对应的线程
                for (DownloadInfo item : mDownloadInfoMap.keySet()) {
                    if (item.getId() == downloadInfo.getId()) {
                        removeModel = item;
                        break;
                    }
                }

                if (removeModel == null) {
                    return;
                }

                //终止线程
                DownloadThread downloadThread = mDownloadInfoMap.remove(removeModel);
                if (downloadThread != null) {
                    downloadThread.stop();
                }

                Log.e(TAG, "线程池中没有找到对应的线程 stopTask: " + downloadInfo);
                //设置为暂停，并通知出去
                removeModel.setCurStatus(CurStatus.PAUSE);
                DownloadListener listener = removeModel.getListener();
                if (listener != null) {
                    listener.onPause();
                }
            }
        }

        void removeThread(DownloadInfo downloadInfo) {
            synchronized (LOCK) {
                DownloadThread thread = mDownloadInfoMap.remove(downloadInfo);
                if (thread != null){
                    return;
                }

                DownloadInfo removeModel = null;
                for (DownloadInfo item : mDownloadInfoMap.keySet()) {
                    if (item.getId() == downloadInfo.getId()){
                        removeModel = item;
                        break;
                    }
                }

                if (removeModel == null) {
                    return;
                }

                mDownloadInfoMap.remove(removeModel);
            }
        }

        /**
         * 删除
         * @param downloadInfo
         */
        public void delete(DownloadInfo downloadInfo) {
            synchronized (LOCK) {
                //停止下载
                stopTask(downloadInfo);

                //删除 临时 文件
                if (!TextUtils.isEmpty(downloadInfo.getTmpFileName())) {
                    File tmpFile = DownloadFileUtils.getFile(downloadInfo.getTmpFileName());
                    if (tmpFile.exists()){
                        tmpFile.delete();
                    }
                }

                //删除 完成 文件
                if (!TextUtils.isEmpty(downloadInfo.getRealFileName())){
                    File realFile = DownloadFileUtils.getFile(downloadInfo.getRealFileName());
                    if (realFile.exists()){
                        realFile.delete();
                    }
                }

                //删除数据库
                downloadInfo.delete();
            }
        }
    }
}
