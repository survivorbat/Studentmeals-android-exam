package nl.mheijden.prog3app.model.services;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

import static com.android.volley.VolleyLog.TAG;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class APIServices {
    private RequestQueue mRequestQueue;
    private APICallbacks APICallbacks;
    private String APIKey;

    public APIServices(Context context, APICallbacks APICallbacks){
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        this.APICallbacks = APICallbacks;
    }

    public ArrayList<Meal> getData(){
        ArrayList<Meal> result = new ArrayList<Meal>();
        return result;
    }
    public boolean addMaaltijd(Meal meal){
        return false;
    }
    public void login(final Context context, String studentNumber, String password){
        Log.i("API","Login attempt for student number "+studentNumber);
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"studentNumber\": "+studentNumber+",\"password\":\""+password+"\"}");
        }
        catch (JSONException e){
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://prog4node.herokuapp.com/api/login", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("token")==null){
                        APICallbacks.loginCallback("error");
                        Log.i("API","Error!");
                    } else {
                        APICallbacks.loginCallback(response.getString("token"));
                        Log.i("API","Token received!");
                    }
                }
                catch (JSONException e){
                    APICallbacks.loginCallback("error");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                APICallbacks.loginCallback("error");
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
    public void getStudents(final String token){
        final ArrayList<Student> rs = new ArrayList<Student>();
        Student gerben = new Student("212199233","Gerben","","Droogers","g@droog.com","0293712947");
        rs.add(gerben);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, "https://prog4node.herokuapp.com/api/student", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject student = response.getJSONObject(i);
                        Student user = new Student(student.getString("StudentNumber"),student.getString("FirstName"),student.getString("Insertion"),student.getString("LastName"),student.getString("Email"),student.getString("PhoneNumber"));
                        rs.add(user);
                    }
                    APICallbacks.loadStudents(rs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mRequestQueue.add(jsObjRequest);
    }
    public ArrayList<Meal> getMeals(final String token){
        ArrayList<Meal> rs = new ArrayList<>();
        Meal pizza = new Meal(1,"Pizza","Voedsame maaltijd gemaakt met allergieën","20-12-2017",getStudent("1",token),2.30,10,"20:23","link",false);
        rs.add(pizza);
        return rs;
    }

    public Student getStudent(String number, final String token){
        return new Student("212199233","Gerben","","Droogers","g@droog.com","0293712947");
    }

    public Meal getMeal(String meal, final String token){
        return new Meal(1,"Pizza","Voedsame maaltijd gemaakt met allergieën","20-12-2017",getStudent("1", token),2.30,10,"20:23","link",false);
    }

    public ArrayList<FellowEater> getFellowEaters(final String token){
        ArrayList<FellowEater> rs = new ArrayList<>();
        rs.add(new FellowEater(0,getStudent("2", token),5,getMeal("2",token)));
        return rs;
    }
}
