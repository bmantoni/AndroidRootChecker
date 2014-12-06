package com.example.bobby.rootchecker;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    private interface RootCheck {
        String name();
        boolean foundRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final ArrayList<RootCheckResult> results = CheckForRoot();
        final RootCheckArrayAdapter a = new RootCheckArrayAdapter(this, results);

        ListView lv = (ListView)findViewById(R.id.checksListView);
        lv.setAdapter(a);
    }

    private ArrayList<RootCheckResult> CheckForRoot() {
        List<RootCheck> checks = LoadRootChecks();
        ArrayList<RootCheckResult> results = new ArrayList<RootCheckResult>();

        for(RootCheck c : checks) {
            results.add(new RootCheckResult(c.name(), !c.foundRoot()));
        }

        return results;
    }

    private List<RootCheck> LoadRootChecks() {
        List<RootCheck> checks = new ArrayList<RootCheck>();

        // handled by RootCloak OK
        checks.add(new RootCheck() {
            @Override
            public String name() {
                return "Runtime.getRuntime ().exec ( \"su\" ) throws exception";
            }

            @Override
            public boolean foundRoot() {
                Boolean isRooted = true;

                try {
                    Process proc = Runtime.getRuntime ().exec ( "su" );
                } catch (IOException e) {
                    isRooted = false;
                }
                return isRooted;
            }
        });

        checks.add(new RootCheck() {
            @Override
            public String name() {
                return "buildtags doesn't contain test-keys";
            }

            // handled by RootCloak OK
            @Override
            public boolean foundRoot() {
                String buildTags = android.os.Build.TAGS;
                return buildTags != null && buildTags.contains("test-keys");
            }
        });

        checks.add(new RootCheck() {
            @Override
            public String name() {
                return "Superuser apk file not found";
            }

            @Override
            public boolean foundRoot() {
                try {
                    File file = new File("/system/app/Superuser.apk");
                    return file.exists();
                } catch (Exception e) { return false; }
            }
        });

        checks.add(new RootCheck() {
            @Override
            public String name() {
                return "getRuntime().exec(\"su\") reading inputstream throws exception";
            }

            @Override
            public boolean foundRoot() {
                String res = exec("su");
                if (res != null) {
                    return true;
                }
                return false;
            }
        });

        checks.add(new RootCheck() {
            @Override
            public String name() {
                return "USB DEBUGGING not enabled (not a 'root' check per se)";
            }

            @Override
            public boolean foundRoot() {
                return Settings.Secure.getInt(MyActivity.this.getContentResolver(), Settings.Global.ADB_ENABLED, 0) == 1;
            }
        });

        return checks;
    }

    public static String exec(String cmd)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            String response = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.length() > 0)
                    response = response + line + '\n';
            }

            return response;
        }
        catch (Exception e)
        {
        }

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
