package com.rtmap.game.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rtmap.game.R;
import com.rtmap.game.view.SmartImageView;

import java.io.IOException;

/**
 * Created by yxy
 * on 2017/7/27.
 */

public class CustomDialog extends Dialog {

    private final Context mContext;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvOk;
    private SmartImageView ivContent;
    private View.OnClickListener listener;

    public CustomDialog(Context context) {
        this(context, R.style.dialog);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ScreenUtil.getScreenWidth(mContext) * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void init() {
        View inflate = View.inflate(mContext, R.layout.dialog_rule, null);
        tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        tvContent = (TextView) inflate.findViewById(R.id.tv_content);
        tvOk = (TextView) inflate.findViewById(R.id.tv_ok);
        ivContent = (SmartImageView) inflate.findViewById(R.id.iv_content);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(v);
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

    public void setTitleVisiable(boolean visiable) {
        if (tvTitle != null)
            tvTitle.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setImageContentVisiable(boolean visiable) {
        if (ivContent != null) {
            ivContent.setVisibility(visiable ? View.VISIBLE : View.GONE);
            if (visiable)
                try {
                    Drawable stream = Drawable.createFromStream(mContext.getAssets().open("beed_rule.png"), "beed_rule.png");
                    int width = stream.getIntrinsicWidth();
                    int height = stream.getIntrinsicHeight();
                    float ratio = (float) width / (float) height;
                    ivContent.setRatio(ratio);
                    ivContent.setBackgroundDrawable(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
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
