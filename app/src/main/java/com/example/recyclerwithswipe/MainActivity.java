package com.example.recyclerwithswipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MyInterface {

    List<ModelClass> data = new ArrayList<ModelClass>();
    @BindView(R.id.recycle1)
    RecyclerView recyclerView;
    DataAdapter adapter;
    ModelClass model;
    Snackbar snackbar;
    @BindView(R.id.layout1)
    LinearLayout linearLayout;
    int position;
    @BindView(R.id.swipe1)
    SwipeRefreshLayout refreshLayout;
    private static final int DEFAULT_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new DataAdapter(MainActivity.this, this);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        //swipeRevealLayout.open(true);
        refreshLayout.setDistanceToTriggerSync(50);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (checkNetwork()) {
                    Delete.table(ModelClass.class);
                    retrofunction();
                    refreshLayout.setRefreshing(false);
                } else {
                    snackbar = Snackbar.make(linearLayout, "Please check your connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getApplicationContext(),"Please check your connection", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration;
        decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        registerForContextMenu(recyclerView);
        recyclerView.setAdapter(adapter);

        data = null;
        data = SQLite.select().from(ModelClass.class).queryList();

        if (data.size() == 0 || data == null) {
            if(checkNetwork()) {
                retrofunction();
            }
            else{
                Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(i,30);
                Toast.makeText(getApplicationContext(),"Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }
        if (data != null && !data.isEmpty()) {
            model = data.get(DEFAULT_INDEX);
            //Toast.makeText(this, "Data is not null", Toast.LENGTH_SHORT).show();
            snackbar = Snackbar.make(linearLayout, "Data is not null", Snackbar.LENGTH_LONG);
            snackbar.show();
            //Toast.makeText(getContext(),""+data.size(),Toast.LENGTH_SHORT).show();
            adapter.setModes(data);
            adapter.setSelected(DEFAULT_INDEX);
        } else {
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
            retrofunction();
        }

    }
    public boolean checkNetwork()
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo.isConnected() || dataInfo.isConnected()) {
            return true;
        }
        else{
            return false;}
    }

    public void retrofunction() {
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RetroInterface retroInterface = builder.create(RetroInterface.class);

        Observable<List<ModelClass>> observable = retroInterface.getUsers();

        Observer<List<ModelClass>> observer = new Observer<List<ModelClass>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.v("onSubscribe --->",""+d);
            }

            @Override
            public void onNext(List<ModelClass> value) {
                model = new ModelClass();
                for (int i = 0; i < value.size(); i++) {
                    model.setId(value.get(i).getId());
                    model.setName(value.get(i).getName());
                    model.setEmail(value.get(i).getEmail());
                    model.setAddress(value.get(i).getAddress());
                    model.save();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.v("onError --->","Erroe:  "+e);
            }

            @Override
            public void onComplete() {
                Log.v("onComplete --->","Complete");
                List<ModelClass> classList;
                classList = SQLite.select().from(ModelClass.class).queryList();
                adapter.setModes(classList);
                //adapter.notifyDataSetChanged();
                //adapter.setSelected(model);
                adapter.setSelected(DEFAULT_INDEX);
                adapter.notifyDataSetChanged();
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void deletedata()
    {
        if (model.delete()) {
            data = SQLite.select().from(ModelClass.class).queryList();
            if (data.size() == 0) {
                retrofunction();
            } else {
                recyclerView.setAdapter(adapter);
                adapter.setModes(data);
                recyclerView.scrollToPosition(position - 1);

                if (position >= data.size()) {
                    model = data.get(position - 1);
                    //adapter.setSelected(model);
                    adapter.setSelected(position - 1);

                } else {
                    model = data.get(position);
                    //adapter.setSelected(model);
                    adapter.setSelected(position);
                }
            }
        }
    }

    @Override
    public void setPosition(int p) {

    }

    public void setLongClick(int p) {

        data = SQLite.select().from(ModelClass.class).queryList();
        //Toast.makeText(this,"Data size: "+data.size(),Toast.LENGTH_SHORT).show();
        position = p;
        model = data.get(position);
        deletedata();
        //adapter.setSelected(model);
        //adapter.setSelected(position);
        //openContextMenu(recyclerView);
    }

    @Override
    public void setAllData() {

    }
}
