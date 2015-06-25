package com.family.familyedu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.familyedu.R;
import com.family.familyedu.bean.User;
import com.family.familyedu.util.Constants;

public class HomeTeacherAdapter extends BaseAdapter {

	Context context;
	List<User> list;

	public HomeTeacherAdapter(Context context, List<User> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					R.layout.hometeacher_item, null);
			holder = new ViewHolder();

			holder.iv_photo = (ImageView) convertView
					.findViewById(R.id.iv_photo);
			holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			holder.tv_level = (TextView) convertView
					.findViewById(R.id.tv_level);
			holder.tv_profession = (TextView) convertView
					.findViewById(R.id.tv_profession);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_eduClass = (TextView) convertView
					.findViewById(R.id.tv_eduClass);
			holder.tv_skilledClass0 = (TextView) convertView
					.findViewById(R.id.tv_skilledClass1);
			holder.tv_skilledClass1 = (TextView) convertView
					.findViewById(R.id.tv_skilledClass2);
			holder.tv_skilledClass2 = (TextView) convertView
					.findViewById(R.id.tv_skilledClass3);
			holder.ivCall = (ImageView) convertView
					.findViewById(R.id.ivCall);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final User bean = (User) getItem(position);

		if (Constants.SEX_M.equals(bean.getSex())) {
			 holder.iv_sex.setImageResource(R.drawable.icon_sex_man);

		} else {
			 holder.iv_sex.setImageResource(R.drawable.icon_sex_woman);
		}
		holder.tv_age.setText(bean.getAge());
		if(!TextUtils.isEmpty(bean.getEduLevel())){
			holder.tv_level.setText(bean.getEduLevel());
			holder.tv_level.setVisibility(View.VISIBLE);
		}else{
			holder.tv_level.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(bean.getProfession())){
			holder.tv_profession.setText(bean.getProfession());
			holder.tv_profession.setVisibility(View.VISIBLE);
		}else{
			holder.tv_profession.setVisibility(View.GONE);
		}
		
		holder.tv_name.setText(bean.getNickname());

		holder.tv_address.setText(bean.getAddress());
		
		if(!TextUtils.isEmpty(bean.getEduClass())){
			holder.tv_eduClass.setText(bean.getEduClass());
			holder.tv_eduClass.setVisibility(View.VISIBLE);
		}else{
			holder.tv_eduClass.setVisibility(View.GONE);
		}
		
		String skilledClass = bean.getSkilledClass();
		
		if(!TextUtils.isEmpty(skilledClass)){
			String[] skilledClasss = skilledClass.split(",");
			if(skilledClasss.length == 0){
				holder.tv_skilledClass0.setVisibility(View.GONE);
				holder.tv_skilledClass1.setVisibility(View.GONE);
				holder.tv_skilledClass2.setVisibility(View.GONE);
			}else if(skilledClasss.length == 1){
				holder.tv_skilledClass0.setVisibility(View.VISIBLE);
				holder.tv_skilledClass0.setText(skilledClasss[0]);
				holder.tv_skilledClass1.setVisibility(View.GONE);
				holder.tv_skilledClass2.setVisibility(View.GONE);
			}else if(skilledClasss.length == 2){
				holder.tv_skilledClass0.setText(skilledClasss[0]);
				holder.tv_skilledClass1.setText(skilledClasss[1]);
				holder.tv_skilledClass0.setVisibility(View.VISIBLE);
				holder.tv_skilledClass1.setVisibility(View.VISIBLE);
				holder.tv_skilledClass2.setVisibility(View.GONE);
			}else if(skilledClasss.length == 3){
				holder.tv_skilledClass0.setText(skilledClasss[0]);
				holder.tv_skilledClass1.setText(skilledClasss[1]);
				holder.tv_skilledClass2.setText(skilledClasss[2]);
				holder.tv_skilledClass0.setVisibility(View.VISIBLE);
				holder.tv_skilledClass1.setVisibility(View.VISIBLE);
				holder.tv_skilledClass2.setVisibility(View.VISIBLE);
			}
		}else{
			holder.tv_skilledClass0.setVisibility(View.GONE);
			holder.tv_skilledClass1.setVisibility(View.GONE);
			holder.tv_skilledClass2.setVisibility(View.GONE);
		}
		
		if(TextUtils.isEmpty(bean.getTel())){
			holder.ivCall.setVisibility(View.GONE);
		}else{
			holder.ivCall.setVisibility(View.VISIBLE);
			holder.ivCall.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					 //用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bean.getTel()));  
	                context.startActivity(intent);  
				}
			});
		}
		
		return convertView;
	}

	class ViewHolder {
		ImageView iv_photo;
		ImageView iv_sex;
		TextView tv_age;
		TextView tv_level;
		TextView tv_profession;
		TextView tv_name;
		TextView tv_address;
		TextView tv_eduClass;
		TextView tv_skilledClass0;
		TextView tv_skilledClass1;
		TextView tv_skilledClass2;
		ImageView ivCall;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
