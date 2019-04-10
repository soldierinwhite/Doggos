package se.knowit.doggos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import se.knowit.doggos.adapter.BreedsAdapter
import se.knowit.doggos.databinding.ActivityMainBinding
import se.knowit.doggos.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private var adapter: BreedsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        setSupportActionBar(toolbar)

        toolbar.setOnClickListener {
            app_bar.setExpanded(true)
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.model = viewModel
        adapter = BreedsAdapter(viewModel)
        breedsRecycler.adapter = adapter
        breedsRecycler.setHasFixedSize(true)
        viewModel.fetchBreeds()

        fab.setOnClickListener { view ->
            adapter?.setData(listOf())
            viewModel.refresh()
        }

        filterEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filter = s.toString()
                if (filter.length > 1) {
                    adapter?.setFilter(filter)
                } else {
                    adapter?.setFilter("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }
        })
    }
}
