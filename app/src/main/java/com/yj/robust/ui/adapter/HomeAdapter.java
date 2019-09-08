package com.yj.robust.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.yj.robust.R;
import com.yj.robust.base.URLBuilder;
import com.yj.robust.model.HomeEntity;
import com.yj.robust.model.HomeListEntity;
import com.yj.robust.ui.MainActivity;
import com.yj.robust.ui.activity.HomeGoodsListActivity;
import com.yj.robust.ui.activity.NormalWebViewActivity;
import com.yj.robust.ui.activity.couponReceive.CouponReceiveActivity;
import com.yj.robust.ui.activity.homeSeckill.HomeSeckillActivity;
import com.yj.robust.ui.activity.storeDetail.StoreDetailActivity;
import com.yj.robust.util.DensityUtil;
import com.yj.robust.util.IntentUtils;
import com.yj.robust.util.UserUtils;
import com.yj.robust.util.WindowDisplayManager;
import com.yj.robust.widget.CustomHintView;
import com.yj.robust.widget.MarqueeTextView;
import com.yj.robust.widget.WrapContentGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suo on 2017/4/12.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LinearLayoutManager layoutManager = null;
    //	private GridLayoutManager gridLayoutManager = null;
    private UserUtils mUtils = null;
    private Activity mContext;
    private HomeEntity.HomeData data;
    private List<HomeListEntity.HomeListData.HomeListItem> mList = new ArrayList<>();
    private ShopListClickListener mItemClickListener;
    private String img_url1 = null, img_url2 = null, img_url3 = null;

    public HomeAdapter(Activity mContext, HomeEntity.HomeData data, List<HomeListEntity.HomeListData.HomeListItem> mList, UserUtils mUtils, LinearLayoutManager layoutManager) {
        this.mContext = mContext;
        this.data = data;
        this.mList = mList;
        this.layoutManager = layoutManager;
        this.mUtils = mUtils;
    }

    public void setOnItemClickListener(ShopListClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder;
        BannerViewHolder bannerViewHolder;
        GridViewHolder gridViewHolder;
        HotRecommViewHolder recommViewHolder;
        SecKillItemViewHolder secKillItemViewHolder;
        headTitleViewHolder headTitleViewHolder;
        ImageViewHolder mImageViewHolder;
        ImgViewHolder mImgViewHolder;
        if (viewType == 0) {
            //banner
            bannerViewHolder = new BannerViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_banner, parent, false));
            return bannerViewHolder;
        } else if (viewType == 1) {
            //图片
            mImageViewHolder = new ImageViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_item_img, parent, false));
            return mImageViewHolder;
        } else if (viewType == 2) {
            //图标分类
            gridViewHolder = new GridViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_gridview, parent, false));
            return gridViewHolder;
        } else if (viewType == 3) {
            //秒杀模块
            secKillItemViewHolder = new SecKillItemViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_item1, parent, false));
            return secKillItemViewHolder;

        } else if (viewType == 4) {
            //标题栏
            headTitleViewHolder = new headTitleViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_head_title, parent, false));
            return headTitleViewHolder;
        } else if (viewType == 5) {
            //推荐模块
            recommViewHolder = new HotRecommViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_seckill, parent, false));
            return recommViewHolder;
        } else if (viewType == 6) {

            mImgViewHolder = new ImgViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_recyclerview, parent, false));
            return mImgViewHolder;
        } else {
            //底部的itemz
            itemViewHolder = new ItemViewHolder(LayoutInflater
                    .from(mContext).inflate(R.layout.item_home_item, parent, false), mItemClickListener);
            return itemViewHolder;
        }
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position > 10) {
                        return ((XRecyclerView) recyclerView).getSpanSize(position, gridManager);
                    } else {
                        return 2;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        }

        if (data.getTimeLimitList() != null && data.getTimeLimitList().size() > 0) {
            if (position == 2) {
                return 2;
            } else if (position == 3) {
                return 3;
            } else if (position == 4) {
                return 4;
            } else if (position == 5) {
                return 3;
            } else if (position == 6) {
                return 6;
            } else if (position == 7) {
                return 4;
            } else if (position == 8) {
                return 5;
            } else if (position == 9) {
                return 4;
            }
        } else {

            if (position == 2) {
                return 2;
            } else if (position == 3) {
                return 3;
            } else if (position == 4) {
                return 4;
            } else if (position == 5) {
                return 3;
            } else if (position == 6) {
                return 6;
            } else if (position == 7) {
                return 4;
            } else if (position == 8) {
                return 5;
            } else if (position == 9) {
                return 4;
            }
        }
        return 7;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).mRollPagerView.setLayoutParams(WindowDisplayManager.getBannerHeight(mContext, ((BannerViewHolder) holder).mRollPagerView));
            ((BannerViewHolder) holder).mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (!TextUtils.isEmpty(data.getBannerList().get(position).getProduct_id())) {
                        IntentUtils.IntentToGoodsDetial(mContext, data.getBannerList().get(position).getProduct_id());
                    } else if (!TextUtils.isEmpty(data.getBannerList().get(position).getBanner_url()) && !data.getBannerList().get(position).getBanner_url().equals("null")) {
                        Intent intent = new Intent(mContext, NormalWebViewActivity.class);
                        intent.putExtra("url", data.getBannerList().get(position).getBanner_url());
                        intent.putExtra("title", "no");
                        mContext.startActivity(intent);
                    } else {
                        return;
                    }
                }
            });
            ((BannerViewHolder) holder).mRollPagerView.setHintView(new CustomHintView(mContext,
                    R.drawable.shape_round360_e83, R.drawable.shape_round360_e83_stroke0_5_trans, DensityUtil.dip2px(mContext, mContext.getResources().getDimension(R.dimen.dis6))));
            ((BannerViewHolder) holder).mRollPagerView.setAnimationDurtion(1000);
            ((BannerViewHolder) holder).mRollPagerView.setHintPadding(0, 0, (int) mContext.getResources().getDimension(R.dimen.dis15), (int) mContext.getResources().getDimension(R.dimen.dis5));
            NormalBannerAdapter mBannerAdapter = new NormalBannerAdapter(((BannerViewHolder) holder).mRollPagerView, data.getBannerList(), mContext);
            ((BannerViewHolder) holder).mRollPagerView.setAdapter(mBannerAdapter);

        } else if (holder instanceof ImageViewHolder) {
            Glide.with(mContext)
                    .load(data.getHomeImg7() == null ? R.mipmap.store_img : URLBuilder.getUrl(data.getHomeImg7()))
//					.load(R.mipmap.store_img)
//					.crossFade(500)//动画默认的持续时间是 300毫秒
                    .asBitmap()
//					.centerCrop()
                    .error(R.mipmap.seat_img)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((ImageViewHolder) holder).ivStore);
            Glide.with(mContext)
                    .load(data.getHomeImg8() == null ? R.mipmap.store_img : URLBuilder.getUrl(data.getHomeImg8()))
