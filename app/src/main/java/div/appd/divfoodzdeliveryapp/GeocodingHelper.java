package div.appd.divfoodzdeliveryapp;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class GeocodingHelper {
    private static final String TAG = "GeocodingHelper";
    private static final String API_URL = "http://maps.googleapis.com/maps/api/geocode/json?";

    public static void geocode(Context context, double latitude, double longitude, final GeocodeCallback callback) {
        String urlString = API_URL + "latlng=" + latitude + "," + longitude;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String address = parseAddressFromResponse(response);
                        callback.onAddressFetched(address);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "API request failed: " + error.toString());
                        callback.onAddressFetchFailed();
                    }
                });

        queue.add(request);
    }

    private static String parseAddressFromResponse(JSONObject response) {
        try {
            JSONArray results = response.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject firstResult = results.getJSONObject(0);
                return firstResult.getString("formatted_address");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse API response: " + e.toString());
        }
        return "";
    }

    public interface GeocodeCallback {
        void onAddressFetched(String address);
        void onAddressFetchFailed();
    }
}
