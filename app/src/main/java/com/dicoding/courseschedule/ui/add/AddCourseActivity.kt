package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private var spinnerValue: Int = 0
    private var startTime: String = ""
    private var endTime: String = ""
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = AddViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this,factory)[AddCourseViewModel::class.java]

        supportActionBar?.title = getString(R.string.add_course)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                insertCourse()
                viewModel.saved.observe(this) { event ->
                    event.getContentIfNotHandled()?.let { isSaved ->
                        if (isSaved) {
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddCourseActivity,
                                getString(R.string.input_empty_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun insertCourse() {
        val courseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
        val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
        val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()

        viewModel.insertCourse(
            courseName = courseName,
            lecturer = lecturer,
            note = note,
            day = spinnerValue,
            startTime = startTime,
            endTime = endTime,
        )
        Log.d("insertCourse: name", courseName)
        Log.d("insertCourse: lecture", lecturer)
        Log.d("insertCourse: note", note)
        Log.d("insertCourse: day", spinnerValue.toString())
        Log.d("insertCourse: start", startTime)
        Log.d("insertCourse: end", endTime)
    }

    fun spinnerDialog() {
        val daySpinner: Spinner = findViewById(R.id.spinner_day)
        daySpinner.selectedItemPosition
        spinnerValue = daySpinner.selectedItemPosition
    }

    fun showTimePicker(view: View) {
        val dialog = TimePickerFragment()
        dialog.show(supportFragmentManager,"timePicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag) {
            "startTimePicker" -> {
                findViewById<TextView>(R.id.tv_start_time).text = dateFormat.format(calendar.time)
                startTime = calendar.time.toString()
            }
            "endTimePicker" -> {
                findViewById<TextView>(R.id.tv_end_time).text = dateFormat.format(calendar.time)
                endTime = calendar.time.toString()
            }
        }
    }
}