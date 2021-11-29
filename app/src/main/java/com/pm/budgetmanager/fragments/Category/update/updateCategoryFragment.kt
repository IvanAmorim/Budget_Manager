package com.pm.budgetmanager.fragments.Category.update

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
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.entities.Category
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.radiogroup
import kotlinx.android.synthetic.main.fragment_update_category.view.*


class updateCategoryFragment : Fragment() {

    private val args by navArgs<updateCategoryFragmentArgs>()
    private lateinit var mCategoryViewmodel: CategoryViewmodel

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

        mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
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
        var transactionType: String = ""
        when(radiogroup.checkedRadioButtonId){
            R.id.rb_earnings ->  transactionType = "earnings"
            R.id.rb_expenses -> transactionType = "expenses"
        }

        if (inputcheck(transactionType)) {
            val updatedCategory = Category(args.currentCategory.id, name, transactionType)

            mCategoryViewmodel.updateCategory(updatedCategory)

            Toast.makeText(requireContext(), "Updated successfuly", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateCategoryFragment_to_listCategoryFragment)

        } else {
            return Toast.makeText(
                requireContext(),
                "Category name or type must have a value.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteCategory() {
        val builder = AlertDialog.Builder(requireContext())
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
        builder.create().show()
    }

    private fun inputcheck(transactionType:String): Boolean {
        return !(TextUtils.isEmpty(et_updatenameCategory.text.toString()) || TextUtils.isEmpty(
            transactionType))
    }


}