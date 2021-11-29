package com.pm.budgetmanager.fragments.Transaction.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.TransactionViewmodel
import kotlinx.android.synthetic.main.fragment_list_transactions.view.*


class ListTransactionsFragment : Fragment() {
    private lateinit var mTransactionViewmodel: TransactionViewmodel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_transactions, container, false)


        //Recycler view
        val adapter = ListAdapterTransaction()
        val recyclerView = view.recyclerviewTransaction
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewmodel = ViewModelProvider(this).get(TransactionViewmodel::class.java)
        mTransactionViewmodel.readAllTransactions.observe(viewLifecycleOwner, Observer { transaction ->
            adapter.setData(transaction)
        })

        view.floatingActionButtonTransaction.setOnClickListener{
            findNavController().navigate(R.id.action_listTransactionsFragment_to_addTransactionFragment)
        }

        view.bt_categoryT.setOnClickListener{
            findNavController().navigate(R.id.action_listTransactionsFragment_to_listCategoryFragment)
        }
        view.bt_AccountT.setOnClickListener{
            findNavController().navigate(R.id.action_listTransactionsFragment_to_listAccountFragment)
        }
        return view
    }
}