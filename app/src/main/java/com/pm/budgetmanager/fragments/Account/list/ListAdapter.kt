package com.pm.budgetmanager.fragments.Account.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.pm.budgetmanager.API.models.Account
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.entities.Accounts
import kotlinx.android.synthetic.main.custom_row.view.*
import com.pm.budgetmanager.fragments.Account.list.ListAccountFragment as ListAccountFragment1

class ListAdapter(userIdInSession: String?): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var accountList = emptyList<Account>()
    private  val _userIdInSession = userIdInSession
    private  var  _ctx : Context? = null

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        _ctx = parent.context

        return ListAdapter.MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_row,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return accountList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAccount = accountList[position]
        holder.itemView.txt_id.text = currentAccount.id.toString()
        holder.itemView.txt_account.text = currentAccount.name
        holder.itemView.txt_balance.text = currentAccount.balance.toString()+" €"

        if(position%2 == 0){
            holder.itemView.rowLayout.setBackgroundColor(Color.parseColor("#1A5B92"))
        }
        else {
            holder.itemView.rowLayout.setBackgroundColor(Color.parseColor("#16679A"))
        }


       holder.itemView.rowLayout.setOnClickListener{
           if(_userIdInSession == currentAccount.users_id.toString()){
               val action =
                   ListAccountFragmentDirections.actionListAccountFragmentToUpdateFragment(
                       currentAccount
                   )
               holder.itemView.findNavController().navigate(action)
           }
           else {
               Toast.makeText(_ctx,R.string.only_edit_your_accounts, Toast.LENGTH_LONG).show()
           }
       }
    }

    fun setData(account: List<Account>){
        this.accountList = account
        notifyDataSetChanged()
    }


}