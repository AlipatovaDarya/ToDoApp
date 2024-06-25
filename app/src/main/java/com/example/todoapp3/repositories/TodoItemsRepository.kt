package com.example.todoapp3.repositories

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.utils.TodoItemListSource
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


class TodoItemsRepository {
    val sourceList = TodoItemListSource()
    val todoItems = MutableLiveData(sourceList.list)
    val uncompletedItems = MutableLiveData(getUncompletedItems())
    val completedItemsCounter = MutableLiveData(((
            uncompletedItems.value?.size?.let {
                todoItems.value?.size?.minus(
                    it
                )
            }) ?: 0))

    fun addNewItem(item: TodoItem) {
        todoItems.value?.add(item)
        getCompletedItemsCounter()
    }

    fun getItemById(id: String): TodoItem? {
        for (item in (todoItems.value ?: emptyList<TodoItem>())) {
            if (item.id == id) {
                return item
            }
        }
        return null
    }


    fun deleteItemById(id: String) {
        var i = 0
        while (i < (todoItems.value?.size ?: 0)){
            if((todoItems.value?.get(i)?.id ?: -1) == id){
                todoItems.value?.removeAt(index = i)
                i--
            }
            i++
        }
        getCompletedItemsCounter()
    }

    fun getUncompletedItems(): MutableList<TodoItem> {
        val res = mutableListOf<TodoItem>()
        for (item in todoItems.value ?: emptyList()) {
            if (!item.isCompleted) {
                res.add(item)
            }
        }
        return res
    }

    private fun getCompletedItemsCounter(){
        getUncompletedItems()
        completedItemsCounter.value = (
                uncompletedItems.value?.size?.let {
                    todoItems.value?.size?.minus(
                        it
                    )
                }) ?: 0
    }

    fun editItem(item : TodoItem) {
        deleteItemById(item.id)
        todoItems.value?.add(item)
        getCompletedItemsCounter()
    }


}