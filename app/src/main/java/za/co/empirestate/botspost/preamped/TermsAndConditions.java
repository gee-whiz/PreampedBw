package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TermsAndConditions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_terms_and_conditions);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        final Activity activity = this;

        findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                onBackPressed();
                finish();
            }
        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebclient());
        myWebView.loadUrl("http://utc.empirestate.co.za/bostwana_post_office/terms.html");
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private class MyWebclient extends WebViewClient{

        /**
         * Give the host application a chance to take over the control when a new
         * url is about to be loaded in the current WebView. If WebViewClient is not
         * provided, by default WebView will ask Activity Manager to choose the
         * proper handler for the url. If WebViewClient is provided, return true
         * means the host application handles the url, while return false means the
         * current WebView handles the url.
         * This method is not called for requests using the POST "method".
         *
         * @param view The WebView that is initiating the callback.
         * @param url The url to be loaded.
         * @return True if the host application wants to leave the current WebView
         *         and handle the url itself, otherwise return false.
         */

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        /**
         * Notify the host application that a page has started loading. This method
         * is called once for each main frame load so a page with iframes or
         * framesets will call onPageStarted one time for the main frame. This also
         * means that onPageStarted will not be called when the contents of an
         * embedded frame changes, i.e. clicking a link whose target is an iframe.
         *
         * @param view The WebView that is initiating the callback.
         * @param url The url to be loaded.
         * @param favicon The favicon for this page if it already exists in the
         *            database.
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        /**
         * Notify the host application that a page has finished loading. This method
         * is called only for main frame. When onPageFinished() is called, the
         * rendering picture may not be updated yet. To get the notification for the
         * new Picture, use {@link WebView.PictureListener#onNewPicture}.
         *
         * @param view The WebView that is initiating the callback.
         * @param url The url of the page.
         */
        @Override
        public void onPageFinished(WebView view, String url) {
        }

    }
}
