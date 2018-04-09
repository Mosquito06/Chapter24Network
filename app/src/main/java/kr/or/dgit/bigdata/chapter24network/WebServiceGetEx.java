package kr.or.dgit.bigdata.chapter24network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceGetEx extends AppCompatActivity {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service_get_ex);

        tvResult = (TextView) findViewById(R.id. tvResult);
    }

    public void mReqClicked(View view) {
        String str = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2720057100";
        new HttpRequestTask().execute(str);
    }

    class HttpRequestTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            progressDlg = ProgressDialog. show (WebServiceGetEx.this, "Wait", "Downloading...");
        }

        @Override
        protected void onPostExecute(String result) {
            progressDlg.dismiss();
            tvResult.setText(result);
        }

        @Override
        protected String doInBackground(String... strs) {
            StringBuffer sb =  new StringBuffer();
            BufferedReader br =  null;
            HttpURLConnection con =  null;
            String line =  null;
            try {
                URL url =  new URL(strs[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod( "GET");
                if (con !=  null) {
                    if (con.getResponseCode() == HttpURLConnection. HTTP_OK ) {
                        br =  new BufferedReader( new InputStreamReader(con.getInputStream()));
                    }
                }
                while ((line = br.readLine()) !=  null) { sb.append(line); }
            }  catch (IOException e) {
                e.printStackTrace();
            }  finally {
                try { br.close(); con.disconnect(); }  catch (Exception e) { }
            }
                return sb.toString();
        }

    }
}
