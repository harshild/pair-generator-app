package sdc.vw.com.pairgenerator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val FRAGMENT_TAG_DATA_PROVIDER = "data provider"
    private val FRAGMENT_LIST_VIEW = "list view"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exampleDataProviderFragment = ExampleDataProviderFragment()

        val draggableGridExampleFragment = DraggableGridExampleFragment()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(exampleDataProviderFragment, FRAGMENT_TAG_DATA_PROVIDER)
                    .commit()
            supportFragmentManager.beginTransaction()
                    .add(R.id.layoutForRecyclerView, draggableGridExampleFragment, FRAGMENT_LIST_VIEW)
                    .commit()
        }


        val btGenerate = findViewById<Button>(R.id.bt_generate)
        btGenerate.setOnClickListener { _ ->
            val dataProvider = getDataProvider()
            val list = generatePairs(dataProvider.getList())
            dataProvider.updateList(list)
            draggableGridExampleFragment.notifyDataChange()
        }
    }

    fun generatePairs(inputList: MutableList<PairDataProvider.ConcreteData>): MutableList<PairDataProvider.ConcreteData> {
        var pinnedIndies : MutableList<Int> = mutableListOf()
        var processedElements: MutableList<Int> = mutableListOf()
        var finalList: MutableList<PairDataProvider.ConcreteData> = mutableListOf()
        for(i in 0 until inputList.size){
            if(inputList.get(i).isPinned){
                pinnedIndies.add(i)
            }
        }

        processedElements.addAll(0,pinnedIndies)

        for (i in 0 until inputList.size) {
            var randomIndex:Int
            var element:PairDataProvider.ConcreteData
            if(pinnedIndies.size > 0 && pinnedIndies.contains(i)){
                randomIndex = i
            }else {
                randomIndex = generateRandomIndex(0, inputList.size - 1, processedElements)
                processedElements.add(randomIndex)
            }

            element = inputList.get(randomIndex)
            finalList.add(i,element)
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

