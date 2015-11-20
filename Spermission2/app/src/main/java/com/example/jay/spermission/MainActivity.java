package com.example.jay.spermission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.TabActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost mTab = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(this,FirstTab.class);
        spec = mTab.newTabSpec("FirstTab").setIndicator("권한별")
                .setContent(intent);
        mTab.addTab(spec);

        intent = new Intent(this,SecondTab.class);
        spec = mTab.newTabSpec("SecondTab").setIndicator("어플리케이션별")
                .setContent(intent);
        mTab.addTab(spec);

        intent = new Intent(this,ThirdTab.class);
        spec = mTab.newTabSpec("ThirdTab").setIndicator("권한설명")
                .setContent(intent);
        mTab.addTab(spec);

        intent = new Intent(this,ForthTab.class);
        spec = mTab.newTabSpec("ForthTab").setIndicator("평점")
                .setContent(intent);
        mTab.addTab(spec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

 /*   @Override
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
    }*/
}
