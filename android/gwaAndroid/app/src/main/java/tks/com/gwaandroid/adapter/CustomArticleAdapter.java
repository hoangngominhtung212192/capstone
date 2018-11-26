package tks.com.gwaandroid.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tks.com.gwaandroid.ArticleDetailActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Article;
import tks.com.gwaandroid.model.ArticleSDTO;

public class CustomArticleAdapter extends RecyclerView.Adapter<CustomArticleAdapter.CustomViewHolder> {

    private ArticleSDTO sArticleObject;
    private Context context;

    public CustomArticleAdapter(ArticleSDTO sArticleObject, Context context){
        this.sArticleObject = sArticleObject;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View eView;

        TextView eArticleTitle;
        TextView eArticleDate;
        TextView eArticleDescription;
        private ImageView mImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            eView = itemView;

            eArticleTitle = (TextView) eView.findViewById(R.id.rArticleTitle);
            eArticleDate = (TextView) eView.findViewById(R.id.rArticleDate);
            eArticleDescription = (TextView) eView.findViewById(R.id.rArticleDescription);
            mImage = (ImageView) eView.findViewById(R.id.rArThumbView);


        }
    }

    // create view holder, initialize
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.article_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Article anArticle = sArticleObject.getArticleList().get(position);

        //set article title
        holder.eArticleTitle.setText(anArticle.getTitle());
        System.out.println("title: "+anArticle.getTitle());

        String dateNauthor = anArticle.getDate()+ " | " + anArticle.getAccount().getUsername();
        holder.eArticleDate.setText(dateNauthor);

        holder.eArticleDescription.setText(anArticle.getDescription());
        System.out.println("thumbnail: "+anArticle.getThumbImage());

        // download image from url
        Picasso picasso = Picasso.get();
        if (anArticle.getThumbImage().contains("localhost:8080")) {
            String imageUrl = anArticle.getThumbImage().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        } else {
            picasso.load(anArticle.getThumbImage())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        }

        holder.eView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("articleID", sArticleObject.getArticleList().get(position).getId() + "");
                context.startActivity(intent);
            }
        });

    }

    //get item count
    @Override
    public int getItemCount() {
        List<Article> listE = sArticleObject.getArticleList();
        return listE.size();
    }
}
