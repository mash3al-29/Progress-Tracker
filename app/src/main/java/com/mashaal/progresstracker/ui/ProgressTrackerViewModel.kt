package com.mashaal.progresstracker.ui

import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.lifecycle.ViewModel
import com.mashaal.progresstracker.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressTrackerViewModel(): ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded = _expanded.asStateFlow()

    private val _textFieldTaskName = MutableStateFlow("")
    val textFieldTaskName = _textFieldTaskName.asStateFlow()

    private val _textFieldTaskDescription = MutableStateFlow("")
    val textFieldTaskDescription = _textFieldTaskDescription.asStateFlow()

    private var _sliderPosition = MutableStateFlow(0.0f)
    val sliderPosition = _sliderPosition.asStateFlow()

    val options = listOf("In Progress", "Completed")
    private val _selectedOption = MutableStateFlow(options[0])

    val selectedOption: StateFlow<String> = _selectedOption

    private val _isAlertVisible = MutableStateFlow(false)
    val isAlertVisible = _isAlertVisible.asStateFlow()

    private val _alertExpanded = MutableStateFlow(false)
    val alertExpanded = _alertExpanded.asStateFlow()

    private val _alertTaskName = MutableStateFlow("")
    val alertTaskName = _alertTaskName.asStateFlow()

    private val _alertTaskDescription = MutableStateFlow("")
    val alertTaskDescription = _alertTaskDescription.asStateFlow()

    private val _alertSliderPosition = MutableStateFlow(0.0f)
    val alertSliderPosition = _alertSliderPosition.asStateFlow()

    private val _alertSelectedOption = MutableStateFlow(options[0])
    val alertSelectedOption: StateFlow<String> = _alertSelectedOption

    private val _streakDays = MutableStateFlow(0)
    val streakDays: StateFlow<Int> = _streakDays

    fun saveNewTask(task: Task){
        _allTasks.value = _allTasks.value + task
    }
    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun updateSelectedOption(unit: String) {
        _selectedOption.value = unit
    }

    fun hideBottomSheet() {
        _isBottomSheetVisible.value = false
    }

    fun updateNameTextField(value: String){
        _textFieldTaskName.value = value
    }

    fun updateDescriptionTextField(value: String){
        _textFieldTaskDescription.value = value
    }

    fun updateSliderPosition(value: Float){
        _sliderPosition.value = value
    }

    fun showDropDown() {
        _expanded.value = true
    }

    fun hideDropDown() {
        _expanded.value = false
    }

    fun incrementStreak(){
        _streakDays.value++
    }

    fun decrementStreak(){
        _streakDays.value--
    }

    fun showAlert() {
        _isAlertVisible.value = true
    }

    fun hideAlert() {
        _isAlertVisible.value = false
    }

    fun updateAlertName(value: String) {
        _alertTaskName.value = value
    }

    fun updateAlertDescription(value: String) {
        _alertTaskDescription.value = value
    }

    fun updateAlertSlider(value: Float) {
        _alertSliderPosition.value = value
    }

    fun updateAlertSelectedOption(value: String) {
        _alertSelectedOption.value = value
    }

    fun showAlertDropDown() {
        _alertExpanded.value = true
    }

    fun hideAlertDropDown() {
        _alertExpanded.value = false
    }




}