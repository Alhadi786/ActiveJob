package com.xee.app.activejob.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {

    lateinit var binding: Binding
    private var cachedView: View? = null
    private var isInitialised = false

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        if (cachedView == null) {

            binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)

            cachedView = binding.root

            binding.lifecycleOwner = this

        } else {
            try {
                if (cachedView?.parent != null)
                    (cachedView?.parent as ViewGroup).removeView(cachedView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return cachedView ?: View(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isInitialised) {
            CoroutineScope(Dispatchers.Main).launch {
                setupView()
                isInitialised = true
            }
        }

    }

    abstract fun setupView()

}