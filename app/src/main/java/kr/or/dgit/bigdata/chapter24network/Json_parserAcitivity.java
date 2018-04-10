package kr.or.dgit.bigdata.chapter24network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Json_parserAcitivity extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        listView = (ListView) findViewById(R.id.listView);

        new ParsingXml().execute("order.json");
    }

    private class ParsingXml extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected void onPreExecute() {
            progressDig = ProgressDialog.show(
                    Json_parserAcitivity.this,
                    "Wait",
                    "dom parsing...");
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            MyAdapter adapter = new MyAdapter(Json_parserAcitivity.this, R.layout.itemrow, items);
            progressDig.dismiss();
            listView.setAdapter(adapter);
        }

        @Override
        protected List<Item> doInBackground(String... filenames) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            try(BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(filenames[0])))){
                while( (line = br.readLine())!= null){
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parsingxml(sb.toString());
        }

        private List<Item> parsingxml(String xml) {
            ArrayList<Item> arItem =  new ArrayList<Item>();
            Item itemClass;
            try {
                JSONArray ja =  new JSONArray(xml);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject order = ja.getJSONObject(i);
                    itemClass =  new Item();
                    itemClass.setItemName(order.getString( "Product"));
                    itemClass.setItemPrice(order.getInt( "Price"));
                    itemClass.setMakerName(order.getString( "Maker"));
                    arItem.add(itemClass);
                }
            }  catch (JSONException e) {
                Log. i ("Json_parser", e.getMessage());
            }
            return arItem;
         }

    }
}
