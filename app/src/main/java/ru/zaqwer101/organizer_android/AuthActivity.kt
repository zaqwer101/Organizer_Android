package ru.zaqwer101.organizer_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.google.gson.Gson
import org.json.JSONObject


class AuthActivity : AppCompatActivity() {
    lateinit var editTextAddress: EditText
    lateinit var editTextUsername: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // For testing purposes
        trustAll()
        //

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextUsername = findViewById(R.id.editTextUsername)

        // пытаемся загрузить информацию из файла
        val data = loadJsonFromFile(this, "auth_info.json")
        if (data != null)
        {
            val jsonData = JSONObject(data)
            user = jsonData["user"].toString()
            password = jsonData["password"].toString()
            serverAddress = jsonData["serverAddress"].toString()
        }

        editTextUsername.setText(user)
        editTextPassword.setText(password)
        editTextAddress.setText(serverAddress)


        buttonApply = findViewById(R.id.buttonApply)
        buttonApply.setOnClickListener {
            serverAddress = editTextAddress.text.toString()
            user = editTextUsername.text.toString()
            password = editTextPassword.text.toString()
            if (user == "" || password == "" || serverAddress == "")
            {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val params = HashMap<String,String>()
                params["user"] = user
                params["password"] = password
                params["serverAddress"] = serverAddress

                var out = httpRequest(params, this, applicationContext, serverAddress,
                    object : VolleyCallback {
                        override fun onSuccessResponse(result: JSONObject) {
//                            Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    Request.Method.POST)

                var authInfoData: AuthInfo = AuthInfo(user, password, serverAddress)
                var gson = Gson()
                var jsonString: String = gson.toJson(authInfoData)
                saveJsonToFile(applicationContext, "auth_info.json", jsonString)
                if (getToken(this, applicationContext) != null)
                    finish()
                else
                    Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG)
            }
        }
    }
}

