package com.pm.budgetmanager.fragments.Account.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.API.dto.AccountDto
import com.pm.budgetmanager.API.requests.AccountApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import kotlinx.android.synthetic.main.fragment_add_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAccountFragment: Fragment() {

   // private lateinit var mAccountViewModel: AccountViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_account, container, false)

        setHasOptionsMenu(true)

       // mAccountViewModel = ViewModelProvider(this).get(AccountViewmodel::class.java)


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
                R.string.fill_all_fields,
                Toast.LENGTH_LONG
            ).show()
        }

        /*
Local database

        mAccountViewModel.addAccount(account)

        Toast.makeText(
            requireContext(),
            "Product successfuly added.",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigate(R.id.action_addAccountFragment_to_listAccountFragment)
    */

        //API
        llProgressBarAddAccount.bringToFront()
        llProgressBarAddAccount.visibility = View.VISIBLE

        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.createAccount(
            token = "Bearer ${getToken()}",
            users_id = getUserIdInSession(),
            name = name_et.text.toString(),
            balance = balace_et.text.toString().toFloat()
        )

        call.enqueue(object : Callback<AccountDto> {
            override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                llProgressBarAddAccount.visibility = View.GONE

                if (response.isSuccessful) {
                    val report: AccountDto = response.body()!!

                    if (report.status == "OK") {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.success),
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_addAccountFragment_to_listAccountFragment)
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
                            findNavController().navigate(R.id.action_addAccountFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }
            override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                llProgressBarAddAccount.visibility = View.GONE
                somethingWentWrong()
            }
        })

    }

    private fun isValid():Boolean {
        return (TextUtils.isEmpty(name_et.text.toString()) || TextUtils.isEmpty(balace_et.text.toString()))
    }
}
