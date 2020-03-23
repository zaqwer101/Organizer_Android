package ru.zaqwer101.organizer_android

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

var serverAddress: String = ""
var user: String = ""
var password: String = ""

fun trustAll()
{
    try {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {
                val acceptedIssuers: Array<Any?>?
                    get() = arrayOfNulls(0)

                override fun checkClientTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    TODO("Not yet implemented")
                }
            }
        )
        val sc: SSLContext = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
            override fun verify(arg0: String?, arg1: SSLSession?): Boolean {
                return true
            }
        })
    } catch (e: Exception) {
    }
}

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
//            Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_LONG).show()
        },
        Response.ErrorListener {
            Toast.makeText(applicationContext, "Volley error: $it", Toast.LENGTH_SHORT)
                .show()
        })
    VolleySingleton.getInstance(context).addToRequestQueue(request)
}

fun checkConnection(context: Context, applicationContext: Context): Boolean
{
    if (serverAddress == "" || user == "" || password == "")
    {
        return false
    }
    var token: String = ""
    val requestParams = HashMap<String, String>()
    requestParams["user"] = user
    requestParams["password"] = password

    httpRequest(requestParams, context, applicationContext, serverAddress, object : VolleyCallback {
        override fun onSuccessResponse(result: JSONObject) {
            token = result["token"].toString()
            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
        }
    }, Request.Method.POST)

    return token != ""
}