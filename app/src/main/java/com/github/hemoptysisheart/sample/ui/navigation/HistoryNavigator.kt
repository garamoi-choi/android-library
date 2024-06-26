package com.github.hemoptysisheart.sample.ui.navigation

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.github.hemoptysisheart.ui.navigation.destination.BaseNavigator
import com.github.hemoptysisheart.ui.navigation.destination.Destination
import com.github.hemoptysisheart.ui.navigation.destination.Navigator

/**
 * [com.github.hemoptysisheart.sample.ui.page.HistoryPage]의 화면 이동을 담당한다.
 */
@Immutable
class HistoryNavigator(
    private val base: BaseNavigator
) : Navigator by base {
    companion object : Destination {
        private const val TAG = "HistoryNavigator"

        override val id = "history"
        override val arguments: List<NamedNavArgument> = emptyList()
        override val deepLinks: List<NavDeepLink> = emptyList()

        override fun route(vararg arguments: Any): String {
            if (arguments.isNotEmpty()) {
                throw IllegalArgumentException("does not accept any arguments.")
            } else {
                return id
            }
        }
    }

    override val destination = Companion

    fun selectSize() {
        Log.d(TAG, "#selectSize called.")

        base.navHostController.navigate(SelectSizeNavigator.id) {
            this.popUpTo(base.navHostController.graph.findStartDestination().route!!) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
