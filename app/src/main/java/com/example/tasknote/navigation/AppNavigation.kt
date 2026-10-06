package com.example.tasknote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasknote.ui.tasks.AddTaskScreen
import com.example.tasknote.ui.tasks.TaskDetailScreen
import com.example.tasknote.ui.tasks.TaskScreen
import com.example.tasknote.ui.tasks.TaskViewModel

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.TASK_LIST) {
        composable(Routes.TASK_LIST) {
            TaskScreen(viewModel = viewModel, navController = navController)
        }
        composable(Routes.ADD_TASK) {
            AddTaskScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskDetailScreen(taskId = taskId, viewModel = viewModel, navController = navController)
        }
    }
}