//					.load(R.mipmap.new_good_img)
//					.crossFade(500)//动画默认的持续时间是 300毫秒
                    .asBitmap()
                    .centerCrop()
                    .error(R.mipmap.seat_img)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((ImageViewHolder) holder).ivNewGoods);

            if (data.getShopName() != null) {
                ((ImageViewHolder) holder).tvStoreInfo.setText(data.getShopName());
            } else {
                ((ImageViewHolder) holder).tvStoreInfo.setText("暂无店铺");
//				((ImageViewHolder) holder).tvRange.setVisibility(View.GONE);
            }
            if (data.getJuli() != null) {
                ((ImageViewHolder) holder).tvRange.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).tvRange.setText("距离：" + data.getJuli());
            } else {
                ((ImageViewHolder) holder).tvRange.setVisibility(View.GONE);
            }
            ((ImageViewHolder) holder).ivStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("page", "2");
                    mContext.startActivity(intent);
                }
            });
            ((ImageViewHolder) holder).rlNewGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getShopId() != null && !data.getShopId().equals("")) {
//						Intent intent = new Intent(mContext, HomeGoodsListActivity.class);
//						intent.putExtra("shopId", data.getShopId());
//						mContext.startActivity(intent);
                        Intent intent = new Intent(mContext, StoreDetailActivity.class);
                        intent.putExtra("shopId", data.getShopId());
                        mContext.startActivity(intent);
                    }
                }
            });


        } else if (holder instanceof ImgViewHolder) {

            ((ImgViewHolder) holder).mImgRecyclerView.setLayoutManager(layoutManager);
            HomeMImgAdapter imgAdapter = new HomeMImgAdapter(mContext, data.getBanners());
            ((ImgViewHolder) holder).mImgRecyclerView.setAdapter(imgAdapter);
            imgAdapter.setOnItemClickListener(new HomeMImgAdapter.ImgViewClickListener() {
                @Override
                public void onItemClick(int positons) {
                    if (!TextUtils.isEmpty(data.getBanners().get(positons).getProductId())) {
                        IntentUtils.IntentToGoodsDetial(mContext, data.getBanners().get(positons).getProductId());
                    } else if (!TextUtils.isEmpty(data.getBanners().get(positons).getBannerUrl())) {
                        Intent intent = new Intent(mContext, NormalWebViewActivity.class);
                        intent.putExtra("url", data.getBanners().get(positons).getBannerUrl());
                        intent.putExtra("title", "no");
                        mContext.startActivity(intent);
                    } else {
                        return;
                    }
                }
            });


        } else if (holder instanceof GridViewHolder) {
            List<View> mPagerList = new ArrayList<>();
            //每页数量
            int pageSize = 10;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            //页数
            int pageCount = (int) Math.ceil(data.getProductClassifyList().size() * 1.0 / pageSize);
            for (int i = 0; i < pageCount; i++) {
                // 每个页面都是inflate出一个新实例
                WrapContentGridView gridView = (WrapContentGridView) inflater.inflate(R.layout.gridview, ((GridViewHolder) holder).mViewpager, false);
                gridView.setAdapter(new HomeGridviewAdapter(mContext, data.getProductClassifyList(), i, pageSize));
                mPagerList.add(gridView);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (TextUtils.isEmpty(data.getProductClassifyList().get((int) id).getClassify_id()) && TextUtils.isEmpty(data.getProductClassifyList().get((int) id).getClassify_img())) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("page", "1");
                            mContext.startActivity(intent);
                        } else {
                            IntentUtils.IntentToGoodsList(mContext, data.getProductClassifyList().get((int) id).getClassify_id());
                        }
                    }
                });
                gridView.setFocusable(false);
            }
            //设置适配器
