package com.pm.budgetmanager.fragments.Account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.R
import kotlinx.android.synthetic.main.fragment_list_account.view.*

class ListAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_account, container, false)

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listAccountFragment_to_addAccountFragment)

        }
        return view
    }

}