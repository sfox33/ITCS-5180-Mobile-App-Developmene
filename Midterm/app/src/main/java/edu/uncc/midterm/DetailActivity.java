package edu.uncc.midterm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sean on 3/12/2018.
 * DetailActivity.java
 * Midterm Exam
 */

public class DetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name, date, genres, dev, copyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("App Details");

        image = (ImageView) findViewById(R.id.detailImage);
        name = (TextView) findViewById(R.id.detailName);
        date = (TextView) findViewById(R.id.detailDate);
        genres = (TextView) findViewById(R.id.detailGenres);
        dev = (TextView) findViewById(R.id.detailDeveloper);
        copyright = (TextView) findViewById(R.id.detailCopyright);
        StringBuilder temp = new StringBuilder();

        if(getIntent() != null && getIntent().getExtras() != null) {
            App app = (App) getIntent().getExtras().getSerializable(AppsActivity.APP);
            Picasso.get().load(app.imageUrl).into(image);
            name.setText(app.name);
            date.setText(app.date);
            copyright.setText(app.copyright);
            dev.setText(app.artist);
            for(int i = 0; i < app.genres.size(); i++) {
                temp.append(app.genres.get(i));
                if(i + 1 != app.genres.size()) {
                    temp.append(", ");
                }
            }
            genres.setText(temp.toString());
        }

    }
}
