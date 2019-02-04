package sdc.vw.com.pairgenerator

abstract class AbstractDataProvider {

    abstract class Data {
        abstract val id: Long

        abstract val isSectionHeader: Boolean

        abstract val viewType: Int

        abstract val text: String

        abstract var isPinned: Boolean
    }

    abstract fun getCount(): Int

    abstract fun getItem(index: Int): Data

    abstract fun getItem(name: String): Data

    abstract fun removeItem(position: Int)

    abstract fun moveItem(fromPosition: Int, toPosition: Int)

    abstract fun swapItem(fromPosition: Int, toPosition: Int)

    abstract fun undoLastRemoval(): Int

    abstract fun getList(): MutableList<PairDataProvider.ConcreteData>

    abstract fun updateList(list: MutableList<PairDataProvider.ConcreteData>)

    abstract fun resetList(list: MutableList<String>)
}
