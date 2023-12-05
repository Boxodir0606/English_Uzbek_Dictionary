package uz.boxodir.dictionary.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.InfoFragmentBinding

class InfoScreen : Fragment(R.layout.info_fragment) {


    private var clickItem: ImageView? = null
    private lateinit var binding: InfoFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoFragmentBinding.inflate(layoutInflater)

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.dastur_haqida)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        funtionbuttons()

    }



    private fun funtionbuttons(){
        binding.backMenu.setOnClickListener {
            if (clickItem == null) {
                clickItem = binding.backMenu
                binding.backMenu.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.backMenu.animate()
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