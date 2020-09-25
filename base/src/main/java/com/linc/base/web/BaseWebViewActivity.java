package com.linc.base.web;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.linc.base.R;
import com.linc.base.base.utils.UIUtils;

public class BaseWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    private String mUrl;
    private String mTitle;

    private int yOffset;
    private int xOffset;

    private ImageView ivBack;
    private TextView tvTitle;
    private FrameLayout frameLayoutContainer;
    private ImageView ivMenu;

    private BaseWebViewFragment baseWebViewFragment;

    private PopupWindow mMenuWindow;

    public static void startActivity(Context context,
                                     String url){
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(URL, url);

        context.startActivity(intent);
    }

    public static void startActivity(Context context,
                                     String url,
                                     String title){
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);

        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mUrl = getIntent().getStringExtra(URL);
        mTitle = getIntent().getStringExtra(TITLE);

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        frameLayoutContainer = findViewById(R.id.frame_layout_container);
        ivMenu = findViewById(R.id.iv_menu);

        tvTitle.setText(mTitle);

        xOffset = UIUtils.dip2px(this, 3.5f);
        yOffset = UIUtils.dip2px(this, 20);

        View view = getLayoutInflater().inflate(R.layout.window_web_view_menu, null);
        view.findViewById(R.id.ll_crop).setOnClickListener(this);
        view.findViewById(R.id.ll_open).setOnClickListener(this);
        view.findViewById(R.id.ll_refresh).setOnClickListener(this);

        if (mMenuWindow == null){
            mMenuWindow = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );

            mMenuWindow.setOutsideTouchable(true);
            mMenuWindow.setBackgroundDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_one_inch_alpha)
            );
        }

        ivMenu.setOnClickListener(v -> mMenuWindow.showAsDropDown(ivMenu, -xOffset, -yOffset, Gravity.START));
        ivBack.setOnClickListener(v -> finish());

        baseWebViewFragment = BaseWebViewFragment.newInstance(mUrl);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout_container, baseWebViewFragment)
                .commit();
    }


    @Override
    public void onClick(View v) {
        if (mMenuWindow != null){
            mMenuWindow.dismiss();
        }

        if (baseWebViewFragment == null){
            return;
        }

        int id = v.getId();
        if (id == R.id.ll_open) {
            if (TextUtils.isEmpty(mUrl)) {
                return;
            }

            Uri contentUri = Uri.parse(mUrl);

            Intent intent = new Intent();
            intent.setAction("android.intent.action.View");
            intent.setData(contentUri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.getApplication().startActivity(intent);
        } else if (id == R.id.ll_refresh) {
            baseWebViewFragment.reload();
        }
    }
}
