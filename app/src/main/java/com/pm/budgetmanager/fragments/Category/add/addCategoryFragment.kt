package com.pm.budgetmanager.fragments.Category.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.entities.Category
import kotlinx.android.synthetic.main.fragment_add_category.*


class addCategoryFragment : Fragment() {


    private lateinit var mCategoryViewModel: CategoryViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)

        mCategoryViewModel = ViewModelProvider(this).get(CategoryViewmodel::class.java)

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
            return Toast.makeText(
                requireContext(),
                "Category name must have a value.",
                Toast.LENGTH_LONG
            ).show()
        }



        val category = Category(0,et_nameCategory.text.toString(),transactionType.toString() )

        mCategoryViewModel.addCategory(category)

        Toast.makeText(
            requireContext(),
            "Category successfuly added.",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigate(R.id.action_addCategoryFragment_to_listCategoryFragment)
    }

    private fun isValid(transactionType:String):Boolean {
       return (TextUtils.isEmpty(et_nameCategory.text.toString()) || TextUtils.isEmpty(transactionType.toString()))

    }
}