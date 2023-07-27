package com.example.shopify.authentication.ui.view.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.shopify.AllOrdersFragment.Model.LineItemsOrder
import com.example.shopify.R
import com.example.shopify.authentication.model.pojo.*
import com.example.shopify.authentication.model.repository.AuthenticationRepository
import com.example.shopify.authentication.remote.AuthenticationClient
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModel
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModelFactory
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FragmentSignInBinding
import com.example.shopify.homeActivity.HomeActivity
import com.example.shopify.utilities.Constants
import com.example.shopify.utilities.MySharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.properties.Delegates

private const val TAG = "SignInFragment"

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var authenticationViewModel: AuthenticationViewModel
    lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    lateinit var cartDraftOrder: DraftOrderResponse
    lateinit var favDraftOrder: DraftOrderResponse
    var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 100
    private val TAG = "Google_SIGN_IN_TAG"
    var customerLogin_id :Long =0
    var card_id: Long = 0
    var fav_id: Long = 0
    lateinit var updatedCustomer: CustomerResponsePut
    lateinit var customer: Customer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        customerLogin_id = MySharedPreferences.getInstance(requireContext()).getCustomerID()!!
        card_id = MySharedPreferences.getInstance(requireContext()).getCartID()!!
        fav_id = MySharedPreferences.getInstance(requireContext()).getFavID()!!

        val cart_line_item = line_items(title = "cart item", quantity = 2, price = "1200")
        val cart_draftOrder =
            DraftOrder(line_items = listOf(cart_line_item), note = Constants.CART_NOTE)
        cartDraftOrder = DraftOrderResponse(cart_draftOrder)

        val fav_line_item = line_items(title = "fav item", quantity = 1, price = "1500")
        val fav_draftOrder =
            DraftOrder(line_items = listOf(fav_line_item), note = Constants.FAV_NOTE)
        favDraftOrder = DraftOrderResponse(fav_draftOrder)


        authenticationViewModelFactory = AuthenticationViewModelFactory(
            AuthenticationRepository.getInstance(
                AuthenticationClient()
            ))
        authenticationViewModel = ViewModelProvider(this,
            authenticationViewModelFactory)[AuthenticationViewModel::class.java]


        val firstname = Constants.first_name
        val lastname = Constants.last_name

        binding.loginBtn.setOnClickListener {
            //  authenticationViewModel.updateCustomer(customerLogin_id, updatedCustomer)
            //Log.i(TAG, "onViewCreated: $updatedCustomer")
            Log.i(TAG, "onViewCreated: $customerLogin_id")
            signInWithEmailAndPassword(firstname, lastname)
        }
        binding.signUpTxt.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_signInFragment_to_signUpFragment)

        }
        binding.googleLoginBtn.setOnClickListener {
            signIn()
        }

        binding.guestBtn.setOnClickListener {
            MySharedPreferences.getInstance(requireContext()).saveCurrencyCode("EGP")
            MySharedPreferences.getInstance(requireContext()).saveExchangeRate(1.0f)
            signInAnonymously()
        }
        lifecycleScope.launch {
            authenticationViewModel.customerUpdated.collect{
                when(it){
                    is State.Success ->{
                        val updatedCustomerResponse = it.data.customer
                        Log.i(TAG, "onViewCreated: $updatedCustomerResponse")

                    } else ->{
                    Log.i(TAG, "onViewCreated: canot response cusromer updated")
                }
                }
            }
        }

    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(activity, HomeActivity::class.java))
                    activity?.finish()
                    Toast.makeText(requireContext(), "logged as Guest", Toast.LENGTH_LONG).show()
                } else {
                    val errorMessage = it.exception?.localizedMessage
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signInWithEmailAndPassword(firstName: String, lastName: String) {
        val email = binding.emailTextField.editText?.text.toString()
        val password = binding.passwordTextField.editText?.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please Fill All Data", Toast.LENGTH_LONG).show()
            return
        }
        if (password.length < 6 || password.length > 20) {
            Toast.makeText(requireContext(), "Enter Correct Password", Toast.LENGTH_LONG).show()
            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(requireContext(), "Enter Valid format in email ", Toast.LENGTH_LONG)
                .show()
            return
        }
        lifecycleScope.launch {
            authenticationViewModel.getCustomerByEmail(email, requireContext())
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (auth.currentUser?.isEmailVerified!!) {
                            Log.i(TAG, "signInWithEmailAndPassword: ${auth.currentUser!!.uid}")
                            if (Constants.CustomerListResponseSize == 0) {
                                authenticationViewModel.addCustomer(CustomerResponse(Customer(email,
                                    firstName,
                                    lastName)))
                                lifecycleScope.launch {
                                    authenticationViewModel.customerDetails.collect { result ->
                                        when (result) {
                                            is State.Success -> {
                                                Log.i(TAG, "signInWithEmailAndPassword: ${result.data.customer}")
                                                customer = result.data.customer
                                                createDraftOrder(cartDraftOrder)
                                                createDraftOrder(favDraftOrder)

                                                customer.note = card_id.toString()
                                                Log.i(TAG, "createDraftOrder: $card_id")
                                                customer.tags = fav_id.toString()
                                                Log.i(TAG, "createDraftOrder: $fav_id")
                                                Log.i(TAG, "createDraftOrder: Before:    ${customer.toString()}")
                                                updatedCustomer = CustomerResponsePut(
                                                    CustomerBody(id =customer.id,note = customer.note, tags = customer.tags ))
                                                authenticationViewModel.updateCustomer(customer.id!!, updatedCustomer)


                                            }
                                            else -> {
                                                //Toast.makeText()

                                            }
                                        }

                                    }


                                }
                            }

                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finish()
                            Toast.makeText(
                                requireContext(),
                                "Sign in Successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please Verify your email ",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else {
                        val errorMessage = it.exception?.localizedMessage
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }

    private fun signIn() {
        Log.i(TAG, "signIn: ")
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult: ")
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG token", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAGFailed", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.i(TAG, "firebaseAuthWithGoogle: ")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity(),
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGsigninsuccess", "signInWithCredential:success")
                        val user: FirebaseUser? = auth.getCurrentUser()
                        val emailWithGoogle: String = user?.email!!
                        val names = user.displayName?.split("\\s".toRegex())
                        val firstNameWithGoogle = names?.get(0)
                        val lastNameWithGoogle = names?.get(1)
                        Log.i(TAG,
                            "firebaseAuthWithGoogle: $emailWithGoogle\n $firstNameWithGoogle\n $lastNameWithGoogle\n")

                        lifecycleScope.launch {
                                authenticationViewModel.getCustomerByEmail(emailWithGoogle,
                                    requireContext())
                            if (Constants.CustomerListResponseSize == 0) {
                                authenticationViewModel.addCustomer(CustomerResponse(Customer(
                                    emailWithGoogle,
                                    firstNameWithGoogle,
                                    lastNameWithGoogle)))
                               /* authenticationViewModel.customerDetails.collect { result ->
                                    when (result) {
                                        is State.Success -> {
                                            Log.i(TAG, "signInWithEmailAndPassword: ${result.data.customer}")
                                            customer = result.data.customer
                                            createDraftOrder(cartDraftOrder)
                                            createDraftOrder(favDraftOrder)

                                            customer.note = card_id.toString()
                                            Log.i(TAG, "createDraftOrder: $card_id")
                                            customer.tags = fav_id.toString()
                                            Log.i(TAG, "createDraftOrder: $fav_id")
                                            Log.i(TAG, "createDraftOrder: Before:    ${customer.toString()}")
                                            updatedCustomer = CustomerResponsePut(
                                                CustomerBody(id =customer.id,note = customer.note, tags = customer.tags ))
                                            authenticationViewModel.updateCustomer(customer.id!!, updatedCustomer)


                                        }
                                        is State.Failure -> {
                                            Toast.makeText(context,"Fail Create Customer with google account",Toast.LENGTH_LONG)
                                                .show()

                                        }
                                        is State.Loading->Toast.makeText(context,"Loading",Toast.LENGTH_LONG)
                                            .show()
                                    }

                                }*/


                            }
                        }

                        if (task.result.additionalUserInfo!!.isNewUser) {
                        }
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireActivity(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        Toast.makeText(requireContext(), "Account Created", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
                        // If sign in fails, display a message to the user.
                        Log.i("TAG sign in fail", "signInWithCredential:failure", task.exception)
                    }
                })
    }

    private fun createDraftOrder(draftOrderResponse: DraftOrderResponse) {
        authenticationViewModel.createDraftOrder(draftOrderResponse)
        lifecycleScope.launch {
            authenticationViewModel.draftOrder.collect { result ->
                when (result) {
                    is State.Success -> {
                        if (result.data.draft_order!!.note.equals(Constants.CART_NOTE)) {
                            MySharedPreferences.getInstance(requireContext())
                                .saveCartID(result.data.draft_order!!.id!!)
                            Log.i(TAG, "createCartDraftOrder: ${result.data.draft_order!!.id!!}")

                        } else if (result.data.draft_order!!.note.equals(Constants.FAV_NOTE)) {
                            MySharedPreferences.getInstance(requireContext())
                                .saveFavID(result.data.draft_order!!.id!!)
                            Log.i(TAG, "createFavDraftOrder: ${result.data.draft_order!!.id!!}")


                        }

//                        customer.note = card_id.toString()
//                        Log.i(TAG, "createDraftOrder: $card_id")
//                        customer.tags = fav_id.toString()
//                        Log.i(TAG, "createDraftOrder: $fav_id")
//                        Log.i(TAG, "createDraftOrder: Before:    ${customer.toString()}")
//                        updatedCustomer = CustomerResponsePut(customer)
//                        authenticationViewModel.updateCustomer(customerLogin_id, updatedCustomer)

                    }
                    else -> {

                    }
                }

            }

        }
    }

}