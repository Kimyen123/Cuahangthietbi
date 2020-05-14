package com.example.btlon.cuahangthietbionline.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.btlon.cuahangthietbionline.R;
import com.example.btlon.cuahangthietbionline.adapter.DienthoaiAdapter;
import com.example.btlon.cuahangthietbionline.model.Loaisp;
import com.example.btlon.cuahangthietbionline.model.Sanpham;
import com.example.btlon.cuahangthietbionline.ultil.CheckConnection;
import com.example.btlon.cuahangthietbionline.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolbarphukien;
    ListView lvphukien;
    DienthoaiAdapter phukienAdapter;
    ArrayList<Sanpham> mangphukien,results;

    int idsanpham;
    int page=1;

    View footerView;
    boolean isLoading = false;
    MyHandler myHandler;
    Boolean limitData = false; //xac nhan da het du lieu



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        AnhXa();
        ActionTooBar();
        GetDuLieuSanPham(page);

        LoadMoreData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menushoppingcart,menu);
        MenuItem itemSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (limitData==false) {
                    for (int i = 0; i < 10; i++) {
                        GetDuLieuSanPham(++page);
                    }
                    limitData=true;
                }
//                while (limitData==false){
//                    GetDuLieuSanPham(++page);
//                }
                ArrayList<Sanpham> tmp = new ArrayList<>();
                for(Sanpham s: results){
                    if (s.getTensp().toLowerCase().contains(newText.toLowerCase())){
                        tmp.add(s);
                    }
                    if(tmp.size()>0){
                        mangphukien.clear();
                        mangphukien.addAll(tmp);
                        phukienAdapter.notifyDataSetChanged();
                    }

                    if (tmp.isEmpty()){
                        mangphukien.clear();
                        phukienAdapter.notifyDataSetChanged();
                        //Toast.makeText(MainActivity.this,"khong tim thay",Toast.LENGTH_LONG);
                    }
                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuGioHang:
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {

        lvphukien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangphukien.get(i));
                startActivity(intent);
            }
        });

        lvphukien.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItem, int totalItem ) {
                if(firstItem + visibleItem == totalItem && totalItem!=0 && isLoading==false && limitData==false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void AnhXa() {
        toolbarphukien = findViewById(R.id.toolbarphukien);
        lvphukien = findViewById(R.id.listviewphukien);

        //Lay du lieu vao mangDienthoai
        Intent intent = getIntent();
        idsanpham = intent.getIntExtra("idloaisanpham",-1);
       mangphukien = new ArrayList<>();
       results = new ArrayList<>();
        phukienAdapter = new DienthoaiAdapter(getApplicationContext(),mangphukien);
        lvphukien.setAdapter(phukienAdapter);

        //video 15
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar,null);
        myHandler = new MyHandler();
    }

    private void ActionTooBar(){
        setSupportActionBar(toolbarphukien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarphukien.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetDuLieuSanPham(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdansanpham + String.valueOf(Page);
        //String duongdan = Server.Duongdansanpham+Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response!=null && response.length()!=2){
                            lvphukien.removeFooterView(footerView);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String tensp = jsonObject.getString("tensp");
                                    Integer giasp = jsonObject.getInt("giasp");
                                    String hinhanhsp = jsonObject.getString("hinhanhsp");
                                    String motasp = jsonObject.getString("motasp");
                                    int idsanpham = jsonObject.getInt("idsanpham");
                                    mangphukien.add(new Sanpham(id, tensp, giasp, hinhanhsp, motasp, idsanpham));
                                    results.add(new Sanpham(id, tensp, giasp, hinhanhsp, motasp, idsanpham));
                                }
                                phukienAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            limitData = true;
                            lvphukien.removeFooterView(footerView);
                          //  CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu!");

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("idsanpham",String.valueOf(idsanpham));
                return param;
            }
        };

        requestQueue.add(stringRequest);



    }

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvphukien.addFooterView(footerView);
                    break;
                case 1:
                    GetDuLieuSanPham(++page); //tang bien page len 1 roi moi thuc hien function
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
            super.run();
        }
    }

}
