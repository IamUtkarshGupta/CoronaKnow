package e.utkarshgupta.newstest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;


public class NewsFeedDetails extends Activity {

    private TextToSpeech tts;
    TextView title;
    TextView author;
    TextView date_updated;
    ProgressBar mProgressBar;
    TextView description;
    TextView link;
    Spanned Text;
    TextView source;
    Button btnaudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        source = (TextView)findViewById(R.id.source);
        title = (TextView)findViewById(R.id.title);
        author = (TextView)findViewById(R.id.author);
        date_updated = (TextView)findViewById(R.id.date_updated);
        description = (TextView)findViewById(R.id.description);
        link = (TextView)findViewById(R.id.link);
        btnaudio=findViewById(R.id.btnAudio);
        Text = Html.fromHtml(" <br />" +
                "<a href='"+getIntent().getStringExtra("link")+"'>Click here to visit post</a>");

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        author.setText(intent.getStringExtra("author"));
        date_updated.setText(intent.getStringExtra("updated"));
        description.setText(intent.getStringExtra("description"));
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(Text);
        source.setText(intent.getStringExtra("source"));




        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub

            }
        });


        btnaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voiceOutput();
            }
        });





    }


    public void voiceOutput()
    {

        CharSequence txt=title.getText()+" "+description.getText();

        tts.speak(txt, TextToSpeech.QUEUE_FLUSH,null,"id1");
    }

    public void onDestroy()
    {
        if(tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}
