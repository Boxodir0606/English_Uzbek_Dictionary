package uz.boxodir.dictionary.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentSplashBinding

class StartProgramma : Fragment(R.layout.fragment_splash) {


    private lateinit var binding: FragmentSplashBinding
    private var clicklable: AppCompatButton? = null
    private var clicklableImage: AppCompatImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clicklableImage = null
        clicklable = null
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        option()
    }

    private fun option() {
        binding.appCompatButton.setOnClickListener {

            if (clicklable == null) {
                clicklable = binding.appCompatButton
                binding.appCompatButton.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.appCompatButton.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                clicklable = null
                                findNavController().navigate(R.id.action_startProgramma_to_homeFragment2)
                            }.start()
                    }.start()
            }
        }

        binding.imageView6.setOnClickListener {
            if (clicklableImage == null) {
                clicklableImage = binding.imageView6
                binding.imageView6.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.imageView6.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                findNavController().navigate(R.id.action_startProgramma_to_infoScreen)
                                clicklableImage = null
                            }.start()
                    }.start()
            }
        }
    }
//    fun onBackPressed() {
//        AlertDialog.Builder(requireContext())
//            .setCancelable(false)
//            .setTitle("Chiqish")
//            .setPositiveButton("Ha") { dialogInterface: DialogInterface, i: Int ->
//                dialogInterface.dismiss()
//            }
//            .setMessage("O'yindan chiqmochimisiz!")
//            .setNegativeButton("Yo'q") { dialod: DialogInterface, v: Int -> dialod.dismiss() }
//            .show()
//    }


}