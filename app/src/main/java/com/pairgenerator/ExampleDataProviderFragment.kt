package com.pairgenerator

import android.os.Bundle
import androidx.fragment.app.Fragment


class ExampleDataProviderFragment : Fragment() {
    var mDataProvider: AbstractDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true  // keep the mDataProvider instance
        mDataProvider = PairDataProvider(TeamDataProvider().getListOfMembers(TeamDataProvider().getListOfProjectName().get(0)).toMutableList())
    }

    fun getDataProvider(): AbstractDataProvider {
        return this.mDataProvider!!
    }
}