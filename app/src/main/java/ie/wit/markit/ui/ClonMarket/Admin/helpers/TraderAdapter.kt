package ie.wit.AdminFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_trader.view.*

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ie.wit.markit.ui.ClonMarket.ui_user.ClonTrader.ClonTraderFragment

interface TraderListener {
    fun onTraderClick(clonTrader: ClonTraderModel)
}

class traderAdapter(options: FirebaseRecyclerOptions<ClonTraderModel>,
                    private val listener: TraderListener?)
    : FirebaseRecyclerAdapter<ClonTraderModel,
        traderAdapter.TraderViewHolder>(options) {

    class TraderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(clonTrader: ClonTraderModel, listener: TraderListener) {
            with(clonTrader) {
                itemView.tag = clonTrader
                itemView.viewTitle.text = clonTrader.Title
                itemView.viewDescription.text = clonTrader.Description
                itemView.viewNumber.text = clonTrader.Number
                itemView.viewEmail.text = clonTrader.TraderEmail
                itemView.viewStart.text = clonTrader.TraderStart
                itemView.viewEnd.text = clonTrader.TraderEnd

                if(listener is ClonTraderFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.onTraderClick(clonTrader) }

//                if(clonTrader.isfavourite) itemView.imagefavourite.setImageResource(android.R.drawable.star_big_on)

                if(!clonTrader.profilepic.isEmpty()) {
                    Picasso.get().load(clonTrader.profilepic.toUri())
                        //.resize(180, 180)
                        .transform(jp.wasabeef.picasso.transformations.CropCircleTransformation())
                        .into(itemView.imageIcon)
                }
                else
                    itemView.imageIcon.setImageResource(ie.wit.R.mipmap.ic_launcher_round)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraderViewHolder {

        return TraderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_trader, parent, false))
    }

    override fun onBindViewHolder(holder: TraderViewHolder, position: Int, model: ClonTraderModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}