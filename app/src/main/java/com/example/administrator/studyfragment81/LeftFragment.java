package com.example.administrator.studyfragment81;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class LeftFragment extends Fragment implements GetInter{
    private List<String> list;
    private List<Bean> listBean;
    private static final String TAG = "LeftFragment";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                MyAdapter myAdapter = new MyAdapter();
                left_listView.setAdapter(myAdapter);
                left_listView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RightFragment rightFragment = new RightFragment();

                    }
                });
            }
        }
    };
    private ListView left_listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_item,container,false);
        left_listView = (ListView) view.findViewById(R.id.left_listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        OkhttpUtils.getData("http://api.eleteam.com/v1/category/list-with-product",this);
    }

    @Override
    public void getData(String data) {
        listBean = new ArrayList<>();
        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject data1 = jsonObject.optJSONObject("data");
            JSONArray categories = data1.optJSONArray("categories");
            for (int i = 0; i <categories.length() ; i++) {
                JSONObject jsonObject1 = categories.optJSONObject(i);
                String name = jsonObject1.optString("name");
                list.add(name);
                JSONArray products = jsonObject1.optJSONArray("products");
                for (int j = 0; j <products.length() ; j++) {
                    JSONObject jsonObject2 = products.optJSONObject(j);
                    String image_small = jsonObject2.optString("image_small");
                    String name1 = jsonObject2.optString("name");
                    String price = jsonObject2.optString("price");
                    listBean.add(new Bean(image_small,name1,price));
                    Log.d(TAG,listBean.toString());
                }
            }
            //根据题意删除第一个条目。。。
            list.remove(0);
            //发送一个什么。。。。
            mHandler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            ViewHolder vh=null;
            if(convertview==null)
            {
                vh = new ViewHolder();
                convertview = View.inflate(getContext(),R.layout.baseleft_item,null);
                vh.text = (TextView) convertview.findViewById(R.id.left_textView);
                convertview.setTag(vh);
            }else {
                vh= (ViewHolder) convertview.getTag();
            }
            vh.text.setText(list.get(position).toString());
            return convertview;
        }
        class ViewHolder{
            TextView text;
        }
    }
}
