package ie.wit.AdminFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_feed.view.*

interface PostListener {
    fun onDonationClick(clonTrader: ClonTraderModel)
}

class feedAdapter constructor(var clonPosts: ArrayList<ClonTraderModel>,
                                private val listener: PostListener, reportall : Boolean)
    : RecyclerView.Adapter<feedAdapter.MainHolder>() {

    val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_feed,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val trader = clonPosts[holder.adapterPosition]
        holder.bind(trader,listener,reportAll)
    }

    override fun getItemCount(): Int = clonPosts.size

    fun removeAt(position: Int) {
        clonPosts.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(clonTrader: ClonTraderModel, listener: PostListener, reportAll: Boolean) {
            itemView.tag = clonTrader
            itemView.name.text = clonTrader.Title
            itemView.Description.text = clonTrader.PostBody

            if(!reportAll)
                itemView.setOnClickListener { listener.onDonationClick(clonTrader) }

            if(!clonTrader.profilepic.isEmpty()) {
                Picasso.get().load(clonTrader.profilepic.toUri())
                    //.resize(180, 180)
                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            }
            else
                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher)
        }
    }
}