//			LogUtils.i("pagerlist的长度" + mPagerList.size());

            ((GridViewHolder) holder).mViewpager.setAdapter(new ViewPagerAdapter(mPagerList));
            ((GridViewHolder) holder).mViewpager.setFocusable(false);

        } else if (holder instanceof SecKillItemViewHolder) {

            if (position == 3) {
                img_url1 = data.getHomeImg1();
                img_url2 = data.getHomeImg2();
                img_url3 = data.getHomeImg3();

            } else if (position == 5) {
                img_url1 = data.getClassifyHomeImg1();
                img_url2 = data.getClassifyHomeImg2();
                img_url3 = data.getClassifyHomeImg3();
                ((SecKillItemViewHolder) holder).ivLine.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(URLBuilder.getUrl(img_url1))
//					.crossFade(500)//动画默认的持续时间是 300毫秒
//					.asBitmap()
                    .error(R.mipmap.default_goods)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((SecKillItemViewHolder) holder).ivSeckill);

            Glide.with(mContext)
                    .load(URLBuilder.getUrl(img_url2))
//					.crossFade(500)//动画默认的持续时间是 300毫秒
//					.asBitmap()
                    .error(R.mipmap.default_goods)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((SecKillItemViewHolder) holder).ivSignIn);

            Glide.with(mContext)
                    .load(URLBuilder.getUrl(img_url3))
