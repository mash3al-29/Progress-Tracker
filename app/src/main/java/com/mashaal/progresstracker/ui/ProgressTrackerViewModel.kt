package com.mashaal.progresstracker.ui

import androidx.lifecycle.ViewModel
import com.mashaal.progresstracker.models.Status
import com.mashaal.progresstracker.models.Task
import com.mashaal.progresstracker.models.TaskStatus
import com.mashaal.progresstracker.models.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow




class ProgressTrackerViewModel(): ViewModel() {
    companion object {
        const val PROGRESS_MAX_VALUE = 100f
        const val DEFAULT_SLIDER_POSITION = 0f
        const val DEFAULT_STREAK_DAYS = 0
    }

    val taskStatusOptions = TaskStatus.entries

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

    private val _taskName = MutableStateFlow("")
    val taskName = _taskName.asStateFlow()

    private val _taskDescription = MutableStateFlow("")
    val taskDescription = _taskDescription.asStateFlow()

    private var _sliderPosition = MutableStateFlow(DEFAULT_SLIDER_POSITION)
    val sliderPosition = _sliderPosition.asStateFlow()

    private val _selectedOption = MutableStateFlow(TaskStatus.InProgress)

    val selectedOption: StateFlow<TaskStatus> = _selectedOption

    private val _isAlertVisible = MutableStateFlow(false)
    val isAlertVisible = _isAlertVisible.asStateFlow()

    private val _alertExpanded = MutableStateFlow(false)
    val alertExpanded = _alertExpanded.asStateFlow()

    private val _expandedForFab = MutableStateFlow(false)

    val expandedForFab = _expandedForFab.asStateFlow()

    private val _alertTaskName = MutableStateFlow("")
    val alertTaskName = _alertTaskName.asStateFlow()

    private val _alertTaskDescription = MutableStateFlow("")
    val alertTaskDescription = _alertTaskDescription.asStateFlow()

    private val _alertSliderPosition = MutableStateFlow(0.0f)
    val alertSliderPosition = _alertSliderPosition.asStateFlow()

    private val _alertSelectedOption = MutableStateFlow(TaskStatus.InProgress)
    val alertSelectedOption: StateFlow<TaskStatus> = _alertSelectedOption

    private val _streakDays = MutableStateFlow(DEFAULT_STREAK_DAYS)
    val streakDays: StateFlow<Int> = _streakDays
    private var _taskBeingEdited: Task? = null


    fun updateFabVisibility(currentOffset: Int, currentIndex: Int) {
        val isScrollingUp = currentOffset < previousScrollOffset || currentIndex == 0
        _isFabVisible.value = isScrollingUp
        previousScrollOffset = currentOffset
    }

    fun toggleFab(){
        _expandedForFab.value = !_expandedForFab.value
    }

    fun setFabInvisible(){
        _expandedForFab.value = false
    }
    
    fun handleMiniFabClick(taskStatus: TaskStatus) {
        updateSelectedOption(taskStatus)
        showBottomSheet()
        setFabInvisible()
    }

    fun taskBeingEditedState(): Status?{
        return _taskBeingEdited?.status
    }



    fun updateCompletedMessage(value: String) {
        _completedMessage.value = value
    }


    fun saveNewTask(){
        val status = when(selectedOption.value) {
            TaskStatus.InProgress -> Status.InProgress(sliderPosition.value)
            TaskStatus.Completed -> Status.Completed(completedMessage.value)
            TaskStatus.Streak -> Status.Streak(_streakDays.value)
        }

        val task = Task(
            name = taskName.value,
            description = taskDescription.value,
            status = status
        )
        _allTasks.value = _allTasks.value + task
    }

    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun updateAlertSelectedOption(value: TaskStatus) {
        _alertSelectedOption.value = value
        if (value != TaskStatus.InProgress) {
            _alertSliderPosition.value = DEFAULT_SLIDER_POSITION
        }
    }


