package fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.MsgAdapter;
import adapter.RecyclerViewDivide;
import bwie.com.basemodule.ShowDialog;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/21.
 * Description：
 */

public class MessageFragment extends Fragment {

    private View mRoot;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MsgAdapter adapter;
    private List<Object> list;
    private RecyclerViewDivide divide;
    private String[] titles={"删除对话框","清空聊天记录"};
    private AlertDialog.Builder adb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.msg_layout,container,false);
        }
        ViewGroup parent= (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        initView();
        return mRoot;
    }

    private void initView() {
        list=new ArrayList<>();
        manager = new LinearLayoutManager(getContext());
        recyclerView = mRoot.findViewById(R.id.msg_recy);
        divide = new RecyclerViewDivide(getContext(), LinearLayoutManager.VERTICAL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Map<String,EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        adapter=new MsgAdapter(list,getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(divide);
        recyclerView.setAdapter(adapter);
        adapter.setMsgLongClick(position -> {
            Toast.makeText(App.AppContext, "长按，我是：" + position, Toast.LENGTH_SHORT).show();
            showDialog();
        });
        adapter.setMsgItemClick(position -> Toast.makeText(App.AppContext, "点击，我是："+position, Toast.LENGTH_SHORT).show());
    }

    public void showDialog(){
        adb = new AlertDialog.Builder(getActivity());
        adb.setItems(titles, (dialog, which) -> {
            switch (which){
                case 0:
                    Toast.makeText(App.AppContext, "删除对话框", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(App.AppContext, "删除聊天记录", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        adb.create();
        adb.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager=null;
        if (adb!=null){
            adb=null;
        }
    }
}
