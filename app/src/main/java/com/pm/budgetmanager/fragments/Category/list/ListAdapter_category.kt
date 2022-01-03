package com.pm.budgetmanager.fragments.Category.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.pm.budgetmanager.API.models.Categorys
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.entities.Accounts
import com.pm.budgetmanager.data.entities.Category
import com.pm.budgetmanager.fragments.Account.list.ListAdapter
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter_category(userIdInSession: String?): RecyclerView.Adapter<ListAdapter_category.MyViewHolder>() {

    private var categoryList = emptyList<Categorys>()
    private val _userIdInSession = userIdInSession
    private var _ctx : Context?=null


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {}

   // private var categoryList = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        _ctx = parent.context
       // return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
        return ListAdapter_category.MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    //@SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.itemView.txt_id.text = currentCategory.id.toString()
        holder.itemView.txt_account.text = currentCategory.name
        holder.itemView.txt_balance.text = currentCategory.transactionType

        if(position%2 == 0){
            holder.itemView.rowLayout.setBackgroundColor(Color.parseColor("#1A5B92"))
        }
        else {
            holder.itemView.rowLayout.setBackgroundColor(Color.parseColor("#16679A"))
        }

       holder.itemView.rowLayout.setOnClickListener {
           if (_userIdInSession == currentCategory.users_id.toString()) {
               val action =
                   listCategoryFragmentDirections.actionListCategoryFragmentToUpdateCategoryFragment(
                       currentCategory
                   )

               holder.itemView.findNavController().navigate(action)
           } else {
               Toast.makeText(_ctx, R.string.ony_edit_your_categorys, Toast.LENGTH_LONG).show()
           }
       }
    }

    fun setData(category : List<Categorys>){
        this.categoryList = category
        notifyDataSetChanged()
    }


}