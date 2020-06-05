package thang.daovanbao.readnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayTitle;
    ArrayList<String> arrayLink;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listNews);
        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTitle);
        listView.setAdapter(arrayAdapter);
        editText = findViewById(R.id.edtLink);
        button = findViewById(R.id.btnEnter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayTitle.clear();
                arrayLink.clear();
                new ReadRss().execute(editText.getText().toString());
                new ReadRss().execute("https://vnexpress.net/rss/the-thao.rss");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("linkNew", arrayLink.get(position));
                startActivity(intent);
            }
        });


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                new ReadRss().execute("https://vnexpress.net/rss/the-thao.rss");
                Log.d("capnhat", "da cap nhat sau 1s ");

            }
        };

        //Cap nhat du lieu sau 6h
        long timeLapLai = 6 * 60 * 60 * 60;
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, timeLapLai);
    }
    private class ReadRss extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(strings[0]);

                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String tieude = "";
            String link = "";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element, "title");
                arrayTitle.add(tieude);
                link = parser.getValue(element, "link");
                arrayLink.add(link);
            }
            arrayAdapter.notifyDataSetChanged();

        }
    }
}
