package com.family.familyedu;

import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}
