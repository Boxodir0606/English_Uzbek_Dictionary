package uz.boxodir.dictionary.adaptoprs

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.R
import com.example.dictionary.databinding.ItemWordBinding
import uz.boxodir.dictionary.models.DictionaryModel

class FavouriteAdaptor : RecyclerView.Adapter<FavouriteAdaptor.ViewHolder>() {
    private lateinit var cursor: Cursor
    private var setOnItemChangeListener: ((Int, Int, Int) -> Unit)? = null
    private var longClickListener: ((DictionaryModel) -> Unit)? = null


    fun setCursor(cursor: Cursor) {
        this.cursor = cursor
    }


    fun setChangeListener(listener: (Int, Int, Int) -> Unit) {
        setOnItemChangeListener = listener
    }

    fun setLongClickListener(listener: (DictionaryModel) -> Unit) {
        longClickListener = listener
    }


    inner class ViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView.setOnClickListener {
                val data = getWordByPosition(adapterPosition)
                if (data.isFavourite == 0) {
                    setOnItemChangeListener?.invoke(data.id, 1, adapterPosition)
                } else {
                    setOnItemChangeListener?.invoke(data.id, 0, adapterPosition)
                }
            }
            binding.root.setOnClickListener {
                val data = getWordByPosition(adapterPosition)
                longClickListener?.invoke(data)
            }

        }

        fun bind(data: DictionaryModel) {
            binding.apply {
                textEnglish.text = data.english
                typ.text = data.type
                if (data.isFavourite == 0) {
                    imageView.setImageResource(R.drawable.no_favoritttt)
                } else {
                    imageView.setImageResource(R.drawable.is_favoritttt)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cursor.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getWordByPosition(position))
    }

    private fun getWordByPosition(position: Int): DictionaryModel {
        cursor.moveToPosition(position)
        @SuppressLint("Range")
        val dictionaryModel = DictionaryModel(
            cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("english")),
            cursor.getString(cursor.getColumnIndex("type")),
            cursor.getString(cursor.getColumnIndex("transcript")),
            cursor.getString(cursor.getColumnIndex("uzbek")),
            cursor.getString(cursor.getColumnIndex("countable")),
            cursor.getInt(cursor.getColumnIndex("is_favourite")),
        )
        Log.d("TTT", "getWordByPosition:$dictionaryModel ")
        return dictionaryModel
    }
}