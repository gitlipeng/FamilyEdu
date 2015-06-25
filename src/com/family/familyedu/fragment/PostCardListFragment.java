package com.family.familyedu.fragment;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

import com.family.familyedu.MainActivity;
import com.family.familyedu.PostCardAvtivity;
import com.family.familyedu.PostCardDetailActivity;
import com.family.familyedu.PostPositionActivity;
import com.family.familyedu.R;
import com.family.familyedu.adapter.PostsAdapter;
import com.family.familyedu.bean.PostBean;
import com.family.familyedu.util.Util;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 交流帖子
 * @author user
 *
 */
public class PostCardListFragment extends BaseFragment implements OnItemClickListener{
	private static final int POSTREQUESTID = 1;
	private MainActivity activity;
	private List<PostBean> positionList = new ArrayList<PostBean>();
	PullToRefreshListView mPullToRefreshView;
	private ILoadingLayout loadingLayout;
	ListView mMsgListView;
	private static final int STATE_DISPLAY = -1;// 初始化加载
	private static final int STATE_REFRESH = 0;// 下拉刷新
	private static final int STATE_MORE = 1;// 加载更多
	
	private int limit = 10;		// 每页的数据是10条
	private int curPage = 0;		// 当前页的编号，从0开始
	private PostsAdapter adapter;
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
		setTitleText("圈子");
		setTopRightButton("", R.drawable.send_post, this);
		initListView();
		queryData(0, STATE_DISPLAY);
	}
	
	
	private void initListView() {
		mPullToRefreshView = (PullToRefreshListView) currentView.findViewById(R.id.list);
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
		adapter = new PostsAdapter(activity,positionList);
		mMsgListView.setAdapter(adapter);
//		mPullToRefreshView.setAdapter(adapter);
		mMsgListView.setOnItemClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseRight:
			if (Util.getUserInfo(activity) == null) {
				Util.showLToast(activity, "请登录后操作");
				return;
			}
			Intent intent = new Intent(activity,PostCardAvtivity.class);
			startActivityForResult(intent, POSTREQUESTID);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 获取交流帖子
	 */
	public void queryData(final int page, final int actionType){
		if(page == 0 && STATE_DISPLAY == actionType)
			showDialog();
		BmobQuery query = new BmobQuery("t_posts");
		query.setLimit(limit);			// 设置每页多少条数据
		query.setSkip(page*limit);		// 从第几条数据开始，
		query.order("-createdAt");
		query.include("fromUserID");
		query.addWhereNotEqualTo("status", "0");
		query.findObjects(activity, new FindCallback() {
			@Override
			public void onSuccess(JSONArray array) {
				hideDialog();
				Gson gson = new Gson();
				if (array != null && array.length() > 0) {
					if(actionType == STATE_REFRESH  || actionType == STATE_DISPLAY){
						// 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
						curPage = 0;
						positionList.clear();
					}
					
					// 将本次查询的数据添加到bankCards中
					for(int i = 0; i< array.length();i++){
						JSONObject object = array.optJSONObject(i);
						PostBean bean = gson.fromJson(object.toString(), PostBean.class);
						positionList.add(bean);
					}
					
					// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
					curPage++;
					adapter.notifyDataSetChanged();
				}else if(actionType == STATE_MORE){
					Util.showLToast(activity, "没有更多数据了");
				}else if(actionType == STATE_REFRESH){
					Util.showLToast(activity, "没有数据");
				}
				mPullToRefreshView.onRefreshComplete();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
				mPullToRefreshView.onRefreshComplete();
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == POSTREQUESTID) {
			if (resultCode == Activity.RESULT_OK) {
				// 发布成功
				// 刷新(从第一页开始装载数据)
				queryData(0, STATE_DISPLAY);
			}

		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PostBean bean = positionList.get(position - 1);
		Intent intent = new Intent(activity,PostCardDetailActivity.class);
		intent.putExtra("postBean", bean);
		startActivity(intent);
	}
}
