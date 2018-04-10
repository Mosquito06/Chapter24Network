package kr.or.dgit.bigdata.chapter24network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Dom_parserAcitivity extends AppCompatActivity {
    ListView listView;
    ProgressDialog progressDig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        listView = (ListView) findViewById(R.id.listView);

        new ParsingXml().execute("order.xml");
    }

    private class ParsingXml extends AsyncTask<String, Void, List<Item>>{
        @Override
        protected void onPreExecute() {
            progressDig = ProgressDialog.show(
                    Dom_parserAcitivity.this,
                    "Wait",
                    "dom parsing...");
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            MyAdapter adapter = new MyAdapter(Dom_parserAcitivity.this, R.layout.itemrow, items);
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
            List<Item> arItems = new ArrayList<>();

            try {
                //Dom parse 초기화 작업
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
                Document doc = builder.parse(is);
                // 여기까지 기본 작업, 아래 부터 root를 찾아 원하는 자료를 parsing
                //root element find  <order><item>....</order>

                Element order = doc.getDocumentElement();
                NodeList items = order.getElementsByTagName("item");
                Item itemClass;

                //order하위 item엘리먼트 순회
                for(int i=0; i<items.getLength(); i++){
                    itemClass = new Item();
                    Node item = items.item(i);        // <item Maker="Samsung" Price="23000">Mouse</item>
                    Node text = item.getFirstChild(); // Mouse를 가지고 있는 Node(태그 안 텍스트도 Node로 인식)
                    itemClass.setItemName(text.getNodeValue()); // "Mouse"
                    //<item attributes...> </item> attributes 구하기
                    NamedNodeMap attrs = item.getAttributes(); //maker="samsung" price="2300"
                    //attribute를 순회
                    for(int j=0; j<attrs.getLength(); j++){
                        Node attr = attrs.item(j);  // maker="samsung" or price="2300" // Item 안의 속성 값을 구하고 가져옴
                        if (attr.getNodeName().equalsIgnoreCase("price")){
                            itemClass.setItemPrice(Integer.parseInt(attr.getNodeValue()));
                        }else{
                            itemClass.setMakerName(attr.getNodeValue());
                        }
                    }
                    arItems.add(itemClass);
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return arItems;
        }

    }

}
