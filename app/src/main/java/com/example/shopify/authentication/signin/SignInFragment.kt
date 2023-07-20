package com.example.shopify.authentication.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.shopify.homeActivity.HomeActivity
import com.example.shopify.R
import com.example.shopify.authentication.AuthenticationActivity
import com.example.shopify.authentication.signup.SignUpFragment
import com.example.shopify.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            signInWithEmailAndPassword()
        }
        binding.signUpTxt.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment)

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
    private fun signInWithEmailAndPassword(){
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
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified!!) {
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()
                        Toast.makeText(requireContext(), "Sign in Successfully", Toast.LENGTH_LONG)
                            .show()
                    }else{
                        Toast.makeText(requireContext(), "Please Verify your email ", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                else{
                    val errorMessage = it.exception?.localizedMessage
                    Toast.makeText(requireContext(),errorMessage,Toast.LENGTH_LONG).show()
                }
            }

    }
    private fun isValidEmail(email: String): Boolean {
        val pattern =  Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }


}