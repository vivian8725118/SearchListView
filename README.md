[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/vivian8725118/SearchListView.svg?branch=master)](https://github.com/vivian8725118/SearchListView)
[![Coverage Status](https://coveralls.io/repos/vivian8725118/SearchListView/badge.svg?branch=master&service=github)](https://coveralls.io/github/vivian8725118/SearchListView?branch=master)
# SearchListView
带搜索栏的 listview，轻拉出现搜索栏，用力拉出现下拉刷新
#ScreenShot
![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/SearchListView.gif)
![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/S51217-161726.jpg)
![Alt text](https://github.com/vivian8725118/SearchListView/blob/master/S51217-163013.jpg)
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
    
    
