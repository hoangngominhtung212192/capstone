package tks.com.gwaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import tks.com.gwaandroid.adapter.CustomGalleryAdapter;
import tks.com.gwaandroid.model.Modelimage;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView gRecyclerView;
    private CustomGalleryAdapter customGalleryAdapter;
    private List<Modelimage> modelimageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gRecyclerView = findViewById(R.id.gRecycleView);
        modelimageList = (List<Modelimage>) getIntent().getExtras().getSerializable("GALLERY");
        generateData();
    }

    private void generateData() {
        customGalleryAdapter = new CustomGalleryAdapter(modelimageList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GalleryActivity.this);
        gRecyclerView.setLayoutManager(layoutManager);
        gRecyclerView.setAdapter(customGalleryAdapter);
    }
}
