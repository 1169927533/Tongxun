package com.example.a11699.graduatemanager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.lei.chatinformation;
import com.example.a11699.graduatemanager.utils.PopWindow_image;
import com.facebook.drawee.view.DraweeView;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.ImageUtils;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class historyAdapter  extends RecyclerView.Adapter  {
    private static final int TYPE_LEFT=0;//左边item
    private static final int TYPE_RIGHT=1;//右边item
    private Context context;
    List<EMMessage> emMessages;
    private FinalBitmap fb;//显示图片
    private String name;//当前聊天的人
    public historyAdapter(List<EMMessage> emMessages, Context context,String name) {
        this.emMessages = emMessages;
        this.context = context;
        this.fb=FinalBitmap.create(context);
        this.name=name;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==TYPE_LEFT){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leftchat,null);
            leftChatViewholder holder=new leftChatViewholder(view);
            return  holder;
        }else if(i==TYPE_RIGHT){
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rightchat,null);
            rightChatViewHolder holder=new rightChatViewHolder(view);
            return  holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        EMMessage message=emMessages.get(i);
        Log.i("zjc","消息总数"+emMessages.size());
        if(viewHolder instanceof leftChatViewholder){
            if(message.getType()==EMMessage.Type.TXT){
                ((leftChatViewholder)viewHolder).shoudaoMessage.setVisibility(View.VISIBLE);
                String mess=((EMTextMessageBody)message.getBody()).getMessage();
                Log.i("zjc","Adapter答应数据“："+mess);
                ((leftChatViewholder)viewHolder).shoudaoMessage.setText(mess);
            }else if(message.getType()==EMMessage.Type.IMAGE){
                ((leftChatViewholder)viewHolder).shoudaoMessage.setVisibility(View.GONE);
                ((leftChatViewholder)viewHolder).leftimage.setVisibility(View.VISIBLE);
                String ThumbnailUrl = ((EMImageMessageBody) message.getBody()).getThumbnailUrl(); // 获取缩略图片地址
                String thumbnailPath = ImageUtils.getScaledImage(context,ThumbnailUrl);
                String imageRemoteUrl = ((EMImageMessageBody) message.getBody()).getRemoteUrl();// 获取远程原图片地址
                fb.display( ((leftChatViewholder)viewHolder).leftimage,thumbnailPath);//显示图片
                imageClick( ((leftChatViewholder)viewHolder).leftimage, imageRemoteUrl);//图片添加监听
            }

        }else if(viewHolder instanceof  rightChatViewHolder){

            if(message.getType()==EMMessage.Type.TXT) {
                //  ((rightChatViewHolder) viewHolder).fasongmessage.setVisibility(View.VISIBLE);
                String mess = ((EMTextMessageBody) message.getBody()).getMessage();
                Log.i("zjc","在Adapter显示发送的文字数据"+mess);
                ((rightChatViewHolder) viewHolder).fasongmessage.setText(mess);
            }else if (message.getType()==EMMessage.Type.IMAGE){
                ((rightChatViewHolder)viewHolder).fasongmessage.setVisibility(View.GONE);
                ((rightChatViewHolder)viewHolder).rightImage.setVisibility(View.VISIBLE);
                // 自己发的消息
                String LocalUrl = ((EMImageMessageBody) message.getBody()).getLocalUrl(); // 获取本地图片地址
                Bitmap bm = ImageUtils.decodeScaleImage(LocalUrl,160,160);//获取缩略图
                ((rightChatViewHolder)viewHolder).rightImage.setImageBitmap(bm);//显示图片
                Log.e("zjc","bm="+bm+"==LocalUrl="+LocalUrl);
                imageClick(((rightChatViewHolder)viewHolder).rightImage, LocalUrl);//图片添加监听
            }

        }
    }
    private void imageClick(ImageView imageView, final String imageUrl){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new PopWindow_Image(Chat.this, imageUrl).showAtLocation(arg0, 0, 0, 0);
                new PopWindow_image(fb,context,imageUrl).showAtLocation(v,0,0,0);
            }
        });
    }
    @Override
    public int getItemCount() {
        return emMessages.size();
    }
    public class leftChatViewholder extends RecyclerView.ViewHolder{
        TextView shoudaoMessage;
        DraweeView leftimage;
        public leftChatViewholder(@NonNull View itemView) {
            super(itemView);
            shoudaoMessage=itemView.findViewById(R.id.shoudaoMessage);
            leftimage=itemView.findViewById(R.id.leftimage);
        }
    }
    public class rightChatViewHolder extends RecyclerView.ViewHolder{
        TextView fasongmessage;
        DraweeView rightImage;
        public rightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            fasongmessage=itemView.findViewById(R.id.fasongmessage);
            rightImage=itemView.findViewById(R.id.rightImage);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        int sa=emMessages.size();
        Log.i("zjc",sa+"");
        EMMessage message = emMessages.get(position);//每条消息
        if(message.getFrom().equals(name)){
            //是自己发送的消息的时候
            return TYPE_LEFT;
        }else{

            return TYPE_RIGHT;
        }
    }
}
