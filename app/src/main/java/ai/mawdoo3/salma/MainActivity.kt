package ai.mawdoo3.salma

import ai.mawdoo3.salma.databinding.ActivityMainBinding
import android.os.Bundle
import com.banking.common.base.BaseActivity

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close);
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true);

    }
}