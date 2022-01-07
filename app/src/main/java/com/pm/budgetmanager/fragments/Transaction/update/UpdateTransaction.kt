package com.pm.budgetmanager.fragments.Transaction.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pm.budgetmanager.API.dto.AccountDto
import com.pm.budgetmanager.API.dto.TransactionsDto
import com.pm.budgetmanager.API.models.Account
import com.pm.budgetmanager.API.models.Categorys
import com.pm.budgetmanager.API.requests.AccountApi
import com.pm.budgetmanager.API.requests.CategorysApi
import com.pm.budgetmanager.API.requests.TransactionsApi
import com.pm.budgetmanager.API.retrofit.ServiceBuilder
import com.pm.budgetmanager.R
import com.pm.budgetmanager.Utils.Utils.Companion.getToken
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import com.pm.budgetmanager.databinding.FragmentUpdateTransactionBinding
import com.pm.budgetmanager.fragments.Transaction.add.DatePickerFragment
import kotlinx.android.synthetic.main.fragment_update_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTransaction : Fragment() {
    private val args by navArgs<UpdateTransactionArgs>()
    //  private lateinit var mAccountViewmodel: AccountViewmodel
    //  private lateinit var mCategoryViewmodel: CategoryViewmodel
    //  private lateinit var mTransactionViewModel: TransactionViewmodel

    private var categoryList = emptyList<Categorys>()
    private var accountList = emptyList<Account>()
    private var branchListAcount: MutableList<String> = ArrayList()
    private var branchListCategory: MutableList<String> = ArrayList()

    private var _binding: FragmentUpdateTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedDate: String
    private var categoryId=0
    private var accountId =0
    private var oldValue : Float = 0.0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)

        view.etn_valueUpdate.setText(args.currentTransaction.value.toString())
        view.txt_commentsUpdate.setText(args.currentTransaction.comments)
        view.tvSelectedDateUpdate.setText(args.currentTransaction.date)
        selectedDate = args.currentTransaction.date
        oldValue = args.currentTransaction.value

        setHasOptionsMenu(true)

        /** Spinner Category**/
        getCategorys(view)
      /*  val mySpinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategoryUpdate);

        mCategoryViewmodel = ViewModelProvider(this).get(CategoryViewmodel::class.java)
        mCategoryViewmodel.readAllCategorys.observe(viewLifecycleOwner, Observer {
         this.categoryList = it
            categoryList.forEach { category ->
                branchListCategory.add(category.name)
            }
        })


          branchListCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerCategory.setAdapter(branchListCategoryAdapter)
         //mySpinnerCategory.setSelection(0)
        mySpinnerCategory.setSelection(branchListCategory.indexOf(args.currentTransaction.category_name))

*/

        /** Spinner Account**/
        getAccounts(view)
      /*  val branchListAccountAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListAcount
        )
        getAccounts()
        val mySpinnerAccount = view.findViewById<Spinner>(R.id.spinnerAccountUpdate);

        //accountList = adapterT.getAccounts(token)
        //branchListAcount = adapterT.setDataAccount(accountList)
        branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerAccount.setAdapter(branchListAccountAdapter)
        mySpinnerAccount.setSelection(branchListAcount.indexOf(args.currentTransaction.account_Name))

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
 */


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
                        selectedDate = date.toString()
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

        //mTransactionViewModel = ViewModelProvider(this).get(TransactionViewmodel::class.java)

        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save, menu)
        inflater.inflate(R.menu.delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateTransaction()
        }
        if (item.itemId == R.id.menu_delete) {
            deleteTransaction()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTransaction() {

        /*if (isValid()) {
            return Toast.makeText(
                requireContext(),
                getString(R.string.isValid),
                Toast.LENGTH_LONG
            ).show()
        }*/

        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategoryUpdate)
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccountUpdate)

        val categoryUpdated: String = mySpinner.selectedItem.toString()
        val accountUpdated: String = mySpinnerAccount.selectedItem.toString()

        val valueUpdated: Float = etn_valueUpdate.text.toString().toFloat()

        val comments = txt_commentsUpdate.text.toString()

        //val transaction = Transactions(args.currentTransaction.id,resultupdated,accountSpinnerupdated,selectedDate,txt_commentsUpdate.text.toString(), valueupdated)

        // mTransactionViewModel.updateTransaction(transaction)

        if (inputcheck(categoryUpdated, accountUpdated, selectedDate, comments, valueUpdated)) {
           // = categoryList.indexOfFirst { it.name.equals(categoryUpdated) }
            converToId(categoryUpdated,accountUpdated)

            val request = ServiceBuilder.buildService(TransactionsApi::class.java)
            val call = request.updateTransaction(

                token = "Bearer ${getToken()}",
                id = args.currentTransaction.id,
                category_id = categoryId,
                accounts_id = accountId,
                date = selectedDate,
                comments = comments,
                value = valueUpdated


            )

            call.enqueue(object : Callback<TransactionsDto> {
                override fun onResponse(
                    call: Call<TransactionsDto>,
                    response: Response<TransactionsDto>
                ) {
                    if (response.isSuccessful) {
                        val transaction: TransactionsDto = response.body()!!

                        if (transaction.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.success),
                                Toast.LENGTH_LONG
                            ).show()

                            val temp = oldValue-valueUpdated
                            updateAccount(temp)
                          //  findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
                        } else {
                         /*   Toast.makeText(
                                requireContext(), getString(
                                    resources.getIdentifier(
                                        transaction.message, "string",
                                        context?.packageName
                                    )
                                ), Toast.LENGTH_LONG
                            ).show()*/
                            somethingWentWrong()
                        }
                    } else {
                        if (response.code() == 401) {
                            unauthorized(navigatonHandlder = {
                                findNavController().navigate(R.id.action_updateTransaction_to_fragment_signIn)
                            })
                        } else {
                            somethingWentWrong()
                        }
                    }
                }

                override fun onFailure(call: Call<TransactionsDto>, t: Throwable) {
                    somethingWentWrong()
                }
            })
        }else
            Toast.makeText(requireContext(),R.string.fill_all_fields,Toast.LENGTH_SHORT).show()

        // findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
    }

    fun updateAccount(value : Float){
        categoryList.forEach {
            if (it.id == categoryId) {
                accountList.forEach { account ->
                    if (account.id == accountId) {
                        when (it.transactionType) {
                            "earnings" -> account.balance -= value
                            "expenses" -> account.balance +=value
                        }
                        update(account.id, account.name, account.balance)
                    }
                }
            }
        }
    }

    fun update(id : Int, name : String, balance : Float){
        llProgressBarUpdateTransaction.bringToFront()
        llProgressBarUpdateTransaction.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.updateAccount(
            token = "Bearer ${getToken()}",
            id = id,
            name = name,
            balance = balance
        )

        call.enqueue(object : Callback<AccountDto> {

            override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                llProgressBarUpdateTransaction.visibility = View.GONE
                if (response.isSuccessful) {
                    val report: AccountDto = response.body()!!

                    if (report.status == "OK") {
                        /*  Toast.makeText(
                              requireContext(),
                              getString(R.string.successfull_updated_account),
                              Toast.LENGTH_LONG
                          ).show()*/
                         findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
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
                            findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                somethingWentWrong()
            }
        })
    }

    private fun inputcheck(
        categoryUpdated: String,
        accountUpdated: String,
        selectedDate: String,
        comments: String,
        valueUpdated: Float
    ): Boolean {

        return (!(TextUtils.isEmpty(categoryUpdated) && TextUtils.isEmpty(accountUpdated) && TextUtils.isEmpty(
            selectedDate
        ) && TextUtils.isEmpty(comments) && TextUtils.isEmpty(valueUpdated.toString())))
    }

    private fun converToId(categoryUpdated: String,
                           accountUpdated: String,){
        categoryList.forEach {
            if (categoryUpdated == it.name) {
                categoryId = it.id
            }
        }
        // = accountList.indexOfFirst { it.name.equals(accountUpdated) }
        accountList.forEach {
            if (accountUpdated == it.name) {
                accountId = it.id
            }
        }
    }

    private fun deleteTransaction() {


        /*  val builder = AlertDialog.Builder(requireContext())
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
    }*/

        converToId(args.currentTransaction.category_name,args.currentTransaction.account_Name)

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->


            val request = ServiceBuilder.buildService(TransactionsApi::class.java)
            val call = request.deleteTransaction(
                token = "Bearer ${getToken()}",
                id = args.currentTransaction.id
            )

            call.enqueue(object : Callback<TransactionsDto> {
                override fun onResponse(call: Call<TransactionsDto>, response: Response<TransactionsDto>) {
                    if (response.isSuccessful) {
                        val transaction: TransactionsDto = response.body()!!

                        if (transaction.status == "OK") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.successfull_deleted_transaction),
                                Toast.LENGTH_LONG
                            ).show()
                            updateAccount(args.currentTransaction.value)
                          //  findNavController().navigate(R.id.action_updateTransaction_to_listTransactionsFragment)
                        } else {
                            Toast.makeText(
                                requireContext(), getString(
                                    resources.getIdentifier(
                                        transaction.message, "string",
                                        context?.packageName
                                    )
                                ), Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        if (response.code() == 401) {
                            unauthorized(navigatonHandlder = {
                                findNavController().navigate(R.id.action_updateTransaction_to_fragment_signIn)
                            })
                        } else {
                            somethingWentWrong()
                        }
                    }
                }

                override fun onFailure(call: Call<TransactionsDto>, t: Throwable) {
                    somethingWentWrong()
                }
            })
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.delete_transaction))
        builder.setMessage(getString(R.string.question_delete_transaction))
        builder.create().show()

    }


    private fun getCategorys(view : View){
        view.llProgressBarUpdateTransaction.bringToFront()
        view.llProgressBarUpdateTransaction.visibility = View.VISIBLE



        val request = ServiceBuilder.buildService(CategorysApi::class.java)
        val call = request.getCategorys(token = "Bearer ${getToken()}")


        call.enqueue(object : Callback<List<Categorys>> {
            override fun onResponse(
                call: Call<List<Categorys>>,
                response: Response<List<Categorys>>
            ) {

                  view.llProgressBarUpdateTransaction.visibility = View.GONE

                if (response.isSuccessful) {
                    val category: List<Categorys> = response.body()!!
                    setDataCategory(category,view)

                } else {
                    if (response.code() == 401) {
                        unauthorized(navigatonHandlder = {
                            findNavController().navigate(R.id.action_addCategoryFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<List<Categorys>>, t: Throwable) {
                llProgressBarUpdateTransaction.visibility = View.GONE
                somethingWentWrong()
            }
        })
    }

    private fun getAccounts(view :View) {
        view.llProgressBarUpdateTransaction.bringToFront()
        view.llProgressBarUpdateTransaction.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.getAccounts(token = "Bearer ${getToken()}")

        call.enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {

                llProgressBarUpdateTransaction.visibility = View.GONE

                if (response.isSuccessful) {
                    val account: List<Account> = response.body()!!
                    setDataAccount(account, view)

                } else {
                    if (response.code() == 401) {
                        unauthorized(navigatonHandlder = {
                            findNavController().navigate(R.id.action_addCategoryFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                llProgressBarUpdateTransaction.visibility = View.GONE
                somethingWentWrong()
            }
        })

    }

    fun setDataCategory(category: List<Categorys>, view :View) {
        this.categoryList = category
        category.forEach { this.branchListCategory.add(it.name) }

        val branchListCategoryAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListCategory
        )

        val mySpinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategoryUpdate)

        branchListCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerCategory.adapter = branchListCategoryAdapter

        //mySpinnerCategory.setSelection(branchListAcount.indexOf(args.currentTransaction.category_name))
    }

    fun setDataAccount(account: List<Account>, view : View){
        this.accountList = account
        account.forEach { branchListAcount.add(it.name) }

        val accounts : MutableList<String> = ArrayList()
        account.forEach { accounts.add(it.name) }

        val branchListAccountAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListAcount
        )
        val mySpinnerAccount = view.findViewById<Spinner>(R.id.spinnerAccountUpdate)

        branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerAccount.adapter = branchListAccountAdapter
        mySpinnerAccount.setSelection(branchListAcount.indexOf(args.currentTransaction.account_Name))

    }

}


/*
    private fun isValid():Boolean {
        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        return (TextUtils.isEmpty(mySpinner.selectedItem.toString()) || TextUtils.isEmpty(mySpinnerAccount.selectedItem.toString())
                || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(etn_value.text.toString()))
    }

}*/