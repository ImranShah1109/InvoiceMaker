package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Buyer;
import com.example.invoicemaker.model.Cart;
import com.example.invoicemaker.model.Invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Currency;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
// create final invoice in form of pdf
public class MakeInvoiceActivity extends AppCompatActivity {

    public static final String INVOICE = "com.example.invoicemaker.INVOICE";

    String buyer_id,invoice_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_invoice);

        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        Intent intent = getIntent();
        String buyer,iid,b,i;
        buyer = intent.getStringExtra(NewInvoice.BUYER);
        iid = intent.getStringExtra(NewInvoice.INVOICE);
        b = intent.getStringExtra("buyer_id");
        i = intent.getStringExtra("invoice_id");
        if(buyer != null && iid != null)
        {
            buyer_id = buyer;
            invoice_id = iid;
        }
        else
        {
            buyer_id = b;
            invoice_id = i;
        }
        System.out.println("MakeInvoice--> get both ids :" +buyer_id+ " " +invoice_id);
        int bid = Integer.parseInt(buyer_id);
        int invoice = Integer.parseInt(invoice_id);

        MakeInvoice(bid, invoice);

    }

    public void MakeInvoice(int bid, int iid)
    {
        MyDbHandler dbHandler = new MyDbHandler(MakeInvoiceActivity.this);

        List<Cart> cartList = dbHandler.getCartListByIds(bid,iid);

        int quantity = 0;
        float total = 0.0f, total_tax = 0.0f, final_amt = 0.0f;

        for(Cart cart : cartList)
        {
            int q = cart.getQuantity();
            quantity = quantity + q;

            float price = cart.getProduct_price();
            total = total + (price * q);

            float tax = cart.getTax_amount();
            total_tax = total_tax + (tax*q);

            float f = cart.getTotal();
            final_amt = final_amt + f;
        }

        Invoice invoice = new Invoice();
        invoice.setTotal_qty(quantity);
        invoice.setTotal_amt(total);
        invoice.setTotal_tax_amt(total_tax);
        invoice.setFinal_amt(final_amt);

        dbHandler.completeInvoice(invoice,iid);

        printInvoice(iid,bid);
    }

    public void printInvoice(int iid,int bid)
    {
        MyDbHandler dbHandler = new MyDbHandler(MakeInvoiceActivity.this);
        List<Invoice> list = dbHandler.getInvoice(iid);
        List<Buyer> buyerList = dbHandler.getBuyerInfo(bid);

        int i = 0;
        String date = null;
        float f_amt = 0;
        for(Invoice invoice : list)
        {
            i = invoice.getInvoice_id();
            System.out.println("Invoice No. :" +i);
            date = invoice.getInvoice_date();
            System.out.println("Invoice Date :" +date);
            f_amt = invoice.getFinal_amt();
            System.out.println("Final Amount :" +f_amt);
        }

        String b_name = null;
        for(Buyer buyer : buyerList)
        {
            b_name = buyer.getName();
            System.out.println(b_name);
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(i));
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setText(date);
        TextView textView2 = (TextView) findViewById(R.id.textView3);
        textView2.setText(Float.toString(f_amt));
        TextView textView4 = (TextView) findViewById(R.id.textView5);
        textView4.setText(b_name);
    }

    synchronized public void printInvoicePdf(View view) throws IOException, InterruptedException {
        Intent intent = getIntent();
        String buyer,iid,b,i;
        buyer = intent.getStringExtra(NewInvoice.BUYER);
        iid = intent.getStringExtra(NewInvoice.INVOICE);
        b = intent.getStringExtra("buyer_id");
        i = intent.getStringExtra("invoice_id");

        if(buyer != null && iid != null)
        {
            buyer_id = buyer;
            invoice_id = iid;
        }
        else
        {
            buyer_id = b;
            invoice_id = i;
        }
        System.out.println("from pdf :"+invoice_id+"\t"+buyer_id);

        // Rupees symbol
        Currency currency = Currency.getInstance("INR");
        String symbol = currency.getSymbol();

        MyDbHandler dbHandler = new MyDbHandler(MakeInvoiceActivity.this);

        List<Invoice> invoiceList = dbHandler.getInvoice(Integer.parseInt(invoice_id));
        String date = null,q = null,t_amt=null,t_t_amt=null,f_amt=null;
        for(Invoice invoice : invoiceList)
        {
            date = invoice.getInvoice_date();
            q = Integer.toString(invoice.getTotal_qty());
            t_amt = Float.toString(invoice.getTotal_amt());
            t_t_amt = Float.toString(invoice.getTotal_tax_amt());
            f_amt = Float.toString(invoice.getFinal_amt());
        }

        List<Buyer> buyerList = dbHandler.getBuyerInfo(Integer.parseInt(buyer_id));
        String name = null,address = null,mobile = null;
        for(Buyer buyer1 : buyerList)
        {
            name = buyer1.getName();
            address = buyer1.getAddress();
            mobile = buyer1.getMobile();
        }


        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595,850
                ,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,595,850,paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        canvas.drawLine(29,30,canvas.getWidth()-29,30,paint);
        canvas.drawLine(30,30,30,canvas.getHeight()-30,paint);
        canvas.drawLine(29,canvas.getHeight()-30,canvas.getWidth()-29,canvas.getHeight()-30,paint);
        canvas.drawLine(canvas.getWidth()-30,canvas.getHeight()-30,canvas.getWidth()-30,30,paint);

        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("ONLY BRAND",230,70,paint);

        paint.setTextSize(11);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("SHOP NO. 3, NIRANJAN APARTMENT, HANDEWADI ROAD, HADAPSAR, Pune, Maharashtra, 411028",35,85,paint);
        paint.setTextSize(15);
        canvas.drawText("7972202369",261,105,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(30,120,canvas.getWidth()-30,120,paint);

        paint.setTextSize(20);
        paint.setColor(Color.rgb(128, 255, 0));
        canvas.drawRect(31,121,canvas.getWidth()-31,159,paint);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Invoice",261,145,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(30,160,canvas.getWidth()-30,160,paint);

        paint.setTextSize(15);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas.drawText("Invoice No. : "+invoice_id,45,175,paint);
        canvas.drawText("Invoice Date & Time : "+date,45,195,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(30,200,canvas.getWidth()-30,200,paint);

        paint.setColor(Color.rgb(128, 255, 0));
        canvas.drawRect(31,201,canvas.getWidth()-31,228,paint);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(" Customer Details ",231,220,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(30,229,canvas.getWidth()-30,229,paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas.drawText("Name      \t:"+name,45,245,paint);
        canvas.drawText("Address   \t:"+address,45,265,paint);
        canvas.drawText("Mobile No.  :"+mobile,45,285,paint);

        paint.setStrokeWidth(1);
        canvas.drawLine(30,290,canvas.getWidth()-30,290,paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Sr. No.",45,310,paint);
        canvas.drawText("Name of Product",95,310,paint);
        canvas.drawText("Price of Product",220,310,paint);
        canvas.drawText("Tax Rate",340,310,paint);
        canvas.drawText("QTY",440,310,paint);
        canvas.drawText("Total",485,310,paint);
        canvas.drawLine(30,315,canvas.getWidth()-30,315,paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        List<Cart> cartList = dbHandler.getCartListByIds(Integer.parseInt(buyer_id),Integer.parseInt(invoice_id));
        int j=1;
        float y = 330f;
        for(Cart cart : cartList)
        {
            String p_name,price,qty,tax,total;

            canvas.drawText(""+Integer.toString(j),45,y+0,paint);
            canvas.drawText(""+cart.getProduct_name(),100,y+0,paint);
            canvas.drawText(""+Float.toString(cart.getProduct_price()),225,y+0,paint);
            canvas.drawText(""+Float.toString(cart.getTax_amount()),345,y+0,paint);
            canvas.drawText(""+Integer.toString(cart.getQuantity()),445,y+0,paint);
            canvas.drawText(""+Float.toString(cart.getTotal()),490,y+0,paint);
            y = y+5;
            paint.setStrokeWidth(1);
            canvas.drawLine(30,y+0,canvas.getWidth()-30,y+0,paint);

            y = y+15;
            j++;
        }
        y = y+30;
        paint.setStrokeWidth(1);
        canvas.drawLine(92,290,92,y+0,paint);
        canvas.drawLine(218,290,218,y+0,paint);
        canvas.drawLine(338,290,338,y+0,paint);
        canvas.drawLine(438,290,438,y+0,paint);
        canvas.drawLine(483,290,483,y+0,paint);

        paint.setStrokeWidth(1);
        canvas.drawLine(30,y+0,canvas.getWidth()-30,y+0,paint);
        canvas.drawLine(338,y+0,338,y+23,paint);
        canvas.drawLine(438,y+0,438,y+23,paint);
        canvas.drawLine(483,y+0,483,y+23,paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        y=y+20;
        canvas.drawText("Total",171,y+0,paint);
        canvas.drawText(""+symbol +t_t_amt,345,y+0,paint);
        canvas.drawText(""+q,445,y+0,paint);
        canvas.drawText(""+symbol +f_amt,490,y+0,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(30,y+3,canvas.getWidth()-30,y+3,paint);

        y=y+20;
        canvas.drawText("Total Amount Before Tax :",280,y+0,paint);
        canvas.drawText(""+symbol +t_amt,495,y+0,paint);
        y = y +20;
        canvas.drawText("Total Amount With Tax :",280,y+0,paint);
        canvas.drawText(""+symbol +f_amt,495,y+0,paint);

        y = y+5;
        paint.setStrokeWidth(1);
        canvas.drawLine(30,y+0,canvas.getWidth()-30,y+0,paint);

        pdfDocument.finishPage(page);

        //String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/InvoiceMaker/";

        File file = new File(this.getExternalFilesDir("/"),"InvoiceMaker"+invoice_id+".pdf");



        try
        {
            //File f = new File(Environment.getExternalStorageDirectory().getPath()+"/InvoiceMaker/"+"InvoiceMaker"+invoice_id+".pdf");
            //boolean t = f.createNewFile();
           // boolean t = f.mkdir();
            //Log.d("folder", String.valueOf(t));
            //FileOutputStream fileOutputStream = new FileOutputStream(f);

            pdfDocument.writeTo(new FileOutputStream(file));
            Thread.sleep(3000);
            Toast.makeText(MakeInvoiceActivity.this,"Print Invoice Successfully!",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        pdfDocument.close();
        System.out.println("MakeInvoice : "+file.exists());
        if(file.exists())
        {
            Intent intent1 = new Intent(this, PdfViewerActivity.class);
            intent1.putExtra(MakeInvoiceActivity.INVOICE, invoice_id);
            startActivity(intent1);
        }

    }
}