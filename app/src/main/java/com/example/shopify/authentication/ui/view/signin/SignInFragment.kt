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
import com.example.shopify.R
import com.example.shopify.authentication.model.pojo.Customer
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.repository.AuthenticationRepository
import com.example.shopify.authentication.remote.AuthenticationClient
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModel
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModelFactory
import com.example.shopify.databinding.FragmentSignInBinding
import com.example.shopify.homeActivity.HomeActivity
import com.example.shopify.utilities.Constants
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
import kotlinx.coroutines.launch
import java.util.regex.Pattern

private const val TAG = "SignInFragment"

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var authenticationViewModel: AuthenticationViewModel
    lateinit var authenticationViewModelFactory: AuthenticationViewModelFactory
    var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 100
    private val TAG = "Google_SIGN_IN_TAG"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        authenticationViewModelFactory = AuthenticationViewModelFactory(
            AuthenticationRepository.getInstance(
                AuthenticationClient()
            ))
        authenticationViewModel = ViewModelProvider(this,authenticationViewModelFactory)[AuthenticationViewModel::class.java]



        val firstname = Constants.first_name
        val lastname = Constants.last_name

        binding.loginBtn.setOnClickListener {
            signInWithEmailAndPassword(firstname,lastname)
        }
        binding.signUpTxt.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment)

        }
        binding.googleLoginBtn.setOnClickListener {
            signIn()
        }

        binding.guestBtn.setOnClickListener {
            signInAnonymously()
        }

    }
    private fun signInAnonymously(){
        auth.signInAnonymously()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(activity,HomeActivity::class.java))
                    activity?.finish()
                    Toast.makeText(requireContext(),"logged as Guest",Toast.LENGTH_LONG).show()
                }
                else{
                    val errorMessage = it.exception?.localizedMessage
                    Toast.makeText(requireContext(),errorMessage,Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signInWithEmailAndPassword( firstName:String , lastName:String){
        val email = binding.emailTextField.editText?.text.toString()
        val password = binding.passwordTextField.editText?.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(requireContext(),"Please Fill All Data",Toast.LENGTH_LONG).show()
            return
        }
        if(password.length<6 || password.length >20){
            Toast.makeText(requireContext(),"Enter Correct Password",Toast.LENGTH_LONG).show()
            return
        }
        if(!isValidEmail(email)){
            Toast.makeText(requireContext(),"Enter Valid format in email ",Toast.LENGTH_LONG).show()
            return

        }
        lifecycleScope.launch {
            val job = lifecycleScope.launch {
                authenticationViewModel.getCustomerByEmail(email)
            }
            job.join()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (auth.currentUser?.isEmailVerified!!) {
                            Log.i(TAG, "signInWithEmailAndPassword: ${auth.currentUser!!.uid}")
                            if (Constants.CustomerListResponseSize == 0) {
                                authenticationViewModel.addCustomer(
                                    CustomerResponse(
                                        Customer(
                                            email,
                                            firstName,
                                            lastName
                                        )
                                    )
                                )
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
        val pattern =  Pattern.compile(
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
                        val emailWithGoogle:String = user?.email!!
                        val names = user.displayName?.split("\\s".toRegex())
                        val firstNameWithGoogle = names?.get(0)
                        val lastNameWithGoogle = names?.get(1)
                        Log.i(TAG, "firebaseAuthWithGoogle: $emailWithGoogle\n $firstNameWithGoogle\n $lastNameWithGoogle\n")

                        lifecycleScope.launch {
                            val job = lifecycleScope.launch {
                                authenticationViewModel.getCustomerByEmail(emailWithGoogle)
                            }
                            job.join()
                            if (Constants.CustomerListResponseSize == 0) {
                                authenticationViewModel.addCustomer(CustomerResponse(Customer(
                                            emailWithGoogle,
                                            firstNameWithGoogle,
                                            lastNameWithGoogle)))
                            }
                        }

                        if (task.result.additionalUserInfo!!.isNewUser) {}
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


}