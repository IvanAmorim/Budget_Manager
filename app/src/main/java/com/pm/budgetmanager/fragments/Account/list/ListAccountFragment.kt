package com.pm.budgetmanager.fragments.Account.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import kotlinx.android.synthetic.main.fragment_list_account.view.*

class ListAccountFragment : Fragment() {

    private lateinit var mAccountViewmodel: AccountViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_account, container, false)

        setHasOptionsMenu(true)

        //Recycler view
        val adapter = ListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        mAccountViewmodel.readAllAccounts.observe(viewLifecycleOwner, Observer { account ->
            adapter.setData(account)
        })

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listAccountFragment_to_addAccountFragment)
        }
        view.bt_category.setOnClickListener{
            findNavController().navigate(R.id.action_listAccountFragment_to_listCategoryFragment)
        }
        view.bt_transaction.setOnClickListener{
            findNavController().navigate(R.id.action_listAccountFragment_to_listTransactionsFragment)
        }




        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.login, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.signin) {
            openlogin()
        }

        return super.onOptionsItemSelected(item)
    }

    private  fun openlogin(){
        findNavController().navigate(R.id.action_image2_to_fragment_signIn)
    }
}