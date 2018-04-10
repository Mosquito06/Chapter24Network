package kr.or.dgit.bigdata.chapter24network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;

public class XmlParserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser);
        new XmlJsonMake().execute();
    }

    public void mOnClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btnParserMain01:
                intent = new Intent(this, Dom_parserAcitivity.class);
                break;
            case R.id.btnParserMain02:
                intent = new Intent(this, Sax_parserAcitivity.class);
                break;
            case R.id.btnParserMain03:
                intent = new Intent(this, Pull_parserAcitivity.class);
                break;
            case R.id.btnParserMain04:
                intent = new Intent(this, Json_parserAcitivity.class);
                break;
        }
        startActivity(intent);
    }
    private class XmlJsonMake extends android.os.AsyncTask<Void, Void, Void> {
        ProgressDialog progressDlg;

        protected void onPreExecute() {
            progressDlg = ProgressDialog.show(XmlParserActivity.this, "Wait",
                    "order.xml & order.json Making ...");
        }

        protected void onPostExecute(Void result) {
            progressDlg.dismiss();
            Toast.makeText(XmlParserActivity.this, "order.xml & order.json make",
                    Toast.LENGTH_LONG).show();
        }

        protected Void doInBackground(Void... strs) {
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("order.xml", MODE_PRIVATE);
                String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<order>\n"
                        + "    <item Maker=\"Samsung\" Price=\"23000\">Mouse</item>\n"
                        + "    <item Maker=\"LG\" Price=\"12000\">KeyBoard</item>\n"
                        + "    <item Price=\"156000\" Maker=\"Western Digital\">HDD</item>\n"
                        + "</order>";
                fos.write(str.getBytes());
                fos.close();
                fos = openFileOutput("order.json", MODE_PRIVATE);
                String Json = "[\n"
                        + " {\"Product\":\"Mouse\", \"Maker\":\"Samsung\", \"Price\":23000},\n"
                        + " {\"Product\":\"KeyBoard\", \"Maker\":\"LG\", \"Price\":12000},\n"
                        + " {\"Product\":\"HDD\", \"Maker\":\"Western Digital\", \"Price\":156000} \n"
                        + "]";
                fos.write(Json.getBytes());
                fos.close();
                Thread.sleep(3000);

                Log.d("XML", str);
                Log.d("JSON", Json);
            } catch (Exception e) {
                Log.d("XmlParser", e.getMessage());
            }
            return null;
        }
    }
}
