package se.knowit.doggos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import kotlinx.android.synthetic.main.item_breed.view.*
import se.knowit.doggos.R
import se.knowit.doggos.data.entity.Breed
import se.knowit.doggos.viewmodel.MainViewModel

class BreedsAdapter(
    val viewModel: MainViewModel
) : RecyclerView.Adapter<BreedsAdapter.ViewHolder>(), BindableAdapter<List<Breed>> {
    private val breeds = mutableListOf<BreedWithImage>()
    private val filteredBreeds = mutableListOf<BreedWithImage>()
    private var filter: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false))
    }

    override fun getItemCount(): Int = filteredBreeds.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredBreeds[position], viewModel) { url, pos ->
            updateItemPicture(url, pos)
        }
    }

    override fun setData(data: List<Breed>?) {
        data?.let {
            breeds.clear()
            breeds.addAll(data.map { it.toBreedWithImage() })
            filter()
        }
    }

    fun setFilter(filter: String) {
        this.filter = filter
        filter()
    }

    private fun filter() {
        val tempFiltered = filteredBreeds.toList()
        filteredBreeds.clear()
        filter?.takeIf { it.isNotEmpty() }?.let { tempFilter ->
            filteredBreeds.addAll(breeds.filter {
                it.name.contains(tempFilter, true) ||
                        it.subBreeds.any { subBreed -> subBreed.contains(tempFilter, true) }
            })
        } ?: filteredBreeds.addAll(breeds)
        if (tempFiltered.size != filteredBreeds.size) {
            notifyDataSetChanged()
        }
    }

    private fun updateItemPicture(imageUrl: String, position: Int) {
        breeds[position].imageUrl = imageUrl
        notifyItemChanged(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(breedWithImage: BreedWithImage, viewModel: MainViewModel, updateImageListener: (String, Int) -> Unit) {
            if (breedWithImage.imageUrl != null) {
                itemView.image_loading_progress.visibility = GONE
                Glide.with(itemView)
                    .load(breedWithImage.imageUrl)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .centerCrop()
                    .error(R.drawable.error_image)
                    .into(itemView.breedImage)
            } else {
                itemView.image_loading_progress.visibility = VISIBLE
                Glide.with(itemView)
                    .load(R.raw.doggos_progress_bar)
                    .into(DrawableImageViewTarget(itemView.image_loading_progress))
                viewModel.fetchImageForBreed(breedWithImage.name) { url ->
                    if (url != null) {
                        updateImageListener(url, adapterPosition)
                    } else {
                        itemView.breedImage.setImageResource(R.drawable.error_image)
                    }
                }
            }
            itemView.breed_name.text = breedWithImage.name.capitalize()
        }
    }
}

data class BreedWithImage(
    val name: String,
    val subBreeds: List<String>,
    var imageUrl: String? = null
)

fun Breed.toBreedWithImage() = BreedWithImage(name, subBreeds)
