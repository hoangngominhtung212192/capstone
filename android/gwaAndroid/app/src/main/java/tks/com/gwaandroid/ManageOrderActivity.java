package tks.com.gwaandroid;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomManageOrderAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.ManageOrderDataResponse;
import tks.com.gwaandroid.model.Orderrequest;
import tks.com.gwaandroid.network.RetrofitClientInstance;
import tks.com.gwaandroid.utils.BottomNavigationViewHelper;

public class ManageOrderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    int currentPage, totalPage, sortType, tradepostId;
    String status;

    RecyclerView manageorder_list;
    TextView empty_text;
    CustomManageOrderAdapter adapter;
    List<Orderrequest> manageOrderData;
    int currentVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        setTitle("Manage Trade order");

        //init
        tradepostId = getIntent().getIntExtra("TRADEPOSTID", 0);
        status = AppConstant.PENDING_STATUS;
        currentPage = 1;
        sortType = AppConstant.SORT_DATE_DESC;
        currentVisiblePosition = 0;

        manageOrderData = new ArrayList<Orderrequest>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.manageorder_nav);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_manageorder_pending:
                        status = AppConstant.PENDING_STATUS;
                        break;
                    case R.id.nav_manageorder_payment:
                        status = AppConstant.APPROVED_STATUS;
                        break;
                    case R.id.nav_manageorder_succeed:
                        status = AppConstant.SUCCEED_STATUS;
                        break;
                    case R.id.nav_manageorder_cancelled:
                        status = AppConstant.CANCELLED_STATUS;
                        break;
                }
                currentPage = 1;
                currentVisiblePosition = 0;
                manageOrderData = new ArrayList<Orderrequest>();
                loadManageOrderData();
                return true;
            }
        });

        manageorder_list = findViewById(R.id.list_manageorder);
        manageorder_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPage) {
                        currentVisiblePosition = ((LinearLayoutManager) manageorder_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        // a little bit delay
                        currentPage++;
                        loadManageOrderData();
                    }
                }
            }
        });

        //load Data
        loadManageOrderData();


    }

    private void loadManageOrderData() {
        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<ManageOrderDataResponse> call = trademarketAPI.getManageOrderData(tradepostId,status,currentPage,sortType);

        call.enqueue(new Callback<ManageOrderDataResponse>() {
            @Override
            public void onResponse(Call<ManageOrderDataResponse> call, Response<ManageOrderDataResponse> response) {
                ManageOrderDataResponse result = response.body();
                totalPage = result.getTotalPage();
                List<Orderrequest> pageData = result.getData();
                manageOrderData.addAll(pageData);
                renderManageOrderData(manageOrderData);
            }

            @Override
            public void onFailure(Call<ManageOrderDataResponse> call, Throwable t) {
                Log.d("MANAGEORDERAPI", "onFailure: " + t.getMessage());

            }
        });

    }

    private void renderManageOrderData(List<Orderrequest> manageOrderData) {
        empty_text = (TextView) findViewById(R.id.txt_empty_manageorder);

        if(manageOrderData == null || manageOrderData.size() < 1){
            empty_text.setText("EMPTY");
            empty_text.setVisibility(View.VISIBLE);
            manageorder_list.setVisibility(View.GONE);
        }else {
            manageorder_list.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.GONE);
            adapter = new CustomManageOrderAdapter(manageOrderData, ManageOrderActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManageOrderActivity.this);
            manageorder_list.setLayoutManager(layoutManager);
            manageorder_list.setAdapter(adapter);

            // scroll to the last position
            ((LinearLayoutManager) manageorder_list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        }

    }
}