//					.crossFade(500)//动画默认的持续时间是 300毫秒
//					.asBitmap()
                    .error(R.mipmap.default_goods)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((SecKillItemViewHolder) holder).ivNeckTicket);


            ((SecKillItemViewHolder) holder).ivSeckill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//					if (mUtils.isLogin()) {
                    if (position == 3) {
                        Intent intent = new Intent(mContext, HomeSeckillActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        IntentUtils.IntentToGoodsList(mContext, data.getClassifyId1());
                    }
//					} else {
//						IntentUtils.IntentToLogin(mContext);
//					}
                }
            });
            ((SecKillItemViewHolder) holder).ivSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUtils.isLogin()) {
                        IntentUtils.IntentToGoodsList(mContext, data.getClassifyId2());
                    } else {
                        IntentUtils.IntentToLogin(mContext);
                    }
                }
            });
            ((SecKillItemViewHolder) holder).ivNeckTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUtils.isLogin()) {
                        if (position == 3) {
                            Intent intent = new Intent(mContext, CouponReceiveActivity.class);
                            mContext.startActivity(intent);
                        } else {
                            IntentUtils.IntentToGoodsList(mContext, data.getClassifyId3());
                        }
                    } else {
                        IntentUtils.IntentToLogin(mContext);
                    }
                }
            });
        } else if (holder instanceof headTitleViewHolder) {
            if (position == 4) {
                ((headTitleViewHolder) holder).tvMoreProduct.setVisibility(View.GONE);
                ((headTitleViewHolder) holder).tvMore.setVisibility(View.GONE);
                ((headTitleViewHolder) holder).tvTitle.setBackgroundResource(R.drawable.shape_corner_one);
            }
            if (position == 7) {
                ((headTitleViewHolder) holder).tvTitle.setText("热门推荐");
                ((headTitleViewHolder) holder).tvMoreProduct.setText("精选超值商品");
                ((headTitleViewHolder) holder).tvMoreProduct.setVisibility(View.VISIBLE);
                ((headTitleViewHolder) holder).tvMore.setVisibility(View.VISIBLE);
                ((headTitleViewHolder) holder).tvTitle.setBackgroundResource(R.drawable.shape_corner_two);
                ((headTitleViewHolder) holder).tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//						if (mUtils.isLogin()) {
                        Intent intent = new Intent(mContext, HomeGoodsListActivity.class);
                        intent.putExtra("type", "hot");
                        mContext.startActivity(intent);
//						} else {
//							IntentUtils.IntentToLogin(mContext);
//						}
                    }
                });
            }
            if (position == 9) {
                ((headTitleViewHolder) holder).tvMoreProduct.setVisibility(View.VISIBLE);
                ((headTitleViewHolder) holder).tvMore.setVisibility(View.VISIBLE);
                ((headTitleViewHolder) holder).tvTitle.setText("全球精选");
                ((headTitleViewHolder) holder).tvMoreProduct.setText("精选人气商品");
                ((headTitleViewHolder) holder).tvTitle.setBackgroundResource(R.drawable.shape_corner_three);
                ((headTitleViewHolder) holder).tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//						if (mUtils.isLogin()) {
                        Intent intent = new Intent(mContext, HomeGoodsListActivity.class);
                        intent.putExtra("type", "popular");
                        mContext.startActivity(intent);
//						} else {
//							IntentUtils.IntentToLogin(mContext);
//						}
                    }
                });
            }
        } else if (holder instanceof HotRecommViewHolder) {//@TODO 秒杀环节
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ((HotRecommViewHolder) holder).recommRecyclerView.setLayoutManager(layoutManager);
            HotRecommAdapter hotRecommAdapter = new HotRecommAdapter(mContext, data.getProductHotList());
            ((HotRecommViewHolder) holder).recommRecyclerView.setAdapter(hotRecommAdapter);
            hotRecommAdapter.setOnItemClickListener(new HotRecommAdapter.ProfitDetialClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
//					LogUtils.i(" 我点击了====" + postion);
                    IntentUtils.IntentToGoodsDetial(mContext, data.getProductHotList().get(postion).getProduct_id());
                }
            });
            ((HotRecommViewHolder) holder).rlMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "--相应点击--", Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(mContext, HomeSeckillActivity.class);
