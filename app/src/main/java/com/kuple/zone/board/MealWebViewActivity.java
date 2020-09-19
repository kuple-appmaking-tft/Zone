package com.kuple.zone.board;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.kuple.zone.R;

public class MealWebViewActivity extends AppCompatActivity {
    private WebView nWebView;           //웹뷰 선언
    private WebSettings nWebSettings;   //웹뷰 설정

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_web_view);

        nWebView = (WebView) findViewById(R.id.mealWebView);

        nWebView.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.
        nWebSettings = nWebView.getSettings(); // 웹뷰에서 webSettings를 사용할 수 있도록 함.
        nWebSettings.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
        nWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
        nWebSettings.setAllowFileAccess(true); //파일 엑세스
        nWebSettings.setLoadWithOverviewMode(true); // 메타태그
        nWebSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
        nWebSettings.setSupportZoom(true); // 화면 줌 사용 여부
        nWebSettings.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        nWebSettings.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        nWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
        nWebSettings.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자

        nWebView.loadUrl("https://portal.korea.ac.kr/");
    }
}