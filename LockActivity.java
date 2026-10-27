package com.example.applocker;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.*;
import android.view.Gravity;
import android.view.WindowManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.CancellationSignal;
import java.util.concurrent.Executor;

public class LockActivity extends Activity {
    EditText pin; String target;
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);target=getIntent().getStringExtra("target");
        SharedPreferences prefs=getSharedPreferences("locker",MODE_PRIVATE);
        LinearLayout l=new LinearLayout(this);l.setOrientation(1);l.setGravity(Gravity.CENTER);l.setPadding(28,28,28,28);l.setBackgroundColor(0xFF101828);setContentView(l);
        TextView t=new TextView(this);t.setText("🔒 App Locked");t.setTextColor(0xFFFFFFFF);t.setTextSize(28);t.setGravity(Gravity.CENTER);l.addView(t);
        pin=new EditText(this);pin.setSingleLine(true);pin.setInputType(2|128);pin.setHint("Enter PIN");l.addView(pin);
        Button unlock=new Button(this);unlock.setText("UNLOCK");l.addView(unlock);unlock.setOnClickListener(v->{if(prefs.getString("pin","").equals(pin.getText().toString())) { prefs.edit().putString("unlockedPackage", target == null ? "" : target).putLong("unlockedAt", System.currentTimeMillis()).apply(); finish(); } else Toast.makeText(this,"Wrong PIN",Toast.LENGTH_SHORT).show();});
        Button bio=new Button(this);bio.setText("USE FINGERPRINT / BIOMETRICS");l.addView(bio);bio.setOnClickListener(v->showBiometric());
    }
    void showBiometric(){
        if(android.os.Build.VERSION.SDK_INT>=28){
            try { BiometricPrompt prompt=new BiometricPrompt.Builder(this).setTitle("Unlock App").setSubtitle("Verify your identity").setNegativeButton("Use PIN",getMainExecutor(),(d,w)->{}).build(); prompt.authenticate(new CancellationSignal(),getMainExecutor(),new BiometricPrompt.AuthenticationCallback(){@Override public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result){runOnUiThread(()->{getSharedPreferences("locker",MODE_PRIVATE).edit().putString("unlockedPackage", target == null ? "" : target).putLong("unlockedAt", System.currentTimeMillis()).apply();finish();});}}); }
            catch(Exception e){Toast.makeText(this,"Biometric authentication unavailable; use PIN",Toast.LENGTH_SHORT).show();}
        } else Toast.makeText(this,"Biometric prompt needs Android 9 or newer; use PIN",Toast.LENGTH_SHORT).show();
    }
}
