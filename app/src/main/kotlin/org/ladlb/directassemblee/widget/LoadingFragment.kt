package org.ladlb.directassemblee.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ladlb.directassemblee.AbstractFragment
import org.ladlb.directassemblee.R

class LoadingFragment : AbstractFragment() {

    override fun getClassName() = "LoadingFragment"

    companion object {

        fun newInstance() = LoadingFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

}