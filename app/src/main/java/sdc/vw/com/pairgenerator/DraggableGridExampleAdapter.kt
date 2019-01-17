package sdc.vw.com.pairgenerator

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder
import java.util.ArrayList

internal class DraggableGridExampleAdapter(private val mProvider: AbstractDataProvider) : RecyclerView.Adapter<DraggableGridExampleAdapter.MyViewHolder>(), DraggableItemAdapter<DraggableGridExampleAdapter.MyViewHolder> {
    private var mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_SWAP
    var isPinned: Boolean = false
    var viewHolder: MyViewHolder? = null
    var pinnedItems: MutableList<Int> = ArrayList()

    inner class MyViewHolder : AbstractDraggableItemViewHolder {
        var mContainer: FrameLayout
        var mTextView: TextView
        var mAnchorTextView: TextView
        var isPinned: Boolean = false

        constructor(v : View): super(v){
            mContainer = v.findViewById(R.id.container)
            mTextView = v.findViewById(android.R.id.text1)
            mAnchorTextView = v.findViewById(R.id.anchorText)

            mTextView.setOnClickListener { view ->
                val item = mProvider.getItem(mTextView.text.toString())
                if (item != null && !item.isPinned) {
                    item.isPinned = !item.isPinned
                    mAnchorTextView.setText("Anchor")
                    mContainer.setBackgroundColor(view.resources.getColor(R.color.bg_swipe_item_pinned))

                } else {
                    item.isPinned = !item.isPinned
                    mAnchorTextView.setText("")
                    mContainer.setBackgroundColor(view.resources.getColor(R.color.bg_item_normal_state))

                }

            }
        }
    }

    init {

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true)
    }

    fun setItemMoveMode(itemMoveMode: Int) {
        mItemMoveMode = itemMoveMode
    }

    override fun getItemId(position: Int): Long {
        return mProvider.getItem(position).id
    }

    override fun getItemViewType(position: Int): Int {
        return mProvider.getItem(position).viewType
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_grid_item, parent, false)

        return  MyViewHolder(v)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        val item = mProvider.getItem(position)

        // set text
        holder.mTextView.setText(item.text)

        // set background resource (target view ID: container)
        val dragState = holder.dragState

        if (dragState.isUpdated) {
            val bgResId: Int

            if (dragState.isActive) {
                bgResId = R.drawable.bg_item_dragging_active_state

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.foreground)
            } else if (dragState.isDragging) {
                bgResId = R.drawable.bg_item_dragging_state
            } else {
                bgResId = R.drawable.bg_item_normal_state
            }

            holder.mContainer.setBackgroundResource(bgResId)
        }
    }

    override fun getItemCount(): Int {
        return mProvider.getCount()
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {



        if (!mProvider.getItem(toPosition).isPinned) {
            Log.d(TAG, "onMoveItem1(fromPosition = $fromPosition, toPosition = $toPosition)")
            mProvider.swapItem(fromPosition, toPosition)
        }
    }

    override fun onCheckCanStartDrag(@NonNull holder: MyViewHolder, position: Int, x: Int, y: Int): Boolean {
        Log.d(TAG, "onCheckCanDrag(position = $position, x = $x y = $y")
        Log.d(TAG, "==="+mProvider.getItem(position).isPinned)

        return !mProvider.getItem(position).isPinned
    }

    override fun onGetItemDraggableRange(@NonNull holder: MyViewHolder, position: Int): ItemDraggableRange? {
        // no drag-sortable range specified
        return null
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        Log.d(TAG, "onCheckCanDrop(draggingPosition = $draggingPosition, dropPosition = $dropPosition)")
        return true
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "MyDraggableItemAdapter"
    }
}
