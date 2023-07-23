package com.example.shopify.authentication.ui.view.signup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.shopify.utilities.Constants
import com.example.shopify.R
import com.example.shopify.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding =  FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {
            singUpWithEmailAndPassword(view)
        }
        binding.txtSignIn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun singUpWithEmailAndPassword(view:View) {
        val email = binding.signUpEmailTextField.editText?.text.toString().trim()
        val firstname = binding.signUpFirstnameTextField.editText?.text.toString().trim()
        val lastname = binding.signUpLastnameTextField.editText?.text.toString().trim()
        val password = binding.signUpPasswordTextField.editText?.text.toString().trim()
        val confirmPassword = binding.signUpConfirmPasswordTextField.editText?.text.toString().trim()

        if(email.isEmpty() || firstname.isEmpty()||lastname.isEmpty() || password.isEmpty()||confirmPassword.isEmpty()){
            Toast.makeText(requireContext(),"Please Fill All Data", Toast.LENGTH_LONG).show()
            return
        }
        if(password.length<6 || password.length >20){
            Toast.makeText(requireContext(),"Password should be at least 6 and less than 20 characters ", Toast.LENGTH_LONG).show()
            return
        }
        if(password != confirmPassword){
            Toast.makeText(requireContext(),"Password and Confirm Password not match ", Toast.LENGTH_LONG).show()
            return
        }
        if(!isValidEmail(email)){
            Toast.makeText(requireContext(),"Enter Valid format in email ",Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    verifyEmailAddress()
                }
                else{
                    Toast.makeText(requireContext(),it.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        Constants.first_name = firstname
        Constants.last_name = lastname
    }

    private fun verifyEmailAddress() {
        auth.currentUser?.sendEmailVerification()!!
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_signInFragment)


                }
                else{
                    Toast.makeText(requireContext(),"please verify your email ", Toast.LENGTH_LONG).show()
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