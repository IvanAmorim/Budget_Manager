package com.pm.budgetmanager



import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar

        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
       currentLanguage = intent.getStringExtra(currentLang).toString()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.language, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.english){
            setLocale("en")
        }
        if(item.itemId == R.id.portuguese){
            setLocale("pt")
        }
        if(item.itemId == R.id.maps){
            val intent = Intent(this@MainActivity, Maps::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

        override fun onSupportNavigateUp(): Boolean {
            val navController = findNavController(R.id.fragmentContainerView)

            return navController.navigateUp() || super.onSupportNavigateUp()
        }

    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@MainActivity, getString(R.string.Language_already_selected), Toast.LENGTH_SHORT).show()
        }
    }



}