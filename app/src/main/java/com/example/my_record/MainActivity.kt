package com.example.my_record

import com.example.my_record.ui.screen.CreateScreen
import com.example.my_record.ui.screen.HomeScreen
import com.example.my_record.ui.screen.ListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.NavHostController
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val db = Room.databaseBuilder(
//            applicationContext,
//            MemoDatabase::class.java,
//            "memo.db"
//        ).fallbackToDestructiveMigration()
//            .build()
//
        val db = AppDatabase.getDatabase(this)
        val dao = db.recordDao()
        val viewModel = RecordViewModel(dao)

        setContent {
            // ① ナビゲーション管理者
            val navController = rememberNavController()
            AppNavHost(navController, viewModel)
            // ② 画面の入れ物
//            NavHost(
//                navController = navController,
//                startDestination = "home"
//            ) {
//
//                // ③ 画面登録
//                composable("home") {
//                    HomeScreen(navController)
//                }
//
//                composable("create") {
//                    CreateScreen(navController)
//                }
//
//                composable("list") {
//                    ListScreen(navController)
//                }
//            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: RecordViewModel
) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("create") { CreateScreen(navController, viewModel) }
        composable("list") { ListScreen(navController, viewModel) }
    }
}
