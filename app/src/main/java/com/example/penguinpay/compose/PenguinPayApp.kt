package com.example.penguinpay.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.penguinpay.compose.transaction.MakeTransactionScreen
import com.example.penguinpay.compose.transaction.ReviewTransactionScreen
import com.example.penguinpay.viewmodel.TransactionViewModel


@Composable
fun PenguinPayApp() {
    val navController = rememberNavController()
    PenguinPayNavHost(
        navController = navController
    )
}

/**
 * I'm used to using the xml style Nav graph, but for such a small app,
 * this seemed more straightforward
 */
@Composable
fun PenguinPayNavHost(
    navController: NavHostController
) {
    val viewModel: TransactionViewModel = viewModel()
    NavHost(navController = navController, startDestination = "makeTransaction") {
        composable(route = "makeTransaction") {
            MakeTransactionScreen(
                viewModel = viewModel,
                onReviewClick = {
                    viewModel.onReviewClicked()
                    navController.navigate("reviewTransaction")
                }
            )
        }
        composable(route = "reviewTransaction") {
            ReviewTransactionScreen(
                viewModel = viewModel,
                onSubmitClicked = {
                    viewModel.onSubmitClicked()
                    navController.navigateUp()
                }
            )
        }
    }
}