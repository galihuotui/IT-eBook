package com.cky.rx.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.rx.R;
import com.cky.rx.fragment.base.BaseActivity;
import com.cky.rx.fragment.base.BundleKey;
import com.cky.rx.model.BookDetailResult;
import com.cky.rx.model.BookItemToShow;
import com.cky.rx.network.Network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class BookDetailActivity extends BaseActivity {

    private static final String TAG = "BookDetailActivity";

    private String BookId;

    private String downloadLink;

    @Bind(R.id.book_description_value)
    TextView tvBookDescriptionValue;
    @Bind(R.id.book_publisher_value)
    TextView tvBookPublisher;
    @Bind(R.id.book_author_value)
    TextView tvBookAuthor;
    @Bind(R.id.book_isbn_value)
    TextView tvBookIsbnValue;
    @Bind(R.id.book_pages_value)
    TextView tvBookPagesValue;
    @Bind(R.id.book_year_value)
    TextView tvBookYearValue;
    @Bind(R.id.progressBar)
    ProgressBar pbLoading;
    @Bind(R.id.content_layout)
    ScrollView svContent;
    @Bind(R.id.tvDownloadBook)
    TextView tvDownloadBook;
    //@Bind(R.id.ivBookImage)
    //ImageView ivBookImage;
    //@Bind(R.id.collapsing_tool_bar)
    //CollapsingToolbarLayout collapsingToolbarLayout;


    public static void start(Context context, BookItemToShow bookItemToShow) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(BundleKey.BOOKID, bookItemToShow.id);
        context.startActivity(intent);
    }


    Observer<BookDetailResult> observer = new Observer<BookDetailResult>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (pbLoading.getVisibility() == View.VISIBLE) {
                pbLoading.setVisibility(View.GONE);
            }
            Toast.makeText(BookDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(BookDetailResult bookDetailResult) {


            tvBookDescriptionValue.setText(bookDetailResult.Description);
            tvBookPublisher.setText(bookDetailResult.Publisher);
            tvBookAuthor.setText(bookDetailResult.Author);
            tvBookIsbnValue.setText(bookDetailResult.ISBN);
            tvBookPagesValue.setText(bookDetailResult.Page);
            tvBookYearValue.setText(bookDetailResult.Year);
            //collapsingToolbarLayout.setTitle(bookDetailResult.Title);
            //Glide.with(BookDetailActivity.this).load(bookDetailResult.Image).into(ivBookImage);
            //getSupportActionBar().setTitle(bookDetailResult.Title);
            downloadLink = bookDetailResult.Download;
            if (svContent.getVisibility() == View.GONE) {
                svContent.setVisibility(View.VISIBLE);
            }
            if (pbLoading.getVisibility() == View.VISIBLE) {
                pbLoading.setVisibility(View.GONE);
            }
        }
    };

    private void getBookDetail(String bookId) {
        subscription = Network.getIteBooksApi()
                .getBookDetail(bookId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pbLoading.getVisibility() == View.GONE) {
                            pbLoading.setVisibility(View.VISIBLE);
                        }
                        if (svContent.getVisibility() == View.VISIBLE) {
                            svContent.setVisibility(View.GONE);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);

        ButterKnife.bind(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BookId = getIntent().getStringExtra(BundleKey.BOOKID);
        getBookDetail(BookId);

        tvDownloadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadLink != null) {
                    //DLManager.getInstance(BookDetailActivity.this).dlStart(downloadLink);
                    showTipDialog(downloadLink);
                }
            }
        });
    }

    private void showTipDialog(final String downloadLink) {
        new AlertDialog.Builder(BookDetailActivity.this)
                .setTitle(getString(R.string.tip))
                .setMessage(getString(R.string.go_to_download))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String referer = "http://www.it-ebooks.info/book/"+BookId+"/";
                        /*
                        Uri uri = Uri.parse(downloadLink);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        BookDetailActivity.this.startActivity(intent);
                        */

                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                        Uri uri = Uri.parse(downloadLink);
                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                        request.addRequestHeader("Referer", referer);
                        long id = downloadManager.enqueue(request);

                        //new Thread(getTrueDownloadLinkTask).start();

                    }
                })
                .show();
    }

    Runnable getTrueDownloadLinkTask = new Runnable() {
        @Override
        public void run() {
            if (downloadLink != null) {
                String referer = "http://www.it-ebooks.info/book/"+BookId+"/";
                Log.d(TAG, "referer" + "-->" + referer);
                try {
                    Request request = new Request.Builder()
                            .url(downloadLink)
                            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                            .addHeader("Accept-Encoding", "gzip, deflate, sdch")
                            .addHeader("Referer", "referer")
                            .build();
                    Call call = Network.getOkHttpClient().newCall(request);
                    Response response = call.execute();
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        Log.d(TAG, headers.name(i) + "-->" + headers.value(i));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /*
    public void saveToFile(String data) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileOutputStream = openFileOutput("myFileName", MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    */

    public String loadFromFile() {

        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = openFileInput("myFileName");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }
}
