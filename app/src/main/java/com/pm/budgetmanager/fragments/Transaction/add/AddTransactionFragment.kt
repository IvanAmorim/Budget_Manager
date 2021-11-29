package com.pm.budgetmanager.fragments.Transaction.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Dao.AccountDao_Impl
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.Viewmodel.TransactionViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import com.pm.budgetmanager.data.entities.Category
import com.pm.budgetmanager.data.entities.Transactions
import com.pm.budgetmanager.databinding.FragmentAddTransactionBinding
import kotlinx.android.synthetic.main.fragment_add_transaction.*

class addTransactionFragment : Fragment() {

    private lateinit var mCategoryViewmodel: CategoryViewmodel
    private lateinit var mAccountViewmodel: AccountViewmodel

    private var categoryList = emptyList<Category>()
    private var accountList = emptyList<Accounts>()
    private val branchListAcount: MutableList<String> = ArrayList()
    private val branchListCategory: MutableList<String> = ArrayList()


    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    var selectedDate: String =""

    private lateinit var mTransactionViewModel: TransactionViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        /** Spinner Category**/
        val branchListCategoryAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListCategory
        )

        val mySpinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory);

        mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
        mCategoryViewmodel.readAllCategorys.observe(viewLifecycleOwner, Observer {
            this.categoryList = it
            categoryList.forEach { category ->
                branchListCategory.add(category.name)
            }
            branchListCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinnerCategory.setAdapter(branchListCategoryAdapter)
        })

        /** Spinner Account**/
        val branchListAccountAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListAcount
        )

        val mySpinnerAccount = view.findViewById<Spinner>(R.id.spinnerAccount);

        mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        mAccountViewmodel.readAllAccounts.observe(viewLifecycleOwner, Observer {
            this.accountList = it
            accountList.forEach {
                branchListAcount.add(it.name)
            }
            branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinnerAccount.setAdapter(branchListAccountAdapter)
        })


        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewmodel::class.java)

        setHasOptionsMenu(true)

        /**Date Picker**/
            _binding = FragmentAddTransactionBinding.bind(view)

            binding.apply {
                btnSelectDate.setOnClickListener {
                    // create new instance of DatePickerFragment
                    val datePickerFragment = DatePickerFragment()
                    val supportFragmentManager = requireActivity().supportFragmentManager

                    // we have to implement setFragmentResultListener
                    supportFragmentManager.setFragmentResultListener(
                        "REQUEST_KEY",
                        viewLifecycleOwner
                    ) { resultKey, bundle ->
                        if (resultKey == "REQUEST_KEY") {
                            val date = bundle.getString("SELECTED_DATE")
                            tvSelectedDate.text = date
                            selectedDate=date.toString()
                        }
                    }

                    // show
                    datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                }
            }
        /**End date picker**/
    return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save){
            if(isValid()){
                 Toast.makeText(
                    requireContext(),
                    getString(R.string.isValid),
                    Toast.LENGTH_LONG
                ).show()
            }else
            addTransaction()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun addTransaction(){

        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        val result : String
        val accountSpinner: String

        result= mySpinner.selectedItem.toString()
        accountSpinner = mySpinnerAccount.selectedItem.toString()

        val value:Float
        value = etn_value.text.toString().toFloat()

        val transaction = Transactions(0,result,accountSpinner,selectedDate,txt_comments.text.toString(), value)

        mTransactionViewModel.addTransaction(transaction)
        var account = Accounts(0,"",0f)
        accountList.forEach { accounts ->
            if(accounts.name==accountSpinner)
            {
                categoryList.forEach { category ->
                    if (category.name == result)
                    {
                        when (category.transactionType){
                            "earnings" -> account = Accounts(accounts.Id,accounts.name,accounts.balance+value)
                            "expenses" -> account = Accounts(accounts.Id,accounts.name,accounts.balance-value)
                        }
                    }
                }
            }
        }

        mAccountViewmodel.updateAccount(account)


        Toast.makeText(
            requireContext(),
            "Category successfuly added.",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigate(R.id.action_addTransactionFragment_to_listTransactionsFragment)
    }

    private fun isValid():Boolean {
        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        return (TextUtils.isEmpty(mySpinner.selectedItem.toString()) || TextUtils.isEmpty(mySpinnerAccount.selectedItem.toString())
                || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(etn_value.text.toString()))
    }
}