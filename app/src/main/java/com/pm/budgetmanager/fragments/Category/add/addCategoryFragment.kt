package com.pm.budgetmanager.fragments.Category.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.API.dto.CategoryDto
import com.pm.budgetmanager.API.requests.CategorysApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.entities.Category
import kotlinx.android.synthetic.main.fragment_add_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class addCategoryFragment : Fragment() {


    private lateinit var mCategoryViewModel: CategoryViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)

       // mCategoryViewModel = ViewModelProvider(this).get(CategoryViewmodel::class.java)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save){
            addCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addCategory(){
        var transactionType: String = ""
        when(radiogroup.checkedRadioButtonId){
            R.id.rb_earnings ->  transactionType = "earnings" //Toast.makeText(requireContext(),"Earnings selected",Toast.LENGTH_SHORT).show()
            R.id.rb_expenses -> transactionType = "expenses"//Toast.makeText(requireContext(),"expenses selected",Toast.LENGTH_SHORT).show()
        }

        if(isValid(transactionType)){
            Toast.makeText(
                requireContext(),
                R.string.fill_all_fields,
                Toast.LENGTH_LONG
            ).show()
        }else {
/*
        val category = Category(0,et_nameCategory.text.toString(),transactionType.toString() )

        mCategoryViewModel.addCategory(category)

        Toast.makeText(
            requireContext(),
            "Category successfuly added.",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigate(R.id.action_addCategoryFragment_to_listCategoryFragment)
    */

            val request = ServiceBuilder.buildService(CategorysApi::class.java)
            val call = request.createCategorys(
                token = "Bearer ${getToken()}",
                name = et_nameCategory.text.toString(),
                transactionType = transactionType,
                users_id = getUserIdInSession()
            )

            call.enqueue(object : Callback<CategoryDto> {
                override fun onResponse(call: Call<CategoryDto>, response: Response<CategoryDto>) {
                    //llProgressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        val report: CategoryDto = response.body()!!

                        if (report.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.success),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_addCategoryFragment_to_listCategoryFragment)
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
                                findNavController().navigate(R.id.action_addCategoryFragment_to_fragment_signIn)
                            })
                        } else {
                            somethingWentWrong()
                            Toast.makeText(
                                requireContext(), "API", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryDto>, t: Throwable) {
                    // llProgressBar.visibility = View.GONE
                    somethingWentWrong()
                    Toast.makeText(
                        requireContext(), "onFailure", Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }



    private fun isValid(transactionType:String):Boolean {
       return (TextUtils.isEmpty(et_nameCategory.text.toString()) || TextUtils.isEmpty(transactionType.toString()))

    }
}