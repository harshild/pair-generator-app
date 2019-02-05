package sdc.vw.com.pairgenerator

import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils

class DraggableGridExampleFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: DraggableGridExampleAdapter? = null
    private var mWrappedAdapter: RecyclerView.Adapter<*>? = null
    private var mRecyclerViewDragDropManager: RecyclerViewDragDropManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun notifyDataChange() {
        mAdapter?.notifyDataSetChanged()
        mWrappedAdapter?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mRecyclerView = getView()!!.findViewById(R.id.recycler_view)
        mLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        // drag & drop manager
        mRecyclerViewDragDropManager = RecyclerViewDragDropManager()
        mRecyclerViewDragDropManager!!.setDraggingItemShadowDrawable(
                ContextCompat.getDrawable(this.context!!, R.drawable.material_shadow_z3) as NinePatchDrawable)
        // Start dragging after long press
        mRecyclerViewDragDropManager!!.setInitiateOnLongPress(true)
        mRecyclerViewDragDropManager!!.setInitiateOnMove(false)
        mRecyclerViewDragDropManager!!.setLongPressTimeout(750)

        // setup dragging item effects (NOTE: DraggableItemAnimator is required)
        mRecyclerViewDragDropManager!!.dragStartItemAnimationDuration = 250
        mRecyclerViewDragDropManager!!.draggingItemAlpha = 0.8f
        mRecyclerViewDragDropManager!!.draggingItemScale = 1.3f
        mRecyclerViewDragDropManager!!.draggingItemRotation = 15.0f

        //adapter
        val myItemAdapter = DraggableGridExampleAdapter(getDataProvider())
        mAdapter = myItemAdapter

        mWrappedAdapter = mRecyclerViewDragDropManager!!.createWrappedAdapter(myItemAdapter)      // wrap for dragging

        val animator = DraggableItemAnimator() // DraggableItemAnimator is required to make item animations properly.

        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = mWrappedAdapter  // requires *wrapped* adapter
        mRecyclerView!!.itemAnimator = animator

        // additional decorations

        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView!!.addItemDecoration(ItemShadowDecorator(ContextCompat.getDrawable(context!!, R.drawable.material_shadow_z1) as NinePatchDrawable))
        }

        mRecyclerViewDragDropManager!!.attachRecyclerView(mRecyclerView!!)

        // for debugging
        //        animator.setDebug(true);
        //        animator.setMoveDuration(2000);
    }

    override fun onPause() {
        mRecyclerViewDragDropManager!!.cancelDrag()
        super.onPause()
    }

    override fun onDestroyView() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager!!.release()
            mRecyclerViewDragDropManager = null
        }

        if (mRecyclerView != null) {
            mRecyclerView!!.itemAnimator = null
            mRecyclerView!!.adapter = null
            mRecyclerView = null
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter)
            mWrappedAdapter = null
        }
        mAdapter = null
        mLayoutManager = null

        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

    }

//    private void updateItemMoveMode(boolean swapMode) {
//        int mode = (swapMode)
//                ? RecyclerViewDragDropManager.ITEM_MOVE_MODE_SWAP
//                : RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
//
//        mRecyclerViewDragDropManager.setItemMoveMode(mode);
//        mAdapter.setItemMoveMode(mode);
//
//        Snackbar.make(getView(), "Item move mode: " + (swapMode ? "SWAP" : "DEFAULT"), Snackbar.LENGTH_SHORT).show();
//    }

    private fun supportsViewElevation(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun getDataProvider(): AbstractDataProvider {
        return (activity as MainActivity).getDataProvider()
    }
}
