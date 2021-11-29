package com.pm.budgetmanager.fragments.Transaction.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.entities.Transactions
import kotlinx.android.synthetic.main.custom_row_transaction.view.*
import kotlinx.android.synthetic.main.custom_row_transaction.view.tv_categoryName

class ListAdapterTransaction: RecyclerView.Adapter<ListAdapterTransaction.MyViewHolder>() {

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {}

    private var transactionList = emptyList<Transactions>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.custom_row_transaction, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTransaction = transactionList[position]
        holder.itemView.tv_id.text = currentTransaction.id.toString()
        holder.itemView.tv_categoryName.text = currentTransaction.transactionCategory.toString()
        holder.itemView.tv_transactionValue.text = currentTransaction.value.toString()
        holder.itemView.tv_accountName.text = currentTransaction.account.toString()
        holder.itemView.tv_comments.text = currentTransaction.comments.toString()

        if(position%2 == 0){
            holder.itemView.rowLayoutTransaction.setBackgroundColor(Color.parseColor("#1A5B92"))
        }
        else {
            holder.itemView.rowLayoutTransaction.setBackgroundColor(Color.parseColor("#16679A"))
        }

        holder.itemView.rowLayoutTransaction.setOnClickListener{
            val action = ListTransactionsFragmentDirections.actionListTransactionsFragmentToUpdateTransaction(currentTransaction)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(transaction: List<Transactions>){
        this.transactionList = transaction
        notifyDataSetChanged()
    }


}