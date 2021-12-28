package com.pm.budgetmanager.fragments.Transaction.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pm.budgetmanager.R
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.Viewmodel.TransactionViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import com.pm.budgetmanager.data.entities.Category
import com.pm.budgetmanager.data.entities.Transactions
import com.pm.budgetmanager.databinding.FragmentAddTransactionBinding
import com.pm.budgetmanager.databinding.FragmentUpdateTransactionBinding
import com.pm.budgetmanager.fragments.Transaction.add.DatePickerFragment
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.fragment_update_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.view.*

class UpdateTransaction : Fragment() {
    private val args by navArgs<UpdateTransactionArgs>()
    private lateinit var mAccountViewmodel: AccountViewmodel
    private lateinit var mCategoryViewmodel: CategoryViewmodel
    private lateinit var mTransactionViewModel: TransactionViewmodel

    private var categoryList = emptyList<Category>()
    private var accountList = emptyList<Accounts>()
    private val branchListAcount: MutableList<String> = ArrayList()
    private val branchListCategory: MutableList<String> = ArrayList()

    private var _binding: FragmentUpdateTransactionBinding? = null
    private val binding get() = _binding!!
    lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)

        view.etn_valueUpdate.setText(args.currentTransaction.value.toString())
        view.txt_commentsUpdate.setText(args.currentTransaction.comments)
        view.tvSelectedDateUpdate.setText(args.currentTransaction.date)
        selectedDate=args.currentTransaction.date

        setHasOptionsMenu(true)

        /** Spinner Category**/
        val branchListCategoryAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListCategory
        )

        val mySpinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategoryUpdate);

        mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
        mCategoryViewmodel.readAllCategorys.observe(viewLifecycleOwner, Observer {
            this.categoryList = it
            categoryList.forEach { category ->
                branchListCategory.add(category.name)
            }

            branchListCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinnerCategory.setAdapter(branchListCategoryAdapter)
            //mySpinnerCategory.setSelection(categoryList))
            //mySpinnerCategory.setSelection(branchListCategory.indexOf(args.currentTransaction.transactionCategory))
        })

        /** Spinner Account**/
        val branchListAccountAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListAcount
        )

        val mySpinnerAccount = view.findViewById<Spinner>(R.id.spinnerAccountUpdate);

        mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        mAccountViewmodel.readAllAccounts.observe(viewLifecycleOwner, Observer {
            this.accountList = it
            accountList.forEach {
                branchListAcount.add(it.name)
            }
            branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinnerAccount.setAdapter(branchListAccountAdapter)
            mySpinnerAccount.setSelection(branchListAcount.indexOf(args.currentTransaction.account))
        })

        /**Date Picker**/
        _binding = FragmentUpdateTransactionBinding.bind(view)

        binding.apply {
            btnSelectDateUpdate.setOnClickListener {
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
                        tvSelectedDateUpdate.text = date
                        selectedDate=date.toString()
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewmodel::class.java)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save,menu)
        inflater.inflate(R.menu.delete,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save){
            updateTransaction()
        }
        if(item.itemId == R.id.menu_delete){
            deleteTransaction()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTransaction(){
        if(isValid()){
            return Toast.makeText(
                requireContext(),
                getString(R.string.isValid),
                Toast.LENGTH_LONG
            ).show()
        }

        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategoryUpdate);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccountUpdate);
        val resultupdated : String
        val accountSpinnerupdated: String

        resultupdated= mySpinner.selectedItem.toString()
        accountSpinnerupdated = mySpinnerAccount.selectedItem.toString()

        val valueupdated:Float
        valueupdated = etn_valueUpdate.text.toString().toFloat()

        //val transaction = Transactions(args.currentTransaction.id,resultupdated,accountSpinnerupdated,selectedDate,txt_commentsUpdate.text.toString(), valueupdated)

       // mTransactionViewModel.updateTransaction(transaction)

        findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
    }

    private fun deleteTransaction(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            mTransactionViewModel.deleteTransaction(args.currentTransaction)
            Toast.makeText(requireContext(),"Successfuly removed",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete?")
        builder.setMessage("Are you sure you want to delete?")
        builder.create().show()
    }

    private fun isValid():Boolean {
        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        return (TextUtils.isEmpty(mySpinner.selectedItem.toString()) || TextUtils.isEmpty(mySpinnerAccount.selectedItem.toString())
                || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(etn_value.text.toString()))
    }

}