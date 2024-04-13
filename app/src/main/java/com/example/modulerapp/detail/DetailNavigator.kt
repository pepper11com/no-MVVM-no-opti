package com.example.modulerapp.detail

import com.example.presentation.photodetail.listener.DetailNavigationListener
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class DetailNavigator(
    private val destinationsNavigator: DestinationsNavigator,
) : DetailNavigationListener {

    override fun closeDetails() { destinationsNavigator.navigateUp() }
}