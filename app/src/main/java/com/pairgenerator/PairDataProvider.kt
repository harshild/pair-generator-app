package com.pairgenerator

import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import java.util.*

class PairDataProvider : AbstractDataProvider {
    override fun resetList(list: MutableList<String>) {
        mData.clear()
        for (i in 0 until list.size)
            createView(list.get(i))
    }

    override fun getItem(name: String): Data {
        for (i in 0 until mData.size) {
            if (mData.get(i).text.equals(name))
                return mData.get(i)
        }
        throw java.lang.IndexOutOfBoundsException("koi na")
    }

    override fun getList(): MutableList<ConcreteData> {
        return mData;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun updateList(list: MutableList<ConcreteData>) {
        mData.clear()
        mData.addAll(0, list)
    }

    private val mData: MutableList<ConcreteData>
    private var mLastRemovedData: ConcreteData? = null
    private var mLastRemovedPosition = -1

    constructor(devList: MutableList<String>) {
        mData = LinkedList<ConcreteData>()
        for (j in 0 until devList.size) {
            createView(devList.get(j))
        }
    }

    private fun createView(text: String) {
        val id = mData.size.toLong()
        val viewType = 0
        val swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP or RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN
        mData.add(ConcreteData(id, viewType, text, swipeReaction))
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(index: Int): Data {
        if (index < 0 || index >= getCount()) {
            throw IndexOutOfBoundsException("index = $index")
        }

        return mData[index]
    }

    override fun undoLastRemoval(): Int {
        if (mLastRemovedData != null) {
            val insertedPosition: Int
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size) {
                insertedPosition = mLastRemovedPosition
            } else {
                insertedPosition = mData.size
            }

            mData.add(insertedPosition, mLastRemovedData!!)

            mLastRemovedData = null
            mLastRemovedPosition = -1

            return insertedPosition
        } else {
            return -1
        }
    }

    override fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) {
            return
        }

        val item = mData.removeAt(fromPosition)

        mData.add(toPosition, item)
        mLastRemovedPosition = -1
    }

    override fun swapItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) {
            return
        }

        Collections.swap(mData, toPosition, fromPosition)
        mLastRemovedPosition = -1
    }

    override fun removeItem(position: Int) {

        val removedItem = mData.removeAt(position)

        mLastRemovedData = removedItem
        mLastRemovedPosition = position
    }

    class ConcreteData internal constructor(override val id: Long, override val viewType: Int, @NonNull text: String, swipeReaction: Int) : Data() {
        @NonNull
        override val text: String = text
        override var isPinned: Boolean = false

        override val isSectionHeader: Boolean
            get() = false

        @NonNull
        override fun toString(): String {
            return text
        }
    }

}