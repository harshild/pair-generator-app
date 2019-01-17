package sdc.vw.com.pairgenerator

import android.os.Bundle
import androidx.fragment.app.Fragment


class ExampleDataProviderFragment : Fragment() {
    var mDataProvider: AbstractDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true  // keep the mDataProvider instance
        mDataProvider = PairDataProvider()
    }

    fun getDataProvider(): AbstractDataProvider {
        return this.mDataProvider!!
    }

}