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


public class PostionAdapter extends BaseAdapter  {
	
	Context context;
	List<PositionBean> list;
	public PostionAdapter(Context context,List<PositionBean> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.position_item, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PositionBean bean = (PositionBean) getItem(position);

		holder.tv_title.setText(bean.getTitle());
		holder.tv_content.setText("    "+bean.getContent());
		if(bean.getLearnClassID() != null){
			holder.tv_class.setText(bean.getLearnClassID().getName());
		}
		holder.tv_address.setText(bean.getArea());
		holder.tv_time.setText(bean.getDuration()+"小时");
		holder.tv_price.setText(bean.getPrice()+"元/小时");
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_content;
		TextView tv_class;
		TextView tv_address;
		TextView tv_time;
		TextView tv_price;
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
