package uz.boxodir.dictionary.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.dictionary.databinding.InfoDialogBinding
import uz.boxodir.dictionary.local.data.DictionaryDb
import uz.boxodir.dictionary.models.DictionaryModel
import java.util.Locale

class MoreDialog(private val word: DictionaryModel) : DialogFragment(),
    TextToSpeech.OnInitListener {
    private lateinit var binding: InfoDialogBinding
    private var tts: TextToSpeech? = null
    private var clickItem: ImageView? = null
    private var isFav = 0
    private lateinit var database: DictionaryDb


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InfoDialogBinding.inflate(inflater, container, false)
        clickItem = null
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tts = TextToSpeech(requireContext(), this)
        database = DictionaryDb(requireContext())
        binding.textView6.text = word.english
        binding.textView9.text = word.uzbek
        binding.textView8.text = "[ ${word.type} ]"
        binding.textView7.text = "[ ${word.transcript} ]"
        binding.textView10.text = word.countable


        isFav = word.isFavourite
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)


        binding.closeDialog.setOnClickListener {
            if (clickItem == null) {
                clickItem = binding.closeDialog
                binding.closeDialog.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.closeDialog.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                clickItem = null
                                dialog?.dismiss()
                            }.start()
                    }.start()
            }
        }

        binding.textSound.setOnClickListener {
            if (clickItem == null) {
                clickItem = binding.textSound
                binding.textSound.animate()
                    .scaleX(0.7f)
                    .setDuration(200)
                    .scaleY(0.7f)
                    .withEndAction {
                        binding.textSound.animate()
                            .setDuration(90)
                            .scaleY(1f)
                            .scaleX(1f)
                            .withEndAction {
                                clickItem = null
                                speakOut()
                            }.start()
                    }.start()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                binding.textSound.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    private fun speakOut() {
        val text = binding.textView6.text
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}