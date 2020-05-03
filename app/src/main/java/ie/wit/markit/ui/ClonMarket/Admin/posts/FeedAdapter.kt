package ie.wit.AdminFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.ui_user.ClonTrader.ClonTraderFragment
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_feed.view.*
import kotlinx.android.synthetic.main.card_feed.view.Description
import kotlinx.android.synthetic.main.card_feed.view.imageIcon
import kotlinx.android.synthetic.main.card_trader.view.*
import kotlinx.android.synthetic.main.fragment_add_post.view.*

interface PostListener {
    fun onPostClick(clonTrader: ClonTraderModel)
}

class feedAdapter(options: FirebaseRecyclerOptions<ClonTraderModel>,
                    private val listener: PostListener?)
    : FirebaseRecyclerAdapter<ClonTraderModel,
        feedAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(clonTrader: ClonTraderModel, listener: PostListener) {
            with(clonTrader) {
                itemView.tag = clonTrader
            itemView.user.text = clonTrader.email
            itemView.post_title.text = clonTrader.Post_Title
            itemView.Description.text = clonTrader.PostBody

                if(listener is ViewMyPostsFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.onPostClick(clonTrader) }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_feed, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: ClonTraderModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}

//interface PostListener {
//    fun onPostClick(clonTrader: ClonTraderModel)
//}
//
//class feedAdapter constructor(var clonPosts: ArrayList<ClonTraderModel>,
//                                private val listener: PostListener, reportall : Boolean)
//    : RecyclerView.Adapter<feedAdapter.MainHolder>() {
//
//    val reportAll = reportall
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
//        return MainHolder(
//            LayoutInflater.from(parent?.context).inflate(
//                R.layout.card_feed,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MainHolder, position: Int) {
//        val trader = clonPosts[holder.adapterPosition]
//        holder.bind(trader,listener,reportAll)
//    }
//
//    override fun getItemCount(): Int = clonPosts.size
//
//    fun removeAt(position: Int) {
//        clonPosts.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        fun bind(clonTrader: ClonTraderModel, listener: PostListener, reportAll: Boolean) {
//            itemView.tag = clonTrader
//            itemView.current_time.text = clonTrader.CurrentTime
//            itemView.post_title.text = clonTrader.Post_Title
//            itemView.Description.text = clonTrader.PostBody
//
//            if(!reportAll)
//                itemView.setOnClickListener { listener.onPostClick(clonTrader) }
//
////            if(clonTrader.isfavourite) itemView.imagefavourite.setImageResource(android.R.drawable.star_big_on)
//
//            if(!clonTrader.profilepic.isEmpty()) {
//                Picasso.get().load(clonTrader.profilepic.toUri())
//                    //.resize(180, 180)
//                    .transform(CropCircleTransformation())
//                    .into(itemView.imageIcon)
//            }
//            else
//                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher)
//        }
//    }
//}