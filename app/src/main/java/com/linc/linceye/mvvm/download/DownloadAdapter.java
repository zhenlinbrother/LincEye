package com.linc.linceye.mvvm.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linc.download.bean.DownloadInfo;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.mvvm.loacalVideo.LocalVideoPlayerActivity;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;
import com.linc.linceye.utils.MimeTypeUtils;
import com.linc.lrecyclerview.config.LRecyclerConfig;
import com.linc.lrecyclerview.swipe.LSwipeViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DownloadAdapter extends CommonSimpleAdapter<DownloadInfo> {
    public DownloadAdapter(Context context, List<DownloadInfo> downloadInfos) {
        super(context, downloadInfos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(mInflater.inflate(LRecyclerConfig.SWIPE_LAYOUT, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        DownloadInfo downloadInfo = mData.get(position);

        //设置照片
        Integer img = null;
        if (downloadInfo.getMimeType() != null){
            int mimeType = MimeTypeUtils.getMultimediaType(downloadInfo.getMimeType());
            switch (mimeType) {
                case MimeTypeUtils.TYPE_IMAGE:
                    img = R.drawable.ic_file_img;
                    break;
                case MimeTypeUtils.TYPE_AUDIO:
                    img = R.drawable.ic_file_audio;
                    break;
                case MimeTypeUtils.TYPE_VIDEO:
                    img = R.drawable.ic_file_video;
                    break;
                default:
                    img = R.drawable.ic_file_other;
                    break;
            }
        }

        if (img == null){
            img = R.drawable.ic_file_other;
        }

        if (TextUtils.isEmpty(downloadInfo.getCover())){
            contentViewHolder.ivCover.setImageResource(img);
        } else {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(img)
                    .error(img);
            String imageUrl = downloadInfo.getCover();
            Glide.with(mContext.get())
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(contentViewHolder.ivCover);
        }

        //设置名称
        String fileName = downloadInfo.getRealFileName();
        if (TextUtils.isEmpty(fileName)){
            fileName = downloadInfo.getFileName();
        }
        contentViewHolder.tvName.setText(fileName);

        //时间大小
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-MM");
        String info = dateFormat.format(downloadInfo.getCreateTime());
        long totalSize = downloadInfo.getTotalSize();
        if (totalSize > 0) {
            String fileSize = formatFileSize(totalSize);
            info = info + "    " + fileSize;
        }
        contentViewHolder.tvInfo.setText(info);

        contentViewHolder.itemView.setOnClickListener(v -> {
            VideoHeaderBean videoHeaderBean = new VideoHeaderBean();
            videoHeaderBean.playerUrl = downloadInfo.getUrl();
            videoHeaderBean.videoTitle = downloadInfo.getRealFileName();

            Intent intent = new Intent(mContext.get(), LocalVideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("HeaderBean", videoHeaderBean);
            intent.putExtras(bundle);
            mContext.get().startActivity(intent);
        });

    }

    /**
     *
     * 格式化时间
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS <= 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static class ContentViewHolder extends LSwipeViewHolder {
        public ImageView ivCover;
        public TextView tvName;
        public TextView tvInfo;

        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.menu_right_delete;
        }

        @Override
        public int getContentLayout() {
            return R.layout.item_download;
        }

        @Override
        public void initItem(FrameLayout var1) {
            ivCover = var1.findViewById(R.id.iv_cover);
            tvName = var1.findViewById(R.id.tv_name);
            tvInfo = var1.findViewById(R.id.tv_info);
        }
    }
}
