<<<<<<< HEAD
>### 自定义ViewGroup：实现弧形菜单

>#####效果预览：
***
> ![预览图片](https://github.com/JqyModi/ArcMenu/blob/master/S70511-10280879.jpg) 
***
> #####实现步骤：
> - 1.新建一个类继承至ViewGroup并重写至少前两个方法：一个是动态编码用一个是布局方式展示用
> - 2.自定义属性：
>	- 1.在values文件夹下新建attrs.xml文件：以<declare-styleable name="属性名">节点新建自己的属性及属性类型
```
			<!--规格参数：表示是X * X的正方形格子：如4*4-->
			<attr name="standard" format="integer"/>
			</declare-styleable>
 ```
>	- 2.构造方法中初始化1中自定义的属性：通过context.obtainStyledAttributes(attrs, R.styleable.属性名)获取到属性数组进行初始化
>	- 3.初始化完成ta.recycle();避免内存泄漏
>	- 4.因为是ViewGroup控件所以其内部是可以添加子控件的：重写onMeasure方法来测量（计算子控件及自身的真实宽高：相对于子控件中设置了wrap_content的控件需要操作的）
>		- 1.onMeasure中有三种常见的测量模式：通过MeasureSpec.getMode(widthMeasureSpec)方式获取：
>		- 2.ALT_MOST:相当于我们布局中设置了wrap_content模式：这种模式是需要我们开发者自己计算出每个子控件的宽高在通过子控件的宽高计算出改容器控件的宽高的
>		- 3.EXACTLY：相当于我们布局中设置了match_parent模式或者是精确地值的时候
>	- 5.在onlayout中定位我们的子控件的位置：通过getChildAt(index)及getChildrenCount来获取到容器控件中每个子控件的实例（拿到实例之后你就可以为所欲为了  哈哈）
>		- 1.先通过数学公式等计算出子控件需要摆放的位置（坐标：一般以左上角的坐标为准）
>		- 2.通过View.layout(l,t,r,b)给子控件一一布局
>		- 3.此处可以给子控件加上一些特效动画之类的操作
>	- 6.重点来了：这是方法是自定义View必须重写的方法了：onDraw方法：
>		- 1.通过canvas及Paint我们可以在canvas上绘制任何我们想要绘制的图案（只要你有足够的想象力：画笔一般需要设置的基本属性：颜色、大小、抗锯齿）
>		- 2.无论我们是想画什么类型的图案都是通过计算出改图案的坐标位置在通过画笔在画布上绘制出来的（所以计算坐标点位置才是本环节的重点）
>		- 3.canvas提供了基本图形的绘制方法供开发者使用：如画圆、画线、画文字、画矩形等等
>	- 7.既然我们是自定义View那肯定得跟用户交互：必须提供相应的点击或者触摸事件
>		- 1.特殊操作我们需要重写onTouch事件：通过判断用户的按下、移动、弹起操作来处理我们的业务逻辑
>		- 2.如果是需要提供子控件的点击事件：我们应该自定义回调接口供用户设置控件的点击事件时提供相应的回调操作（参考或者说自己脑补自定义回调接口的5个步骤：定义接口类型、定义改接口类型的变量、set方法、通过子控件的点击事件设置自定义的接口回调、在控件的实例中调用相应的回调方法）
>	- 8.我们都知道一般的ViewGroup都支持margin，所以我们自定义控件也应该要支持用户的margin操作才是合理的：
>		- 1.	通过重写:
```
			generateLayoutParams(AttributeSet attrs) {
					return new MarginLayoutParams(getContext(),attrs);
			 }
```
>                  并返回系统提供给我们的MarginLayoutParams(getContext(),attrs)来是我们的控件支持margin操作
>		- 2.如果我们设置了布局的margin支持之后那么我们在定位控件及子控件的时候必须加入margin值来重新计算各控件的宽高：
>			- 1.通过这种方式mParams = (MarginLayoutParams) childView.getLayoutParams()来获取每个控件的margin布局参数
>			- 2.计算坐标位置的时候加上margin值即可
=======
### 自定义ViewGroup：实现弧形菜单
> - ###1.实现步骤：
```
> 	- 1.新建一个类继承至ViewGroup并重写至少前两个方法：一个是动态编码用一个是布局方式展示用
> 	- 2.自定义属性：
	>	- 1.在values文件夹下新建attrs.xml文件：以<declare-styleable name="属性名">节点新建自己的属性及属性类型
	>            <!--规格参数：表示是X * X的正方形格子：如4*4-->
	>            <attr name="standard" format="integer"/>
	>            </declare-styleable>
	>        - 2.构造方法中初始化1中自定义的属性：通过context.obtainStyledAttributes(attrs, R.styleable.属性名)获取到属性数组进行初始化
	>        - 3.初始化完成ta.recycle();避免内存泄漏
>        	- 4.因为是ViewGroup控件所以其内部是可以添加子控件的：重写onMeasure方法来测量（计算子控件及自身的真实宽高：相对于子控件中设置了wrap_content的控件需要操作的）
	>            - 1.onMeasure中有三种常见的测量模式：通过MeasureSpec.getMode(widthMeasureSpec)方式获取：
	>            - 2.ALT_MOST:相当于我们布局中设置了wrap_content模式：这种模式是需要我们开发者自己计算出每个子控件的宽高在通过子控件的宽高计算出改容器控件的宽高的
	>            - 3.EXACTLY：相当于我们布局中设置了match_parent模式或者是精确地值的时候
>       - 3..在onlayout中定位我们的子控件的位置：通过getChildAt(index)及getChildrenCount来获取到容器控件中每个子控件的实例（拿到实例之后你就可以为所欲为了  哈哈）
>            - 1.先通过数学公式等计算出子控件需要摆放的位置（坐标：一般以左上角的坐标为准）
>            - 2.通过View.layout(l,t,r,b)给子控件一一布局
>            - 3.此处可以给子控件加上一些特效动画之类的操作
>       - 4.重点来了：这是方法是自定义View必须重写的方法了：onDraw方法：
>            - 1.通过canvas及Paint我们可以在canvas上绘制任何我们想要绘制的图案（只要你有足够的想象力：画笔一般需要设置的基本属性：颜色、大小、抗锯齿）
>            - 2.无论我们是想画什么类型的图案都是通过计算出改图案的坐标位置在通过画笔在画布上绘制出来的（所以计算坐标点位置才是本环节的重点）
>            - 3.canvas提供了基本图形的绘制方法供开发者使用：如画圆、画线、画文字、画矩形等等
>       - 5.既然我们是自定义View那肯定得跟用户交互：必须提供相应的点击或者触摸事件
>	     - 1.特殊操作我们需要重写onTouch事件：通过判断用户的按下、移动、弹起操作来处理我们的业务逻辑
>            - 2.如果是需要提供子控件的点击事件：我们应该自定义回调接口供用户设置控件的点击事件时提供相应的回调操作（参考或者说自己脑补自定义回调接口的5个步骤：定义接口类型、定义改接口类型的变量、set方法、通过子控件的点击事件设置自定义的接口回调、在控件的实例中调用相应的回调方法）
>       - 6.我们都知道一般的ViewGroup都支持margin，所以我们自定义控件也应该要支持用户的margin操作才是合理的：
>            - 1.	通过重写generateLayoutParams(AttributeSet attrs) {
>                            return new MarginLayoutParams(getContext(),attrs);
>                            }
>                  并返回系统提供给我们的MarginLayoutParams(getContext(),attrs)来是我们的控件支持margin操作
>            - 2.如果我们设置了布局的margin支持之后那么我们在定位控件及子控件的时候必须加入margin值来重新计算各控件的宽高：
>                - 1.通过这种方式mParams = (MarginLayoutParams) childView.getLayoutParams()来获取每个控件的margin布局参数
>                - 2.计算坐标位置的时候加上margin值即可
```
>>>>>>> origin/master
***
>#####注意事项：
>	- 1.学习自定义ViewGroup之后自己尝试写了自定义View之五子棋界面的实现：当我在执行绘制棋子的时候出现了内存泄漏问题于是乎我有开始了对内存泄漏的探索之旅
>	- 2.带着提出问题解决问题的思路我开始了这段别样的旅程：
>		- 1.为什么会出现内存泄漏？
>		- 1.因为自己对这块知识不是很了解：故上网查阅了大量资料，无意中我发现了网络上开发者们常用的一个工具就是leakcanary据说可以检测自己的应用中是否有内存泄漏及内存泄漏的精确定位
>		- 2.于是乎我就开始使用这个工具：通过github.com/square/leakcanary/了解到使用方法之后便开始尝试在demo中加入这个神奇的工具：导入依赖-重写application-初始化-待检测页面调用检测方法
>		- 3.似乎一切都是那么的顺利正当我暗自偷笑的时候问题出现了：leakcanary工具虽然检测到了内存泄漏，但我点击进入的时候发现里面一片空白啥也没有，我在想是不是那个步骤出了问题导致工具不能正确的检测到内存泄漏的地方（待解决）
>		- 4.有一个坑令我印象特别深刻：就是官方给出的工具版本是1.5.1的但是jcenter中只有1.3.1版本导致AS在下载aar文件时一直下载不到 导致AS一直处于gradle状态等了半个小时发现还是不行于是乎开始了思考问题的起因后来发现版本问题导致（假死状态），改成1.3.1gradle恢复正常
>			- 2.内存泄漏怎么解决？
>		- 3.特别提示：自定义View的时候我们一般会有动态绘制视图的需求（其实就是通过用户的点击触摸灯事件与用户交互时绘制图像文字等操作）：
>			- 1.此操作不能将画布提出来作为一个全局变量使用否则程序会崩溃至于为什么我也不清楚，个人认为是onDraw中的canvas的绘制是要不停的绘制界面当我们把它放到onTouch中使用时每绘制一帧就刷新一次这样会使...
>			- 2.解决方式是在onTouch中记录下用户操作的坐标位置然后调用onDraw方法重新绘制界面即可
>
***
