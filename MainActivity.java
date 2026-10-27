package com.example.applocker;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.*;

public class MainActivity extends Activity {
    SharedPreferences prefs;
    LinearLayout root;
    EditText pin;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("locker", MODE_PRIVATE);
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(24,24,24,24); root.setBackgroundColor(0xFF101828);
        ScrollView scroll = new ScrollView(this); scroll.addView(root); setContentView(scroll);
        TextView title = text("🔐 APP LOCKER", 28, 0xFFFFFFFF); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView desc = text("Set a PIN, choose apps, then enable Accessibility service.", 15, 0xFFCCD6E0); root.addView(desc);
        pin = new EditText(this); pin.setSingleLine(true); pin.setInputType(2 | 128); pin.setHint("Create / change PIN (4+ digits)"); root.addView(pin);
        Button save = button("SAVE PIN"); root.addView(save); save.setOnClickListener(v -> { String p=pin.getText().toString(); if(p.length()<4){toast("PIN must be at least 4 digits");return;} prefs.edit().putString("pin",p).apply(); pin.setText(""); toast("PIN saved"); });
        Button accessibility = button("ENABLE APP MONITORING"); root.addView(accessibility); accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        TextView appsTitle=text("Choose apps to lock",20,0xFFFFFFFF); root.addView(appsTitle);
        Button refresh=button("REFRESH APP LIST"); root.addView(refresh); refresh.setOnClickListener(v -> recreate());
        List<ApplicationInfo> apps = new ArrayList<>();
        PackageManager pm=getPackageManager();
        for(ApplicationInfo ai:pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
            if(!ai.packageName.equals(getPackageName()) && pm.getLaunchIntentForPackage(ai.packageName)!=null) apps.add(ai);
        }
        Collections.sort(apps,(a,c)->pm.getApplicationLabel(a).toString().compareToIgnoreCase(pm.getApplicationLabel(c).toString()));
        Set<String> selected=new HashSet<>(prefs.getStringSet("locked", new HashSet<>()));
        for(ApplicationInfo ai:apps){ CheckBox cb=new CheckBox(this); cb.setText(pm.getApplicationLabel(ai)); cb.setTextColor(0xFFFFFFFF); cb.setTag(ai.packageName); cb.setChecked(selected.contains(ai.packageName)); root.addView(cb); cb.setOnCheckedChangeListener((button,checked)->{ Set<String> s=new HashSet<>(prefs.getStringSet("locked",new HashSet<>())); if(checked)s.add(ai.packageName);else s.remove(ai.packageName);prefs.edit().putStringSet("locked",s).apply(); }); }
        TextView note=text("Note: Android requires you to manually enable this app's Accessibility service in Settings. Fingerprint unlock is available only when your device has biometric authentication enrolled.",14,0xFFFFD166); root.addView(note);
    }
    TextView text(String s,int size,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setPadding(0,12,0,12);return t;}
    Button button(String s){Button b=new Button(this);b.setText(s);return b;}
    void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}
