package uz.boxodir.dictionary.adaptoprs

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.R
import com.example.dictionary.databinding.ItemWordBinding

import uz.boxodir.dictionary.utils.spannable
import uz.boxodir.dictionary.models.DictionaryModel

class DictionaryAdapter() : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {
    private val data: ArrayList<DictionaryModel> = arrayListOf()
    private lateinit var cursor: Cursor
    private var query=""
    private var setOnItemChangeListener:((Int,Int,Int)->Unit)?=null
    private var longClickListener:((DictionaryModel)->Unit)?=null

    fun setCursor(cursor: Cursor) {
        this.cursor = cursor
    }

    fun setLongClickListener(block:(DictionaryModel)->Unit){
        longClickListener=block
    }

    fun setChangeListener(listener:(Int, Int,Int)->Unit){
        setOnItemChangeListener=listener
    }

    fun setQuery(query: String){
        this.query=query
    }



    inner class ViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView.setOnClickListener {
                val data=getWordByPosition(adapterPosition)
                if (data.isFavourite==0){
                    setOnItemChangeListener?.invoke(data.id,1,adapterPosition)
                }
                else{
                    setOnItemChangeListener?.invoke(data.id,0,adapterPosition)
                }
            }

            binding.root.setOnClickListener {
                longClickListener?.invoke(getWordByPosition(adapterPosition))
            }

        }

        @SuppressLint("SetTextI18n")
        fun bind(data: DictionaryModel) {
            binding.apply {
                if (query.isEmpty()) {
                    textEnglish.text = data.english
                    Log.d("TTT", "bind:EMPTY ")
                }else{
                    textEnglish.text=data.english.spannable(query,itemView.context)
                    Log.d("TTT", "bind:SPAN ")
                }
                typ.text="[${data.type}]"
                if (data.isFavourite==0){
                    imageView.setImageResource(R.drawable.no_favoritttt)
                }
                else{
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
        return dictionaryModel
    }
}