package com.family.familyedu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.familyedu.R;
import com.family.familyedu.bean.PositionBean;
import com.family.familyedu.bean.PostCommentBean;


/**
 * 交流评论列表
 * @author user
 *
 */
public class PostCommentAdapter extends BaseAdapter  {
	
	Context context;
	List<PostCommentBean> list;
	public PostCommentAdapter(Context context,List<PostCommentBean> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.postdetail_item, null);
			holder = new ViewHolder();
			holder.imgNDHead = (ImageView) convertView.findViewById(R.id.imgNDHead);
			holder.tvNDAuthor = (TextView) convertView.findViewById(R.id.tvNDAuthor);
			holder.tvNDDate = (TextView) convertView.findViewById(R.id.tvNDDate);
			holder.tvNDFloor = (TextView) convertView.findViewById(R.id.tvNDFloor);
			holder.tvNDContent = (TextView) convertView.findViewById(R.id.tvNDContent);
			holder.rlNDAuthorFloor = (RelativeLayout) convertView.findViewById(R.id.rlNDAuthorFloor);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PostCommentBean bean = (PostCommentBean) getItem(position);

//		holder.imgNDHead.setText(bean.getTitle());
		holder.tvNDAuthor.setText(bean.getFromUserID().getNickname());
//		holder.tvNDFloor.setText(bean.getArea());
		holder.tvNDDate.setText(bean.getCreatedAt());
		holder.tvNDContent.setText(bean.getContent());
//		holder.rlNDAuthorFloor.setText(bean.getPrice()+"元/小时");
		return convertView;
	}

	class ViewHolder {
		ImageView imgNDHead;
		TextView tvNDAuthor;
		TextView tvNDDate;
		TextView tvNDFloor;
		TextView tvNDContent;
		RelativeLayout rlNDAuthorFloor;
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
