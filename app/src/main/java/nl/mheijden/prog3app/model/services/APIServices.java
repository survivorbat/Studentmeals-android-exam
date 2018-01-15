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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public void addMaaltijd(Meal meal){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        JSONObject post = new JSONObject();
        try {
            int doesCookeat=0;
            if(meal.isDoesCookEat()){
                doesCookeat=1;
            }
            post = new JSONObject("{\"Dish\": \""+meal.getDish()+"\",\"Info\":\""+meal.getInfo()+"\",\"DateTime\":\""+meal.getDate()+"\",\"ChefID\":" + meal.getChefID().getstudentNumber() + ",\"Price\":" + meal.getPrice() + ",\"MaxFellowEaters\":" + meal.getMax() + ",\"DoesCookEat\":\"" + doesCookeat + "\"}");
        }
        catch (JSONException e){
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL+"/api/meal", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.addedMeal(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                APICallbacks.addedMeal(false);
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
        mRequestQueue.add(request);
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"AmountOfGuests\": "+fellowEater.getGuests()+",\"StudentNumber\":"+fellowEater.getStudent().getstudentNumber()+",\"MealID\":"+fellowEater.getMeal().getId()+"}");
        }
        catch (JSONException e){
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL+"/api/felloweater", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.addedFellowEater(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                APICallbacks.addedFellowEater(false);
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
        mRequestQueue.add(request);
    }
    public void login(final Context context, String studentNumber, String password){
        Log.i("API","Login attempt for student number "+studentNumber);
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"studentNumber\": "+studentNumber+",\"password\":\""+password+"\"}");
        }
        catch (JSONException e){
            e.printStackTrace();
            APICallbacks.loginCallback("errorobj");
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL+"/api/login", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("token")==null){
                        APICallbacks.loginCallback("errorwrong");
                        Log.i("API","Error: "+response);
                    } else {
                        APICallbacks.loginCallback(response.getString("token"));
                        Log.i("API","Token received!");
                    }
                }
                catch (JSONException e){
                    APICallbacks.loginCallback("errorobj");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.toString().equals("com.android.volley.AuthFailureError")){
                    APICallbacks.loginCallback("errorwrong");
                } else {
                    APICallbacks.loginCallback("errorconn");
                }
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
        final String token = sharedPreferences.getString("APITOKEN",null);
        final ArrayList<Meal> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL+"/api/meal", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject mealapi = response.getJSONObject(i);
                        Meal meal = new Meal(mealapi.getInt("ID"),mealapi.getString("Dish"),mealapi.getString("Info"),mealapi.getString("DateTime"),new Student(mealapi.getString("ChefID")),mealapi.getDouble("Price"),mealapi.getInt("MaxFellowEaters"),Boolean.parseBoolean(mealapi.getString("DoesCookEat")));
                        rs.add(meal);
                    }
                    getMealImages(rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    getMealImages(rs);
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

    private void getMealImages(final ArrayList<Meal> meals){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        final int[] counter = {0};
        for(final Meal s : meals){
            ImageRequest jsObjRequest = new ImageRequest(BASEURL+"/api/meal/"+s.getId()+"/picture", new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    File fileDir = context.getFilesDir();
                    File f = new File(fileDir, "mealPictures_"+s.getId());
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        response.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    if(++counter[0] ==meals.size()){
                        APICallbacks.loadMeals(meals);
                    }
                }
            },0,0,null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    File fileDir = context.getFilesDir();
                    File f = new File(fileDir, "mealPictures_"+s.getId());
                    f.delete();
                    if(++counter[0] ==meals.size()){
                        APICallbacks.loadMeals(meals);
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

    public void deleteMeal(Meal meal){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASEURL+"/api/meal/"+meal.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.removedMeal(true);
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                APICallbacks.removedMeal(false);
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + token;
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }
        };
        mRequestQueue.add(request);
    }
    public void deleteFellowEater(FellowEater fellowEater){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASEURL+"/api/felloweater/"+fellowEater.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.removedFellowEater(true);
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                APICallbacks.removedFellowEater(false);
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + token;
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }
        };
        mRequestQueue.add(request);
    }

    public void getFellowEaters(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN",null);
        final ArrayList<FellowEater> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL+"/api/felloweater", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject felloweater = response.getJSONObject(i);
                        FellowEater fellowEater = new FellowEater(felloweater.getInt("ID"),new Student(felloweater.getString("StudentNumber")),felloweater.getInt("AmountOfGuests"),new Meal(felloweater.getInt("MealID")));
                        rs.add(fellowEater);
                        APICallbacks.loadFellowEaters(rs);
                    }
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

    private void getStudentImages(final ArrayList<Student> students){
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
