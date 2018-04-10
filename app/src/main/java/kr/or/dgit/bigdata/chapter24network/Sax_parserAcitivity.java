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
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Sax_parserAcitivity extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        listView = (ListView) findViewById(R.id.listView);

        new ParsingXml().execute("order.xml");
    }

    private class ParsingXml extends AsyncTask<String, Void, List<Item>> {
        @Override
        protected void onPreExecute() {
            progressDig = ProgressDialog.show(
                    Sax_parserAcitivity.this,
                    "Wait",
                    "dom parsing...");
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            MyAdapter adapter = new MyAdapter(Sax_parserAcitivity.this, R.layout.itemrow, items);
            progressDig.dismiss();
            listView.setAdapter(adapter);
        }

        @Override
        protected List<Item> doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            SaxHandler handler = null;

            try(BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(params[0])))){
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return parsingxml(sb.toString());
        }

        private List<Item> parsingxml(String xml) {
            SaxHandler handler = null;
            InputStream is = null;

            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                handler = new SaxHandler();
                reader.setContentHandler(handler);
                is = new ByteArrayInputStream(xml.getBytes("utf-8"));
                reader.parse(new InputSource(is));

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return handler.arItems;
        }

    }

    private class SaxHandler extends DefaultHandler{
        boolean initItem = false; // 태그가 새로 시작하는지, 기존의 태그인지를 확인하기 위한 플래그
        // characters() 함수는 태그가 끝날 때 마다 호출 되기 때문에 해당 태그가 끝나는 태그인지를 확인해야 함
        // ex)<order> characters() 호출 <Item> characters() 호출 되는 식
        // 따라서 태그 내 text를 다 읽

        Item itemClass;
        List<Item> arItems = new ArrayList<>();

        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            Log.d("SAX", "startElement : " + localName);
            if(localName.equalsIgnoreCase("item")){
                initItem = true;
                itemClass = new Item();
            }

            if(attributes.getLength() > 0){
                for(int i = 0; i < attributes.getLength(); i++){
                    if(attributes.getLocalName(i).equalsIgnoreCase("price")){
                        itemClass.setItemPrice(Integer.parseInt(attributes.getValue(i)));
                    }else{
                        itemClass.setMakerName(attributes.getValue(i));
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(initItem){
                Log.d("SAX", "characters : " + new String(ch));
                itemClass.setItemName(new String(ch, start, length).toString());
                arItems.add(itemClass);
                initItem = false;
            }
        }
    }

}