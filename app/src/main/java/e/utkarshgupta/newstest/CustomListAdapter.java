package e.utkarshgupta.newstest;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<Post> {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private static class ViewHolder {
        TextView title;
        TextView author;
        TextView date_updated;
        ImageView thumbnailURL;
    }
    public CustomListAdapter(Context context, int resource, ArrayList<Post> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        //get the person's information
        final String title = getItem(position).getTitle();
        String imgUrl = getItem(position).getThumbnailURL();
        final String author = getItem(position).getAuthor();
        final String updated = getItem(position).getDate_updated();
        final String description = getItem(position).getDescription();
        final String link =getItem(position).getPostURL();
        final String source = getItem(position).getSource();

        try{


            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            final ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.cardTitle);
           //     holder.thumbnailURL = (ImageView) convertView.findViewById(R.id.cardImage);
                holder.author = (TextView) convertView.findViewById(R.id.cardAuthor);
                holder.date_updated = (TextView) convertView.findViewById(R.id.cardUpdated);
         //       holder.mProgressBar = (ProgressBar)convertView.findViewById(R.id.cardProgressDialog);

                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            holder.title.setText(title);
            holder.author.setText(author);
            holder.date_updated.setText(updated);


           convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewsFeedDetails.class);

                    intent.putExtra("title",title);
                    intent.putExtra("author",author);
                    intent.putExtra("updated",updated);
                    intent.putExtra("description",description);
                    intent.putExtra("link",link);
                    intent.putExtra("source",source);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });


            return convertView;
        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }



    }

}
