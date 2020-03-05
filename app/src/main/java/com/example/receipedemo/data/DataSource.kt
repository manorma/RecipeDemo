package com.example.receipedemo.data

public class DataSource {

    companion object{
        fun createTakList():List<Task> {
            var task = ArrayList<Task>()
            task.add(Task("Take out od the trash",true,3))
            task.add(Task("Walk the dog",false,2))
            task.add(Task("Make my bed",true,2))
            task.add(Task("Unload the diswasher",false,0))
            task.add(Task("Make dinner",true,5))
            return task

        }
    }
}
