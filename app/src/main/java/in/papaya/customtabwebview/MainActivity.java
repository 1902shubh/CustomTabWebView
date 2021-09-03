package in.papaya.customtabwebview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    public ActionBar actionBar;
    private Toolbar toolbar;
    private String url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.url = "https://papayacoders.in/";
        initComponent();
        initToolbar();
        loadWebFromUrl();
    }

    private void initComponent() {
//        this.appbar_layout = (AppBarLayout) findViewById(C1820R.C1823id.appbar_layout);
        this.webView = (WebView) findViewById(R.id.webview);

    }

    private void initToolbar() {
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
//        toolbar2.setNavigationIcon((int) C1820R.C1822drawable.ic_arrow_back);
//        this.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(C1820R.C1821color.grey_80), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(this.toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        this.actionBar = supportActionBar;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeButtonEnabled(true);
        this.actionBar.setTitle((CharSequence) null);
//
//        Tools.changeOverflowMenuIconColor(this.toolbar, getResources().getColor(C1820R.C1821color.grey_80));
//        Tools.setSystemBarColor(this, 17170443);
//        Tools.setSystemBarLight(this);
    }

    private void loadWebFromUrl() {
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        this.webView.getSettings().setDefaultTextEncodingName("utf-8");
        this.webView.setBackgroundColor(0);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                MainActivity.this.actionBar.setTitle((CharSequence) null);
                MainActivity.this.actionBar.setSubtitle(getHostName(str));
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                MainActivity.this.actionBar.setTitle((CharSequence) webView.getTitle());
            }
        });
        this.webView.loadUrl(this.url);
        this.webView.setWebChromeClient(new MyChromeClient());
    }

    private String getHostName(String str) {
        try {
            String host = new URI(str).getHost();
            if (host.startsWith("www.")) {
                return host;
            }
            return "www." + host;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return str;
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == ) {
//            onBackChecker();
//        } else
//            if (menuItem.getItemId() == C1820R.C1823id.action_refresh) {
//            loadWebFromUrl();
//        } else
        if (menuItem.getItemId() == R.id.openBrowser) {
            directLinkToBrowser(this, this.url);
        } else if (menuItem.getItemId() == R.id.copyLink) {
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", this.webView.getUrl()));
            Toast.makeText(this, "Url Copied", Toast.LENGTH_SHORT).show();
        }
//            else if (menuItem.getItemId() == C1820R.C1823id.action_back) {
//            onBackPressed();
//        } else if (menuItem.getItemId() == C1820R.C1823id.action_forward && this.webView.canGoForward()) {
//            this.webView.goForward();
//        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void directLinkToBrowser(Activity activity, String str) {
        if (!URLUtil.isValidUrl(str)) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_SHORT).show();
        } else {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.otion_menu, menu);
        return true;
    }


    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            onBackChecker();
        }
    }

    private void onBackChecker() {
//        if (this.from_notif.booleanValue()) {
//            Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
//            intent.addFlags(67108864);
//            startActivity(intent);
//            finish();
//            return;
//        }
        super.onBackPressed();
    }

    private class MyChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChromeClient() {
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
//            MainActivity.this.progressBar.setProgress(i + 10);
            if (i >= 100) {
                MainActivity.this.actionBar.setTitle((CharSequence) webView.getTitle());
            }
        }

        public Bitmap getDefaultVideoPoster() {
            if (this.mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(MainActivity.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            MainActivity.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
//            MainActivity.this.appbar_layout.setVisibility(0);
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = view;
            view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            this.mOriginalSystemUiVisibility = MainActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = MainActivity.this.getRequestedOrientation();
            this.mCustomViewCallback = customViewCallback;
            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(3846);
//            MainActivity.this.appbar_layout.setVisibility(8);
        }
    }

    public void onResume() {
        this.webView.onResume();
        super.onResume();
    }

    public void onPause() {
        this.webView.onPause();
        super.onPause();
    }
}