package com.app.wpf.wfilemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.wpf.filetools.Handler.SendMessage;
import com.app.wpf.filetools.Util.Config;
import com.app.wpf.filetools.Util.FileInfo;
import com.app.wpf.filetools.FileScanThread;
import com.app.wpf.wfilemanager.Adapter.FileListAdapter;
import com.app.wpf.wfilemanager.Permission.PermissionList;
import com.app.wpf.wfilemanager.Utils.ConfigSharePreference;
import com.socks.library.KLog;
import com.wpf.utils.FileCoryThread;
import com.wpf.utils.FileUtils;
import com.wpf.requestpermission.RequestPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private LinearLayout main_view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton button_Add;
    private RequestPermission requestPermission;
    private String rootPath = Environment.getExternalStorageDirectory().getPath() + "/";
    private String curPath = rootPath;
    private boolean[] longClickList;
    private List<File> copyFileList = new ArrayList<>();
    private boolean isFail = false,isClip,isCopy;
    private int scrollPositionX = 0,scrollPositionY = 0, scroll_dx = 0,scroll_dy = 0;
    private List<FileInfo> curFileList = new ArrayList<>();
    private FileListAdapter fileListAdapter = new FileListAdapter() {
        @Override
        public void onItemClick(int position) {
            scrollPositionX = scroll_dx;
            scrollPositionY = scroll_dy;
            if(getLongClickNum() == 0) {
                FileInfo fileInfo = fileListAdapter.getFileList().get(position);
                String filePath = fileInfo.filePath;
                if (fileInfo.isDirectory) doClickIsDir(filePath + "/");
                else doClickIsFile(filePath, fileInfo.fileName);
            } else onItemLongClick(position);
        }

        @Override
        public boolean onItemLongClick(int position) {
            if(longClickList.length != 0 && !(isClip||isCopy)) {
                if(getLongClickNum() == 0) chooseButtonAction();
                longClickList[position] = !longClickList[position];
                int longClickNum = getLongClickNum();
                if(longClickNum == 0) chooseButtonAction();
                setToolbarTitle(longClickNum == 0 ? curPath + "/" :
                        "已选择" + String.valueOf(longClickNum) + "项");
                fileListAdapter.doItemLongClick(position);
                invalidateOptionsMenu();
                return true;
            } else {
                KLog.e("长按错误");
                return false;
            }
        }
    };
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    if(msg.obj instanceof FileInfo) {
                        FileInfo fileInfo = (FileInfo) msg.obj;
                        fileListAdapter.addItem(fileInfo);
                    }
                    break;
                case 0x02:
                    if(msg.obj instanceof List) {
                        curFileList = (List<FileInfo>) msg.obj;
                        longClickList = new boolean[curFileList.size()];
                        //notifyList(fileInfos);
                        recyclerView.scrollTo(scrollPositionX,scrollPositionY);
                    }
                    break;
                case 0x03:
                    copyFileList.clear();
                    invalidateOptionsMenu();
                    pushFinish();
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initTool();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Config.canScanHideFile = ConfigSharePreference.getBooleanYC(this);
        Config.preferentialDisplayFolder = ConfigSharePreference.getBooleanYX(this);
        if(!isFail)
            requestPermission = new RequestPermission(this, PermissionList.permissionList, 1) {
                @Override
                public void onSuccess() {
                    //TODO  当从隐藏目录退出时需要返回根目录
                    scan(curPath);
                }

                @Override
                public void onFail(String[] strings) {
                    isFail = true;
                    KLog.d("用户拒绝");
                }
            };
    }

    private void init() {
        main_view = (LinearLayout) findViewById(R.id.main_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(fileListAdapter, recyclerView);
        recyclerView.setAdapter(animatorAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scroll_dx += dx;
                scroll_dy += dy;
            }
        });
        button_Add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        button_Add.setOnClickListener(this);
        button_Add.setTag("Add");
    }

    private void initTool() {
        toolbar.setTitle("/sdcard");
        setSupportActionBar(toolbar);
        getSupportActionBar().setShowHideAnimationEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((getLongClickNum() == 0 && copyFileList.isEmpty())?
                R.menu.menu_main :
                copyFileList.isEmpty()?
                R.menu.menu_main_delete:
                R.menu.menu_copy,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            case R.id.action_set:
                startActivity(new Intent(this,SetActivity.class));
                break;
            case R.id.action_selectAll:
                if(getLongClickNum() != longClickList.length)
                    selectAll();
                else
                    clearSelectAll();
                break;
            case R.id.action_clip:
                clip();
                break;
            case R.id.action_copy:
                copy();
                break;
            case R.id.action_push:
                push();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void selectAll() {
        for(int i=0;i<longClickList.length;++i) {
            if(!longClickList[i]) {
                longClickList[i] = true;
                fileListAdapter.doItemLongClick(i);
            }
        }
        setToolbarTitle("已选择" + String.valueOf(longClickList.length) + "项");
    }

    private void clearSelectAll() {
        clearLongClick();
        setToolbarTitle(curPath);
        SendMessage.send(handler,0x02,curFileList,0,0);
        invalidateOptionsMenu();
    }

    private void clip() {
        copy();
        isClip = true;
    }

    private void copy() {
        copyFileList = getCoayFile();
        clearSelectAll();
        isCopy = true;
    }

    private void push() {
        new FileCoryThread(copyFileList,curPath,isClip) {

            @Override
            public void onFinish(boolean success) {
                SendMessage.send(handler,0x03,null,0,0);
            }
        }.start();
    }

    private void pushFinish() {
        scan(curPath);
        copyFileList.clear();
        isCopy = isClip = false;
        button_Add.setVisibility(View.INVISIBLE);
    }

    private List<File> getCoayFile() {
        List<File> files = new ArrayList<>();
        for(int i = 0;i<longClickList.length;++i) {
            if(longClickList[i]) files.add(new File(curFileList.get(i).filePath));
        }
        return files;
    }

    private void setToolbarTitle(String title) {
        if(title.equals(rootPath))
            getSupportActionBar().setTitle("/");
        else
            getSupportActionBar().setTitle(title.replace(rootPath,"/"));
    }

    private void scan(String strPath) {
        fileListAdapter.clearAll();
        curPath = strPath;
        setToolbarTitle(curPath);
        new FileScanThread(strPath) {
            @Override
            public void addItem(FileInfo fileInfo) {
                SendMessage.send(handler,0x01,fileInfo,0,0);
            }

            @Override
            public void onFinish() {
                SendMessage.send(handler,0x02,getFileInfoList(),0,0);
            }
        }.start();
    }

    private void notifyList(List<FileInfo> fileInfos) {
        fileListAdapter.setFileList(fileInfos);
    }

    private String getLastPath(String filePath) {
        if(rootPath.equals(filePath))
            return rootPath;
        if("/".equals(filePath)) filePath = "/storage/";
        String lastPath = new File(filePath).getParent();
        return lastPath == null ? "/" : lastPath+"/";
    }

    private void doClickIsDir(String filePath) {
        scan(filePath);
    }

    private void doClickIsFile(String filePath,String fileName) {

    }

    private int getLongClickNum() {
        int i = 0;
        if(longClickList == null) return i;
        for(boolean b : longClickList) if(b) i++;
        return i;
    }

    private void clearLongClick() {
        for(int i=0;i<longClickList.length;++i) longClickList[i] = false;
        fileListAdapter.clearItemClick();
        chooseButtonAction();
    }

    @Override
    public void onBackPressed() {
        if(getLongClickNum() == 0 && copyFileList.isEmpty()) {
            if (!curPath.equals(getLastPath(curPath))) {
                scan(getLastPath(curPath));
            }
            else super.onBackPressed();
        } else {
            clearSelectAll();
            pushFinish();
        }
    }

    private void chooseButtonAction() {
        switch ((String) button_Add.getTag()) {
            case "Add":
                button_Add.startAnimation(AnimationUtils.loadAnimation(this,R.anim.add_delete));
                button_Add.setTag("Del");
                break;
            case "Del":
                button_Add.startAnimation(AnimationUtils.loadAnimation(this,R.anim.delete_add));
                button_Add.setTag("Add");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                switch ((String) button_Add.getTag()) {
                    case "Add":
                        showAddDialog();
                        break;
                    case "Del":
                        showDelDialog();
                        break;
                }
                break;
        }
    }

    private void showAddDialog() {
        new AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("请选择")
                .setItems(new CharSequence[]{"文件","文件夹"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showInputName(i);
                    }
                }).show();
    }

    private void showInputName(final int type) {
        final EditText editText = new EditText(this);
        editText.setTextColor(Color.GRAY);
        new AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("请输入名称")
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean addSuccess = false;
                        if(type == 0)
                            addSuccess = FileUtils.addFile(curPath+"/",editText.getText().toString());
                        else if(type == 1)
                            addSuccess = FileUtils.addDir(curPath +"/"+ editText.getText().toString());
                        Toast.makeText(MainActivity.this,"新建文件"+(type==0?"":"夹")+(addSuccess?"成功":"失败"),Toast.LENGTH_LONG).show();
                        if(addSuccess) scan(curPath);
                    }
                }).show();
    }

    private void showDelDialog() {
        new AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("警告")
                .setMessage("确认删除?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                    }
                }).show();
    }

    private void delete() {
        for(int i = 0; i< longClickList.length;++i) {
            if(longClickList[i]) FileUtils.delete(fileListAdapter.getFileList().get(i).filePath);
        }
        scan(curPath);
        chooseButtonAction();
    }

    private void showSelectView() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}