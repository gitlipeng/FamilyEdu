package com.family.familyedu;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.User;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.LogUtil;
import com.family.familyedu.util.Util;

/**
 * 注册
 *
 * @author user
 */
public class RegisterActivity extends BaseActivity implements OnClickListener, TaskListener {
    /**
     * 注册帐号
     */
    private String userName;

    /**
     * 注册密码
     */
    private String password;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 注册帐号
     */
    private EditText mUserName;

    /**
     * 密码
     */
    private EditText mPassword;

    /**
     * 密码确认
     */
    private EditText mConfimPassword;

    /**
     * 注册按钮
     */
    private Button mRegisterBtn;

    private RadioGroup mUserTypeRG;

    private BaseTask task;

    private BaseData registerData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        task = new BaseTask(this, this);
        registerData = new BaseData();
        initView();
    }

    private void initView() {
        currentView = layoutInflater.inflate(R.layout.register, null);
        mainView.addView(this.currentView, mathLayoutParams);

        setTitleText(getString(R.string.register));
        setTopLeftButton("", R.drawable.return_back, this);

        mUserName = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.pwd);
        mConfimPassword = (EditText) findViewById(R.id.confim_pwd);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);
        mUserTypeRG = (RadioGroup) findViewById(R.id.userType);
        mUserTypeRG
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (group.getCheckedRadioButtonId() == R.id.jiazhang) {
                            // 家长
                            userType = Constants.USERTYPE_PARENT;
                        } else {
                            // 家教
                            userType = Constants.USERTYPE_TEACHER;
                        }
                    }
                });
    }

    /**
     * 验证数据
     *
     * @return
     */
    private boolean checkData() {
        String emailAddress = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confimPassword = mConfimPassword.getText().toString().trim();

        // 邮箱地址是否为空
        if ("".equals(emailAddress)) {
            mUserName.setError(Util.getTextError("请输入用户名"));
            mUserName.requestFocus();
            return false;
        } else {
            mUserName.setError(null);
        }

        // 密码是否为空
        if ("".equals(password)) {
            mPassword.setError(Util.getTextError("请输入密码"));
            mPassword.requestFocus();
            return false;
        } else {
            mPassword.setError(null);
        }

        // 密码长度
        // if(password.length() < 6){
        // mPassword.setError(Util.getTextError(getString(R.string.error_msg_30)));
        // mPassword.requestFocus();
        // return false;
        // }else{
        // mPassword.setError(null);
        // }

        // 密码格式
        // if (!Util.checkPasswordNum(password)) {
        // mPassword.setError(Util.getTextError(getString(R.string.error_msg_31)));
        // mPassword.requestFocus();
        // return false;
        // }else{
        // mPassword.setError(null);
        // }

        // 密码确认是否为空
        if ("".equals(confimPassword)) {
            mConfimPassword.setError(Util.getTextError("请输入确认密码"));
            mConfimPassword.requestFocus();
            return false;
        } else {
            mConfimPassword.setError(null);
        }

        // 密码和密码确认是否一致
        if (!password.equals(confimPassword)) {
            mConfimPassword.setError(Util.getTextError("密码与确认密码不一致，请重新输入"));
            mConfimPassword.requestFocus();
            return false;
        } else {
            mConfimPassword.setError(null);
        }

        if (TextUtils.isEmpty(userType)) {
            Util.showSToast(this, "请选择您的身份");
        }
        return true;
    }

    private void register() {
        JSONObject jsonObject = new JSONObject();
        try {
            userName = mUserName.getText().toString().trim();
            password = mPassword.getText().toString().trim();
            jsonObject.put("loginName", userName);
            jsonObject.put("loginPwd", password);
            jsonObject.put("userType", userType);
            task.requestData(Constants.FEREGISTER, jsonObject, registerData, "parseRegisterData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBaseLeft:
                // 返回
                finish();
                break;
            case R.id.btn_register:
                // 注册
                if (checkData()) {
                    register();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(Object object) {
        if (object.getClass().equals(BaseData.class)) {
            //注册成功
            registerData = (BaseData) object;
            resigterHX(userName,userName);
        }
    }

    @Override
    public void requestFail(Object object) {

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            login();
        }
    };

   /**
     * 注册环信账号,推荐服务端处理
     */
    private void resigterHX(final String username, final String pwd) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, pwd);
                    handler.sendEmptyMessage(0);
                } catch (final Exception e) {
                    //注册失败
//                    int errorCode = e.getErrorCode();
//                    if (errorCode == EMError.NONETWORK_ERROR) {
//                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
//                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
//                        Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
//                    } else if (errorCode == EMError.UNAUTHORIZED) {
//                        Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
//                    } else {
//                    Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }).start();
    }


    private void login() {
        BaseTask task = new BaseTask(this);
        showDialog();
        task.login(userName, password, new SaveListener() {
            @Override
            public void onSuccess() {
                Util.savePassword(password);// 记住密码
                loginHX(userName,userName);//登录环信
            }

            @Override
            public void onFailure(int code, String msg) {
                hideDialog();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void loginSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
