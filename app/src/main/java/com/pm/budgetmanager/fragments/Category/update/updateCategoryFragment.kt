package com.pm.budgetmanager.fragments.Category.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pm.budgetmanager.API.dto.CategoryDto
import com.pm.budgetmanager.API.requests.CategorysApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class updateCategoryFragment : Fragment() {

    private val args by navArgs<updateCategoryFragmentArgs>()
    //private lateinit var mCategoryViewmodel: CategoryViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_category, container, false)

        view.et_updatenameCategory.setText(args.currentCategory.name)
        when(args.currentCategory.transactionType){
            "expenses" -> view.radiogroup.check(R.id.rb_expenses)
            "earnings" -> view.radiogroup.check(R.id.rb_earnings)
        }
        setHasOptionsMenu(true)

        //mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save, menu)
        inflater.inflate(R.menu.delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateCategory()
        }
        if (item.itemId == R.id.menu_delete) {
            deleteCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateCategory() {
        val name = et_updatenameCategory.text.toString()
        var transactionType = ""
        when(radiogroup.checkedRadioButtonId){
            R.id.rb_earnings ->  transactionType = "earnings"
            R.id.rb_expenses -> transactionType = "expenses"
        }

        if (inputcheck(transactionType)) {
          /*  val updatedCategory = Category(args.currentCategory.id, name, transactionType)

            mCategoryViewmodel.updateCategory(updatedCategory)

            Toast.makeText(requireContext(), "Updated successfuly", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateCategoryFragment_to_listCategoryFragment)

        } else {
            return Toast.makeText(
                requireContext(),
                "Category name or type must have a value.",
                Toast.LENGTH_LONG
            ).show()
        }*/

            val request = ServiceBuilder.buildService(CategorysApi::class.java)
            val call = request.updateCategorys(
                token = "Bearer ${getToken()}",
                id = args.currentCategory.id,
                name = name,
                transactionType = transactionType
            )

            call.enqueue(object : Callback<CategoryDto> {
                override fun onResponse(call: Call<CategoryDto>, response: Response<CategoryDto>) {
                    if (response.isSuccessful) {
                        val report: CategoryDto = response.body()!!

                        if (report.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.successfull_updated_category),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_updateCategoryFragment_to_listCategoryFragment)
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
                                findNavController().navigate(R.id.action_updateCategoryFragment_to_fragment_signIn)
                            })
                        } else {
                            somethingWentWrong()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryDto>, t: Throwable) {
                    somethingWentWrong()
                }
            })

        }else{
            return Toast.makeText(
                requireContext(),
                getString(R.string.fill_name_and_transactiontype),
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun deleteCategory() {
       /* val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mCategoryViewmodel.deleteCategory(args.currentCategory)
            Toast.makeText(
                requireContext(), "Successfuly removed: ${args.currentCategory.name}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateCategoryFragment_to_listCategoryFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentCategory.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentCategory.name}")
        builder.create().show()*/

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->

            val request = ServiceBuilder.buildService(CategorysApi::class.java)
            val call = request.deleteCategory(
                token = "Bearer ${getToken()}",
                id = args.currentCategory.id
            )

            call.enqueue(object : Callback<CategoryDto> {
                override fun onResponse(call: Call<CategoryDto>, response: Response<CategoryDto>) {
                    if (response.isSuccessful) {
                        val report: CategoryDto = response.body()!!

                        if(report.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.successfull_deleted_category),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_updateCategoryFragment_to_listCategoryFragment)
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
                                findNavController().navigate(R.id.action_updateCategoryFragment_to_fragment_signIn)
                            })
                        }
                        else {
                            somethingWentWrong()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryDto>, t: Throwable) {
                    somethingWentWrong()
                }
            })
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.delete_category))
        builder.setMessage(getString(R.string.question_delete_category))
        builder.create().show()
    }

    private fun inputcheck(transactionType:String): Boolean {
        return !(TextUtils.isEmpty(et_updatenameCategory.text.toString()) || TextUtils.isEmpty(
            transactionType))
    }


}