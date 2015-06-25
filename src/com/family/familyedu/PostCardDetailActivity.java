package com.family.familyedu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.family.familyedu.adapter.PostCommentAdapter;
import com.family.familyedu.adapter.ShareAdapter;
import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.PostBean;
import com.family.familyedu.bean.PostCommentBean;
import com.family.familyedu.bean.PostCommentListData;
import com.family.familyedu.bean.ShareBean;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.Util;
import com.family.familyedu.widget.SelectPopupWindow;
import com.google.gson.Gson;

/**
 * 交流帖子详情
 * 
 * @author user
 * 
 */
public class PostCardDetailActivity extends BaseActivity implements
		OnClickListener, TaskListener {
	private BaseTask task;
	private PostCommentListData postCommentListData;
	private BaseData baseDate;
	private List<PostCommentBean> commentList;
	private ListView lv_noteDetailView;
	private View headerView;
	private static final int STATE_NEXTPAGE = 1;// 下一页
	private static final int STATE_PRIVIOUSPAGE = -1;// 上一页
	private int currentState;
	/**
	 * 头像
	 */
	private ImageView imgNDHIcon;
	/**
	 * 
	 * 回复数量
	 */
	private TextView tvNDHReplys;
	/**
	 * 标题
	 */
	private TextView tvNDHTitle;

	/**
	 * 浏览数量
	 */
	private TextView tvNDHHits;
	/**
	 * 赞
	 */
	private TextView tvHandUp;
	/**
	 * 倒赞
	 */
	private TextView tvHandDown;
	/**
	 * 内容布局，不是第一页时隐藏
	 */
	private LinearLayout lyNDHAuthorContent;
	/**
	 * 楼主头像
	 */
	private ImageView imgNDHAuthorHead;
	/**
	 * 楼主姓名
	 */
	private TextView tvNDHAuthorName;
	/**
	 * 发布日期
	 */
	private TextView tvNDHAuthorDate;
	/**
	 * 内容
	 */
	private TextView tvContent;
	/**
	 * 举报
	 */
	private TextView tvNDHAuthorReport;
	/**
	 * 回复
	 */
	private Button btnNDHAuthorReply;
	/**
	 * 回复
	 */
	private Button btnNDReply;
	/**
	 * 上一页
	 */
	private ImageButton btnNDPrevious;
	/**
	 * 选择页码
	 */
	private Button btnNDJumpPage;
	/**
	 * 下一页
	 */
	private ImageButton btnNDNext;

	/**
	 * 对象
	 */
	private PostBean postBean;

	private PostCommentAdapter adapter;

	/**
	 * 图片点击动画
	 */
	private Animation scaleInAnimation;

	/**
	 * 回复布局
	 */
	private LinearLayout rlNDPage;

	private int limit = 2; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始
	private boolean isLoading;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this, true);
		postCommentListData = new PostCommentListData();
		baseDate = new BaseData();
		commentList = new ArrayList<PostCommentBean>();
		if (getIntent() != null) {
			postBean = (PostBean) getIntent().getSerializableExtra("postBean");
		}
		initView();
		setHeaderData();
		// 请求头部数据
		requestHeaderData();
		// 查询评论列表
		queryCommentsData(curPage);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		currentView = layoutInflater.inflate(R.layout.postdetail, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText("详情");
		setTopLeftButton("", R.drawable.return_back, this);
		setTopRightButton("", R.drawable.detail_share, this);

		this.lv_noteDetailView = ((ListView) this.currentView
				.findViewById(R.id.lvPostDetail));
		headerView = layoutInflater.inflate(R.layout.postdetail_head, null);
		lv_noteDetailView.addHeaderView(headerView);

		rlNDPage = (LinearLayout) currentView.findViewById(R.id.rlNDPage);

		adapter = new PostCommentAdapter(this, commentList);
		lv_noteDetailView.setAdapter(adapter);

		imgNDHIcon = (ImageView) headerView.findViewById(R.id.imgNDHIcon);
		tvNDHTitle = (TextView) headerView.findViewById(R.id.tvNDHTitle);
		tvNDHReplys = (TextView) headerView.findViewById(R.id.tvNDHReplys);
		tvNDHHits = (TextView) headerView.findViewById(R.id.tvNDHHits);
		tvHandUp = (TextView) headerView.findViewById(R.id.tvHandUp);
		tvHandUp.setOnClickListener(this);
		tvHandDown = (TextView) headerView.findViewById(R.id.tvHandDown);
		tvHandDown.setOnClickListener(this);

		lyNDHAuthorContent = (LinearLayout) headerView
				.findViewById(R.id.lyNDHAuthorContent);
		imgNDHAuthorHead = (ImageView) headerView
				.findViewById(R.id.imgNDHAuthorHead);
		tvNDHAuthorName = (TextView) headerView
				.findViewById(R.id.tvNDHAuthorName);
		tvNDHAuthorDate = (TextView) headerView
				.findViewById(R.id.tvNDHAuthorDate);
		tvContent = (TextView) headerView.findViewById(R.id.tvContent);
		// tvNDHAuthorReport = (TextView) headerView
		// .findViewById(R.id.tvNDHAuthorReport);
		// btnNDHAuthorReply = (Button) headerView
		// .findViewById(R.id.btnNDHAuthorReply);

		btnNDReply = (Button) findViewById(R.id.btnNDReply);
		btnNDReply.setOnClickListener(this);
		btnNDPrevious = (ImageButton) findViewById(R.id.btnNDPrevious);
		btnNDPrevious.setOnClickListener(this);
		btnNDJumpPage = (Button) findViewById(R.id.btnNDJumpPage);
		btnNDJumpPage.setOnClickListener(this);
		btnNDNext = (ImageButton) findViewById(R.id.btnNDNext);
		btnNDNext.setOnClickListener(this);

		scaleInAnimation = Util.getCommonScaleIn();
	}

	/**
	 * 设置头部内容
	 */
	private void setHeaderData() {
		if (postBean != null) {
			tvNDHTitle.setText(postBean.getTitle());
			tvNDHAuthorName.setText(postBean.getFromUserID().getNickname());
			tvNDHAuthorDate.setText(postBean.getCreatedAt());
			tvContent.setText(postBean.getContent());
			tvHandUp.setText(postBean.getUpCount());
			tvHandDown.setText(postBean.getDownCount());
		}
		tvNDHReplys.setText(postCommentListData.getCommentsSize());
		if (!"".equals(postCommentListData.getCommentsSize())
				&& postCommentListData.getCommentsSize() != null) {
			btnNDJumpPage.setText((curPage + 1)
					+ "/"
					+ (int) Math.ceil(Double.valueOf(postCommentListData
							.getCommentsSize()) / limit));
		}
		if ("0".equals(postCommentListData.getCommentsSize())) {
			btnNDJumpPage.setText((curPage + 1) + "/1");
		}
	}

	/**
	 * 请求头部数据
	 */
	private void requestHeaderData() {
		try {
			JSONObject object = new JSONObject();
			object.put("postsID", postBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FEGETPOSTSDETAILFORUSER, object,
					postCommentListData, "parsePostCommentListData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取回复列表
	 */
	public void queryCommentsData(final int page) {
		if (isLoading) {
			return;
		}
		isLoading = true;
		showDialog();
		BmobQuery query = new BmobQuery("t_comment");
		query.setLimit(limit); // 设置每页多少条数据
		query.setSkip(page * limit); // 从第几条数据开始，
		// query.order("-createdAt");
		query.include("fromUserID");
		// query.include("postsID");
		query.addWhereEqualTo("postsID", postBean.getObjectId());
		query.findObjects(this, new FindCallback() {
			@Override
			public void onSuccess(JSONArray array) {
				hideDialog();
				isLoading = false;
				rlNDPage.setVisibility(View.VISIBLE);
				Gson gson = new Gson();
				if (array != null && array.length() > 0) {
					// if(actionType == STATE_REFRESH || actionType ==
					// STATE_DISPLAY){
					// // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
					// curPage = 0;
					commentList.clear();
					// }

					// 将本次查询的数据添加到bankCards中
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.optJSONObject(i);
						PostCommentBean bean = gson.fromJson(object.toString(),
								PostCommentBean.class);
						commentList.add(bean);
					}
					adapter.notifyDataSetChanged();

					if (currentState == STATE_NEXTPAGE) {
						curPage++;
					} else if (currentState == STATE_PRIVIOUSPAGE) {
						curPage--;
					}
					if (curPage > 0) {
						lyNDHAuthorContent.setVisibility(View.GONE);
						headerView.findViewById(R.id.lyNDHAuthorContent)
								.setVisibility(View.GONE);
					} else {
						lyNDHAuthorContent.setVisibility(View.VISIBLE);
						headerView.findViewById(R.id.lyNDHAuthorContent)
								.setVisibility(View.VISIBLE);
					}
					if (!"".equals(postCommentListData.getCommentsSize())
							&& postCommentListData.getCommentsSize() != null) {
						btnNDJumpPage.setText((curPage + 1)
								+ "/"
								+ (int) Math.ceil(Double
										.valueOf(postCommentListData
												.getCommentsSize())
										/ limit));
					}
					if ("0".equals(postCommentListData.getCommentsSize())) {
						btnNDJumpPage.setText((curPage + 1) + "/1");
					}
				} else {
					// 没有数据
					if (currentState == STATE_NEXTPAGE) {
						Util.showSToast(PostCardDetailActivity.this, "已经是最后一页");
					}
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
				isLoading = false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.btnBaseRight:
			// 分享
			showShare();
			// Util.showShare(
			// 1,
			// false,
			// null,
			// "欢迎来观看",
			// null,
			// "http://img4.imgtn.bdimg.com/it/u=4063121288,317063508&fm=90&gp=0.jpg",
			// "http://www.xiaonei.com", this, mainView);
			// Util.showShare(this, "");
			break;
		case R.id.tvHandUp:
			// 赞
			handUp();
			break;
		case R.id.tvHandDown:
			// 倒赞
			handDown();
			break;
		case R.id.btnNDReply:
			// 回复
			if (Util.getUserInfo(this) == null) {
				Util.showLToast(this, "请登录后操作");
				return;
			}
			Intent intent = new Intent(this, PostCardAvtivity.class);
			intent.putExtra("type", "reply");
			intent.putExtra("postId", postBean.getObjectId());
			startActivity(intent);
			break;
		case R.id.btnNDPrevious:
			// 前一页
			currentState = STATE_PRIVIOUSPAGE;
			if (curPage == 0) {
				Util.showSToast(this, "已经是第一页");
				return;
			}
			queryCommentsData(curPage - 1);
			break;
		case R.id.btnNDJumpPage:
			// 选择页码
			break;
		case R.id.btnNDNext:
			// 下一页
			currentState = STATE_NEXTPAGE;
			queryCommentsData(curPage + 1);
			break;
		default:
			break;
		}
	}

	/**
	 * 赞
	 */
	private void handUp() {
		if (!"1".equals(postCommentListData.getCanDoUp()))
			return;
		try {
			tvHandUp.startAnimation(scaleInAnimation);
			scaleInAnimation
					.setAnimationListener(new Animation.AnimationListener() {

						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							tvHandUp.startAnimation(Util.getCommonScaleOut());
						}
					});

			JSONObject object = new JSONObject();
			object.put("postsID", postBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FEDOUPPOSTS, object, baseDate,
					"parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 倒赞
	 */
	private void handDown() {
		if (!"1".equals(postCommentListData.getCanDoDown()))
			return;
		try {

			tvHandDown.startAnimation(scaleInAnimation);
			scaleInAnimation
					.setAnimationListener(new Animation.AnimationListener() {

						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							tvHandDown.startAnimation(Util.getCommonScaleOut());
						}
					});

			JSONObject object = new JSONObject();
			object.put("postsID", postBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FEDODOWNPOSTS, object, baseDate,
					"parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void requestSuccess(Object object) {
		if (PostCommentListData.class == object.getClass()) {
			postCommentListData = (PostCommentListData) object;
			postBean = postCommentListData.getPostBean();
			setHeaderData();
		} else if (BaseData.class == object.getClass()) {
			// 赞或者倒赞
			requestHeaderData();
		}
	}

	@Override
	public void requestFail(Object object) {

	}
}
