package com.fenmi.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fenmi.app.GlobalConstant.Global;
import com.fenmi.app.utils.AesUtil;
import com.fenmi.app.utils.Base64Utils;
import com.fenmi.app.utils.RsaUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_str;
    private TextView tv_str;
    private Button btn_encode, btn_decode, btn_aesencode, btn_aesdecode,btn_allencode,btn_alldecode;
    private String secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_str = (EditText) findViewById(R.id.et_str);
        tv_str = (TextView) findViewById(R.id.tv_str);
        btn_encode = (Button) findViewById(R.id.btn_encode);
        btn_decode = (Button) findViewById(R.id.btn_decode);
        btn_aesdecode = (Button) findViewById(R.id.btn_aesdecode);
        btn_aesencode = (Button) findViewById(R.id.btn_aesencode);
        btn_allencode = (Button) findViewById(R.id.btn_allencode);
        btn_alldecode = (Button) findViewById(R.id.btn_alldecode);
        secretKey = AesUtil.generateKey();

        btn_encode.setOnClickListener(this);
        btn_decode.setOnClickListener(this);
        btn_aesencode.setOnClickListener(this);
        btn_aesdecode.setOnClickListener(this);
        btn_allencode.setOnClickListener(this);
        btn_alldecode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_encode:
                //rsa加密
                String s = et_str.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    try {
                        PublicKey publicKey = RsaUtils.loadPublicKey(Global.PUBLIC_KEY);
                        byte[] bytes = RsaUtils.encryptData(s.getBytes(), publicKey);
                        String encode = Base64Utils.encode(bytes);
//                        String encode = AesUtil.encrypt(secretKey, new String(bytes));
                        tv_str.setText(encode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_decode:
                //rsa解密
                String s1 = tv_str.getText().toString().trim();
                if (!TextUtils.isEmpty(s1)) {
                    try {
                        PrivateKey privateKey = RsaUtils.loadPrivateKey(Global.PRIVATE_KEY);
                        byte[] bytes = RsaUtils.decryptData(Base64Utils.decode(s1), privateKey);
//                        byte[] bytes = RsaUtils.decryptData(AesUtil.decrypt(secretKey,s1).getBytes(), privateKey);
                        if (null != bytes)
                            tv_str.setText(new String(bytes));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_aesencode:
                //aes加密
                String str1 = et_str.getText().toString();
                if(!TextUtils.isEmpty(str1)){
                    String encrypt = AesUtil.encrypt(secretKey, str1);
                    tv_str.setText(encrypt);
                }
                break;
            case R.id.btn_aesdecode:
                //aes解密
                String str2 = tv_str.getText().toString();
                if(!TextUtils.isEmpty(str2)){
                    String decrypt = AesUtil.decrypt(secretKey, str2);
                    tv_str.setText(decrypt);
                }
                break;
            case R.id.btn_allencode:
                //rsa+aes加密
                String ar1 = et_str.getText().toString();
                if(!TextUtils.isEmpty(ar1)){
                    String encrypt = AesUtil.encrypt(secretKey, ar1);
                    if(!TextUtils.isEmpty(encrypt)){
                        try {
                            PublicKey publicKey = RsaUtils.loadPublicKey(Global.PUBLIC_KEY);
                            byte[] bytes = RsaUtils.encryptData(encrypt.getBytes(), publicKey);
                            String encode = Base64Utils.encode(bytes);
//                        String encode = AesUtil.encrypt(secretKey, new String(bytes));
                            tv_str.setText(encode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.btn_alldecode:
                //rsa+aes解密
                String ar2 = tv_str.getText().toString().trim();
                if (!TextUtils.isEmpty(ar2)) {
                    try {
                        PrivateKey privateKey = RsaUtils.loadPrivateKey(Global.PRIVATE_KEY);
                        byte[] bytes = RsaUtils.decryptData(Base64Utils.decode(ar2), privateKey);
                        if (null != bytes)
                            tv_str.setText(AesUtil.decrypt(secretKey,new String(bytes)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
