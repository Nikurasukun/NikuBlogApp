package xyz.nikurasu.nikublog

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat


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
                if (isChromeInstalledAndVersionGreaterThan65()) {
                    val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
                    builder.setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    val CustomTabsIntent: CustomTabsIntent = builder.build()
                    CustomTabsIntent.launchUrl(this@MainActivity, request.url)
                } else {
                    var url : String = request.url.toString()
                    var intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
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

    private fun isChromeInstalledAndVersionGreaterThan65(): Boolean {
        var pInfo: PackageInfo?
        try {
            pInfo = packageManager.getPackageInfo("com.android.chrome", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        if(pInfo != null) {
            val firstDotIndex: Int = pInfo.versionName.indexOf(".")
            val majorVersion: String = pInfo.versionName.substring(0, firstDotIndex)
            return Integer.parseInt(majorVersion) > 65
        }
        return false
    }
}


