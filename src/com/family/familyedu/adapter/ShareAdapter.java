package com.family.familyedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.family.familyedu.R;
import com.family.familyedu.bean.ShareBean;

public class ShareAdapter extends BaseAdapter {
	private static final String TAG = "ShareAdapter";
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private List<ShareBean> m_shareList = new ArrayList<ShareBean>();

	public ShareAdapter(Context paramContext, List<ShareBean> paramList) {
		this.context = paramContext;
		this.layoutInflater = LayoutInflater.from(paramContext);
		this.m_shareList = paramList;
	}

	public void dataNotifyChange(List<ShareBean> paramList) {
		this.m_shareList = paramList;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.m_shareList.size();
	}

	public Object getItem(int paramInt) {
		return this.m_shareList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (this.m_shareList == null)
			return null;
		ViewHolder localViewHolder;
		if (paramView == null) {
			paramView = this.layoutInflater
					.inflate(R.layout.u_share_item, null);
			localViewHolder = new ViewHolder();
			localViewHolder.img_tag = ((ImageView) paramView
					.findViewById(R.id.imgShareTag));
			localViewHolder.tv_name = ((TextView) paramView
					.findViewById(R.id.tvShareName));
			paramView.setTag(localViewHolder);
		} else {
			localViewHolder = (ViewHolder) paramView.getTag();
		}
		ShareBean localShareBean = (ShareBean) this.m_shareList.get(paramInt);
		localViewHolder.img_tag
				.setBackgroundResource(localShareBean.getResId());
		localViewHolder.tv_name.setText(localShareBean.getShareName());
		return paramView;
	}

	private class ViewHolder {
		public ImageView img_tag;
		public TextView tv_name;

		private ViewHolder() {
		}
	}
}