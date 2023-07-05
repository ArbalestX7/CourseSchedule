package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()
    private lateinit var nearestCourses: LiveData<Course?>

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    fun setNearestCourses(queryType: QueryType){
        nearestCourses = repository.getNearestSchedule(queryType)
    }

    fun getNearestCourse() = Transformations.switchMap(_queryType){
        repository.getNearestSchedule(it)
    }
}
