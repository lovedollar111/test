package cn.dogplanet.app.util;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.widget.library.ILoadingLayout;
import cn.dogplanet.app.widget.library.PullToRefreshGridView;
import cn.dogplanet.app.widget.library.PullToRefreshListView;
import cn.dogplanet.app.widget.library.PullToRefreshScrollView;

public class PullToRefreshHelper {

	/**
	 * 生成PullToRefreshGridView的下拉刷新Indicator
	 * 
	 * @param listView
	 */
	public static void initIndicatorStart(PullToRefreshGridView gridView) {
		ILoadingLayout startLabels = gridView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_refresh_pull_label));// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));// 刷新时
		startLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_ready));// 下来达到一定距离时，显示的提示
	}

	/**
	 * 生成PullToRefreshListView的下拉刷新Indicator
	 * 
	 * @param listView
	 */
	public static void initIndicatorStart(PullToRefreshListView listView) {
		ILoadingLayout startLabels = listView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_refresh_pull_label));// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));// 刷新时
		startLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_ready));// 下来达到一定距离时，显示的提示
	}
	
	public static void initIndicatorStart(PullToRefreshScrollView scrollView) {
		ILoadingLayout startLabels = scrollView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_refresh_pull_label));// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));// 刷新时
		startLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_ready));// 下来达到一定距离时，显示的提示
	}

	/**
	 * 生成PullToRefreshListView的Indicator
	 * 
	 * @param listView
	 */
	public static void initIndicator(PullToRefreshListView listView) {

		ILoadingLayout endLabels = listView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_footer_hint_normal));
		endLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));
		endLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_footer_hint_ready));
	}

	/**
	 * 生成PullToRefreshGridView的Indicator
	 * 
	 * @param listView
	 */
	public static void initIndicator(PullToRefreshGridView gridView) {
		ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_footer_hint_normal));
		endLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));
		endLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_ready));
	}
	
	public static void initIndicator(PullToRefreshScrollView scrollView) {
		ILoadingLayout endLabels = scrollView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_footer_hint_normal));
		endLabels.setRefreshingLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_loading));
		endLabels.setReleaseLabel(GlobalContext.getInstance().getString(
				R.string.refreshlist_header_hint_ready));
	}

}
