package sdc.vw.com.pairgenerator

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import java.util.*
import com.hootsuite.nachos.NachoTextView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get the spinner from the xml.
        val dropdown = findViewById<View>(R.id.spinner1) as Spinner
        val items = DataProvider().getListOfProjectName()
        val dropDownAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = dropDownAdapter
        val nameChips: NachoTextView = findViewById(R.id.nacho_text_view)

        dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                nameChips.setText(listOf())
                val suggestions = DataProvider().getListOfMembers(items[position])
                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, suggestions)
                nameChips.setAdapter(adapter);
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }



        val btGenerate = findViewById<Button>(R.id.bt_generate)
        btGenerate.setOnClickListener { _ ->
            var list = generatePairs(nameChips.chipValues)
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

