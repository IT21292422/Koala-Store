package com.project.ecommerceapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.ecommerceapp.R
import android.content.Intent
import com.project.ecommerceapp.databinding.ActivityIntroductionBinding
import com.project.ecommerceapp.profile.LoginActivity
import com.project.ecommerceapp.profile.RegisterActivity

class IntroductionActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroductionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            val intent = Intent(this@IntroductionActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.signinTxt.setOnClickListener {
            val intent = Intent(this@IntroductionActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}