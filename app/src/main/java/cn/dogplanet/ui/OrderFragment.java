package cn.dogplanet.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseFragment;
import cn.dogplanet.ui.order.OrderFindActivity;
import cn.dogplanet.ui.order.OrderListFragment;

public class OrderFragment extends BaseFragment {


    public final static String ORDER_TYPE_ALL = "all";
    public final static String ORDER_TYPE_WAIT = "wait";
    public final static String ORDER_TYPE_SUCCESS = "success";
    public final static String ORDER_TYPE_BACK_ALL_MONEY = "backAllMoney";
    public final static String ORDER_TYPE_BACK_MONEY = "backMoney";

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Unbinder bind;

    private OrderListFragment allFragment;
    private OrderListFragment waitFragment;
    private OrderListFragment successFragment;
    private OrderListFragment backAllMoneyFragment;
    private OrderListFragment backMoneyFragment;
    private ArrayList<Fragment> fragments;
    private List<String> mDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        bind = ButterKnife.bind(this, view);
        initFragment();
        return view;
    }

    private void initFragment() {
        // TODO Auto-generated method stub
        mDataList = new ArrayList<>();
        mDataList.add("全部");
        mDataList.add("未支付");
        mDataList.add("已支付");
        mDataList.add("退款中");
        mDataList.add("已退款");
        allFragment = OrderListFragment
                .newFragment(OrderFragment.ORDER_TYPE_ALL);
        waitFragment = OrderListFragment
                .newFragment(OrderFragment.ORDER_TYPE_WAIT);

        successFragment = OrderListFragment
                .newFragment(OrderFragment.ORDER_TYPE_SUCCESS);
        backMoneyFragment = OrderListFragment
                .newFragment(OrderFragment.ORDER_TYPE_BACK_MONEY);
        backAllMoneyFragment = OrderListFragment
                .newFragment(OrderFragment.ORDER_TYPE_BACK_ALL_MONEY);
        fragments = new ArrayList<>();
        fragments.add(allFragment);
        fragments.add(waitFragment);
        fragments.add(successFragment);
        fragments.add(backMoneyFragment);
        fragments.add(backAllMoneyFragment);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        final CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.layout_viewpager_title);
                // 初始化
                final TextView titleText = commonPagerTitleView.findViewById(R.id.tv_title);
                titleText.setText(mDataList.get(index));
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextSize(18);
                    }

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextSize(13);
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {

                    }

                });

                commonPagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;    // 没有指示器，因为title的指示作用已经很明显了
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    @OnClick({R.id.et_search, R.id.img_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_search:
            case R.id.img_input:
                startActivity(OrderFindActivity.newIntent());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return titles[position];
            return "";
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
