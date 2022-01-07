package com.pm.budgetmanager.fragments.Transaction.list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pm.budgetmanager.API.models.Transactions
import com.pm.budgetmanager.R
import kotlinx.android.synthetic.main.custom_row_transaction.view.*

class ListAdapterTransaction(userIdInSession: String?): RecyclerView.Adapter<ListAdapterTransaction.MyViewHolder>() {

    private var transactionList = emptyList<Transactions>()
    private val _userIdInSession = userIdInSession
    private var _ctx : Context?=null


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {}

 //   private var transactionList = emptyList<Transactions>()
  //  private var categoryList = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        _ctx = parent.context

      /*  return MyViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.custom_row_transaction, parent, false)
        )*/
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_row_transaction,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTransaction = transactionList[position]

        holder.itemView.tv_id.text = currentTransaction.id.toString()
        holder.itemView.tv_categoryName.text = currentTransaction.category_name
        holder.itemView.tv_transactionValue.text = currentTransaction.value.toString()
        holder.itemView.tv_accountName.text = currentTransaction.account_Name
        holder.itemView.tv_comments.text = currentTransaction.comments

        if(position%2 == 0){
            holder.itemView.rowLayoutTransaction.setBackgroundColor(Color.parseColor("#1A5B92"))
        }
        else {
            holder.itemView.rowLayoutTransaction.setBackgroundColor(Color.parseColor("#16679A"))
        }

        holder.itemView.rowLayoutTransaction.setOnClickListener{
            if(_userIdInSession == currentTransaction.users_id.toString()){
                val action = ListTransactionsFragmentDirections.actionListTransactionsFragmentToUpdateTransaction(currentTransaction)
                holder.itemView.findNavController().navigate(action)
            }
            else {
                Toast.makeText(_ctx,R.string.only_edit_your_transactions, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun setData(transaction: List<Transactions>){
        this.transactionList = transaction
        notifyDataSetChanged()
    }


}