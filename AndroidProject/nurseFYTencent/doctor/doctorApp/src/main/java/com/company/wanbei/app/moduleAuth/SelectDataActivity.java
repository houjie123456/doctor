package com.company.wanbei.app.moduleAuth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.company.wanbei.app.base.BaseActivity;
import com.company.wanbei.app.bean.SelectDataBean;
import com.company.wanbei.app.moduleAuth.imp.SelectDataPresenterImp;
import com.company.wanbei.app.util.ExitApp;
import com.tencent.qcloud.tuikit.tuichat.fromApp.util.MyDialog;
import com.tencent.qcloud.tuikit.tuichat.fromApp.util.MyToast;
import com.tencent.qcloud.tuikit.tuichat.fromApp.view.MyTextView;
import com.company.wanbei.app.view.SearchBar;
import com.company.wanbei.app.R;

import java.util.ArrayList;

//import androidx.core.content.ContextCompat;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YC on 2018/6/24.
 */

public class SelectDataActivity extends BaseActivity implements AuthInterface.SelectInterface{
    private Dialog myDialog;

    private RecyclerView recordRecycler;
    private ArrayList<SelectDataBean> array;
    private RecordListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private int type,page=1;
    private SelectDataPresenterImp presenter;
    private String keyWords = "";
    private SearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApp.getInstance().addActivity(this);		// add Activity ????????????
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????????????????????
        setContentView(R.layout.activity_select_data);
        initData();
        initHead();
        initView();
        setListener();
        getData();
    }

    private void getData() {
        if (type == AuthActivity.SEX) presenter.getSex(); //??????
        if (type == AuthActivity.TYPE) presenter.getJob(); //??????
        if (type == AuthActivity.POSITION) presenter.getPosition(); //??????
        if (type == AuthActivity.DEPATE) presenter.getDepartment(); //??????
        if (type == AuthActivity.HOSPITAL) presenter.getHospital(keyWords); //??????
//        if (type == AddDrugsActivity.DRUG_TIME) presenter.getTime(); //????????????
//        if (type == AddDrugsActivity.DRUG_EAT) presenter.getEat(); //????????????
//        if (type == SettingPictureActivity.TIME) presenter.getSettingPictureTime(); //????????????????????????
//        if (type == SettingPictureActivity.NUM) presenter.getSettingPictureNum(); //????????????????????????
//        if (type == AddVisitActivity.TYPE) presenter.getVisitType(); //????????????????????????
//        if (type == CreateVoiceActivity.VOICE) presenter.getSettingVoiceTime(); //????????????????????????
    }

    private void initData() {
        type = getIntent().getIntExtra("type",1);
    }


    private void initHead() {
        RelativeLayout head = (RelativeLayout) findViewById(R.id.head_layout);
        MyTextView title = (MyTextView) head.findViewById(R.id.head_top_title);
        title.setText("??????");
        ImageView rightIV = (ImageView)head.findViewById(R.id.head_top_image);
        rightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView(){
        presenter = new SelectDataPresenterImp(this);

        searchBar = (SearchBar) findViewById(R.id.select_search);
        searchBar.setLayoutStyle(R.drawable.shape_search_bar_style);
        searchBar.setVisibility(View.GONE);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.select_refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.base_red_color));
        recordRecycler = (RecyclerView)findViewById(R.id.select_recycler);
        recordRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        array = new ArrayList<>();
        adapter = new RecordListAdapter(getContext(),array);
        recordRecycler.setAdapter(adapter);
        recordRecycler.setItemAnimator(new DefaultItemAnimator());

        if (type == AuthActivity.HOSPITAL) searchBar.setVisibility(View.VISIBLE); //??????

