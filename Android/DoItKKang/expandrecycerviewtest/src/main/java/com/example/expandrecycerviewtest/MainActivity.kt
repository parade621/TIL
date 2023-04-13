package com.example.expandrecycerviewtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var personList: List<Person>
    private lateinit var adapter: ExpandableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_list)

        personList = ArrayList()
        personList = loadData()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpandableAdapter(personList){model ->
            Log.d("로그봐라0","${model.name}이거 클릭됨")

        }
        recyclerView.adapter = adapter

    }

    private fun loadData(): List<Person> {
        val people = ArrayList<Person>()

        val persons = resources.getStringArray(R.array.people)
        val images = resources.obtainTypedArray(R.array.images)

        for (i in persons.indices) {
            val person = Person().apply {
                name = persons[i]
                image = images.getResourceId(i, -1)
            }
            people.add(person)
        }
        return people
    }
}