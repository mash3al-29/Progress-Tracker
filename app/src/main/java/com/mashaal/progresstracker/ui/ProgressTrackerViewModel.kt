package com.mashaal.progresstracker.ui

import androidx.lifecycle.ViewModel
import com.mashaal.progresstracker.models.Status
import com.mashaal.progresstracker.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressTrackerViewModel(): ViewModel() {

    private val _isFabVisible = MutableStateFlow(true)
    val isFabVisible: StateFlow<Boolean> = _isFabVisible

    private var previousScrollOffset = 0

    private val _completedMessage = MutableStateFlow("")
    val completedMessage: StateFlow<String> = _completedMessage

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

    private var _taskBeingEdited: Task? = null

    fun updateFabVisibility(currentOffset: Int, currentIndex: Int) {
        val isScrollingUp = currentOffset < previousScrollOffset || currentIndex == 0
        _isFabVisible.value = isScrollingUp
        previousScrollOffset = currentOffset
    }

    fun updateCompletedMessage(value: String) {
        _completedMessage.value = value
    }

    fun saveNewTask(){
        val task = Task(
            name = textFieldTaskName.value,
            description = textFieldTaskDescription.value,
            initialProgress = sliderPosition.value,
            streakDays = 0,
            status = if(selectedOption.value == options[0]) Status.InProgress(sliderPosition.value) else Status.Completed(completedMessage.value)
        )
        _allTasks.value = _allTasks.value + task
    }
    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun updateAlertSelectedOption(value: String) {
        if (_sliderPosition.value < 100f) {
            _alertSelectedOption.value = value
        }
    }


    fun updateSelectedOption(value: String) {
        if (_sliderPosition.value < 100f) {
            _selectedOption.value = value
        }
    }

    fun hideBottomSheet() {
        _isBottomSheetVisible.value = false
        _textFieldTaskDescription.value = ""
        _textFieldTaskName.value = ""
        _completedMessage.value = ""
        _selectedOption.value = options[0]
        _sliderPosition.value = 0.0f
    }

    fun deleteTask(index: Int){
        val taskToDelete = _allTasks.value[index]
        _allTasks.value = _allTasks.value.filterNot { it == taskToDelete }
    }

    fun updateNameTextField(value: String){
        _textFieldTaskName.value = value
    }

    fun updateDescriptionTextField(value: String){
        _textFieldTaskDescription.value = value
    }

    fun updateSliderPosition(value: Float){
        if (value == 100f) {
            _selectedOption.value = options[1]
        }
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

    fun showAlert(task: Task) {
        _taskBeingEdited = task
        _isAlertVisible.value = true
        _alertTaskName.value = task.name
        _alertTaskDescription.value = task.description
        _alertSliderPosition.value = if (task.status is Status.InProgress) task.status.currentProgress else 0.0f
        _streakDays.value = task.streakDays
        _alertSelectedOption.value = if (task.status is Status.InProgress) options[0] else options[1]
    }

    fun updateTask() {
        val originalTask = _taskBeingEdited ?: return

        val updatedStatus = when (_alertSelectedOption.value) {
            options[1] -> Status.Completed(_completedMessage.value)
            else -> Status.InProgress(_alertSliderPosition.value)
        }

        val updatedTask = originalTask.copy(
            name = _alertTaskName.value,
            description = _alertTaskDescription.value,
            status = updatedStatus,
            streakDays = _streakDays.value
        )

        _allTasks.value = _allTasks.value.map {
            if (it == originalTask) updatedTask else it
        }

        _taskBeingEdited = null
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
        if (value == 100f) {
            _alertSelectedOption.value = options[1]
        }
        _alertSliderPosition.value = value
    }

    fun showAlertDropDown() {
        _alertExpanded.value = true
    }

    fun hideAlertDropDown() {
        _alertExpanded.value = false
    }

}