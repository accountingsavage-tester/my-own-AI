package com.example.apkbuilder;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText appName;
    private EditText packageName;
    private EditText versionName;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 50, 40, 40);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("APK Builder");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        appName = field("App name", "My App");
        packageName = field("Package name", "com.example.myapp");
        versionName = field("Version", "1.0");

        root.addView(appName);
        root.addView(packageName);
        root.addView(versionName);

        Button build = new Button(this);
        build.setText("Generate Build Configuration");
        build.setOnClickListener(v -> Toast.makeText(this,
                "Configuration ready for GitHub Actions: " + appName.getText(),
                Toast.LENGTH_LONG).show());
        root.addView(build, new LinearLayout.LayoutParams(-1, -2));

        TextView info = new TextView(this);
        info.setText("This app is the local companion UI. GitHub Actions performs the actual APK compilation.");
        info.setPadding(0, 30, 0, 0);
        root.addView(info);

        setContentView(root);
    }

    private EditText field(String hint, String value) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setText(value);
        e.setSingleLine(true);
        e.setPadding(0, 20, 0, 20);
        return e;
    }
}
