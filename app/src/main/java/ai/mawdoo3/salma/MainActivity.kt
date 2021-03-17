package ai.mawdoo3.salma

import ai.mawdoo3.salma.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_analytics -> {

            }
            R.id.action_help -> {

            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}