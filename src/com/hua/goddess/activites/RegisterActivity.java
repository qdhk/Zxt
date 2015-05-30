/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hua.goddess.activites;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.hua.goddess.R;
import com.hua.goddess.dao.WsRequestHelper;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends Activity {

	final String REGISTER = "user_register";
	// WsHelper wh = new WsHelper();
	WsRequestHelper wh;
	private EditText emailEditText;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	ProgressDialog pd;
	private String sex = null;

	private String uid = null;

	RadioGroup rg;
	RadioButton b1;
	RadioButton b2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		pd = new ProgressDialog(RegisterActivity.this);
		// DeviceUuidFactory uuid = new DeviceUuidFactory(this);
		// uid = uuid.getDeviceUuid().toString();

		emailEditText = (EditText) findViewById(R.id.email);
		emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);// 设置限制邮箱格式
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);

		rg = (RadioGroup) findViewById(R.id.sex);
		b1 = (RadioButton) findViewById(R.id.male);
		b2 = (RadioButton) findViewById(R.id.female);
		wh = new WsRequestHelper(new WsRequestHelper.InterfaceCallBack() {
			@Override
			public void RequestCallBack(Object result) {
				// TODO Auto-generated method stub
				pd.dismiss();
				// Toast.makeText(RegisterActivity.this, result.toString(),
				// Toast.LENGTH_SHORT).show();
				if (result.toString().equals("注册成功")) {
					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", userNameEditText.getText()
							.toString().trim());

					intent.putExtras(bundle);
					setResult(100, intent);
					finish();
				}

			}
		});

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == b1.getId()) {
					sex = "1";
					Toast.makeText(RegisterActivity.this, "男",
							Toast.LENGTH_LONG).show();
				}
				if (checkedId == b2.getId()) {
					sex = "2";
					Toast.makeText(RegisterActivity.this, "女",
							Toast.LENGTH_LONG).show();
				}

			}

		});
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String email = emailEditText.getText().toString().trim();
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		// if (TextUtils.isEmpty(email)) {
		// Toast.makeText(this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
		// emailEditText.requestFocus();
		// return;
		// } else
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (sex == null) {
			Toast.makeText(this, "请选择您的性别！", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {

			pd.setMessage("正在注册...");
			pd.show();

			Map<String, Object> values = new HashMap<String, Object>();
			values.put("name", username);
			values.put("pwd", pwd);
			values.put("sex", sex);
			wh.Request(REGISTER, values);

		}
	}

	public void back(View view) {

		this.finish();
	}

}
