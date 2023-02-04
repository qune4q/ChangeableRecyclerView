package com.tbondarenko.changeablerecyclerview.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tbondarenko.changeablerecyclerview.R
import com.tbondarenko.changeablerecyclerview.databinding.ActivityMainBinding
import com.tbondarenko.changeablerecyclerview.ui.adapter.NumberRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var numberAdapter: NumberRecyclerViewAdapter

    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        recyclerView = binding.numberRecyclerView
        numberAdapter = NumberRecyclerViewAdapter {
            viewModel.removeNumberState(it)
            val message = getString(R.string.snackBar_delete, it.number)
            showSnack(binding.root, message)
        }
        recyclerView.adapter = numberAdapter
        recyclerView.itemAnimator = ScaleInBottomAnimator()
        recyclerView.itemAnimator?.apply {
            addDuration = 400
            removeDuration = 400
            moveDuration = 400
            changeDuration = 400
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.numberState.collect { listNumber ->
                    numberAdapter.add(listNumber)
                }
            }
        }
    }

    private fun showSnack(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}