package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.favorite.DatabaseHelper;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Web extends AppCompatActivity {
    Activity activity;
    CompoundIconTextView compoundIconTextView;
    FloatingActionButton floatingActionButton, floatingActionButton1;
    String devName, qrCodeLink, qrCodeTitle, qrCodeId, githubLink, license, licenseLink, title, model, versionRelease, versionName, id, cardBackground, youtubeLink, fullName;
    int version, versioncode;
    WebView webView;
    DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    private InterstitialAd githubInterstitialAd, youtubeInterstitialAd, backButtonInterstitialAd;
    Boolean goBack;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"SetJavaScriptEnabled", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_webview);

        databaseHelper = new DatabaseHelper(this);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        compoundIconTextView = findViewById(R.id.noInternet);

        activity = this;

        progressDialog = new ProgressDialog(Web.this, R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        youtubeLink = getIntent().getExtras().getString("youtubeLink");
        cardBackground = getIntent().getExtras().getString("cardbg");
        githubLink = getIntent().getExtras().getString("githubLink");
        title = getIntent().getExtras().getString("dependencyName");
        devName = getIntent().getExtras().getString("developerName");
        license = getIntent().getExtras().getString("license");
        licenseLink = getIntent().getExtras().getString("licenseLink");
        id = getIntent().getExtras().getString("id");
        fullName = getIntent().getExtras().getString("fullName");
        model = Build.MODEL;
        version = Build.VERSION.SDK_INT;
        versionRelease = Build.VERSION.RELEASE;
        versionName = BuildConfig.VERSION_NAME;
        versioncode = BuildConfig.VERSION_CODE;

        qrCodeLink = githubLink;
        qrCodeId = id;
        qrCodeTitle = title;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView webAdView = findViewById(R.id.adWeb);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        webAdView.loadAd(adRequest1);

        webAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }
        });
        //Banner Ad End

        // Interstitial Ad start GitHub Icon
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequestGithub = new AdRequest.Builder().build();

        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/3494136300", adRequestGithub, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        githubInterstitialAd = interstitialAd;

                        githubInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage("com.github.android");
                                if (launchIntent != null) {
                                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.setPackage("com.github.android");
                                    getApplication().startActivity(intent1);
                                } else {
                                    openCustomTabs(getApplication().getApplicationContext(), githubLink);
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage("com.github.android");
                                if (launchIntent != null) {
                                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.setPackage("com.github.android");
                                    getApplication().startActivity(intent1);
                                } else {
                                    openCustomTabs(getApplication().getApplicationContext(), githubLink);
                                }
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        githubInterstitialAd = null;
                    }
                });
        // Interstitial Ad End GitHub Icon

        // Interstitial Ad start YouTube Icon
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequestYoutube = new AdRequest.Builder().build();

        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/1476337817", adRequestYoutube, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        youtubeInterstitialAd = interstitialAd;

                        youtubeInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        youtubeInterstitialAd = null;
                    }
                });
        // Interstitial Ad End YouTube Icon

        // Interstitial Ad back pressed
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adBackButton = new AdRequest.Builder().build();

        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/4429511003", adBackButton, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        backButtonInterstitialAd = interstitialAd;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                webView.clearCache(true);
                                if (goBack) {
                                    Web.super.onBackPressed();
                                } else {
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                webView.clearCache(true);
                                if (goBack) {
                                    Web.super.onBackPressed();
                                } else {
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                    }
                });

        floatingActionButton = findViewById(R.id.github);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                githubInterstitialAd.show(Web.this);
            }
        });

        floatingActionButton1 = findViewById(R.id.youtube);

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtubeInterstitialAd.show(Web.this);
                }
            });
            floatingActionButton1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(28);
                    ClipboardManager clipboard1 = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("DepHub", youtubeLink);
                    clipboard1.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Link copied", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbarWeb);
        AppBarLayout appBarLayout = findViewById(R.id.appbarWeb);
        toolbar.setTitle(title);
        toolbar.setSubtitle(devName);

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Web.this, "Dependency Id : " + id, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getOverflowIcon();
        //noinspection ConstantConditions
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView = findViewById(R.id.webViewActivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.hide();
        }

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.show();
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshWeb);
        swipeRefreshLayout.setColorSchemeResources(R.color.blacktowhite);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show();
                view.loadUrl(githubLink);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                progressDialog.cancel();
                webView.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
                floatingActionButton1.setVisibility(View.INVISIBLE);
                compoundIconTextView.setVisibility(View.VISIBLE);
                webView.setBackgroundColor(Color.WHITE);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compoundIconTextView.setVisibility(View.INVISIBLE);
                                webView.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                floatingActionButton.setVisibility(View.VISIBLE);
                                floatingActionButton1.setVisibility(View.VISIBLE);
                                webView.reload();
                            }
                        }, 3000);

                    }
                });
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.cancel();
            }
        });
        webView.loadUrl(githubLink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.reload();
                    }

                }, 3000);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.webview_settings, menu);

        Cursor cursor = databaseHelper.getFavorite();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String databaseId = cursor.getString(1);

                if (databaseId.equals(id)) {
                    menu.findItem(R.id.addtofavorite).setTitle("Remove From Favorites");
                } else {
                    menu.findItem(R.id.addtofavorite).setTitle("Add To Favorites");
                }
            }
        }

        menu.findItem(R.id.licensetype).setTitle("License : " + license);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addtofavorite:

                if (item.getTitle().equals("Add To Favorites")) {

                    boolean inserted = databaseHelper.insertData(id, title, devName, githubLink, cardBackground, fullName, license, licenseLink, youtubeLink);
                    if (inserted) {
                        Toast.makeText(Web.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Web.this, "It's already in My Favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                if (item.getTitle().equals("Remove From Favorites")) {

                    Integer deleteFavorite = databaseHelper.deleteFavorite(id);
                    if (deleteFavorite > 0) {
                        Toast.makeText(Web.this, "Removed From Favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Web.this, "It's already Removed From Favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.licensetype:
                if (license.equals("No License")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Web.this, R.style.CustomAlertDialog);
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("This dependency has No License.");
                    alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    openCustomTabs(getApplicationContext(), licenseLink);
                }
                break;

            case R.id.share:
                Intent intent60 = new Intent(Intent.ACTION_SEND);
                intent60.setType("text/plain");
                String shareBody10 = "About Android Dependency";
                String shareSub10 = "Hi there\n\nDependency Name : " + title + "\nDependency Website : " + githubLink + "\n\nIn-App link : https://dephub.co/app/" + id + "\n\nInformation Delivered by : DepHub\nInformation Provided by : Github\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                intent60.putExtra(Intent.EXTRA_SUBJECT, shareBody10);
                intent60.putExtra(Intent.EXTRA_TEXT, shareSub10);
                startActivity(Intent.createChooser(intent60, "Share this Dependency using"));
                break;

            case R.id.reportbug:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] mailTo = {"mailtodephub@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, mailTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report - " + title);
                intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                        "\n\nI landed up with a problem while using DepHub. There's a bug in " + title + "\n\nPlease describe your problem below:\n");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
                break;

            case R.id.qrCode:
                Intent intent1 = new Intent(getApplicationContext(), QRCode.class);
                intent1.putExtra("qrCodeLink", qrCodeLink);
                intent1.putExtra("qrCodeTitle", qrCodeTitle);
                intent1.putExtra("developerName", devName);
                intent1.putExtra("qrCodeId", qrCodeId);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCustomTabs(Context applicationContext, String link) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary));
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        builder.setUrlBarHidingEnabled(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(Web.this, Uri.parse(link));
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        webView.clearCache(true);
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (backButtonInterstitialAd != null) {
            goBack = true;
            backButtonInterstitialAd.show(Web.this);
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backButtonInterstitialAd != null) {
            goBack = true;
            backButtonInterstitialAd.show(Web.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (backButtonInterstitialAd != null) {
            goBack = false;
            backButtonInterstitialAd.show(Web.this);
        } else {
        }
        super.onResume();
    }
}