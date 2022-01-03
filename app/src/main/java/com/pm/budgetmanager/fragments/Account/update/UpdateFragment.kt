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
import com.pm.budgetmanager.API.dto.AccountDto
import com.pm.budgetmanager.API.requests.AccountApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
   // private lateinit var mAccountViewmodel: AccountViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        view.et_updateName.setText(args.currentAccount.name)
        view.et_updateBalance.setText(args.currentAccount.balance.toString())

        setHasOptionsMenu(true)

       // mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
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
        val balance = et_updateBalance.text.toString().toFloat()

        if(inputcheck(account,balance)){
            /*val updatedAccount = Accounts(args.currentAccount.id,account,balance.toFloat())

            mAccountViewmodel.updateAccount(updatedAccount)

            Toast.makeText(requireContext(),"Updated successfuly",Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)
*/
            /***update
            id = args.currentAccount.id,
            name = et_updateName.text.toString(),
            balance = et_updateBalance.text.toString().toFloat()
             **/
            update(args.currentAccount.id,account,balance)

        }else{
            return Toast.makeText(
                requireContext(),
                getString(R.string.fill_title_and_description),
                Toast.LENGTH_LONG
            ).show()
            /*return Toast.makeText(
                requireContext(),
                "Account name or balance must have a value.",
                Toast.LENGTH_LONG
            ).show()*/
        }

    }

    fun update(id : Int, name : String, balance : Float){
        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.updateAccount(
            token = "Bearer ${getToken()}",
            id = id,
            name = name,
            balance = balance
        )

        call.enqueue(object : Callback<AccountDto> {
            override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                if (response.isSuccessful) {
                    val report: AccountDto = response.body()!!

                    if (report.status == "OK") {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.successfull_updated_account),
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)
                    } else {
                        Toast.makeText(
                            requireContext(), getString(
                                resources.getIdentifier(
                                    report.message, "string",
                                    context?.packageName
                                )
                            ), Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (response.code() == 401) {
                        unauthorized(navigatonHandlder = {
                            findNavController().navigate(R.id.action_updateFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                somethingWentWrong()
            }
        })
    }

    private fun deleteAccount(){
        /*val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
       // mAccountViewmodel.deleteAccount(args.currentAccount)
        Toast.makeText(requireContext(),"Successfuly removed: ${args.currentAccount.name}",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete ${args.currentAccount.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentAccount.name}")
        builder.create().show()
*/
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            val request = ServiceBuilder.buildService(AccountApi::class.java)
            val call = request.deleteAccount(
                token = "Bearer ${getToken()}",
                id = args.currentAccount.id
            )

            call.enqueue(object : Callback<AccountDto> {
                override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                    if (response.isSuccessful) {
                        val report: AccountDto = response.body()!!

                        if(report.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.successfull_deleted_account),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_updateFragment_to_listAccountFragment)
                        }
                        else {
                            Toast.makeText(
                                requireContext(), getString(
                                    resources.getIdentifier(
                                        report.message, "string",
                                        context?.packageName
                                    )
                                ), Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        if(response.code() == 401){
                            unauthorized(navigatonHandlder = {
                                findNavController().navigate(R.id.action_updateFragment_to_fragment_signIn)
                            })
                        }
                        else {
                            somethingWentWrong()
                        }
                    }
                }

                override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                    somethingWentWrong()
                }
            })
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.delete_account))
        builder.setMessage(getString(R.string.question_delete_account))
        builder.create().show()
    }

    private fun inputcheck(name : String, balance : Float):Boolean{
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(balance.toString()))
    }


}