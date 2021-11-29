package com.pm.budgetmanager.fragments.Account.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mAccountViewmodel: AccountViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        view.et_updateName.setText(args.currentAccount.name)
        view.et_updateBalance.setText(args.currentAccount.balance.toString())

        setHasOptionsMenu(true)

        mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save,menu)
        inflater.inflate(R.menu.delete,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save){
            updateAccount()
        }
        if(item.itemId == R.id.menu_delete){
            deleteAccount()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateAccount(){
        val account = et_updateName.text.toString()
        val balance = et_updateBalance.text.toString()

        if(inputcheck()){
            val updatedAccount = Accounts(args.currentAccount.Id,account,balance.toFloat())

            mAccountViewmodel.updateAccount(updatedAccount)

            Toast.makeText(requireContext(),"Updated successfuly",Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)

        }else{
            return Toast.makeText(
                requireContext(),
                "Account name or balance must have a value.",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun deleteAccount(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
        mAccountViewmodel.deleteAccount(args.currentAccount)
        Toast.makeText(requireContext(),"Successfuly removed: ${args.currentAccount.name}",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete ${args.currentAccount.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentAccount.name}")
        builder.create().show()


    }

    private fun inputcheck():Boolean{
        return !(TextUtils.isEmpty(et_updateName.text.toString()) || TextUtils.isEmpty(et_updateBalance.text.toString()))
    }


}