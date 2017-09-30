package io.reyurnible.android.constraintradiogroup

import android.content.Context
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable

/**
 * ConstraintLayout CheckableItem Holder Group Layout
 */
class ConstraintRadioGroupLayout
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var onCheckedChangeListener: OnCheckedChangeListener? = null

    private var currentCheckItemId: Int? = null

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (checkableChildList.isEmpty()) {
            (child as? Checkable)?.let {
                it.isChecked = true
                currentCheckItemId = child?.id
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
        fun onCheckedChanged(group: ConstraintRadioGroupLayout, checkedId: Int)
    }

    private val ViewGroup.childList: List<View>
        get() = (0 until childCount).map { getChildAt(it) }

    private val ViewGroup.checkableChildList: List<Checkable>
        get() = childList.mapNotNull { it as? Checkable }
}
