package com.linc.download.bean;

import com.abc.lib_log.JLogUtils;
import com.linc.download.constant.DownloadConstant;
import com.linc.download.db.DownloadDB;
import com.linc.download.listener.DownloadListener;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * <下载的信息> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Table(database = DownloadDB.class)
public class DownloadInfo extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private long id;

    /**
     * 下载url【必填】
     */
    @Column
    private String url;

    /**
     * 文件名称，用户传入。状态为 init 时，会使用【必填】
     */
    @Column
    private String fileName;

    /**
     * 创建时间【必填】
     */
    @Column
    private long createTime;

    /**
     * 资源当前状态【必填】
     */
    @Column(defaultValue = DownloadConstant.DEFAULT_STATUS + "")
    private int status;

    //--------------------------------- 初始化 -----------------------------------------
    /**
     * 总长度【初始化完填写】
     */
    @Column (defaultValue = DownloadConstant.DEFAULT_TOTAL_SIZE + "")
    private long totalSize;

    /**
     * 类型 【初始化完填写】
     */
    @Column
    private String type;

    @Column
    private String mimeType;

    /**
     * 资源标记【初始化完填写】
     */
    @Column
    private String etag;

    /**
     * 临时文件名称【初始化完填写】
     * xxx.tmp
     */
    @Column
    private String tmpFileName;

    /**
     * 真是文件名称【初始化完填写】
     * xxx.mp4
     */
    @Column
    private String realFileName;

    /**
     * 封面
     */
    @Column
    private String cover;

    /**
     * 最后修改时间
     */
    @Column
    private String lastModified;

    // ------------------------------- 非正常信息 -----------------------------
    /**
     * 错误信息
     */
    @Column
    private String errorMsg;

    /**
     * 异常信息
     */
    @Column
    private String tip;

    //--------------------------------- 未持久化信息 ---------------------------

    /**
     * 监听器
     */
    private DownloadListener listener;

    /**
     * 运行时状态【展示使用】
     */
    private int curStatus;

    /**
     * 进度 [0-100]
     */
    private  int percent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getTmpFileName() {
        return tmpFileName;
    }

    public void setTmpFileName(String tmpFileName) {
        this.tmpFileName = tmpFileName;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public DownloadListener getListener() {
        return listener;
    }

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    /**
     * 判断 status 是否含有 type
     * @param type
     * @return
     */
    public boolean isStatusContains(int type) {
        return Status.contains(status, type);
    }

    /**
     * 判断 curStatus 是否含有 type
     * @param type
     * @return
     */
    public boolean isCurStatusContains(int type) {
        return CurStatus.contains(curStatus, type);
    }

    /**
     * 往 status 添加 type
     * @param type
     */
    public void addStatus(int type) { status |= type; }

    /**
     * 从 status 移除 type
     * @param type
     */
    public void removeStatus(int type) { status &= ~type; }

    /**
     * 往 curStatus 添加 type
     * @param type
     */
    public void addCurStatus(int type) { curStatus |= type; }

    /**
     * 往 curStatus 移除 type
     * @param type
     */
    public void removeCurStatus(int type) { curStatus &= ~type; }

    public void log(JLogUtils log) {
        log.title("DownloadInfo")
                .param("id=" + id)
                .param("url='" + url + "'")
                .param("filename='" + fileName)
                .param("createTime='" + createTime)
                .param("status=" + Status.getStatus(status) + "【" + status + "】")
                .param("totalSize=" + totalSize)
                .param("type='" + type + "'")
                .param("mimeType='" + mimeType + "'")
                .param("etag='" + etag + "'")
                .param("tmpFilename='" + tmpFileName)
                .param("realFilename='" + realFileName)
                .param("errorMsg='" + errorMsg)
                .param("tip='" + tip)
                .param("curStatus=" + curStatus)
                .param("listener='" + listener)
                .param("percent='" + percent);
    }
}
