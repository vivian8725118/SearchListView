package vivian.com.searchlistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vivian.com.searchlistview.widget.SearchBar;
import vivian.com.searchlistview.widget.SearchListView;

public class MainActivity extends Activity {
    SearchListView mListView;
    SearchBar mSearchBar;
    ArrayList<String> mList=new ArrayList<>();
    MyAdapter mAdappter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=(SearchListView)findViewById(R.id.listview);
        mSearchBar=new SearchBar(this);

        mSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 15/11/23  the click event
                
            }
        });
        mListView.addHeaderView(mSearchBar);

        mListView.pullRefreshEnable(true);//下拉刷新
        mListView.setAutoFetchMore(true);//自动加载更多

        //initialize
        for(int i=0;i<10;i++){
            String str="item"+(i+1);
            mList.add(str);
        }
        mAdappter=new MyAdapter();
        mListView.setAdapter(mAdappter);

        //初始自动下拉刷新
        mListView.showHeader(true);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.onRefreshComplete();
            }
        }, 3000);

        mListView.setOnRefreshListener(new SearchListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                for (int i = 0; i < 10; i++) {
                    String str = "item" + (i + 1);
                    mList.add(str);
                }

                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdappter.notifyDataSetChanged();
                        mListView.onRefreshComplete();
                    }
                },3000);
            }
        });

        mListView.setOnLastItemVisibleListener(new SearchListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                try {
                    int start=mList.size();
                    for (int i = start; i < start + 10; i++) {
                        String str = "item" + (i + 1);
                        mList.add(str);
                    }
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdappter.notifyDataSetChanged();
                            mListView.onRefreshComplete();
                        }
                    }, 3000);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item,null);
            TextView textView=(TextView)view.findViewById(R.id.textView);
            textView.setText(getItem(position));

            return view;
        }
    }

}
