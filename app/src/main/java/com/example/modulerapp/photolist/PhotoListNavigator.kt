package com.example.modulerapp.photolist

import com.example.presentation.photolist.listener.PhotoListNavigationListener
import com.ramcosta.composedestinations.generated.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class PhotoListNavigator(
    private val destinationsNavigator: DestinationsNavigator,
) : PhotoListNavigationListener {

    override fun openDetails(id: Int) {
        destinationsNavigator.navigate(direction = DetailScreenDestination(id))
    }
}