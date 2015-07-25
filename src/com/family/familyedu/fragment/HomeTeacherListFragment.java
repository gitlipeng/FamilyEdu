package com.family.familyedu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

import com.family.familyedu.LoginActivity;
import com.family.familyedu.MainActivity;
import com.family.familyedu.R;
import com.family.familyedu.adapter.HomeTeacherAdapter;
import com.family.familyedu.bean.User;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.Util;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.test.woshifangnu.ChatActivity;

public class HomeTeacherListFragment extends BaseFragment implements
		OnItemClickListener {
	private BaseTask task;
	private MainActivity activity;
	private List<User> positionList = new ArrayList<User>();
	PullToRefreshListView mPullToRefreshView;
	private ILoadingLayout loadingLayout;
	ListView mMsgListView;
	private static final int STATE_DISPLAY = -1;// ��ʼ������
	private static final int STATE_REFRESH = 0;// ����ˢ��
	private static final int STATE_MORE = 1;// ���ظ���

	private int limit = 10; // ÿҳ��������10��
	private int curPage = 0; // ��ǰҳ�ı�ţ���0��ʼ
	private HomeTeacherAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		initView(inflater);
		return baseView;
	}

	private void initView(LayoutInflater inflater) {
		currentView = inflater.inflate(R.layout.parentfragment, null);
		mainView.addView(this.currentView, mathLayoutParams);
		setTitleText("�ҽ�");
		initListView();
		queryData(0, STATE_DISPLAY);
	}

	private void initListView() {
		mPullToRefreshView = (PullToRefreshListView) currentView
				.findViewById(R.id.list);
		loadingLayout = mPullToRefreshView.getLoadingLayoutProxy();
		loadingLayout.setLastUpdatedLabel("");
		loadingLayout
				.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
		loadingLayout
				.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
		loadingLayout
				.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
		// //��������
		mPullToRefreshView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem == 0) {
					loadingLayout.setLastUpdatedLabel("");
					loadingLayout
							.setPullLabel(getString(R.string.pull_to_refresh_top_pull));
					loadingLayout
							.setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
					loadingLayout
							.setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
				} else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
					loadingLayout.setLastUpdatedLabel("");
					loadingLayout
							.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
					loadingLayout
							.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
					loadingLayout
							.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
				}
			}
		});

		// ����ˢ�¼���
		mPullToRefreshView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// ����ˢ��(�ӵ�һҳ��ʼװ������)
						queryData(0, STATE_REFRESH);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// �������ظ���(������һҳ����)
						queryData(curPage, STATE_MORE);
					}
				});

		mMsgListView = mPullToRefreshView.getRefreshableView();
		// ������adapter
		adapter = new HomeTeacherAdapter(activity, positionList);
		mMsgListView.setAdapter(adapter);
		// mPullToRefreshView.setAdapter(adapter);
		mMsgListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	/**
	 * ��ȡְλ�б�
	 */
	public void queryData(final int page, final int actionType) {
		if (page == 0 && STATE_DISPLAY == actionType)
			showDialog();

		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("skip", page * limit);
			jsonObject.put("limit", limit);
			jsonObject.put("condition", "");
			ace.callEndpoint(activity, Constants.FEGETFAMILYTEACHERLIST,
					jsonObject, new CloudCodeListener() {

						@Override
						public void onSuccess(Object object) {
							hideDialog();
							JSONObject jSONObject;
							try {
								jSONObject = new JSONObject(object.toString());
								JSONArray array = jSONObject
										.getJSONArray("results");
								Gson gson = new Gson();
								if (array != null && array.length() > 0) {
									if (actionType == STATE_REFRESH
											|| actionType == STATE_DISPLAY) {
										// ��������ˢ�²���ʱ������ǰҳ�ı������Ϊ0������bankCards��գ��������
										curPage = 0;
										positionList.clear();
									}

									for (int i = 0; i < array.length(); i++) {
										JSONObject jsonobject = array
												.optJSONObject(i);
										User bean = gson.fromJson(
												jsonobject.toString(),
												User.class);
										positionList.add(bean);
									}

									// ������ÿ�μ��������ݺ󣬽���ǰҳ��+1������������ˢ�µ�onPullUpToRefresh�����оͲ���Ҫ����curPage��
									curPage++;
									adapter.notifyDataSetChanged();
								} else if (actionType == STATE_MORE) {
									Util.showLToast(activity, "û�и���������");
								} else if (actionType == STATE_REFRESH) {
									Util.showLToast(activity, "û������");
								}
								mPullToRefreshView.onRefreshComplete();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							hideDialog();
							mPullToRefreshView.onRefreshComplete();
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
			hideDialog();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// User bean = positionList.get(position - 1);
		// Intent intent = new Intent(activity,PositionDetailActivity.class);
		// intent.putExtra("UserBean", bean);
		// startActivity(intent);
		// ��������ҳ��
        if (Util.getUserInfo(activity) == null) {
            // �ж����δ��¼������ת����¼ҳ��
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
        }


		Intent intent = new Intent(activity,ChatActivity.class);
	    intent.putExtra("userId", positionList.get(position - 1).getUsername());
		startActivity(intent);

	}
}
