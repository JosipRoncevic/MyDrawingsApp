package org.unizd.rma.roncevic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.unizd.rma.roncevic.ui.screen.drawing.DrawingScreen
import org.unizd.rma.roncevic.ui.screen.drawing.DrawingViewModel
import org.unizd.rma.roncevic.ui.screen.drawings_list.DrawingListScreen
import org.unizd.rma.roncevic.ui.screen.drawings_list.DrawingListViewModel
import org.unizd.rma.roncevic.ui.theme.MyDrawingsAppTheme
import org.unizd.rma.roncevic.ui.util.Route
import org.unizd.rma.roncevic.ui.util.UiEvent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MyDrawingsAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.drawingList
                ){
                    composable(route = Route.drawingList){
                        val viewModel = hiltViewModel<DrawingListViewModel>()
                        val drawingList by viewModel.drawingList.collectAsStateWithLifecycle()

                        DrawingListScreen(
                            drawingList = drawingList,
                            onDrawingClick = {
                                navController.navigate(
                                    Route.drawing.replace(
                                        "{id}",
                                        it.id.toString()
                                    )
                                )
                            },
                            onAddDrawingClick = {
                                navController.navigate(Route.newDrawing)
                            }
                        )
                    }
                    composable(route = Route.drawing) {
                        val viewModel = hiltViewModel<DrawingViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = true) {
                            viewModel.event.collect{ event ->
                                when(event){
                                    is UiEvent.NavigateBack ->{
                                        navController.popBackStack()
                                    }
                                    else -> Unit
                                }
                            }
                        }
                        DrawingScreen(
                            state= state,
                            onEvent = viewModel::onEvent
                        )
                    }
                    composable(route = Route.newDrawing) {
                        val viewModel = hiltViewModel<DrawingViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = true) {
                            viewModel.event.collect { event ->
                                when (event) {
                                    is UiEvent.NavigateBack -> {
                                        navController.popBackStack()
                                    }
                                    else -> Unit
                                }
                            }
                        }

                        DrawingScreen(
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}