//        if (type == SettingPictureActivity.TIME) ; //?????????????????????
    }

    private void setListener() {

        searchBar.setOnEditTextDataChanged(new SearchBar.OnEditTextDataChanged() {
            @Override
            public void onTextisEmpty() {
            }

            @Override
            public void onTextChanged() {
                keyWords = searchBar.getRequestKey();
                getData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

//        recordRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            //?????????????????????????????????????????????????????????????????????
//            boolean isSlidingToLast = false;
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                // ???????????????
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    //?????????????????????????????????ItemPosition
//                    int lastVisiblePositions = manager.findLastVisibleItemPosition();
//                    int totalItemCount = manager.getItemCount();
//                    // ???????????????????????????
//                    if (lastVisiblePositions >= (totalItemCount -2) && isSlidingToLast) {
//                        //???????????????????????????
//                        page = page + 1;
//                        getData();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //dx?????????????????????????????????dy??????????????????????????????
//                if(dy > 0){
//                    //??????0???????????????????????????
//                    isSlidingToLast = true;
//                }else{
//                    //????????????0 ???????????????????????????
//                    isSlidingToLast = false;
//                }
//            }
//        });
        adapter.setOnItemClickListener(new OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("id",array.get(position).getId());
                if (type == AuthActivity.SEX) b.putString("name",array.get(position).getSex()); //??????
                if (type == AuthActivity.TYPE) b.putString("name",array.get(position).getJobName()); //??????
                if (type == AuthActivity.POSITION) b.putString("name",array.get(position).getIdVal()); //??????
                if (type == AuthActivity.DEPATE) b.putString("name",array.get(position).getMyName()); //??????
                if (type == AuthActivity.HOSPITAL) b.putString("name",array.get(position).getThisName()); //??????
//                if (type == AddDrugsActivity.DRUG_TIME) {
//                    if(position == array.size()-1){
//                        LayoutInflater factory = LayoutInflater.from(SelectDataActivity.this);
//                        final View textEntryView = factory.inflate(R.layout.input_dialog, null);
//                        final EditText editTextDay = (EditText) textEntryView.findViewById(R.id.editTextDay);
//                        final EditText editTextNum = (EditText)textEntryView.findViewById(R.id.editTextNum);
//                        AlertDialog.Builder ad1 = new AlertDialog.Builder(SelectDataActivity.this);
//                        ad1.setTitle("????????????:");
//                        ad1.setIcon(R.drawable.img_icon);
//                        ad1.setView(textEntryView);
//                        ad1.setPositiveButton("???", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int i) {
//                                b.putString("takingNum",editTextNum.getText().toString());
//                                b.putString("name",editTextDay.getText().toString()+"???"+editTextNum.getText().toString()+"???");
//                                b.putString("takingTime",editTextDay.getText().toString());
//                                intent.putExtras(b);
//                                setResult(RESULT_OK,intent);
//                                finish();
//                            }
//                        });
//                        ad1.setNegativeButton("???", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int i) {
//
//                            }
//                        });
//                        ad1.show();// ???????????????
//                        return;
//                    }else {
//                        b.putString("takingNum",array.get(position).getId());
//                        b.putString("name",array.get(position).getPositionName());
//                        b.putString("takingTime","1");
//                    }
//                }
////                    b.putString("name",array.get(position).getPositionName()); //????????????
//                if (type == AddDrugsActivity.DRUG_EAT) b.putString("name",array.get(position).getPositionName()); //????????????
//                if (type == SettingPictureActivity.TIME ) {//????????????
//                    if(position == array.size()-1){
//                        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(SelectDataActivity.this).builder()
//                                .setTitle("?????????????????????(????????????)")
//                                .setEditText("").setEditType(InputType.TYPE_CLASS_NUMBER);
//                        myAlertInputDialog.setPositiveButton("??????", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(Integer.parseInt(myAlertInputDialog.getResult())<1){
//                                    showToast("?????????????????????????????????");
//                                    return;
//                                }
//                                b.putString("id",myAlertInputDialog.getResult());
//                                b.putString("name",myAlertInputDialog.getResult()+"???");
//                                intent.putExtras(b);
//                                setResult(RESULT_OK,intent);
//
//                                myAlertInputDialog.dismiss();
//                                finish();
//                            }
//                        }).setNegativeButton("??????", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                myAlertInputDialog.dismiss();
//                            }
//                        });
//                        myAlertInputDialog.show();
//                        return;
//                    }else {
//                        b.putString("name",array.get(position).getPositionName());
//                    }
//                }
//                if (type == SettingPictureActivity.NUM ) {//????????????
//                    if(position == array.size()-1){
//                        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(SelectDataActivity.this).builder()
//                                .setTitle("?????????????????????(????????????)")
//                                .setEditText("").setEditType(InputType.TYPE_CLASS_NUMBER);
//                        myAlertInputDialog.setPositiveButton("??????", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(Integer.parseInt(myAlertInputDialog.getResult())<20){
//                                    showToast("????????????????????????20???");
//                                    return;
//                                }
//                                b.putString("id",myAlertInputDialog.getResult());
//                                b.putString("name",myAlertInputDialog.getResult()+"???");
//                                intent.putExtras(b);
//                                setResult(RESULT_OK,intent);
//
//                                myAlertInputDialog.dismiss();
//                                finish();
//                            }
//                        }).setNegativeButton("??????", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                myAlertInputDialog.dismiss();
//                            }
//                        });
//                        myAlertInputDialog.show();
//                        return;
//                    }else {
//                        b.putString("name",array.get(position).getPositionName());
//                    }
//                }
//                if (type == AddVisitActivity.TYPE) b.putString("name",array.get(position).getPositionName()); //????????????
//                intent.putExtras(b);
//                setResult(RESULT_OK,intent);
//                finish();
            }
        });
    }

    /**
     * recycler ?????????
     */
    private class RecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private Context mContext;
        private  ArrayList<SelectDataBean> mList;
        private OnItemClick onItemClickListener;

        public RecordListAdapter(Context context, ArrayList<SelectDataBean> list ){
            this.mContext = context;
            this.mList = list;
        }


        public void setList( ArrayList<SelectDataBean> list){
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_select_data,parent,false);
            return new RecordViewHolder(view,onItemClickListener);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof RecordViewHolder){
                initView((RecordViewHolder) holder, mList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private void setOnItemClickListener(OnItemClick listener){
            onItemClickListener = listener;
        }

        private void initView(RecordViewHolder handler, SelectDataBean bean){
            if (bean == null) return;
            if (type == AuthActivity.SEX)handler.dataTV.setText(bean.getSex()); //??????
            if (type == AuthActivity.TYPE)handler.dataTV.setText(bean.getJobName()); //??????
            if (type == AuthActivity.POSITION)handler.dataTV.setText(bean.getIdVal()); //??????
            if (type == AuthActivity.DEPATE)handler.dataTV.setText(bean.getMyName()); //??????
            if (type == AuthActivity.HOSPITAL){
                handler.dataTV.setText(bean.getThisName()); //??????
                handler.contentTV.setText(bean.getAddress()); //??????
                handler.contentTV.setVisibility(View.VISIBLE);
                handler.levelTV.setVisibility(View.VISIBLE);
                String level = bean.getThisLevel();
                if ("1".equals(level)){
                    handler.levelTV.setText("??????"); //??????
                }else if ("2".equals(level)){
                    handler.levelTV.setText("??????"); //??????
                }else if ("3".equals(level)){
                    handler.levelTV.setText("??????"); //??????
                }else if ("4".equals(level)){
                    handler.levelTV.setText("??????"); //??????
                }else if ("5".equals(level)){
                    handler.levelTV.setText("????????????"); //??????
                }else if ("6".equals(level)){
                    handler.levelTV.setText("????????????"); //??????
                }else if ("7".equals(level)){
                    handler.levelTV.setText("????????????"); //??????
                }else{
                    handler.levelTV.setText(""); //??????
                }

            }
//            if (type == AddDrugsActivity.DRUG_TIME)handler.dataTV.setText(bean.getPositionName()); //????????????
//            if (type == AddDrugsActivity.DRUG_EAT)handler.dataTV.setText(bean.getPositionName()); //????????????
//            if (type == SettingPictureActivity.TIME)handler.dataTV.setText(bean.getPositionName()); //????????????
//            if (type == SettingPictureActivity.NUM)handler.dataTV.setText(bean.getPositionName()); //????????????
//            if (type == AddVisitActivity.TYPE)handler.dataTV.setText(bean.getPositionName()); //????????????

        }
    }

    public interface OnItemClick{
        public void onItemClick(View view, int position);
    }

    /**
     * ?????????
     */
    private static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public MyTextView dataTV;
        public MyTextView contentTV;
        public MyTextView levelTV;
        private OnItemClick onItemClick;

        public RecordViewHolder(View view, OnItemClick onItemClick){
            super(view);
            this.onItemClick = onItemClick;
            view.setOnClickListener(this);
            contentTV = (MyTextView) view.findViewById(R.id.list_item_content);
            dataTV = (MyTextView) view.findViewById(R.id.list_item_data);
            levelTV = (MyTextView) view.findViewById(R.id.list_item_level);
        }

        @Override
        public void onClick(View view) {
            if(onItemClick != null){
                onItemClick.onItemClick(view,getLayoutPosition());
            }
        }
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showDialog() {
        if (myDialog == null) myDialog = MyDialog.showLoadingDialog(this);
        myDialog.show();
    }
    @Override
    public void dismissDialog() {
        if (myDialog != null) myDialog.dismiss();
    }

    @Override
    public void showToast(String txt) {
        MyToast.showToast(this,txt, Toast.LENGTH_SHORT);
    }

    @Override
    public void setListData(ArrayList<SelectDataBean> list) {

//        if(type == AuthActivity.DEPATE){
//            if (page == 1){
//                refreshLayout.setRefreshing(false);
//                array = list;
//                adapter.setList(array);
//            }
//
//            if (page > 1){
//                for (SelectDataBean bean:
//                        list) {
//                    array.add(bean);
//                }
//                adapter.setList(array);
//            }
//        }else {
            refreshLayout.setRefreshing(false);
            array = list;
            adapter.setList(array);
//        }

    }
}
