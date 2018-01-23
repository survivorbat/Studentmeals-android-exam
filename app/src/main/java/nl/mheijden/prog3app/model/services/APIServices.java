package nl.mheijden.prog3app.model.services;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
 * Class for all volley network requests to the Node server
 */

public class APIServices {
    /**
     * RequestQueue for the volley requests
     */
    private final RequestQueue mRequestQueue;
    /**
     * All the callbacks for the domain
     */
    private final APICallbacks APICallbacks;
    /**
     * Context in order to get sharedpreferences
     */
    private final Context context;

    /**
     * Baseurl of the API for debugging
     */
    private final String BASEURL = "http://prog4node.herokuapp.com";




    /* Constructor */

    /**
     * @param context      of the activity for sharedpreferences
     * @param APICallbacks in order to send back data to the domain
     */
    public APIServices(Context context, APICallbacks APICallbacks) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 2048 * 2048); // 2MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        this.APICallbacks = APICallbacks;
        this.context = context;
    }




    /* Add methods */

    /**
     * @param meal that needs to be added
     */
    public void addMaaltijd(Meal meal) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b;
        try {
            meal.getPicture().compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
            b = baos.toByteArray();
        }
        catch (NullPointerException e){
            b = "".getBytes();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JSONObject post = new JSONObject();
        try {
            int doesCookeat = 0;
            if (meal.isDoesCookEat()) {
                doesCookeat = 1;
            }
            post = new JSONObject("{\"Dish\": \"" + meal.getDish() + "\",\"Info\":\"" + meal.getInfo() + "\",\"DateTime\":\"" + meal.getDate() + "\",\"ChefID\":" + meal.getChef().getstudentNumber() + ",\"Price\":" + meal.getPrice() + ",\"MaxFellowEaters\":" + meal.getMaxFellowEaters() + ",\"DoesCookEat\":\"" + doesCookeat + "\",\"Picture\":\"" + Base64.encodeToString(b, Base64.DEFAULT) + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL + "/api/meal", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.addedMeal(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.addedMeal(false);
                }
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

    /**
     * @param student that needs to be added
     */
    public void addStudent(Student student) {
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"firstname\": " + student.getFirstname() + ",\"password\":\"" + student.getPassword() + "\",\"lastname\":\"" + student.getLastname() + "\"" + ",\"insertion\":\"" + student.getInsertion() + "\"" + ",\"email\":\"" + student.getEmail() + "\"" + ",\"phonenumber\":\"" + student.getPhonenumber() + "\",\"studentNumber\":\"" + student.getstudentNumber() + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL + "/api/student", post, new Response.Listener<JSONObject>() {
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

    /**
     * @param fellowEater that needs to be added
     */
    public void addFellowEater(FellowEater fellowEater) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"AmountOfGuests\": " + fellowEater.getGuests() + ",\"StudentNumber\":" + fellowEater.getStudent().getstudentNumber() + ",\"MealID\":" + fellowEater.getMeal().getId() + "}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.loginCallback("error");
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL + "/api/felloweater", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.addedFellowEater(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.addedFellowEater(false);
                }
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




    /* Login method */

    /**
     * @param studentNumber for identification
     * @param password      for verification
     */
    public void login(String studentNumber, String password) {
        Log.i("API", "Login attempt for student number " + studentNumber);
        JSONObject post = new JSONObject();
        try {
            post = new JSONObject("{\"studentNumber\": " + studentNumber + ",\"password\":\"" + password + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.loginCallback("errorobj");
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASEURL + "/api/login", post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("token") == null) {
                        APICallbacks.loginCallback("errorwrong");
                        Log.i("API", "Error: " + response);
                    } else {
                        APICallbacks.loginCallback(response.getString("token"));
                        Log.i("API", "Token received!");
                    }
                } catch (JSONException e) {
                    APICallbacks.loginCallback("errorobj");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    APICallbacks.loginCallback("errorwrong");
                } else {
                    APICallbacks.loginCallback("errorconn");
                }
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }




    /* Get methods */

    /**
     * Get ALL students and then get images afterwards
     */
    public void getStudents() {
        Log.i("API", "getStudents");
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        final ArrayList<Student> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL + "/api/student", null, new Response.Listener<JSONArray>() {
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
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    getStudentImages(rs);
                }
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

    /**
     * Get ALL meals and get images afterwards
     */
    public void getMeals() {
        Log.i("API", "getMeals");
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        final ArrayList<Meal> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL + "/api/meal", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject mealapi = response.getJSONObject(i);
                        Meal meal = new Meal(mealapi.getInt("ID"), mealapi.getString("Dish"), mealapi.getString("Info"), mealapi.getString("DateTime"), new Student(mealapi.getString("ChefID")), mealapi.getDouble("Price"), mealapi.getInt("MaxFellowEaters"), Boolean.parseBoolean(mealapi.getString("DoesCookEat")));
                        rs.add(meal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                getMealImages(rs);
                Log.i("API", "getMeals succesful for " + rs.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    getMealImages(rs);
                }
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

    /**
     * @param meals to add images to for each array item
     */
    private void getMealImages(final ArrayList<Meal> meals) {
        Log.i("API", "getMealImages for " + meals.size());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        final int[] counter = {0};
        File fileDir = context.getFilesDir();
        for (final Meal s : meals) {
            final File f = new File(fileDir, "mealPictures_" + s.getId());
            if (!f.exists() && !f.isDirectory()) {
                @SuppressWarnings("deprecation") ImageRequest jsObjRequest = new ImageRequest(BASEURL + "/api/meal/" + s.getId() + "/picture", new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        File fileDir = context.getFilesDir();
                        File f = new File(fileDir, "mealPictures_" + s.getId());
                        try {
                            FileOutputStream out = new FileOutputStream(f);
                            response.compress(Bitmap.CompressFormat.JPEG, 10, out);
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (++counter[0] == meals.size()) {
                            Log.i("API", "getMealImages successful");
                            APICallbacks.loadMeals(meals);
                        }
                    }
                }, 0, 0, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode==401){
                            APICallbacks.invalidToken();
                        } else {
                            //noinspection ResultOfMethodCallIgnored
                            f.delete();
                            if (++counter[0] == meals.size()) {
                                APICallbacks.loadMeals(meals);
                            }
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
        if (counter[0] != meals.size()) {
            APICallbacks.loadMeals(meals);
        }
    }

    /**
     * @param students that images are needed of for each item in the array
     */
    private void getStudentImages(final ArrayList<Student> students) {
        Log.i("API", "getStudentImages for " + students.size());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        final int[] counter = {0};
        for (final Student s : students) {
            //noinspection deprecation
            @SuppressWarnings("deprecation") ImageRequest jsObjRequest = new ImageRequest(BASEURL + "/api/student/" + s.getstudentNumber() + "/picture", new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    File fileDir = context.getFilesDir();
                    File f = new File(fileDir, "studentPictures_" + s.getstudentNumber());
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        response.compress(Bitmap.CompressFormat.JPEG, 25, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (++counter[0] == students.size()) {
                        APICallbacks.loadStudents(students);
                        Log.i("API", "getStudentImage successful");
                    }
                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode==401){
                        APICallbacks.invalidToken();
                    } else {
                        File fileDir = context.getFilesDir();
                        File f = new File(fileDir, "studentPictures_" + s.getstudentNumber());
                        //noinspection ResultOfMethodCallIgnored
                        f.delete();
                        if (++counter[0] == students.size()) {
                            APICallbacks.loadStudents(students);
                        }
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

    /**
     * get ALL felloweaters
     */
    public void getFellowEaters() {
        Log.i("API", "getFellowEaters");
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        final ArrayList<FellowEater> rs = new ArrayList<>();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, BASEURL + "/api/felloweater", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject felloweater = response.getJSONObject(i);
                        FellowEater fellowEater = new FellowEater(felloweater.getInt("ID"), new Student(felloweater.getString("StudentNumber")), felloweater.getInt("AmountOfGuests"), new Meal(felloweater.getInt("MealID")));
                        rs.add(fellowEater);
                        APICallbacks.loadFellowEaters(rs);
                    }
                    Log.i("API", "getFellowEaters successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    error.printStackTrace();
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




    /* Delete methods */

    /**
     * @param meal to delete
     */
    public void deleteMeal(Meal meal) {
        Log.i("API", "deleteMeal " + meal.getId());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASEURL + "/api/meal/" + meal.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.removedMeal(true);
                Log.i("API", "getFellowEaters successful");
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.removedMeal(false);
                    error.printStackTrace();
                }
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

    /**
     * @param fellowEater to be deleted
     */
    public void deleteFellowEater(FellowEater fellowEater) {
        Log.i("API", "deleteFellowEater " + fellowEater.getId());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, BASEURL + "/api/felloweater/" + fellowEater.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.removedFellowEater(true);
                Log.i("API", "getFellowEaters successful");
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.removedFellowEater(false);
                    error.printStackTrace();
                }
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



    /* Put methods */

    /**
     * @param student object that dictates what the new student object will look like
     */
    public void changeStudent(Student student) {
        Log.i("API", "changeStudent " + student.getstudentNumber());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JSONObject put = new JSONObject();
        try {
            put = new JSONObject("{\"studentNumber\": \"" + student.getstudentNumber() + "\",\"firstname\":\"" + student.getFirstname() + "\",\"lastname\":\"" + student.getLastname() + "\",\"insertion\":\"" + student.getInsertion() + "\",\"lastname\":\"" + student.getLastname() + "\",\"email\":\"" + student.getEmail() + "\",\"phonenumber\":\"" + student.getPhonenumber() + "\",\"password\":\"" + student.getPassword() + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.changedStudent(false);
        }
        System.out.println(put.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASEURL + "/api/student", put, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                APICallbacks.changedStudent(true);
                Log.i("API", "changeStudent successful");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.changedStudent(false);
                    error.printStackTrace();
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
        mRequestQueue.add(request);
    }

    /**
     * @param student object with a new image
     */
    public void changeStudentImage(Student student) {
        Log.i("API", "changeStudentImage " + student.getstudentNumber());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        student.getImage().compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("APITOKEN", null);
        JSONObject put = new JSONObject();
        try {
            put = new JSONObject("{\"Picture\": \"" + Base64.encodeToString(b, Base64.DEFAULT) + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            APICallbacks.changedStudent(false);
        }
        System.out.println(put.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASEURL + "/api/student/" + student.getstudentNumber() + "/picture", put, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("API", "changeStudentImage successful");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401){
                    APICallbacks.invalidToken();
                } else {
                    APICallbacks.changedStudent(false);
                    error.printStackTrace();
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
        mRequestQueue.add(request);
    }
}