//					mContext.startActivity(intent);
                }
            });
            ((HotRecommViewHolder) holder).recommRecyclerView.setFocusable(false);
        } else if (holder instanceof ItemViewHolder) {
            int pos;
            if (data.getTimeLimitList() != null && data.getTimeLimitList().size() > 0) {
                pos = position - 10;
            } else {
                pos = position - 10;
            }
            ((ItemViewHolder) holder).tvTitle.setText(mList.get(pos).getProduct_name());
            ((ItemViewHolder) holder).tvContent.setText(mList.get(pos).getProduct_abstract());
            ((ItemViewHolder) holder).tvPrice.setText("￥" + mList.get(pos).getProduct_current());
            Glide.with(mContext)
                    .load(URLBuilder.getUrl(mList.get(pos).getProduct_listImg()))
//					.crossFade(500)//动画默认的持续时间是 300毫秒
//					.asBitmap()
                    .centerCrop()
                    .error(R.mipmap.default_goods)
//					.skipMemoryCache(true)
//					.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((ItemViewHolder) holder).iv);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            if (data.getTimeLimitList() != null && data.getTimeLimitList().size() > 0) {
                return 6 + mList.size();
            } else {
                return 10 + mList.size();
            }
        }
        return 0;
    }

    public void setData(HomeEntity.HomeData data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rollPagerView)
        RollPagerView mRollPagerView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_store)
        ImageView ivStore;
        @BindView(R.id.iv_new_goods)
        ImageView ivNewGoods;
        @BindView(R.id.rl_new_goods)
        RelativeLayout rlNewGoods;

        @BindView(R.id.img_tv_store_info)
        MarqueeTextView tvStoreInfo;
        @BindView(R.id.img_tv_range)
        TextView tvRange;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewpager)
        ViewPager mViewpager;


        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

//	class mViewViewHolder extends RecyclerView.ViewHolder {
//
//		public mViewViewHolder(View itemView) {
//			super(itemView);
//		}
//	}

    class HotRecommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recommRecyclerView;
        @BindView(R.id.home_seckill_rl_more)
        RelativeLayout rlMore;

        public HotRecommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class SecKillItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_seckill)
        ImageView ivSeckill;
        @BindView(R.id.iv_sign_in)
        ImageView ivSignIn;
        @BindView(R.id.iv_neck_ticket)
        ImageView ivNeckTicket;
        @BindView(R.id.item_home_kill_line)
        View ivLine;


        public SecKillItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class headTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_head_title)
        TextView tvTitle;
        @BindView(R.id.home_head_more_product)
        TextView tvMoreProduct;
        @BindView(R.id.home_head_more)
        TextView tvMore;


        public headTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_head_title)
        TextView tvTitle;
        @BindView(R.id.home_head_more)
        TextView tvMore;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_hot_iv1)
        ImageView iv1;
        @BindView(R.id.home_hot_iv2)
        ImageView iv2;
        @BindView(R.id.home_hot_tv_title1)
        TextView tvTitle1;
        @BindView(R.id.home_hot_tv_title2)
        TextView tvTitle2;
        @BindView(R.id.home_hot_tv_content1)
        TextView tvContent1;
        @BindView(R.id.home_hot_tv_content2)
        TextView tvContent2;
        @BindView(R.id.home_hot_tv_price1)
        TextView tvPrice1;
        @BindView(R.id.home_hot_tv_price2)
        TextView tvPrice2;
        @BindView(R.id.home_hot_ll1)
        LinearLayout ll1;
        @BindView(R.id.home_hot_ll2)
        LinearLayout ll2;


        public HotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRecyclerView)
        RecyclerView mImgRecyclerView;


        public ImgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.home_item_iv)
        ImageView iv;
        @BindView(R.id.home_item_tv_title)
        TextView tvTitle;
        @BindView(R.id.home_item_tv_content)
        TextView tvContent;
        @BindView(R.id.home_item_tv_price)
        TextView tvPrice;
        private ShopListClickListener mListener;

        public ItemViewHolder(View itemView, ShopListClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface ShopListClickListener {
        void onItemClick(View view, int postion);
    }
}
