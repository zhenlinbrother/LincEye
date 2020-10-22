package com.linc.download.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * author       : zinc
 * time         : 2019-10-12 16:04
 * desc         : 下载配置
 * version      : 1.0.0
 */

@Database(version = DownloadDB.VERSION)
public class DownloadDB {

    public static final int VERSION = 1;

    public static final String NAME = "JerryDownload";

}
