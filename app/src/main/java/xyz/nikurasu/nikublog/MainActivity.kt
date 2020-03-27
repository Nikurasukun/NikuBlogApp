package xyz.nikurasu.nikublog

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Connect the Webview Element with the Code
        val myWebView: WebView = findViewById(R.id.webview)

        //Hide the Actionbar
        if (supportActionBar != null){
            supportActionBar?.hide()
        }
        myWebView.webViewClient = WebViewClient()

        //Enable JavaScript
        myWebView.settings.javaScriptEnabled = true

        //Load the URL
        myWebView.loadUrl("https://www.nikurasu.xyz")

        //Only load Links with the given Domain in the Webview, open the others in the Browser or in a other App
        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                //Checks if the link has the given Domain
                if (request.url.host.toString() == "www.nikurasu.xyz"){
                    return false
                }
                //if not it starts an implicit intend to open the Link
                var url : String = request.url.toString()
                var intent: Intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
                return true
            }
        }
    }

    //Makes that you can go back in the browsing history
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        val myWebView: WebView = findViewById(R.id.webview)
        if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
            myWebView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }
}


