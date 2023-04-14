package com.example.expandrecycerviewtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expandrecycerviewtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var personList: List<Person>
    private lateinit var adapter: ExpandableAdapter
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        personList = ArrayList()
        personList = loadData()

        binding.recyclerList.setHasFixedSize(true)
        binding.recyclerList.layoutManager = LinearLayoutManager(this)
        adapter = ExpandableAdapter(personList){model ->
        }
        binding.recyclerList.adapter = adapter
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