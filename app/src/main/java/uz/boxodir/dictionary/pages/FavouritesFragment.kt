package uz.boxodir.dictionary.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.R
import uz.boxodir.dictionary.adaptoprs.FavouriteAdaptor
import com.example.dictionary.databinding.ActivityFavouritesBinding
import uz.boxodir.dictionary.dialog.MoreDialog
import uz.boxodir.dictionary.local.data.DictionaryDb


class FavouritesFragment:Fragment(R.layout.activity_favourites) {
    private lateinit var binding: ActivityFavouritesBinding
    private var adapter = FavouriteAdaptor()
    private lateinit var database: DictionaryDb

    private var cliclable:ImageView?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cliclable = null
        binding= ActivityFavouritesBinding.inflate(layoutInflater)

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.main_screen)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = DictionaryDb(view.context)
        showPlaceHolder(database.getAllFavouriteWords().count == 0)


        initAdapter()
        binding.backBtn.setOnClickListener {
            if (cliclable == null) {
                cliclable = binding.backBtn
                binding.backBtn.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.backBtn.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                findNavController().popBackStack()
                                cliclable = null

                            }.start()
                    }.start()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        binding.rvBookmark.adapter = adapter
        binding.rvBookmark.layoutManager = LinearLayoutManager(context)
        adapter.setCursor(database.getAllFavouriteWords())
        adapter.notifyDataSetChanged()
        adapter.setChangeListener { id, value, position ->
            database.updateWord(id, value)
            adapter.setCursor(database.getAllFavouriteWords())
            showPlaceHolder(database.getAllFavouriteWords().count == 0)
            adapter.notifyItemRemoved(position)
        }

        adapter.setLongClickListener {
            val dialog = MoreDialog(it)
            dialog.show(parentFragmentManager,"")
        }
    }

    private fun showPlaceHolder(isShow: Boolean) {
        binding.placeHolder.isVisible = isShow
    }
}