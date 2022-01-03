package com.pm.budgetmanager.fragments.Transaction.add

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.pm.budgetmanager.Utils.Utils.Companion.getUserIdInSession
import com.pm.budgetmanager.Utils.Utils.Companion.somethingWentWrong
import com.pm.budgetmanager.Utils.Utils.Companion.unauthorized
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.entities.Category
/*import com.pm.budgetmanager.data.Dao.AccountDao_Impl
import com.pm.budgetmanager.data.Viewmodel.AccountViewmodel
import com.pm.budgetmanager.data.Viewmodel.CategoryViewmodel
import com.pm.budgetmanager.data.Viewmodel.TransactionViewmodel
import com.pm.budgetmanager.data.entities.Accounts
import com.pm.budgetmanager.data.entities.Category
import com.pm.budgetmanager.data.entities.Transactions*/
import com.pm.budgetmanager.databinding.FragmentAddTransactionBinding
import com.pm.budgetmanager.fragments.Account.update.UpdateFragment
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class addTransactionFragment : Fragment() {

     private lateinit var mCategoryViewmodel: CategoryViewmodel
    //private lateinit var mAccountViewmodel: AccountViewmodel

     var categoryList = emptyList<Categorys>()
    private var accountList = emptyList<Account>()
    private var branchListAcount: MutableList<String> = ArrayList()
    private var branchListCategory: MutableList<String> = ArrayList()

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    var selectedDate: String = ""

    //private lateinit var mTransactionViewModel: TransactionViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        /** Spinner Category**/
        getCategorys(view)

        /** Spinner Account**/
        getAccounts(view)

        /*mAccountViewmodel = ViewModelProvider(this).get(AccountViewmodel::class.java)
        mAccountViewmodel.readAllAccounts.observe(viewLifecycleOwner, Observer {
            this.accountList = it
            accountList.forEach {
                branchListAcount.add(it.name)
            }
            branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mySpinnerAccount.setAdapter(branchListAccountAdapter)
        })


        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewmodel::class.java)
*/
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
                        selectedDate = date.toString()
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
        inflater.inflate(R.menu.save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            if (isValid()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.isValid),
                    Toast.LENGTH_LONG
                ).show()
            } else
                addTransaction()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun addTransaction() {

        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        val result: String
        val accountSpinner: String
        var category_id = -1
        var account_id = -1

        result = mySpinner.selectedItem.toString()
        categoryList.forEach {
            if (result == it.name) {
                category_id = it.id
            }
        }

        accountSpinner = mySpinnerAccount.selectedItem.toString()
        accountList.forEach {
            if (accountSpinner == it.name) {
                account_id = it.id
            }
        }

        val value: Float
        value = etn_value.text.toString().toFloat()

        val comments: String
        comments = txt_comments.text.toString()


        //val transaction = Transactions(0,category_id,accountSpinner,selectedDate,txt_comments.text.toString(), value)

        //mTransactionViewModel.addTransaction(transaction)

        /**************add transaction******/
        if (!(category_id != -1 && account_id != -1 && TextUtils.isEmpty(selectedDate) && TextUtils.isEmpty(comments)))
        {
            addTransactions(category_id, account_id, selectedDate, comments, value)
        }
        /*********end add transactions**/

        categoryList.forEach {
            if(it.id == category_id){
                accountList.forEach { account ->
                    if(account.id == account_id)
                    {
                        Toast.makeText(requireContext(),"Balance: ${account.balance}",Toast.LENGTH_SHORT).show()
                        when(it.transactionType){
                            "earnings" -> account.balance+=value
                            "expenses" -> account.balance-=value
                            }
                        Toast.makeText(requireContext(),"Balance after: ${account.balance}",Toast.LENGTH_SHORT).show()
                        update(account.id,account.name,account.balance)
                    }

                }
            }
        }


        /*var account = Account(0, "", 0f)
        accountList.forEach { accounts ->
            if (accounts.name == accountSpinner) {
                categoryList.forEach { category ->
                    if (category.name == result) {
                        when (category.transactionType) {
                            "earnings" -> account =
                                Account(accounts.id, accounts.name, accounts.balance + value)
                            "expenses" -> account =
                                Account(accounts.id, accounts.name, accounts.balance - value)
                        }
                    }
                }
            }
        }*/

        // mAccountViewmodel.updateAccount(account)


        /*Toast.makeText(
            requireContext(),
            getString(R.string.added),
            Toast.LENGTH_LONG
        ).show()
*/
        findNavController().navigate(R.id.action_addTransactionFragment_to_listTransactionsFragment)
    }

    private fun isValid(): Boolean {
        val mySpinner = requireView().findViewById<Spinner>(R.id.spinnerCategory);
        val mySpinnerAccount = requireView().findViewById<Spinner>(R.id.spinnerAccount);
        return (TextUtils.isEmpty(mySpinner.selectedItem.toString()) || TextUtils.isEmpty(
            mySpinnerAccount.selectedItem.toString()
        )
                || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(etn_value.text.toString()))
    }


    fun getCategorys(view : View){
        view.llProgressBar.bringToFront()
        view.llProgressBar.visibility = View.VISIBLE


        val request = ServiceBuilder.buildService(CategorysApi::class.java)
        val call = request.getCategorys(token = "Bearer ${getToken()}")


        call.enqueue(object : Callback<List<Categorys>> {
            override fun onResponse(
                call: Call<List<Categorys>>,
                response: Response<List<Categorys>>
            ) {

                  view.llProgressBar.visibility = View.GONE

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
                llProgressBar.visibility = View.GONE
                somethingWentWrong()
            }
        })
    }

    fun getAccounts(view :View) {
        view.llProgressBar.bringToFront()
        view.llProgressBar.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.getAccounts(token = "Bearer ${getToken()}")

        call.enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {

                  this@addTransactionFragment.llProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val account: List<Account> = response.body()!!
                    accountList=account
                    setDataAccount(account,view)
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
                 this@addTransactionFragment.llProgressBar.visibility = View.GONE
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

        val mySpinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory);

        branchListCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerCategory.adapter = branchListCategoryAdapter
    }


    fun setDataAccount(account: List<Account>, view : View){
        this.accountList = account
        account.forEach { branchListAcount.add(it.name) }

        var accounts : MutableList<String> = ArrayList()
        account.forEach { accounts.add(it.name) }

        val branchListAccountAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, branchListAcount
        )
        val mySpinnerAccount = view.findViewById<Spinner>(R.id.spinnerAccount);

        branchListAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerAccount.adapter = branchListAccountAdapter
    }


    fun addTransactions(
        category_id: Int,
        account_id: Int,
        selectedDate: String,
        comments: String,
        value: Float
    ) {
      //  llProgressBar.bringToFront()
       // llProgressBar.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(TransactionsApi::class.java)
        val call = request.createTransaction(
            token = "Bearer ${getToken()}",
            users_id = getUserIdInSession(),
            category_id = category_id,
            accounts_id = account_id,
            date = selectedDate,
            comments = comments,
            value = value,

            )

        call.enqueue(object : Callback<TransactionsDto> {
            override fun onResponse(
                call: Call<TransactionsDto>,
                response: Response<TransactionsDto>
            ) {
               // llProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val transaction: TransactionsDto = response.body()!!

                    if (transaction.status == "OK") {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.success),
                            Toast.LENGTH_LONG
                        ).show()
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
                            findNavController().navigate(R.id.action_addTransactionFragment_to_fragment_signIn)
                        })
                    } else {
                        somethingWentWrong()
                    }
                }
            }

            override fun onFailure(call: Call<TransactionsDto>, t: Throwable) {
                // llProgressBar.visibility = View.GONE
                somethingWentWrong()
            }
        })

    }
    fun update(id : Int, name : String, balance : Float){
  //      llProgressBar.bringToFront()
 //       llProgressBar.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(AccountApi::class.java)
        val call = request.updateAccount(
            token = "Bearer ${getToken()}",
            id = id,
            name = name,
            balance = balance
        )

        call.enqueue(object : Callback<AccountDto> {

            override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
//                llProgressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val report: AccountDto = response.body()!!

                    if (report.status == "OK") {
                      /*  Toast.makeText(
                            requireContext(),
                            getString(R.string.successfull_updated_account),
                            Toast.LENGTH_LONG
                        ).show()*/
                       // findNavController().navigate(R.id.action_addTransactionFragment_to_listTransactionsFragment)
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
                            findNavController().navigate(R.id.action_addTransactionFragment_to_fragment_signIn)
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



}

