package com.family.familyedu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.family.familyedu.R;
import com.family.familyedu.bean.PositionBean;
import com.family.familyedu.bean.PostBean;


public class PostsAdapter extends BaseAdapter  {
	
	Context context;
	List<PostBean> list;
	public PostsAdapter(Context context,List<PostBean> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.postcard_item, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PostBean bean = (PostBean) getItem(position);

		holder.tv_title.setText(bean.getTitle());
		holder.tv_content.setText("    "+ bean.getContent());
		holder.tv_time.setText(bean.getCreatedAt());
		holder.tv_name.setText(bean.getFromUserID().getNickname());
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_content;
		TextView tv_time;
		TextView tv_name;
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
