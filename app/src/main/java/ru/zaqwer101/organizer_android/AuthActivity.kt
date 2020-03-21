package ru.zaqwer101.organizer_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.google.gson.Gson
import org.json.JSONObject
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class AuthActivity : AppCompatActivity() {
    lateinit var editTextAddress: EditText
    lateinit var editTextUsername: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonApply: Button
    var address: String = ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        // For testing purposes
        trustAll()
        //

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonApply = findViewById(R.id.buttonApply)
        buttonApply.setOnClickListener {
            address = editTextAddress.text.toString()
            user = editTextUsername.text.toString()
            password = editTextPassword.text.toString()
            if (user == "" || password == "" || address == "")
            {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_LONG).show()
//                finish()
            }
            else
            {
                var response: String = ""
                val params = HashMap<String,String>()
                params["user"] = user
                params["password"] = password

                var out = httpRequest(params, this, applicationContext, address,
                    object : VolleyCallback {
                        override fun onSuccessResponse(result: JSONObject) {
                            Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
                        }
                    },
                    Request.Method.POST)

                var userData: User = User(user, password)
                var gson = Gson()
                var jsonString: String = gson.toJson(userData)
                saveJsonToFile(applicationContext, "auth_info.json", jsonString)
            }
        }
    }
}

