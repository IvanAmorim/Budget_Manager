package com.pm.budgetmanager.fragments.Category.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pm.budgetmanager.API.models.Categorys
import com.pm.budgetmanager.API.requests.CategorysApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import kotlinx.android.synthetic.main.fragment_list_category.*
import kotlinx.android.synthetic.main.fragment_list_category.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class listCategoryFragment : Fragment() {
      // private lateinit var mCategoryViewmodel: CategoryViewmodel

    private  var  _view : View? = null

       override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           val view = inflater.inflate(R.layout.fragment_list_category, container, false)
            _view=view
           setHasOptionsMenu(true)

           getAndSetData(view)

          /*
           //Recycler view
           val adapter = ListAdapter_category()
           val recyclerView = view.recyclerview_category
           recyclerView.adapter = adapter
           recyclerView.layoutManager = LinearLayoutManager(requireContext())

           mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
           mCategoryViewmodel.readAllCategorys.observe(viewLifecycleOwner, Observer { account ->
               adapter.setData(account)
           })
*/


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
    private fun getAndSetData(view: View) {

        view.llProgressBar.bringToFront()
        view.llProgressBar.visibility = View.VISIBLE


        val adapter = ListAdapter_category(getUserIdInSession())

        val recyclerView = view.recyclerview_category
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val request = ServiceBuilder.buildService(CategorysApi::class.java)
        val call = request.getCategorys(token = "Bearer ${getToken()}")

        call.enqueue(object : Callback<List<Categorys>> {
            override fun onResponse(call: Call<List<Categorys>>, response: Response<List<Categorys>>) {

                 llProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val category: List<Categorys> = response.body()!!
                    adapter.setData(category)
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

            override fun onFailure(call: Call<List<Categorys>>, t: Throwable) {
                llProgressBar.visibility = View.GONE
                somethingWentWrong()
            }
        })
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