package com.app.wpf.filetools.Handler;

import android.os.Handler;
import android.os.Message;

/**
 * Created by wazsj on 6-25-0025.
 * 发送消息
 */

public class SendMessage {

    public static void send(Handler handler,int what,Object obj, int arg1, int arg2) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        message.arg1 = arg1;
        message.arg2 = arg2;
        handler.sendMessage(message);
    }
}
