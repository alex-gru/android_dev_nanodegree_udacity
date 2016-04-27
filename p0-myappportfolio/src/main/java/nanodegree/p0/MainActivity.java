package nanodegree.p0;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void launchPopMovies(View view) throws Exception {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("nanodegree.p1p2", "nanodegree.p1p2.MainActivity"));
        startActivity(intent);
    }
    public void launchStockHawk(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.example.sam_chordas.stockhawk","com.sam_chordas.android.stockhawk.ui.MyStocksActivity"));
        startActivity(intent);
    }
    public void launchLibraryApp(View view) throws Exception {
        String text = "This button will launch " + ((Button) view).getText();
        Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        //TODO: launch app
    }
    public void launchBuildItBigger(View view) throws Exception {
        String text = "This button will launch " + ((Button) view).getText();
        Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        //TODO: launch app
    }
    public void launchXYZReader(View view) throws Exception {
        String text = "This button will launch " + ((Button) view).getText();
        Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        //TODO: launch app
    }
    public void launchCapstone(View view) throws Exception {
        String text = "This button will launch " + ((Button) view).getText();
        Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        //TODO: launch app
    }
}
