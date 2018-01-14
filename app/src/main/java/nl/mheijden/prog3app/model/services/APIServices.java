package nl.mheijden.prog3app.model.services;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class APIServices {
    private RequestQueue mRequestQueue;
    private APICallbacks APICallbacks;
    private Context context;

    private final String BASEURL = "http://prog4node.herokuapp.com";

    public APIServices(Context context, APICallbacks APICallbacks){
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        this.APICallbacks = APICallbacks;
        this.context=context;
    }

    public boolean addMaaltijd(Meal meal){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("APITOKEN",null);
        return false;
    }

    public void addStudent(Student student){
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"firstname\": "+student.getFirstname()+",\"password\":\""+student.getPassword()+"\",\"lastname\":\""+student.getLastname()+"\""+",\"insertion\":\""+student.getInsertion()+"\""+",\"email\":\""+student.getEmail()+"\""+",\"phonenumber\":\""+student.getPhonenumber()+"\",\"studentNumber\":\""+student.getstudentNumber()+"\"}");
        }
        catch (JSONException e){
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        final JSONObject finalPost = post;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL+"/api/student", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.addedStudent(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                APICallbacks.addedStudent(false);
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
    public void addFellowEater(FellowEater fellowEater){

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL+"/api/login", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("token")==null){
                        APICallbacks.loginCallback("error");
                        Log.i("API","Error: "+response);
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
    public void getStudents(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        final ArrayList<Student> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL+"/api/student", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject student = response.getJSONObject(i);
                        Student user = new Student(student.getString("StudentNumber"), student.getString("Firstname"), student.getString("Insertion"), student.getString("Lastname"), student.getString("Email"), student.getString("PhoneNumber"));
                        rs.add(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getStudentImages(rs);
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
    public void getMeals(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("APITOKEN",null);
        ArrayList<Meal> rs = new ArrayList<>();
        byte[] test = "test".getBytes();
        Meal pizza = new Meal(1,"Pizza","Lorem ipsum test","20-12-2017 13:30",new Student("10"),1.50,4,test,false);
        rs.add(pizza);
        pizza = new Meal(2,"Spagehetti","Lorem ipsum test","17-12-2017 12:30",new Student("11"),4.60,6,test,false);
        rs.add(pizza);
        pizza = new Meal(3,"Grieks","Lorem ipsum test","05-12-2017 17:30",new Student("12"),9.70,5,test,false);
        rs.add(pizza);
        pizza = new Meal(4,"Turks","Lorem ipsum test","19-12-2017 16:30",new Student("9"),5.90,14,test,false);
        rs.add(pizza);
        pizza = new Meal(5,"Koffiebroodje","Lorem ipsum test","22-12-2017 18:30",new Student("0"),3.10,12,test,false);
        rs.add(pizza);
        APICallbacks.loadMeals(rs);
    }

    public void deleteMeal(Meal meal){

    }
    public void deleteFellowEater(FellowEater fellowEater){

    }

    public void getFellowEaters(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("APITOKEN",null);
        ArrayList<FellowEater> rs = new ArrayList<>();
        rs.add(new FellowEater(0, new Student("11"), 1, new Meal(5)));
        rs.add(new FellowEater(1, new Student("10"), 1, new Meal(4)));
        rs.add(new FellowEater(2, new Student("12"), 1, new Meal(3)));
        rs.add(new FellowEater(3, new Student("77"), 0, new Meal(3)));
        APICallbacks.loadFellowEaters(rs);
    }

    public void getStudentImages(final ArrayList<Student> students){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        final int[] counter = {0};
        for(final Student s : students){
            ImageRequest jsObjRequest = new ImageRequest(BASEURL+"/api/student/"+s.getstudentNumber()+"/picture", new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    File fileDir = context.getFilesDir();
                    File f = new File(fileDir, "studentPictures_"+s.getstudentNumber());
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        response.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    if(++counter[0] ==students.size()){
                        APICallbacks.loadStudents(students);
                    }
                }
            },0,0,null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    File fileDir = context.getFilesDir();
                    File f = new File(fileDir, "studentPictures_"+s.getstudentNumber());
                    f.delete();
                    if(++counter[0] ==students.size()){
                        APICallbacks.loadStudents(students);
                    }
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
    }
}
