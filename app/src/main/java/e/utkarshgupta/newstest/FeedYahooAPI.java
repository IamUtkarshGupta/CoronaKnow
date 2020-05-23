package e.utkarshgupta.newstest;

import e.utkarshgupta.newstest.model.YahooFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeedYahooAPI {    // API to fetch content from yahoo.com

    String BASE_URL ="https://tools.cdc.gov/api/v2/resources/media/";

    @GET(BASE_URL + "{feed_name}.rss")
    Call<YahooFeed> getFeed(@Path("feed_name") String feed_name);

}
