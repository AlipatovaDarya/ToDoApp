package com.example.todoapp3.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp3.model.Importance
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.repositories.TodoItemsRepository
import java.time.LocalDate

class TodoItemsViewModel : ViewModel() {
    private val rep = TodoItemsRepository()
    val visibilityIsOn = MutableLiveData(true)
    val todoItems : MutableLiveData<MutableList<TodoItem>> = rep.todoItems
    val uncompletedItems : MutableLiveData<MutableList<TodoItem>> = rep.uncompletedItems
    var curItem = MutableLiveData<TodoItem?>(null)
    val completedItemsCounter = rep.completedItemsCounter

    fun addNewItem(item: TodoItem) {
        rep.addNewItem(item)
    }

    fun editItem(
        text: String? = curItem.value?.text,
        importance: Importance = Importance.MEDIUM,
        deadline: LocalDate? = curItem.value?.deadline,
        isCompleted: Boolean? = curItem.value?.isCompleted,
        modifiedDate: LocalDate? = curItem.value?.modifiedDate,
    ){
        if(curItem.value != null){
            val item = TodoItem(
                curItem.value?.id!!,
                text!!,
                importance,
                deadline,
                isCompleted!!,
                curItem.value?.creationDate!!,
                modifiedDate
            )
            rep.editItem(item)
        }
    }

    fun getItemById(id: String) {
        curItem.value = rep.getItemById(id)
    }

    fun deleteItemById(id: String) {
        rep.deleteItemById(id)
    }
}