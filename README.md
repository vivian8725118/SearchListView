[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)


# SearchListView
讨论问题可以点击这个按钮(☞ﾟヮﾟ)☞[![Join the chat at https://gitter.im/vivian8725118/SearchListView](https://badges.gitter.im/vivian8725118/SearchListView.svg)](https://gitter.im/vivian8725118/SearchListView?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)（☜(ﾟヮﾟ☜)对，就是它！！！）

带搜索栏的 listview，轻拉出现搜索栏，用力拉出现下拉刷新
# ScreenShot

<div>
<img hspace=20 src="https://github.com/vivian8725118/SearchListView/blob/master/art/SearchListView.gif" width = "350" height = "611" alt="SearchListView" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/S51217-161726.jpg" width = "350" height = "611" alt="S51217-161726" align=center />
</div>
<div>
<img hspace=20 src="https://github.com/vivian8725118/SearchListView/blob/master/art/S51217-163013.jpg" width = "350" height = "611" alt="S51217-163013" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/NoRefreshableSearchListView.gif" width = "350" height = "611" alt="NoRefreshableSearchListView" align=center />
</div>

### 没有文字的版本
<div>
<img hspace=20 src="https://github.com/vivian8725118/SearchListView/blob/master/art/SearchListViewNoTextHeaderRecord.gif?raw=true" width = "350" height = "611" alt="NoRefreshableSearchListView" align=center />
<img src="https://github.com/vivian8725118/SearchListView/blob/master/art/SearchListViewNoTextFooterRecord.gif?raw=true" width = "350" height = "611" alt="NoRefreshableSearchListView" align=center />
</div>

# Usage
### SearchListView 直接放入布局中
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
### 添加 SearchBar
    SearchBar searchBar=new SearchBar(this);
    mListView.addHeaderView(searchBar);
    
### 设置自动下拉刷新
        mListView.pullRefreshEnable(true);//下拉刷新
        mListView.setAutoFetchMore(true);//自动加载更多
        手动请求网络-->显示 header-->请求成功，关闭 header
### 设置关闭刷新功能
        mListView.setEnableRefresh(false);//设 false关闭刷新功能
        
### 如果有问题需要讨论，请加我 QQ：1354458047 进行讨论
    
# LICENSE

## The MIT License (MIT)

Copyright (c) 2015 Vivian

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
