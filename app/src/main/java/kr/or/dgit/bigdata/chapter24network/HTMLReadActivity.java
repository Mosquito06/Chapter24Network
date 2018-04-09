package kr.or.dgit.bigdata.chapter24network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTMLReadActivity extends AppCompatActivity {
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmlread);

        tv = (TextView) findViewById(R.id.result);
        et = (EditText) findViewById(R.id.loadhtml);
    }

    public void mOnLoadClicked(View view) {
        new UrlDown().execute(et.getText().toString());
    }

    public class UrlDown extends AsyncTask<String, Void, String>{
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show (HTMLReadActivity.this,  "Wait",  et.getText().toString() +  " Reading...");
        }

        @Override
        protected void onPostExecute(String result) {
            tv.setText(result);
            progressDlg.dismiss();
        }

        @Override
        protected String doInBackground(String... strs) {
            StringBuffer sb =  new StringBuffer();
            BufferedReader br =  null;
            HttpURLConnection con =  null;
            String line =  null;
            try {
                URL url =  new URL(strs[0].toString());
                con = (HttpURLConnection) url.openConnection();
                if (con !=  null) {
                    con.setConnectTimeout(10000);
                    con.setUseCaches( false);
                    if (con.getResponseCode() == HttpURLConnection. HTTP_OK ) {
                        br =  new BufferedReader( new InputStreamReader(con.getInputStream()));
                        while ((line = br.readLine()) !=  null) {
                            sb.append(line);
                        }
                    }
                }
            }  catch (IOException e) { e.printStackTrace(); }
            finally { try { br.close(); con.disconnect(); }  catch (Exception e) { ; } }
            return sb.toString();
        }
    }
}
