package com.example.todoapp3.presentation.about_app

import android.net.Uri
import androidx.navigation.NavController
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class MyDivActionHandler(private val navController: NavController) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction(url)) true
        else super.handleAction(action, view, resolver)

    }

    private fun handleSampleAction(action: Uri): Boolean {
        return when (action.host) {
            "navigate_back" -> {
                navController.popBackStack()
                true
            }
            else -> {
                false
            }
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "sample-action"
    }
}