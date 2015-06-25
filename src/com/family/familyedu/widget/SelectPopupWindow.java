package com.family.familyedu.widget;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.family.familyedu.R;
import com.family.familyedu.adapter.SelectAdapter;
import com.family.familyedu.bean.SelectBean;

public class SelectPopupWindow extends PopupWindow {
	private static final String TAG = "SelectPopupWindow";
	private Context context = null;
	private ListView lv_selectView = null;
	private SelectAdapter m_selectAdapter = null;
	private RoundListView rlv_roundView = null;
	private View view = null;

	public SelectPopupWindow(Context paramContext, View paramView) {
		super(paramView);
		this.context = paramContext;
		this.view = paramView;
	}

	public void addView() {
		setContentView(this.view);
		setWidth(-1);
		setHeight(-2);
	}

	@SuppressLint({ "WrongViewCast" })
	public void addView(int paramInt, List<SelectBean> paramList,
			AdapterView.OnItemClickListener paramOnItemClickListener) {
		LayoutInflater localLayoutInflater = (LayoutInflater) this.context
				.getSystemService("layout_inflater");
		switch (paramInt) {
		case 1:
			this.view = localLayoutInflater.inflate(
					R.layout.u_select_round_window, null);
			this.rlv_roundView = ((RoundListView) this.view
					.findViewById(R.id.lvSelectRound));
			this.m_selectAdapter = new SelectAdapter(this.context, paramList, 1);
			this.rlv_roundView.setAdapter(this.m_selectAdapter);
			this.rlv_roundView.setOnItemClickListener(paramOnItemClickListener);
			setContentView(this.view);
			setWidth(-2);
			setHeight(-2);
			break;
		case 2:
			this.view = localLayoutInflater.inflate(
					R.layout.u_select_normal_window, null);
			this.lv_selectView = ((ListView) this.view
					.findViewById(R.id.lvSelectNormal));
			this.m_selectAdapter = new SelectAdapter(this.context, paramList, 2);
			this.lv_selectView.setAdapter(this.m_selectAdapter);
			this.lv_selectView.setOnItemClickListener(paramOnItemClickListener);
			setContentView(this.view);
			setWidth(-1);
			setHeight(-2);
			break;
		default:
			return;
		}
	}
}