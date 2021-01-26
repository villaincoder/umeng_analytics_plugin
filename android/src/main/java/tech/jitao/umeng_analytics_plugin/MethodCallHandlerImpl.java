package tech.jitao.umeng_analytics_plugin;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
    private final MethodChannel channel;
    private final Context context;

    MethodCallHandlerImpl(MethodChannel channel, Context context) {
        this.channel = channel;
        this.context = context;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        switch (call.method) {
            case "preInit":
                preInit(call, result);
                break;
            case "init":
                init(call, result);
                break;
            case "pageStart":
                pageStart(call, result);
                break;
            case "pageEnd":
                pageEnd(call, result);
                break;
            case "event":
                event(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    private void preInit(MethodCall call, MethodChannel.Result result) {
        Boolean logEnabled = call.argument("logEnabled");
        if (logEnabled == null) {
            logEnabled = false;
        }
        UMConfigure.setLogEnabled(logEnabled);

        Boolean encryptEnabled = call.argument("encryptEnabled");
        if (encryptEnabled == null) {
            encryptEnabled = false;
        }
        UMConfigure.setEncryptEnabled(encryptEnabled);

        final String androidKey = call.argument("androidKey");
        final String channel = call.argument("channel");
        UMConfigure.preInit(context, androidKey, channel);

        Integer sessionContinueMillis = call.argument("sessionContinueMillis");
        if (sessionContinueMillis == null) {
            sessionContinueMillis = 30000;
        }
        MobclickAgent.setSessionContinueMillis(sessionContinueMillis);

        Boolean catchUncaughtExceptions = call.argument("catchUncaughtExceptions");
        if (catchUncaughtExceptions == null) {
            catchUncaughtExceptions = true;
        }
        MobclickAgent.setCatchUncaughtExceptions(catchUncaughtExceptions);

        if ("MANUAL".equals(call.argument("pageCollectionMode"))) {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        } else {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }

        result.success(true);
    }

    private void init(MethodCall call, MethodChannel.Result result) {
        Boolean logEnabled = call.argument("logEnabled");
        if (logEnabled == null) {
            logEnabled = false;
        }
        UMConfigure.setLogEnabled(logEnabled);

        Boolean encryptEnabled = call.argument("encryptEnabled");
        if (encryptEnabled == null) {
            encryptEnabled = false;
        }
        UMConfigure.setEncryptEnabled(encryptEnabled);

        final String androidKey = call.argument("androidKey");
        final String channel = call.argument("channel");
        UMConfigure.init(context, androidKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);

        Integer sessionContinueMillis = call.argument("sessionContinueMillis");
        if (sessionContinueMillis == null) {
            sessionContinueMillis = 30000;
        }
        MobclickAgent.setSessionContinueMillis(sessionContinueMillis);

        Boolean catchUncaughtExceptions = call.argument("catchUncaughtExceptions");
        if (catchUncaughtExceptions == null) {
            catchUncaughtExceptions = true;
        }
        MobclickAgent.setCatchUncaughtExceptions(catchUncaughtExceptions);

        if ("MANUAL".equals(call.argument("pageCollectionMode"))) {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        } else {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }

        result.success(true);
    }

    private void pageStart(MethodCall call, MethodChannel.Result result) {
        final String viewName = call.argument("viewName");

        MobclickAgent.onPageStart(viewName);

        result.success(true);
    }

    private void pageEnd(MethodCall call, MethodChannel.Result result) {
        final String viewName = call.argument("viewName");

        MobclickAgent.onPageEnd(viewName);

        result.success(true);
    }

    private void event(MethodCall call, MethodChannel.Result result) {
        final String eventId = call.argument("eventId");
        final String label = call.argument("label");
        final Map<String, String> params = call.argument("params");

        if (params!=null && !params.isEmpty()) {
            MobclickAgent.onEvent(context, eventId, params);
        } else if (TextUtils.isEmpty(label)) {
            MobclickAgent.onEvent(context, eventId);
        } else {
            MobclickAgent.onEvent(context, eventId, label);
        }

        result.success(true);
    }
}
