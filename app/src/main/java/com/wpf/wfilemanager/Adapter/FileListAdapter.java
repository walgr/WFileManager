package com.wpf.wfilemanager.Adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpf.filetools.Util.FileInfo;
import com.wpf.wfilemanager.R;
import com.wpf.wfilemanager.Utils.Get;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wazsj on 6-26-0026.
 * 文件列表适配器
 */

public abstract class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private Context context;
    private List<FileInfo> fileList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.fileview,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo fileInfo = fileList.get(position);
        holder.imageView_file.setImageDrawable((fileInfo.isLongClick)?
                context.getResources().getDrawable(R.drawable.ic_done):
                Get.getTypeImage(context,fileInfo.fileImageType));
        holder.textView_name.setText(fileInfo.fileName);
        holder.textView_lasttime.setText(sf.format(fileInfo.fileLastTime));
        if(!fileInfo.isDirectory) {
            holder.textView_size.setText(Get.getFileSize(fileInfo.fileSize));
        } else {
            holder.textView_size.setText(String.format("%s 项", String.valueOf(fileInfo.fileNum)));
        }
        holder.setBackground(context.getResources().getDrawable(fileInfo.isLongClick
                ? R.drawable.back_itemclick: R.drawable.back_itemnoclick));
        if(fileInfo.isLongClick) startAnimator(holder.imageView_file);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener ,
            View.OnLongClickListener {

        ImageView imageView_file;
//        FloatingActionButton imageView_file;
        TextView textView_name,textView_lasttime,textView_size;

        ViewHolder(View itemView) {
            super(itemView);
//            imageView_file = (FloatingActionButton) itemView.findViewById(R.id.imageButton_file);
            imageView_file = (ImageView) itemView.findViewById(R.id.imageButton_file);
            textView_name = (TextView) itemView.findViewById(R.id.textView_file);
            textView_lasttime = (TextView) itemView.findViewById(R.id.textView_lasttime);
            textView_size = (TextView) itemView.findViewById(R.id.textView_size);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            setAllTextColor();
        }

        void setBackground(Drawable drawable) {
            itemView.setBackground(drawable);
        }

        void setAllTextColor() {
            textView_name.setTextColor(Color.GRAY);
            textView_lasttime.setTextColor(Color.GRAY);
            textView_size.setTextColor(Color.GRAY);
        }

        @Override
        public void onClick(View view) {
            onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return onItemLongClick(getAdapterPosition());
        }
    }

    public void addItem(FileInfo fileInfo) {
        fileList.add(fileInfo);
        notifyItemInserted(fileList.size());
    }

    public void doItemLongClick(int position) {
        fileList.get(position).isLongClick = !fileList.get(position).isLongClick;
        notifyItemChanged(position);
    }

    private void startAnimator(View view) {
        Animator animator = AnimatorInflater.loadAnimator(context,R.animator.longclick);
        animator.setTarget(view);
        animator.start();
    }

    public void clearItemClick() {
        for(int i = 0;i<fileList.size();++i) {
            if(fileList.get(i).isLongClick) {
                fileList.get(i).isLongClick = false;
                notifyItemChanged(i);
            }
        }
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    public void clearAll() {
        setFileList(new ArrayList<FileInfo>());
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public abstract void onItemClick(int position);

    public abstract boolean onItemLongClick(int position);
}
