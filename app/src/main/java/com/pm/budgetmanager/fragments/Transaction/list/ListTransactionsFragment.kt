package com.pm.budgetmanager.fragments.Transaction.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.budgetmanager.API.models.Transactions
import com.pm.budgetmanager.API.requests.TransactionsApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import kotlinx.android.synthetic.main.fragment_list_transactions.*
import kotlinx.android.synthetic.main.fragment_list_transactions.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListTransactionsFragment : Fragment() {
   // private lateinit var mTransactionViewmodel: TransactionViewmodel

    private var _view : View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_transactions, container, false)
        _view = view

        getAndSetData(view)
        setHasOptionsMenu(true)

        /*
        //Recycler view
        val adapter = ListAdapterTransaction()
        val recyclerView = view.recyclerviewTransaction
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewmodel = ViewModelProvider(this).get(TransactionViewmodel::class.java)
        mTransactionViewmodel.readAllTransactions.observe(viewLifecycleOwner, Observer { transaction ->
            adapter.setData(transaction)
        })
*/
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

    private fun getAndSetData(view: View) {

        view.llProgressBarListTransaction.bringToFront()
        view.llProgressBarListTransaction.visibility = View.VISIBLE


        val adapter = ListAdapterTransaction(getUserIdInSession())

        val recyclerView = view.recyclerviewTransaction
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val request = ServiceBuilder.buildService(TransactionsApi::class.java)
        val call = request.getTransactions(token = "Bearer ${getToken()}")

        call.enqueue(object : Callback<List<Transactions>> {
            override fun onResponse(call: Call<List<Transactions>>, response: Response<List<Transactions>>) {

                llProgressBarListTransaction.visibility = View.GONE

                if (response.isSuccessful) {
                    val transaction: List<Transactions> = response.body()!!
                    adapter.setData(transaction)
                } else {
                    if (response.code() == 401) {
                        unauthorized(navigatonHandlder = {
                            findNavController().navigate(R.id.action_listTransactionsFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<List<Transactions>>, t: Throwable) {
                llProgressBarListTransaction.visibility = View.GONE
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
            findNavController().navigate(R.id.action_listTransactionsFragment_to_fragment_signIn)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString((R.string.logout_question)))
        builder.create().show()
    }

}