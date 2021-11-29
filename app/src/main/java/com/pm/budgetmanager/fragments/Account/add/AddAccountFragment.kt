package com.pm.budgetmanager.fragments.Account.add

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import kotlinx.android.synthetic.main.fragment_add_account.*
import java.util.*

class AddAccountFragment: Fragment() {

    private lateinit var mAccountViewModel: AccountViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_account, container, false)

        setHasOptionsMenu(true)

        mAccountViewModel = ViewModelProvider(this).get(AccountViewmodel::class.java)


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save){
            addAccount()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun addAccount(){
        if(isValid()){
            return Toast.makeText(
                requireContext(),
                "Product name must have a value.",
                Toast.LENGTH_LONG
            ).show()
        }

        val account = Accounts(0,name_et.text.toString(), balace_et.text.toString().toFloat())

        mAccountViewModel.addAccount(account)

        Toast.makeText(
            requireContext(),
            "Product successfuly added.",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigate(R.id.action_addAccountFragment_to_listAccountFragment)
    }

    private fun isValid():Boolean {
        return (TextUtils.isEmpty(name_et.text.toString()) || TextUtils.isEmpty(balace_et.text.toString()))
    }
}
