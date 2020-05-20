package thang.daovanbao.readnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Intent intent = getIntent();
        String link = intent.getStringExtra("linkNew");
        WebView webView = findViewById(R.id.webviewNew);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}
