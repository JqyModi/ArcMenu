package com.modi.arcmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 自定义弧形菜单实现
 * Created by modi on 2017/5/6.
 */

public class ArcMenu extends ViewGroup {

	private String position;
	private float radius;
	private RelativeLayout mRLView;

	private final String OPEN = "open";
	private final String CLOSE = "close";
	//默认关闭状态
	private boolean status = false;
	private ItemClickListener mItemClickListener;

	public void setmItemClickListener(ItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}

	public interface ItemClickListener{
		void onItemClick(View v,int position);
		void onItemLongClick(View v,int position);
	}

	public ArcMenu(Context context) {
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	/**
	 * 初始化参数属性等
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		//获取到自定义属性
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu);
		position = typedArray.getString(R.styleable.ArcMenu_position);
		//两种方式都可以获取到position的值
		CharSequence text = typedArray.getText(R.styleable.ArcMenu_position);

		DisplayMetrics metrics = new DisplayMetrics();
		//150dp == 300px
		float defRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
		radius = typedArray.getDimension(R.styleable.ArcMenu_radius, defRadius);

		Log.e("position = ",position+"radius = "+radius);
		//获取完成之后关闭
		typedArray.recycle();
	}

	/**
	 * 让自定义的ViewGroup布局支持margin
	 * @param attrs
	 * @return
	 */
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(),attrs);
	}

	/**
	 * 测量子控件的宽高，然后根据子控件的宽高设置自己的宽高
	 * @param widthMeasureSpec
	 * @param heighteasureSpec
	 */
	protected void onMeasure(int widthMeasureSpec, int heighteasureSpec) {
		//1.获取ViewGroup上级容器推荐的计算模式及宽高
		int wMode = MeasureSpec.getMode(widthMeasureSpec);
		int hMode = MeasureSpec.getMode(heighteasureSpec);
		int wSize = MeasureSpec.getSize(widthMeasureSpec);
		int hSize = MeasureSpec.getSize(heighteasureSpec);
		//2.计算出所以childView的宽高
		measureChildren(widthMeasureSpec,heighteasureSpec);
		//3.声明记录wrap_content模式下的宽高
		int wWidth = 0;
		int wHeight = 0;
		//4.获取子控件的个数
		int childCount = getChildCount();
		//声明记录子控件测量出来的实际的宽高
		int mWidth = 0;
		int mHeight = 0;
		MarginLayoutParams mParams = null;
		//5.声明记录主菜单的位置：(l,t,r,b)
		int lHeight = 0;
		int rHeight = 0;
		int tWidth = 0;
		int bWidth = 0;
		//6.当容器是wrap_content时根据childView计算出的宽高及设置的margin来计算容器的宽高，否则直接使用固定宽高
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			mWidth = childView.getMeasuredWidth();
			mHeight = childView.getMeasuredHeight();
			//获取子控件设置的margin参数
			mParams = (MarginLayoutParams) childView.getLayoutParams();
			if (i == 0){
				//表示是主菜单布局：RelativeLayout
				tWidth += mWidth +mParams.leftMargin+mParams.rightMargin;
				lHeight += mHeight + mParams.topMargin + mParams.bottomMargin;
			}
		}
		//7.主要针对多个子控件时作处理：比较同一宽，高的子控件取大的
		// width = Math.max(tWidth, bWidth);
		// height = Math.max(lHeight, rHeight);
		wWidth = tWidth;
		wHeight = lHeight;
		//8.当容器布局时wrap_content时将宽高设置为我们计算好的值
		//否则直接使用父容器计算的值
		setMeasuredDimension(wMode == MeasureSpec.EXACTLY ? wSize : wWidth,hMode == MeasureSpec.EXACTLY ? hSize : wHeight);





	}

	/**
	 * 定位子控件位置
	 * @param change
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	protected void onLayout(boolean change, int l, final int t, int r, int b) {

		final int childCount = getChildCount();
		int mWidth = 0;
		int mHeight = 0;
		MarginLayoutParams mParams = null;
		//遍历所有孩子节点根据其宽高及margin布局
		//获取子控件之间的夹角大小:子控件平分角度：90度
		//减去2是因为主菜单控件本身也是其一个孩子节点
		//double degrees = (Math.PI / 2) / (childCount - 2);
		//double degrees = 90 / (childCount - 2);
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			mWidth = childView.getMeasuredWidth();
			mHeight = childView.getMeasuredHeight();
			//获取子控件设置的margin参数
			mParams = (MarginLayoutParams) childView.getLayoutParams();
			//记录子控件的位置：左，上，右，下
			int cl = 0,ct = 0,cr = 0,cb = 0;

			View viewParent = (View) childView.getParent();
			int pWidth = viewParent.getWidth();
			int pHeight = viewParent.getHeight();
			int measuredWidth1 = viewParent.getMeasuredWidth();
			int measuredHeight1 = viewParent.getMeasuredHeight();
			//获取计算过后的真实值
			int measuredWidth = childView.getMeasuredWidth();
			int measuredHeight = childView.getMeasuredHeight();
			//输出未计算及计算过后的宽高:150dp
			//150 -> 300px
			Log.e("宽高：","pW = "+pWidth+"  pH = "+pHeight);
			//150 -> 100px
			Log.e("宽高：","measuredWidth1 = "+measuredWidth1+"  measuredHeight1 = "+measuredHeight1);
			//获取主菜单控件的宽高：用于计算子菜单控件的位置
			final View mainMenu = getChildAt(0);
			if (i == 0){	//主菜单定位
				if (position.equals("0")){		//左上角
					cl = mParams.leftMargin;
					ct = mParams.topMargin;
				}else if (position.equals("1")){	//左下角
					cl = mParams.leftMargin;
					ct = pHeight - (mParams.bottomMargin + measuredHeight);
				}else if (position.equals("2")){	//右上角
					cl = pWidth - (mParams.rightMargin + measuredWidth);
					ct = mParams.topMargin;
				}else if (position.equals("3")){	//右下角
					cl = pWidth - (mParams.rightMargin + measuredWidth);
					ct = pHeight - (mParams.bottomMargin + measuredHeight);
				}
				cr = cl+mWidth;
				cb = ct+mHeight;

			}else{
				final View cView = childView;
				//子菜单定位
				//改变角度:跟随绘制到子控件的Index来改变角度
				//角度算不对 控件位置摆放不对
				double degrees1 = Math.PI / 2 / (childCount - 2)*(i-1);
				//double degrees1 = degrees * (i-1);
				Log.e("角度", String.valueOf(degrees1));

				int mMainMenuWidth = mainMenu.getMeasuredWidth();
				int mMainMenuHigth = mainMenu.getMeasuredHeight();

				if (position.equals("0")){		//左上角
					cl = Math.abs((int) (radius*Math.sin(degrees1)));	//仅仅是子控件相同大小情况下适用
					ct = Math.abs((int) ((radius*Math.cos(degrees1))+(mMainMenuHigth/2)-(measuredHeight/2)));
				}else if (position.equals("1")){	//左下角
					cl = Math.abs((int) (radius*Math.cos(degrees1)));	//仅仅是子控件相同大小情况下适用
					ct = Math.abs(pHeight - ((int) ((radius*Math.sin(degrees1))+(mMainMenuHigth/2)+(measuredHeight/2))));
				}else if (position.equals("2")){	//右上角
					cl = Math.abs(pWidth - ((int) (radius*Math.sin(degrees1)) +(mMainMenuWidth/2)+(measuredWidth/2)));
					ct = Math.abs((int) ((radius*Math.cos(degrees1))+(mMainMenuHigth/2)-(measuredHeight/2)));
				}else if (position.equals("3")){	//右下角
					cl = Math.abs(pWidth - ((int) (radius*Math.sin(degrees1)) +(mMainMenuWidth/2)+(measuredWidth/2)));
					ct = Math.abs(pHeight - ((int) ((radius*Math.cos(degrees1))+(mMainMenuHigth/2)+(measuredHeight/2))));
				}

				//左上角
				/*cl = (int) (radius * Math.sin(degrees1));
				ct = (int) (radius * Math.cos(degrees1));

				if (position.equals("1") || position.equals("3")){
					ct = getMeasuredHeight() - measuredHeight - ct;
					//ct = measuredHeight1 - measuredHeight - ct;
				}
				if (position.equals("2")||position.equals("3")){	//右上角
					cl = getMeasuredWidth() - measuredWidth - cl;
					//cl = measuredWidth1 - measuredWidth - cl;
				}*/
				cr = cl+mWidth;
				cb = ct+mHeight;

				//设置点击事件
				mainMenu.setOnClickListener(new OnClickListener() {
					// @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
					@Override
					public void onClick(View v) {
						Toast.makeText(getContext(),"点击事件生效",Toast.LENGTH_SHORT).show();
						//打开状态
						if (!status){	//打开状态
							closeAnim();
							status = true;
						}else {		//关闭状态
							openAnim();
							status = false;
						}
					}
				});

			}

			//cr = cl+mWidth;
			//cb = ct+mHeight;

			//输出每次绘制子控件的时候的位置
			Log.e("位置：","左上右下："+cl+" : "+ct+" : "+cr+" : "+cb);
			//定位主菜单位置
			childView.layout(cl,ct,cr,cb);

			/*//设置点击事件
			mainMenu.setOnClickListener(new OnClickListener() {
				// @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
				@Override
				public void onClick(View v) {
					Toast.makeText(getContext(),"点击事件生效",Toast.LENGTH_SHORT).show();
					//打开状态
					if (!status){	//打开状态
						closeAnim();
						childView.setVisibility(GONE);
						status = true;
					}else {		//关闭状态
						openAnim();
						status = false;
					}
				}
			});*/

			//设置子菜单点击事件
			final int position = i;
			childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.e("子菜单","子菜单点击事件生效");
					//为子菜单设置点击效果动画:并设置点击事件回调
					subClickAnim(view);
					if (mItemClickListener!=null){
						mItemClickListener.onItemClick(view, position);
					}
				}
			});
			//设置子菜单长按事件
			childView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					Log.e("子菜单","子菜单长按事件生效");
					//为子菜单设置点击效果动画:并设置点击事件回调
					subClickAnim(view);
					if (mItemClickListener!=null){
						mItemClickListener.onItemLongClick(view, position);
					}
					return true;
				}
			});

		}
	}

	/**
	 * 子菜单点击动画
	 * @param view
	 */
	private void subClickAnim(View view) {
		AnimatorSet bouncer = new AnimatorSet();
		// ObjectAnimator tXAnim = ObjectAnimator.ofFloat(view, "translationX", view.getPivotX(),mainMenu.getPivotX());
		// ObjectAnimator tYAnim = ObjectAnimator.ofFloat(view, "translationY", view.getPivotY(),mainMenu.getPivotY());
		ObjectAnimator sXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1,1.5f,1);
		ObjectAnimator sYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1,1.5f,1);
		bouncer.setDuration(500);
		// bouncer.play(tXAnim).with(tYAnim);
		bouncer.play(sXAnim).with(sYAnim);
		ValueAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f,1.0f);
		fadeAnim.setDuration(500);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(bouncer).with(fadeAnim);
		animatorSet.start();
	}

	private void openAnim() {
		View mainMenu;
		mainMenu = getChildAt(0);
		// 主菜单本身旋转动画
		//AnimatorSet mBouncer = new AnimatorSet();
		ObjectAnimator rXAnim = new ObjectAnimator().ofFloat(mainMenu, "rotation", 0f, 45f);
		//ObjectAnimator rYAnim = new ObjectAnimator().ofFloat(mainMenu, "rotationY", 0, 90);
		rXAnim.setDuration(500);
		//rYAnim.setDuration(500);
		//mBouncer.play(rXAnim).with(rYAnim);
		//mBouncer.start();
		rXAnim.start();

		for (int j = 0; j < getChildCount(); j++) {

			if (j != 0){
				View view = getChildAt(j);
				view.setVisibility(VISIBLE);
				double degrees1 = Math.PI / 2 / (getChildCount() - 2)*(j-1);

				Log.e("角度1", String.valueOf(degrees1));
				//左上角
				int cl = (int) (radius * Math.sin(degrees1));
				int ct = (int) (radius * Math.cos(degrees1));
				int xFlag = 1;
				int yFlag = 1;
				if (position.equals("1")||position.equals("3")){
					yFlag = -1;
					ct = getMeasuredHeight() - view.getMeasuredHeight() - ct;
					// ct = Math.abs(getMeasuredHeight() - ((int) ((radius*Math.sin(degrees1))+(mainMenu.getMeasuredHeight()/2)+(view.getMeasuredHeight()/2))));
				}
				if (position.equals("2")||position.equals("3")){
					xFlag = -1;
					cl = getMeasuredWidth() - view.getMeasuredWidth() - cl;
					// Math.abs(getMeasuredWidth() - ((int) (radius*Math.sin(degrees1)) +(mainMenu.getMeasuredWidth()/2)+(view.getMeasuredWidth()/2)));
				}

				/*if (position.equals("0")){		//左上角
					cl = (int) (radius*Math.sin(degrees1));	//仅仅是子控件相同大小情况下适用
					ct = (int) ((radius*Math.cos(degrees1))+(mainMenu.getMeasuredHeight()/2)-(view.getMeasuredHeight()/2));
				}else if (position.equals("1")){	//左下角
					cl = (int) (radius*Math.cos(degrees1));	//仅仅是子控件相同大小情况下适用
					ct = getMeasuredHeight() - ((int) ((radius*Math.sin(degrees1))+(mainMenu.getMeasuredHeight()/2)+(view.getMeasuredHeight()/2)));
				}else if (position.equals("2")){	//右上角
					cl = getMeasuredWidth() - ((int) (radius*Math.sin(degrees1)) +(mainMenu.getMeasuredWidth()/2)+(view.getMeasuredWidth()/2));
					ct = (int) ((radius*Math.cos(degrees1))+(mainMenu.getMeasuredHeight()/2)-(view.getMeasuredHeight()/2));
				}else if (position.equals("3")){	//右下角
					cl = getMeasuredWidth() - ((int) (radius*Math.sin(degrees1)) +(mainMenu.getMeasuredWidth()/2)+(view.getMeasuredWidth()/2));
					ct = getMeasuredHeight() - ((int) ((radius*Math.cos(degrees1))+(mainMenu.getMeasuredHeight()/2)+(view.getMeasuredHeight()/2)));
				}*/

				cl = cl * xFlag;
				ct = ct * yFlag;
				float mX = mainMenu.getPivotX();
				float mY = mainMenu.getPivotY();
				// mX = mX * xFlag;
				// mY = mY * yFlag;

				AnimatorSet bouncer = new AnimatorSet();
				// ObjectAnimator tXAnim = ObjectAnimator.ofFloat(view, "translationX", mX,cl);
				// ObjectAnimator tYAnim = ObjectAnimator.ofFloat(view, "translationY", mY,ct);
				ObjectAnimator sXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0,1);
				ObjectAnimator sYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0,1);
				bouncer.setDuration(500);
				// bouncer.play(tXAnim).with(tYAnim);
				bouncer.play(sXAnim).with(sYAnim);
				ValueAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f);
				fadeAnim.setDuration(500);
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.play(bouncer).with(fadeAnim);
				animatorSet.start();
			}
		}

	}
	private void closeAnim() {
		View mainMenu;
		mainMenu = getChildAt(0);
		// 主菜单本身旋转动画
		//AnimatorSet mBouncer = new AnimatorSet();
		ObjectAnimator rXAnim = new ObjectAnimator().ofFloat(mainMenu, "rotation", 45f, 0f);
		//ObjectAnimator rYAnim = new ObjectAnimator().ofFloat(mainMenu, "rotationY", 0, 90);
		rXAnim.setDuration(500);
		//rYAnim.setDuration(500);
		//mBouncer.play(rXAnim).with(rYAnim);
		//mBouncer.start();
		rXAnim.start();

		for (int j = 0; j < getChildCount(); j++) {

			if (j != 0) {
				View view = getChildAt(j);
				AnimatorSet bouncer = new AnimatorSet();
				// ObjectAnimator tXAnim = ObjectAnimator.ofFloat(view, "translationX", view.getPivotX(),mainMenu.getPivotX());
				// ObjectAnimator tYAnim = ObjectAnimator.ofFloat(view, "translationY", view.getPivotY(),mainMenu.getPivotY());
				ObjectAnimator sXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1,0);
				ObjectAnimator sYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1,0);
				bouncer.setDuration(500);
				// bouncer.play(tXAnim).with(tYAnim);
				bouncer.play(sXAnim).with(sYAnim);
				ValueAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f);
				fadeAnim.setDuration(500);
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.play(bouncer).with(fadeAnim);
				animatorSet.start();
				view.setVisibility(GONE);
			}
		}

	}


}
