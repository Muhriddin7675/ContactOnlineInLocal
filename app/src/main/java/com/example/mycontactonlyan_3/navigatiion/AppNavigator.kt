package com.example.mycontactonlyan_3.navigatiion

import androidx.navigation.NavDirections

interface AppNavigator {
     suspend fun navigateTo(direction: NavDirections)
     suspend fun back()
}