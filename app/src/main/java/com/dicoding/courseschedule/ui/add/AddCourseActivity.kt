package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
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
        val start = findViewById<ImageButton>(R.id.ib_start_time).toString()
        val end = findViewById<ImageButton>(R.id.ib_end_time).toString()
        val day: Spinner = findViewById(R.id.spinner_day)
        val dayvm = day.selectedItemPosition.toString().toInt()

        viewModel.insertCourse(
            courseName = courseName,
            lecturer = lecturer,
            note = note,
            day = dayvm,
            startTime = start,
            endTime = end,
        )
        Log.d("insertCourse: course:", courseName)
        Log.d("insertCourse: lecture:",lecturer)
        Log.d("insertCourse: note:",note)
        Log.d("insertCourse: day:",dayvm.toString())
        Log.d("insertCourse: start:",start)
        Log.d("insertCourse: end:",end)
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
                findViewById<TextView>(R.id.tv_start_course).setText(dateFormat.format(calendar.timeInMillis))
            }
            "endTimePicker" -> {
                findViewById<TextView>(R.id.tv_end_time).text = dateFormat.format(calendar.time)
            }
        }
    }
}