package com.example.daystillapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private var selectedDate: String? = null // Variable to store the selected date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the toolbar from the layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Set the toolbar as the ActionBar
        setSupportActionBar(toolbar)

        // Set the title of the toolbar programmatically
        supportActionBar?.title = ""

        // TextView to display the selected date
        val dateTextView = findViewById<TextView>(R.id.date_text_view)

        // Date picker button
        val datePickerButton = findViewById<Button>(R.id.date_picker_button)
        datePickerButton.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date as String (you can adjust the format)
                selectedDate = "${selectedMonth + 1}-$selectedDay-$selectedYear"

                // Display the selected date in the TextView
                dateTextView.text = "Selected Date: $selectedDate"
            }, year, month, day).show()
        }

        // Button to show the selected date
        val addFunctionalityButton = findViewById<Button>(R.id.center_button)
        addFunctionalityButton.setOnClickListener {
            // When the button is clicked, display the selected date if available
            if (selectedDate != null) {
                // If a date is selected, show it in a toast
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_LONG).show()

                // Alternatively, you could update the TextView (but it's already done in the DatePicker)
                dateTextView.text = "Selected Date: $selectedDate"
            } else {
                // If no date is selected, show a message to the user
                Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
