package com.pm.budgetmanager.fragments.Category.list

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
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import kotlinx.android.synthetic.main.fragment_list_account.view.*
import kotlinx.android.synthetic.main.fragment_list_category.view.*

class listCategoryFragment : Fragment() {
       private lateinit var mCategoryViewmodel: CategoryViewmodel

       override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           val view = inflater.inflate(R.layout.fragment_list_category, container, false)

           //Recycler view
           val adapter = ListAdapter_category()
           val recyclerView = view.recyclerview_category
           recyclerView.adapter = adapter
           recyclerView.layoutManager = LinearLayoutManager(requireContext())

           mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
           mCategoryViewmodel.readAllCategorys.observe(viewLifecycleOwner, Observer { account ->
               adapter.setData(account)
           })

           view.floatingActionButton_category.setOnClickListener{
               findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
           }

           view.bt_transactionC.setOnClickListener{
               findNavController().navigate(R.id.action_listCategoryFragment_to_listTransactionsFragment)
           }
           view.bt_AccountC.setOnClickListener{
               findNavController().navigate(R.id.action_listCategoryFragment_to_listAccountFragment)
           }
           return view
       }

   }