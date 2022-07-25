package com.mobiroller.core.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mobiroller.core.R2;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;
import com.mobiroller.core.views.VerticalViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class avePDFViewFragment extends BaseModuleFragment {


    @BindView(R2.id.page_count)
    public TextView pageCount;

    @BindView(R2.id.vertical_view_pager)
    public VerticalViewPager verticalViewPager;

    @BindView(R2.id.next)
    public ImageView next;

    @BindView(R2.id.previus)
    public ImageView previus;

    @BindView(R2.id.bottom_layout)
    public ConstraintLayout bottomLayout;

    @BindView(R2.id.main_layout)
    public RelativeLayout mainLayout;

    @BindView(R2.id.inner_layout)
    public ConstraintLayout innerLayout;

    @BindView(R2.id.indeterminate_horizontal_progress)
    public ProgressBar progressBar;

    private PDFLoader mPdfLoader;
    private String mURL;

    private PdfRenderer mPdfRenderer;

    private int mTotalPageCountNumber;
    private int mCurrentPageNumber;
    private PdfRenderer.Page mCurrentPage;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(false);
        loadUI();
        return view;
    }

    private void loadUI() {
        String web_url;
        if (getArguments().containsKey("pdf_url")) {

            mURL = getArguments().getString("pdf_url");
            loadPDF();
        } else {
            if (screenModel.getLocalizedURL() == null)
                web_url = screenModel.getURL();
            else {
                web_url = localizationHelper.getLocalizedTitle(screenModel.getLocalizedURL());
                if (web_url == null || web_url.isEmpty())
                    web_url = screenModel.getURL();
            }
            if (web_url != null) {
                mURL = web_url;
                loadPDF();
            } else {
                Toast.makeText(getActivity(), R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPdfLoader = new PDFLoader();
            mPdfLoader.execute(mURL);
        } else {
            progressBar.setVisibility(View.GONE);
            mainLayout.setBackgroundColor(Color.WHITE);
            new MaterialDialog.Builder(getActivity())
                    .content(R.string.pdf_version_message)
                    .positiveText(R.string.OK)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
                            startActivity(browserIntent);
                        }
                    }).show();
        }
    }


    private class PDFLoader extends AsyncTask<String, Integer, File> {
        File file;

        @Override
        protected File doInBackground(String... params) {
            file = ImageManager.getFileFromUrl(params[0]);
            if (!file.isFile()) {
                downloadFile(params[0], file);
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {

                    URL u = new URL(params[0]);
                    connection = (HttpURLConnection) u.openConnection();

                    connection.connect();

                    int fileLength = connection.getContentLength();

                    input = connection.getInputStream();
                    output = new FileOutputStream(file);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        if (fileLength > 0) // only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
            }

            return file;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setMax(100);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(File result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                try {
                    openRenderer(result);
                    ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(mTotalPageCountNumber);
                    verticalViewPager.setAdapter(imagePagerAdapter);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), R.string.common_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Sets up a {@link android.graphics.pdf.PdfRenderer} and related resources.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(File file) throws IOException {
        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
            mTotalPageCountNumber = mPdfRenderer.getPageCount();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPdfLoader != null)
            mPdfLoader.cancel(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pdf_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            checkFilePermission();
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void downloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mURL));
        if (screenModel != null && screenModel.getTitle() != null)
            request.setTitle(UtilManager.localizationHelper().getLocalizedTitle(screenModel.getTitle()));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }


    private boolean checkFilePermission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            else
                Toast.makeText(getActivity(), R.string.permission_storage_denied_download_never, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            downloadFile();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadFile();
        } else {
            Toast.makeText(getActivity(), R.string.permission_storage_denied_download, Toast.LENGTH_SHORT).show();
        }
    }

    private static void downloadFile(String url, File outputFile) {

    }

    @OnClick(R2.id.next)
    public void onClickNext() {
        if (mTotalPageCountNumber > mCurrentPageNumber + 1)
            verticalViewPager.post(new Runnable() {
                @Override
                public void run() {
                    verticalViewPager.setCurrentItem(++mCurrentPageNumber);
                }
            });
    }


    @OnClick(R2.id.previus)
    public void onClickPrevius() {
        if (mCurrentPageNumber > 0)
            verticalViewPager.post(new Runnable() {
                @Override
                public void run() {
                    verticalViewPager.setCurrentItem(--mCurrentPageNumber);
                }
            });
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private int size;
        LayoutInflater inflater;

        ImagePagerAdapter(int size) {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.size = size;

        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.layout_pdf_view_item, view, false);
            final PhotoView imageView = imageLayout.findViewById(R.id.image_view);
            final ProgressBar progressBar = imageLayout.findViewById(R.id.progress_bar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottomLayout.setVisibility(View.VISIBLE);
                mainLayout.setBackgroundColor(Color.WHITE);
                new PDFPageLoader(progressBar, imageView, position).execute();
            }

            view.addView(imageLayout, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            setHasOptionsMenu(true);
            pageCount.setVisibility(View.VISIBLE);
            mCurrentPageNumber = position;
            pageCount.setText((position + 1) + " / " + mTotalPageCountNumber);
        }

    }


    private class PDFPageLoader extends AsyncTask<Void, Integer, Bitmap> {

        ImageView imageView;
        ProgressBar progressBar;
        int position;

        PDFPageLoader(ProgressBar progressBar, ImageView imageView, int position) {
            this.progressBar = progressBar;
            this.imageView = imageView;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                try {
                    if (null != mCurrentPage) {
                        mCurrentPage.close();
                    }
                    mCurrentPage = mPdfRenderer.openPage(position);
                    Bitmap bitmap = Bitmap.createBitmap(
                            getResources().getDisplayMetrics().densityDpi * mCurrentPage.getWidth() / (72 * 3),
                            getResources().getDisplayMetrics().densityDpi * mCurrentPage.getHeight() / (72 * 3),
                            Bitmap.Config.ARGB_8888);

                    mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    return bitmap;
                }catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(SharedApplication.context)
                        .load(result)
                        .into(imageView);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout,innerLayout);
        }
    }
}
