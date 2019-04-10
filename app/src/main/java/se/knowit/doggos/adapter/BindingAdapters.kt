package se.knowit.doggos.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("data")
    fun <T> setRecyclerViewItems(recyclerView: RecyclerView, data: T?) {
        if (recyclerView.adapter is BindableAdapter<*>) {
            @Suppress("UNCHECKED_CAST")
            (recyclerView.adapter as BindableAdapter<T>).setData(data)
        }
    }
}
