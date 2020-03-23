package ru.zaqwer101.organizer_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    lateinit var testButton: Button
    lateinit var mainLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testButton = findViewById(R.id.testButton)
        mainLinearLayout = findViewById(R.id.mainLinearLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(getString(R.string.menu_item_serversettings))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title.toString() == getString(R.string.menu_item_serversettings))
        {
            // переходим в меню настроек сервера
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item)
    }
}