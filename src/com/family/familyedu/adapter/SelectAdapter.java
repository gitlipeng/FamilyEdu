package com.family.familyedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.family.familyedu.R;
import com.family.familyedu.bean.SelectBean;

public class SelectAdapter extends BaseAdapter {
	private static final String TAG = "SelectAdapter";
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private int m_iStyle = -1;
	private List<SelectBean> m_selectList = new ArrayList<SelectBean>();

	public SelectAdapter(Context paramContext, List<SelectBean> paramList,
			int paramInt) {
		this.context = paramContext;
		this.m_selectList = paramList;
		this.layoutInflater = LayoutInflater.from(paramContext);
		this.m_iStyle = paramInt;
	}

	public int getCount() {
		return this.m_selectList.size();
	}

	public Object getItem(int paramInt) {
		return this.m_selectList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (this.m_selectList == null)
			return null;
		switch (this.m_iStyle) {
		case 1:
			RoundHolder localRoundHolder;
			if (paramView == null) {
				paramView = this.layoutInflater.inflate(
						R.layout.u_select_round_item, null);
				localRoundHolder = new RoundHolder();
				localRoundHolder.tv_name = ((TextView) paramView
						.findViewById(R.id.tvSelectName));
				paramView.setTag(localRoundHolder);
			} else {
				localRoundHolder = (RoundHolder) paramView.getTag();
			}
			SelectBean localSelectBean1 = (SelectBean) this.m_selectList
					.get(paramInt);
			localRoundHolder.tv_name.setText(localSelectBean1.getName());
			break;
		case 2:
			NormalHolder localNormalHolder;
			if (paramView == null) {
				paramView = this.layoutInflater.inflate(
						R.layout.u_select_normal_item, null);
				localNormalHolder = new NormalHolder();
				localNormalHolder.btn_flag = ((Button) paramView
						.findViewById(R.id.btnSelectFlag));
				localNormalHolder.btn_tag = ((Button) paramView
						.findViewById(R.id.btnSelectTag));
				localNormalHolder.tv_name = ((TextView) paramView
						.findViewById(R.id.tvSelectName));
				paramView.setTag(localNormalHolder);
			} else {
				localNormalHolder = (NormalHolder) paramView.getTag();
			}
			SelectBean localSelectBean2 = (SelectBean) this.m_selectList
					.get(paramInt);
			localNormalHolder.tv_name.setText(localSelectBean2.getName());
			break;
		default:
			break;
		}
		return paramView;
	}

	private class NormalHolder {
		public Button btn_flag;
		public Button btn_tag;
		public TextView tv_name;

		private NormalHolder() {
		}
	}

	private class RoundHolder {
		public TextView tv_name;

		private RoundHolder() {
		}
	}
}