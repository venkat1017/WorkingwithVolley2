package reader.android.com.newsx;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Topnews> userList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TopNewsAdapter mAdapter;
    TopNewsAdapter.OnLoadMoreListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.userListRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        userList = new ArrayList<>();

        getData();

    }

    private void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        JsonObjectRequest jor = new JsonObjectRequest(apiConfig.DATA_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                progressDialog.dismiss();

                try {
                    parseData(response.getJSONArray("articles"));
                    Log.d(response.getJSONArray("articles").toString(),"Data-Json");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley","Error");

                }
            });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jor);
    }

    private void parseData(final JSONArray jsonArray) {

        for (int i = 0; i < 9; i++) {
            Topnews user = new Topnews();
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                user.setTopnews_url(json.getString(apiConfig.TAG_AVATAR_URL));
                user.setTopnews_headline(json.getString(apiConfig.TAG_HEADLINE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            userList.add(user);

        }

        mAdapter = new TopNewsAdapter(this, userList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        if (listener == null){
            mAdapter.setOnLoadMoreListener(
                    new TopNewsAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            userList.add(null);
                            mAdapter.notifyItemInserted(userList.size() - 1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    userList.remove(userList.size() - 1);
                                    mAdapter.notifyItemRemoved(userList.size());
                                    int currSize = mAdapter.getItemCount();
                                    int end = currSize + 8;
                                    for (int i = currSize; i < end; i++) {
                                        if (i == jsonArray.length()) {

                                            break;
                                        } else {

                                            Topnews mUser = new Topnews();
                                            JSONObject jsonObject;
                                            try {
                                                jsonObject = jsonArray.getJSONObject(i);
                                                mUser.setTopnews_url(jsonObject.getString(apiConfig.TAG_AVATAR_URL));
                                                mUser.setTopnews_headline(jsonObject.getString(apiConfig.TAG_HEADLINE));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            userList.add(mUser);
                                            mAdapter.notifyItemInserted(userList.size());

                                        }
                                    }

                                    mAdapter.setLoaded();
                                }
                            }, 3000);

                        }
                    }
            );
        }

    }
}
