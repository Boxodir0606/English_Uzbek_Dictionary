package uz.boxodir.dictionary.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.R
import uz.boxodir.dictionary.adaptoprs.DictionaryAdapter
import com.example.dictionary.databinding.FragmentMainBinding
import uz.boxodir.dictionary.dialog.MoreDialog
import uz.boxodir.dictionary.local.data.DictionaryDb
import uz.boxodir.dictionary.utils.hideKeyboard


@SuppressLint("NotifyDataSetChanged")
class HomeFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private var adapter = DictionaryAdapter()
    private lateinit var handler: Handler
    private lateinit var database: DictionaryDb
    private var search=""

    private var tts: TextToSpeech? = null
    private var isFav = 0

    private var clickItem: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        clickItem = null

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = DictionaryDb(requireContext())


        initAdapter()
        onCLick()
        attachSearch(view)
        onCLicked()
    }


    private fun initAdapter() {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
        adapter.setCursor(database.getAllWords())
        adapter.notifyDataSetChanged()


        adapter.setChangeListener { id, value, position ->
            database.updateWord(id, value)
            adapter.setCursor(database.getWordByEnglish(search))
            adapter.notifyItemChanged(position)
        }
    }

    private fun onCLick() {
        adapter.setLongClickListener {

            val dialog = MoreDialog(it)
            dialog.show(parentFragmentManager, "")
        }
    }

    private fun attachSearch(view: View) {
        binding.searchEditText.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                Log.d("TTT","close ishladi")
                view.hideKeyboard()
                return false
            }
        })
        handler = Handler(Looper.getMainLooper())
        binding.searchEditText.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    search = it
                    adapter.setCursor(database.getWordByEnglish(it.trim()))
                    showPlaceHolder(database.getWordByEnglish(it.trim()).count == 0)
                    adapter.setQuery(it.trim())
                    adapter.notifyDataSetChanged()
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        search =it
                        adapter.setCursor(database.getWordByEnglish(it.trim()))
                        showPlaceHolder(database.getWordByEnglish(it.trim()).count == 0)
                        adapter.setQuery(it.trim())
                        adapter.notifyDataSetChanged()
                    }
                }, 0)
                return true
            }
        })
    }


    private fun showPlaceHolder(isShow: Boolean) {
        binding.placeHolder.isVisible = isShow
    }

    private fun onCLicked() {
        binding.ivBookmarks.setOnClickListener {
            if (clickItem == null) {
                clickItem = binding.ivBookmarks
                binding.ivBookmarks.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.ivBookmarks.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                findNavController().navigate(R.id.action_homeFragment2_to_favouritesFragment)
                                clickItem = null
                            }.start()
                    }.start()
            }
        }

        binding.backs.setOnClickListener {
            if (clickItem == null) {
                clickItem = binding.backs
                binding.backs.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.backs.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                findNavController().popBackStack()
                                clickItem = null

                            }.start()
                    }.start()
            }
        }
    }
}