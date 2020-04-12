package ie.wit.AdminFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import kotlinx.android.synthetic.main.card_trader.view.*

interface TraderListener {
    fun onTraderClick(clonTrader: ClonTraderModel)
}

class traderAdapter constructor(var clonTraders: ArrayList<ClonTraderModel>,
                                private val listener: TraderListener, reportall : Boolean)
    : RecyclerView.Adapter<traderAdapter.MainHolder>() {

    val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_trader,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val trader = clonTraders[holder.adapterPosition]
        holder.bind(trader,listener,reportAll)
    }

    override fun getItemCount(): Int = clonTraders.size

    fun removeAt(position: Int) {
        clonTraders.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(clonTrader: ClonTraderModel, listener: TraderListener, reportAll: Boolean) {
            itemView.tag = clonTrader
            itemView.viewTitle.text = clonTrader.Title
            itemView.viewDescription.text = clonTrader.Description
            itemView.viewNumber.text = clonTrader.Number
            itemView.viewEmail.text = clonTrader.TraderEmail
            itemView.viewStart.text = clonTrader.TraderStart
            itemView.viewEnd.text = clonTrader.TraderEnd

            if(!reportAll)
                itemView.setOnClickListener { listener.onTraderClick(clonTrader) }

            if(!clonTrader.profilepic.isEmpty()) {
                Picasso.get().load(clonTrader.profilepic.toUri())
                    //.resize(180, 180)
//                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            }
            else
                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher)
        }
    }
}