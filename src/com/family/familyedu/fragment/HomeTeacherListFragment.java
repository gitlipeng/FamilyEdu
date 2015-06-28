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
	private static final int STATE_DISPLAY = -1;// 初始化加载
	private static final int STATE_REFRESH = 0;// 下拉刷新
	private static final int STATE_MORE = 1;// 加载更多

	private int limit = 10; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始
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
		setTitleText("家教");
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
		// //滑动监听
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

		// 下拉刷新监听
		mPullToRefreshView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新(从第一页开始装载数据)
						queryData(0, STATE_REFRESH);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多(加载下一页数据)
						queryData(curPage, STATE_MORE);
					}
				});

		mMsgListView = mPullToRefreshView.getRefreshableView();
		// 再设置adapter
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
	 * 获取职位列表
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
										// 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
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

									// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
									curPage++;
									adapter.notifyDataSetChanged();
								} else if (actionType == STATE_MORE) {
									Util.showLToast(activity, "没有更多数据了");
								} else if (actionType == STATE_REFRESH) {
									Util.showLToast(activity, "没有数据");
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
		// 进入聊天页面
		//Intent intent = new Intent(activity,ChatActivity.class);
		//	intent.putExtra("userId", positionList.get(position - 1).getUsername());
		//startActivity(intent);
		
	}
}
