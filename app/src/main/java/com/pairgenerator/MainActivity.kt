package com.pairgenerator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var exampleDataProviderFragment: ExampleDataProviderFragment = ExampleDataProviderFragment()
    val draggableGridExampleFragment = DraggableGridExampleFragment()

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
        val projectName = TeamDataProvider().getListOfProjectName().get(selectedItem)
        getDataProvider().resetList(TeamDataProvider().getListOfMembers(projectName).toMutableList())
        draggableGridExampleFragment.notifyDataChange()
    }

    private val FRAGMENT_TAG_DATA_PROVIDER = "data provider"
    private val FRAGMENT_LIST_VIEW = "list view"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(exampleDataProviderFragment, FRAGMENT_TAG_DATA_PROVIDER)
                    .commit()
            supportFragmentManager.beginTransaction()
                    .add(R.id.layoutForRecyclerView, draggableGridExampleFragment, FRAGMENT_LIST_VIEW)
                    .commit()
        }

        val teams = TeamDataProvider().getListOfProjectName()

        val dropDown = findViewById<Spinner>(R.id.teamDropdown)
        dropDown.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teams)
        dropDown.onItemSelectedListener = this

        val btGenerate = findViewById<Button>(R.id.bt_generate)
        btGenerate.setOnClickListener { _ ->
            val dataProvider = getDataProvider()
            val list = generatePairs(dataProvider.getList())
            dataProvider.updateList(list)
            draggableGridExampleFragment.notifyDataChange()
        }
    }

    fun generatePairs(inputList: MutableList<PairDataProvider.ConcreteData>): MutableList<PairDataProvider.ConcreteData> {
        var pinnedIndies: MutableList<Int> = mutableListOf()
        var processedElements: MutableList<Int> = mutableListOf()
        var finalList: MutableList<PairDataProvider.ConcreteData> = mutableListOf()
        for (i in 0 until inputList.size) {
            if (inputList.get(i).isPinned) {
                pinnedIndies.add(i)
            }
        }

        processedElements.addAll(0, pinnedIndies)

        for (i in 0 until inputList.size) {
            var randomIndex: Int
            var element: PairDataProvider.ConcreteData
            if (pinnedIndies.size > 0 && pinnedIndies.contains(i)) {
                randomIndex = i
            } else {
                randomIndex = generateRandomIndex(0, inputList.size - 1, processedElements)
                processedElements.add(randomIndex)
            }

            element = inputList.get(randomIndex)
            finalList.add(i, element)
        }

        return finalList

    }

    fun getDataProvider(): AbstractDataProvider {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER)
        return (fragment as ExampleDataProviderFragment).getDataProvider()
    }

    fun generateRandomIndex(lowerIndex: Int, upperIndex: Int, indicesToSkip: MutableList<Int>): Int {
        val random: Int = (lowerIndex..upperIndex).random()
        if (indicesToSkip.contains(random)) {
            return generateRandomIndex(lowerIndex, upperIndex, indicesToSkip)
        }
        return random
    }

}

