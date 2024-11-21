package com.example.daystillapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the toolbar from the layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Set the toolbar as the ActionBar
        setSupportActionBar(toolbar)

        // Set the title of the toolbar programmatically
        supportActionBar?.title = ""

        // Set up the button click listener
        val button = findViewById<Button>(R.id.center_button)
        button.setOnClickListener {
            // Handle the button click
            // For example, show a toast message
            Toast.makeText(this, "Very Nice!!", Toast.LENGTH_SHORT).show()
        }
    }
}
