package com.pm.budgetmanager.fragments.Account.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.budgetmanager.API.models.Account
import com.pm.budgetmanager.API.requests.AccountApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import kotlinx.android.synthetic.main.fragment_list_account.*
import kotlinx.android.synthetic.main.fragment_list_account.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAccountFragment : Fragment() {

    private  var  _view : View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_account, container, false)
        _view = view
        setHasOptionsMenu(true)

        getAndSetData(view)
/*
        //Recycler view
        val adapter = ListAdapter()
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        mAccountViewmodel.readAllAccounts.observe(viewLifecycleOwner, Observer { account ->
            adapter.setData(account)
        })
*/
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

    private fun getAndSetData(view: View) {

        view.llProgressBarListAccount.bringToFront()
        view.llProgressBarListAccount.visibility = View.VISIBLE


        val adapter = ListAdapter(getUserIdInSession())

        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.getAccounts(token = "Bearer ${getToken()}")

        call.enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {

                llProgressBarListAccount.visibility = View.GONE

                if (response.isSuccessful) {
                    val reports: List<Account> = response.body()!!
                    adapter.setData(reports)
                } else {
                    if (response.code() == 401) {
                        unauthorized(navigatonHandlder = {
                            findNavController().navigate(R.id.action_listAccountFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                llProgressBarListAccount.visibility = View.GONE
                somethingWentWrong()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.ic_logout) {
            logout()
        }

        if(item.itemId == R.id.ic_refresh){
            _view?.let { getAndSetData(it) }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun logout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
            preferences.edit().putString("token", null).apply()
            findNavController().navigate(R.id.action_listAccountFragment_to_fragment_signIn)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString((R.string.logout_question)))
        builder.create().show()
    }
}