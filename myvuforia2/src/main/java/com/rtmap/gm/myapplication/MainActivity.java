package com.rtmap.gm.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.util.ZXingUtil;

public class MainActivity extends Activity {
    private EditText et;
    private Button btn;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        et.setText((String) SPUtil.get(this, "phone", ""));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(MainActivity.this, "输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                SPUtil.put(MainActivity.this, "phone", phone);
                Intent intent = new Intent(MainActivity.this, AndroidLauncher.class);
                intent.putExtra("ar_phone", phone);
                startActivity(intent);
            }
        });
        final Bitmap bitmap = Bitmap.createBitmap(300, 300,
                Bitmap.Config.ARGB_8888);
        ZXingUtil.createQr("111111111", 300, 300, bitmap);
        Log.e("fail", (bitmap == null) + "");
        iv.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
