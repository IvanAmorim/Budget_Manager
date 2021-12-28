package com.pm.budgetmanager.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pm.budgetmanager.R

class Utils {
        companion object {
            @JvmStatic
            fun Fragment.hideKeyboard() {
                view?.let { activity?.hideKeyboard(it) }
            }

            fun Activity.hideKeyboard() {
                hideKeyboard(currentFocus ?: View(this))
            }

            fun Context.hideKeyboard(view: View) {
                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
            fun Fragment.getToken(): String? {
                val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                val token = preferences.getString("token", null)

                return token
            }

            fun Fragment.getUserIdInSession(): String? {
                val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                val user_id = preferences.getString("user_id", null)

                return user_id
            }

            fun Fragment.unauthorized(navigatonHandlder : () -> Unit){
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton(getString(R.string.signin)) { _, _ ->
                    val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                    preferences.edit().putString("token", null).apply()
                    navigatonHandlder()
                }
                builder.setTitle(getString(R.string.session_expired))
                builder.setMessage(getString((R.string.session_expired_message)))
                builder.create().show()
            }

            fun Fragment.somethingWentWrong(){
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }

    }
}