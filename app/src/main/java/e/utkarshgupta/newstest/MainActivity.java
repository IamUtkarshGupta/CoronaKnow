package e.utkarshgupta.newstest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import e.utkarshgupta.newstest.model.Feed;
import e.utkarshgupta.newstest.model.NYTimesFeed;
import e.utkarshgupta.newstest.model.YahooFeed;
import e.utkarshgupta.newstest.model.channel.Item;
import e.utkarshgupta.newstest.model.channelYahoo.ItemYahoo;
import e.utkarshgupta.newstest.model.entry.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://www.reddit.com/r/";
    private static final String BASE_URL_NYTIMES = "https://rss.nytimes.com/services/xml/rss/nyt/";
    private static final String BASE_URL_YAHOO="https://tools.cdc.gov/api/v2/resources/media/";
    private static final String BASE_URL_REDIFF="http://www.rediff.com/rss/";
    private Button btnLoadFeed;
    private EditText mFeedName;
    private ListView listView;
    Button b1;
    Button ask;
    TextView t1,t2,t3,t4;
    ArrayList<Post> posts;
  //  SpotsDialog dialog;

    String retrieve;
    String json_string;
    String JSON_String;
    JSONArray jsonArray;
    int USER_ID;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ask=findViewById(R.id.ask);

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://edition.cnn.com/interactive/2020/health/coronavirus-questions-answers/#topic-menu";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Here we use an intent without a Chooser unlike the next example
                    startActivity(intent);
                }

            }
        });
        posts = new ArrayList<Post>();


        listView = (ListView) findViewById(R.id.listView);
        LoadNewsFeed();


    }

    public void LoadNewsFeed() {

        initNYTimes("Health");
        initYahoo("403372");
        init("corona");
    }



    private void init(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()                                          //Using retrofit for xml parsing
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);                           //establish connection with link given in FeedAPI according to user's interests

        Call<Feed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<Feed>() {                                      // getting our objects(here entry) of news feed
            @Override
            public void onResponse(Call<Feed> call, retrofit2.Response<Feed> response) {        //conversion of retrofit object to our class feed object


                List<Entry> entrys = response.body().getEntrys();


                for(int i=0;i<entrys.size();i++) {

                    ExtractXML extractXML1 = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                    List<String> postContent = extractXML1.start();

                    ExtractXML extractXML2 = new ExtractXML(entrys.get(i).getContent(), "<img src=");
                    try {
                        postContent.add(extractXML2.start().get(0));
                    }
                    catch (NullPointerException e){          // null image
                        Log.d(TAG,"onResponse : NULL Pointer Exception : "+ e.getMessage());
                        postContent.add(null);
                    }
                    catch (IndexOutOfBoundsException e) {    // no image tag
                        Log.d(TAG,"onResponse : No image tag Exception : "+ e.getMessage());
                        postContent.add(null);
                    }
                    int lastPosition = postContent.size()-1;
                    try {
                        Post newPost = new Post(entrys.get(i).getTitle(),entrys.get(i).getAuthor().getName().substring(3),entrys.get(i).getUpdated(),postContent.get(0),postContent.get(lastPosition),"No description available ", "Source : Reddit");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }


                setListView();

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
              //  dialog.dismiss();
                // Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void initNYTimes(String currentFeed){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_NYTIMES)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedNYTimesAPI feedNYTimesAPI = retrofit.create(FeedNYTimesAPI.class);

        Call<NYTimesFeed> call = feedNYTimesAPI.getFeed(currentFeed);



        call.enqueue(new Callback<NYTimesFeed>() {
            @Override
            public void onResponse(Call<NYTimesFeed> call, retrofit2.Response<NYTimesFeed> response) {


                List<Item> items = response.body().getmChannel().getItems();


                for(int i=0;i<items.size();i++) {


                    try {
                        Post newPost = new Post(items.get(i).getTitle(),items.get(i).getCreator(),items.get(i).getLastBuildDate(),items.get(i).getLink(),"",items.get(i).getDescription(), "Source : The New York Times");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        Log.d(TAG,"GADBADI");
                        e.printStackTrace();
                    }

                }




                setListView();


            }

            @Override
            public void onFailure(Call<NYTimesFeed> call, Throwable t) {
                //dialog.dismiss();
                // Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void initYahoo(String currentFeed){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_YAHOO)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedYahooAPI feedAPIk = retrofit.create(FeedYahooAPI.class);
        Call<YahooFeed> call = feedAPIk.getFeed(currentFeed);
        call.enqueue(new Callback<YahooFeed>() {
            @Override
            public void onResponse(Call<YahooFeed> call, retrofit2.Response<YahooFeed> response) {


                List<ItemYahoo> items = response.body().getmChannel().getItems();



                for(int i=0;i<items.size();i++) {



                    try {
                        Post newPost = new Post(items.get(i).getTitle(),"CDC",items.get(i).getLastBuildDate(),items.get(i).getLink(),"",items.get(i).getDescription(), "Source : Centers for Disease Control and Prevention");
                        posts.add(newPost);
                    }catch (NullPointerException e){
                        Log.d(TAG,"GADBADI");
                        e.printStackTrace();
                    }

                }


                setListView();




            }

            @Override
            public void onFailure(Call<YahooFeed> call, Throwable t) {
                //Log.e(TAG, "Failure : Unable to retrieve RSS Feeds "+t.getMessage());
            }
        });
    }




    private void setListView(){

        Log.d("Size",String.valueOf(posts.size()));
        CustomListAdapter customListAdapter = new CustomListAdapter(getApplicationContext(), R.layout.card_layout_news, posts);
        listView.setAdapter(customListAdapter);





    }

    /*----------------------------------------------*/


}
