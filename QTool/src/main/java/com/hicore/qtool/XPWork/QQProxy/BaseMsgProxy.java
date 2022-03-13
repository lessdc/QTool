package com.hicore.qtool.XPWork.QQProxy;

import com.hicore.HookItem;
import com.hicore.LogUtils.LogUtils;
import com.hicore.ReflectUtils.MField;
import com.hicore.ReflectUtils.XPBridge;
import com.hicore.ReflectUtils.MClass;
import com.hicore.ReflectUtils.MMethod;
import com.hicore.Utils.Utils;
import com.hicore.qtool.JavaPlugin.Controller.PluginMessageProcessor;
import com.hicore.qtool.XposedInit.ItemLoader.BaseHookItem;

import java.lang.reflect.Method;

@HookItem(isRunInAllProc = false,isDelayInit = false)
public class BaseMsgProxy extends BaseHookItem {
    private static final String TAG = "BaseMsgProxy";
    private static final long StartTime = System.currentTimeMillis();
    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public boolean startHook() {
        XPBridge.HookBefore(getMethod(),param -> {
            Object MessageRecord = param.args[0];
            if (System.currentTimeMillis() - StartTime < 10 * 1000)return;
            PluginMessageProcessor.submit(()->PluginMessageProcessor.onMessage(MessageRecord));

        });
        return true;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public boolean check() {
        return getMethod() != null;
    }
    public Method getMethod(){
        Method m = MMethod.FindMethod(MClass.loadClass("com.tencent.imcore.message.BaseMessageManager"),"a",void.class,new Class[]{
                MClass.loadClass("com.tencent.mobileqq.data.MessageRecord"),
                MClass.loadClass("com.tencent.mobileqq.persistence.EntityManager"),
                boolean.class,boolean.class,boolean.class,boolean.class,
                MClass.loadClass("com.tencent.imcore.message.BaseMessageManager$AddMessageContext")
        });
        return m;
    }
}