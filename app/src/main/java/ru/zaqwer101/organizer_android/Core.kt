package ru.zaqwer101.organizer_android

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.File
import java.io.IOException

var serverAddress: String = ""
var user: String = ""
var password: String = ""

fun loadJsonFromFile(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = File(context.filesDir, fileName).readText()
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun saveJsonToFile(context: Context, fileName: String, jsonString: String) {
    val file = File(context.filesDir, fileName)
    file.writeText(jsonString)
}

fun httpRequest(
    params: HashMap<String, String>,
    context: Context,
    applicationContext: Context,
    address: String,
    callback: VolleyCallback,
    requestMethod: Int
) {
    val jsonData = JSONObject(params)
    val queue = Volley.newRequestQueue(context)
    val url = """https://$address/auth"""

    val request = JsonObjectRequest(requestMethod, url, jsonData,
        Response.Listener { response ->
            callback.onSuccessResponse(response);
            Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show()
        },
        Response.ErrorListener {
            Toast.makeText(applicationContext, "Volley error: $it", Toast.LENGTH_SHORT)
                .show()
        })
    VolleySingleton.getInstance(context).addToRequestQueue(request)
}