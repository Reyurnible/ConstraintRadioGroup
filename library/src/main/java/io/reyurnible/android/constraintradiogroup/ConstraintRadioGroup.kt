package io.reyurnible.android.constraintradiogroup

import android.content.Context
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable

/**
 * ConstraintLayout CheckableItem Holder Group
 */
class ConstraintRadioGroup
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : Group(context, attrs, defStyleAttr) {

    var onCheckedChangeListener: OnCheckedChangeListener? = null

    private var currentCheckItemId: Int? = null

    override fun setReferencedIds(ids: IntArray?) {
        super.setReferencedIds(ids)
        if (referencedCheckableChildList.isEmpty()) {
            referencedChildList.find { it is Checkable }?.let {
                (it as Checkable).isChecked = true
                currentCheckItemId = it.id
            }
        }
    }

    fun check(@IdRes id: Int) {
        (findViewById<View>(id) as? Checkable)?.let {
            clearCheck()
            it.isChecked = true
            currentCheckItemId = id
            onCheckedChangeListener?.onCheckedChanged(this, id)
        }
    }

    fun clearCheck() {
        (currentCheckItemId?.let { findViewById<View>(it) } as? Checkable)?.isChecked = false
        currentCheckItemId = null
    }

    @IdRes
    fun getCheckedItemId(): Int? = currentCheckItemId

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: ConstraintRadioGroup, checkedId: Int)
    }

    private val Group.referencedChildList: List<View>
        get() = (parent as ConstraintLayout).let { parent ->
            referencedIds.map { parent.findViewById<View>(it) }
        }

    private val Group.referencedCheckableChildList: List<Checkable>
        get() = referencedChildList.mapNotNull { it as? Checkable }

}
