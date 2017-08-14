package com.rtmap.gm.myvuforia.ImageTargets;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.rtmap.gm.myvuforia.R;

/**
 * Created by yxy
 * on 2017/7/27.
 */

public class CustomDialog extends Dialog {

    private final Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvOk;
    private View.OnClickListener listener;

    public CustomDialog(Context context) {
        this(context, R.style.dialog);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(getScreenWidth(mContext) * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void init() {
        View inflate = mLayoutInflater.inflate(R.layout.dialog_rule, null);
        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        tvContent = (TextView) inflate.findViewById(R.id.tv_content);
        tvOk = (TextView) inflate.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                    dismiss();
                }
            }
        });
        setCancelable(false);
        setContentView(inflate);
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setContentVisiable(boolean visiable) {
        if (tvContent != null)
            tvContent.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setContentText(String content) {
        if (tvContent != null)
            tvContent.setText(content);
    }

    public void setTitleText(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    public void setButtonText(String btn) {
        if (tvOk != null)
            tvOk.setText(btn);
    }
}
