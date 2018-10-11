package sdc.vw.com.pairgenerator

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btGenerate = findViewById<Button>(R.id.bt_generate)
        btGenerate.setOnClickListener { _ ->
            var list = generatePairs(mutableListOf("Harshil", "Sumit", "Abhinav", "Amey","Ashish","Neelam"))
            initTable(list)
        }
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

    fun initTable(list: List<Pair<String,String>>) {
        val ll = findViewById<TableLayout>(R.id.displayLinear)
        ll.removeAllViewsInLayout()
        addNewRow(Pair("FIRST","SECOND"),ll,0)

        list.forEach { element ->
            addNewRow(element, ll,1)

        }

    }

    private fun addNewRow(element: Pair<String, String>, ll: TableLayout, rowIndex: Int) {
        val row = TableRow(this)
        val lp = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        row.layoutParams = lp

        addElementToRow(element.first, row)
        addElementToRow(element.second,row)

        ll.addView(row, rowIndex)
    }

    private fun addElementToRow(element: String, row: TableRow) {


        val gd = GradientDrawable()
        gd.setColor(Color.GRAY) // Changes this drawbale to use a single color instead of a gradient
        gd.cornerRadius = 5f
        gd.setStroke(1, Color.BLACK)


        val tv_element = TextView(this)
        tv_element.setTextColor(ColorStateList.valueOf(Color.WHITE))
        tv_element.setPadding(20, 20, 20, 20)
        tv_element.text = element

        tv_element.setBackground(gd);
        row.addView(tv_element)


    }

}

