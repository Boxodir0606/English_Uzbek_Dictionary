package uz.boxodir.dictionary.pages


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.DialogWordBinding
import uz.boxodir.dictionary.local.data.DictionaryDb
import uz.boxodir.dictionary.models.DictionaryModel
import java.util.Locale

class MeaningWordFragment : Fragment(R.layout.dialog_word), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isFav = 0
    private lateinit var database: DictionaryDb
    private lateinit var binding: DialogWordBinding

    private var cliclab: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DictionaryDb(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        cliclab == null
        binding = DialogWordBinding.inflate(layoutInflater)

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.info_screen)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = this.arguments
        val data = args?.getSerializable("data") as DictionaryModel
        binding.englishWord.text = data.english
        binding.textView2.text = data.uzbek
        binding.textView4.text = data.transcript
        binding.textView5.text = data.countable
        binding.textView3.text = data.type
        isFav = data.isFavourite

        binding.imageView5.setImageResource(if (isFav == 1) R.drawable.is_favoritttt else R.drawable.no_favoritttt)

        binding.imageView5.setOnClickListener {
            if (cliclab == null) {
                cliclab = binding.imageView5
                binding.imageView5.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.imageView5.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                cliclab = null
                                isFav = if (isFav == 1) {
                                    binding.imageView5.setImageResource(R.drawable.no_favoritttt)
                                    0
                                } else {
                                    binding.imageView5.setImageResource(R.drawable.is_favoritttt)
                                    1
                                }
                                database.updateWord(data.id, isFav)
                            }.start()
                    }.start()
            }
        }

        binding.imageView2.setOnClickListener {
            if (cliclab == null) {
                cliclab = binding.imageView2
                binding.imageView2.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.imageView2.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                findNavController().popBackStack()
                                cliclab = null

                            }.start()
                    }.start()
            }

        }

        tts = TextToSpeech(requireContext(), this)

        binding.imageView3.setOnClickListener {
            speakOut()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                binding.imageView3.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = binding.englishWord.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}