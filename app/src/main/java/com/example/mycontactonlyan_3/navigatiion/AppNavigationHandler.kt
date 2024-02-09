package com.example.mycontactonlyan_3.navigatiion

import kotlinx.coroutines.flow.Flow


interface AppNavigationHandler {

    val buffer : Flow<AppNavigation>
}