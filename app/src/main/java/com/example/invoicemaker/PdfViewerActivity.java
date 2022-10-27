package com.example.invoicemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PdfViewerActivity extends AppCompatActivity {

    File file;
    ImageView pdfView;
    Bitmap bitmap1;

    String path,invoice;
    //private String fileP = "/storage/emulated/0/Android/data/com.example.invoicemaker/files/" + File.separator + "InvoiceMaker34.pdf";
    //private String stringFile = Environment.getExternalStorageDirectory().getPath() +File.separator+ "InvoiceMaker29.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();
        String iid,invc;
        invc = intent.getStringExtra(MakeInvoiceActivity.INVOICE);
        iid = intent.getStringExtra("invoice_id");
        if(iid != null)
        {
            invoice = iid;
        }
        else
        {
            invoice = invc;
        }

        pdfView = findViewById(R.id.pdfview);
        //  /storage/emulated/0/Android/data/com.example.invoicemaker/files/InvoiceMaker29.pdf
        String filepath = "/storage/emulated/0/Android/data/com.example.invoicemaker/files/InvoiceMaker"+invoice+".pdf";
        //Log.d("filepath",filepath);
        //String filepath1 = "/storage/emulated/0/Android/data/com.example.invoicemaker/files/InvoiceMaker34.pdf";
           // Thread.sleep(5000);
        File file = new File(filepath);
            System.out.println("pdfviewer : file : "+file.exists());
        synchronized (this)
        {
            if (file.exists())
            {
                Log.d("PdfViewer : ", "come in if");
                ParcelFileDescriptor fileDescriptor = null;
                try {
                    fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                PdfRenderer pdfRenderer = null;
                try {
                    pdfRenderer = new PdfRenderer(fileDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
                int pageWidth = rendererPage.getWidth();
                int pageHeight = rendererPage.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
                rendererPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap1 = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
                rendererPage.render(bitmap1, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                //bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                //path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "InvoiceMaker" + iid, null);

                pdfView.setImageBitmap(bitmap);
                rendererPage.close();

                pdfRenderer.close();
                try {
                    fileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d("PdfViewer", "come in in else");
                Intent intent1 = new Intent(this, MakeInvoiceActivity.class);
                String bid = intent.getStringExtra("buyer_id");
                //Log.d("pdfpage",bid);
                intent1.putExtra("invoice_id", iid);
                intent1.putExtra("buyer_id", bid);
                startActivity(intent1);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        //File file1 = new File(String.valueOf(file));
        if(id == R.id.share)
        {
            //ApplicationInfo api = getApplicationContext().getApplicationInfo();
            //String apkpath = api.sourceDir;

            Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.setType("application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            //intent.setType("text/plain");
            //intent.putExtra(Intent.EXTRA_SUBJECT,"Check Permission");
            //intent.putExtra(Intent.EXTRA_TEXT,"Permission Granted ...");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1, "InvoiceMaker" + invoice, null);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            startActivity(Intent.createChooser(intent,"Share file"));
        }

        return true;
    }

    public void Ok(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}