package com.example.applocker;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Set;

public class AppMonitorService extends AccessibilityService {
    private String lastPackage="";
    private long lastLaunch=0;
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event==null || event.getEventType()!=AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getPackageName()==null) return;
        String pkg=event.getPackageName().toString();
        if(pkg.equals(getPackageName())) { lastPackage=pkg; return; }
        SharedPreferences p=getSharedPreferences("locker",MODE_PRIVATE);
        Set<String> locked=p.getStringSet("locked",null);
        if(locked==null || !locked.contains(pkg) || p.getString("pin","").isEmpty()) return;
        long now=System.currentTimeMillis();
        if(pkg.equals(p.getString("unlockedPackage", "")) && now-p.getLong("unlockedAt", 0L)<5000) return;
        if(pkg.equals(lastPackage) && now-lastLaunch<1500) return;
        lastPackage=pkg; lastLaunch=now;
        Intent i=new Intent(this,LockActivity.class); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP); i.putExtra("target",pkg); startActivity(i);
    }
    @Override public void onInterrupt() { }
}