    fun updateSelectedOption(value: TaskStatus) {
        _selectedOption.value = value
        if (value != TaskStatus.InProgress) {
            _sliderPosition.value = DEFAULT_SLIDER_POSITION
        }
    }

    fun hideBottomSheet() {
        _isBottomSheetVisible.value = false
        _taskDescription.value = ""
        _taskName.value = ""
        _completedMessage.value = ""
        _selectedOption.value = TaskStatus.InProgress
        _sliderPosition.value = DEFAULT_SLIDER_POSITION
        _streakDays.value = DEFAULT_STREAK_DAYS
    }

    fun deleteTask(index: Int){
        val taskToDelete = _allTasks.value[index]
        _allTasks.value = _allTasks.value.filterNot { it.id == taskToDelete.id }
    }

    fun updateTaskName(value: String){
        _taskName.value = value
    }

    fun updateTaskDescription(value: String){
        _taskDescription.value = value
    }



    fun updateSliderPosition(value: Float){
        if (value == PROGRESS_MAX_VALUE) {
            _selectedOption.value = TaskStatus.Completed
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


    fun validateTaskForm(): ValidationResult {
        return when {
            taskName.value.isBlank() -> 
                ValidationResult.Error("Task name cannot be empty")
            selectedOption.value == TaskStatus.Completed && completedMessage.value.isBlank() -> 
                ValidationResult.Error("Completed tasks need a completion message")
            else -> ValidationResult.Success
        }
    }


    fun validateAlertForm(): ValidationResult {
        return when {
            alertTaskName.value.isBlank() -> 
                ValidationResult.Error("Task name cannot be empty")
            alertSelectedOption.value == TaskStatus.Completed && completedMessage.value.isBlank() -> 
                ValidationResult.Error("Completed tasks need a completion message")
            else -> ValidationResult.Success
        }
    }


    fun showAlert(task: Task) {
        _taskBeingEdited = task
        _isAlertVisible.value = true
        _alertTaskName.value = task.name
        _alertTaskDescription.value = task.description

        when (task.status) {
            is Status.InProgress -> {
                _alertSliderPosition.value = task.status.currentProgress
                _alertSelectedOption.value = TaskStatus.InProgress
            }
            is Status.Completed -> {
                _completedMessage.value = task.status.message
                _alertSelectedOption.value = TaskStatus.Completed
            }
            is Status.Streak -> {
                _streakDays.value = task.status.currentStreak
                _alertSelectedOption.value = TaskStatus.Streak
            }
        }
    }

    fun updateTask() {
        val originalTask = _taskBeingEdited ?: return
        // extra validation here from data side to not update the completed status information for any unexpected scenarios
        val updatedStatus = when (originalTask.status) {
            is Status.InProgress -> {
                when (_alertSelectedOption.value) {
                    TaskStatus.InProgress -> Status.InProgress(_alertSliderPosition.value)
                    TaskStatus.Completed -> Status.Completed(_completedMessage.value)
                    TaskStatus.Streak -> Status.InProgress(_alertSliderPosition.value)
                }
            }
            is Status.Streak -> {
                Status.Streak(_streakDays.value)
            }
            is Status.Completed -> {
                originalTask.status
            }
        }

        val updatedTask = originalTask.copy(
            name = _alertTaskName.value,
            description = _alertTaskDescription.value,
            status = updatedStatus
        )

        _allTasks.value = _allTasks.value.map {
            if (it.id == originalTask.id) updatedTask else it
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
        if (value == PROGRESS_MAX_VALUE) {
            _alertSelectedOption.value = TaskStatus.Completed
        }
        _alertSliderPosition.value = value
    }

    fun showAlertDropDown() {
        if (alertSelectedOption.value == TaskStatus.InProgress) {
            _alertExpanded.value = true
        }
    }

    fun hideAlertDropDown() {
        _alertExpanded.value = false
    }

}