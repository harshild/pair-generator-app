package sdc.vw.com.pairgenerator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btGenerate = findViewById<Button>(R.id.bt_generate)
        btGenerate.setOnClickListener { _ ->  generatePairs(mutableListOf())}

    }

    fun generatePairs(inputList: MutableList<String>): List<Pair<String,String>> {
        Collections.shuffle(inputList)

        var finalList : MutableList<Pair<String,String>> = mutableListOf()
        val filter = (0 until inputList.size)
                .filter { it % 2 == 0 || (inputList.size%2 !=0 && inputList.size - 1 == it) }

        filter.mapTo(finalList) {
            if(inputList.size == it + 1)
                Pair(inputList[it], "No Pair")
            else
                Pair(inputList[it], inputList[it + 1])
        }

        return finalList

    }
}
