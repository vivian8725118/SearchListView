[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)


# SearchListView
讨论问题可以点击这个按钮(☞ﾟヮﾟ)☞[![Join the chat at https://gitter.im/vivian8725118/SearchListView](https://badges.gitter.im/vivian8725118/SearchListView.svg)](https://gitter.im/vivian8725118/SearchListView?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)（☜(ﾟヮﾟ☜)对，就是它！！！）

带搜索栏的 listview，轻拉出现搜索栏，用力拉出现下拉刷新
#ScreenShot

<!--![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/SearchListView.gif)-->
<!--![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/S51217-161726.jpg)-->
<!--![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/S51217-163013.jpg)-->
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/SearchListView.gif" width = "350" height = "611" alt="SearchListView" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/S51217-161726.jpg" width = "350" height = "611" alt="S51217-161726" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/S51217-163013.jpg" width = "350" height = "611" alt="S51217-163013" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/NoRefreshableSearchListView.gif" width = "350" height = "611" alt="NoRefreshableSearchListView" align=center />

#Usage
###SearchListView 直接放入布局中
         <vivian.com.searchlistview.widget.SearchListView
             android:id="@+id/listview"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:layout_gravity="center_horizontal"
             android:background="@color/stand_backgroud"
             android:cacheColorHint="#00000000"
             android:drawSelectorOnTop="false"
             android:listSelector="@android:color/transparent">
   </vivian.com.searchlistview.widget.SearchListView>
###添加 SearchBar
    SearchBar searchBar=new SearchBar(this);
    mListView.addHeaderView(searchBar);
    
###设置自动下拉刷新
        mListView.pullRefreshEnable(true);//下拉刷新
        mListView.setAutoFetchMore(true);//自动加载更多
        手动请求网络-->显示 header-->请求成功，关闭 header
###设置关闭刷新功能
        mListView.setEnableRefresh(false);//设 false关闭刷新功能
        
###如果有问题需要讨论，请加我 QQ：1354458047 进行讨论
    
    
