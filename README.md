
##一、简介
=========================================

    **CarouselFigureView是一个支持自动切换、无限循环的轮播图控件。其主要作用就是实现项目当中的轮播图。
    CarouselFigureView有很强的适配性，可满足基本上所有的轮播图需求。
    你可以在代码中随意禁用自动切换、无限循环功能，也可以隐藏指示点。**

##二、版本
=========================================
    2016.5.17-v1.0.0

    







##三、详解
=========================================
###1、属性
--------------------------------
    isAutoPlay：              是否支持自动切换，boolean值
    
    isInfiniteLoop：          是否支持无限循环，boolean值  
    
    pointLeft_Right_Margin:   指示点的左右间距，为了保证指示点居中，这个间距将会设置给左右两边，
                              所以如果您需要10dp的间距，您仅需将该属性设置为5dp即可     
                              
    isNeedIndicationPoint:    是否需要指示点，boolean值
    
    playIntervalTime:         轮播图的切换时间，单位毫秒，默认为3秒
    
    pointBottomMargin：       指示点距离下方的距离
    
    pointBackground：         指示点的背景，内容为资源文件，这里如果需要自定义背景，需要提供selector文件，
                              并在其中给予state_selected＝true 以及state_selected＝false两种状态的背景，                 
                              稍后提供代码
                              
##2、方法：  
--------
    setURL(List<String> urlList)                
        设置数据，数据为url集合
        
    setResourceList(@DrawableRes List<Integer> resourceList)  
        设置数据，数据为resource文件

    setViewPagerSwitchStyle(ViewPager.PageTransformer transformer) 
        自定义ViewPager的切换方式
        
    startLoad()     
        显示轮播图，在设置数据之后调用该方法，注意必须在此之前设置数据，否则会抛出异常
        
    setViewPagerSwitchSpeed(int switchSpeed)
            自定义ViewPager的切换动画速度，单位是毫秒，注意切换动画速度一定要快于轮播图展示时间。并且轮播图
            真实的展示时间＝ playIntervalTime（设置的轮播图切换间隔） － switchSpeed（切换动画速度)
            
    setCarouselFigureItemClickListener(CarouselFigureItemClickListener listener)
            条目的点击事件，包含view 以及 position两个参数。
            position的最大值等于数据长度－1,所以可以直接使用，不需要做取余等处理。
###3、demo代码：
————————————————
####布局文件：            
```
    <?xml version="1.0" encoding="utf-8"?>
        <com.my.view.CarouselFigureView
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:mengyuan="http://schemas.android.com/apk/res-auto"
            android:id="@+id/carousel_figure_view"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            mengyuan:isAutoPlay="true"
            mengyuan:isInfiniteLoop="true"
            mengyuan:isNeedIndicationPoint="true"
            mengyuan:playIntervalTime="4000"
            mengyuan:pointBottomMargin="3dp"
            mengyuan:pointLeft_Right_Margin="5dp"
            mengyuan:pointBackground="@drawable/point_bg_test"
        />
```
####activity：
```
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          carouselFigureView = (CarouselFigureView) findViewById(R.id.carousel_figure_view);
          ArrayList<String> url = new ArrayList<>();
          url.add("http://o.ypgimg.com/content/2016/5/16/3b61698c-cf8b-4abe-9c39-d474b454d790.jpg");
          url.add("http://o.ypgimg.com/content/2016/5/16/88d50d38-09fb-4d3e-bd0c-fc52e2a26c74.jpg");
          url.add("http://o.ypgimg.com/content/2016/5/16/e5a7b0e3-1a21-4e66-adc8-17a1b7c1a92f.jpg");
          url.add("http://o.ypgimg.com/content/2016/5/16/8bae5357-d542-4e52-ad5f-ad7d021fc163.jpg");
  
          carouselFigureView.setURL(url);
          carouselFigureView.startLoad();
          carouselFigureView.setViewPagerSwitchStyle(new DepthPageTransformer());
          carouselFigureView.setViewPagerSwitchSpeed(500);
      }
      
```      
      
####最后，贴一下指示点背景，在res/drawable文件下
####point_seclted.xml
```
<?xml version="1.0" encoding="utf-8"?>
      <shape xmlns:android="http://schemas.android.com/apk/res/android"
          android:shape="oval">
          <size
              android:width="10dp"
              android:height="10dp" />
          <solid android:color="@android:color/white" />
          <stroke
              android:width="1dp"
              android:color="@android:color/white" />
      </shape>
```     
      
####point_unselected.xml
```
    <?xml version="1.0" encoding="utf-8"?>
      <shape xmlns:android="http://schemas.android.com/apk/res/android"
          android:shape="oval">
          <size
              android:width="10dp"
              android:height="10dp" />
          <solid android:color="@android:color/transparent" />
          <stroke
              android:width="1dp"
              android:color="@android:color/white" />
    </shape>
```
 
####真正设置为指示点背景的文件：point_bg.xml
```
<?xml version="1.0" encoding="utf-8"?>
     <selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/point_unselect" android:state_selected="false"/>
    <item android:drawable="@drawable/point_select" android:state_selected="true"/>
      </selector>
```

>希望这个demo可以在大家实现轮播图的时候有帮助

>有任何问题欢迎大家给我留言：**mengyuanzz@126.com**