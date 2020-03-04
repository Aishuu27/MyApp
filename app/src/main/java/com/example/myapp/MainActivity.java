package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static String BaseUrl = "http://dummy.restapiexample.com/";
    private EditText edtName, edtSalary, edtAge, edtId;
    private TextView text;
    private String name;
    private int salary, age, id;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Instabug.Builder(getApplication(), "ed24ee7ffcfd8f30714a81abcbf1ebe9")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.FLOATING_BUTTON)
                .build();

        edtName = findViewById(R.id.name);
        edtSalary = findViewById(R.id.salary);
        edtAge = findViewById(R.id.age);
        edtId = findViewById(R.id.id1);
        text = findViewById(R.id.text);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                salary = Integer.parseInt(edtSalary.getText().toString());
                age = Integer.parseInt(edtAge.getText().toString());
                id = Integer.parseInt(edtId.getText().toString());
                SendData();
                edtName.setText("");
                edtSalary.setText("");
                edtAge.setText("");
                edtId.setText("");
            }
        });
    }

    void SendData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(240, TimeUnit.SECONDS);
        httpClient.connectTimeout(240, TimeUnit.SECONDS);
        httpClient.writeTimeout(240, TimeUnit.SECONDS);

        Log.d("MainActivity", "BaseUrl: " + BaseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("salary", salary);
        jsonObject.addProperty("age", age);
       // jsonObject.addProperty("name", name);

        Postcall p = retrofit.create(Postcall.class);
        Call<JsonObject> call = p.postcurrentdata(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
//
                        String str = response.body().getAsJsonObject("data").toString();
                        // String str = response.body().get("status").getAsString();
                        text.setText(str);
                        Log.d("MainActivity", "Text: " + str);
                    } catch (JsonIOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                text.setText(t.getMessage());
            }
        });
    }
}
