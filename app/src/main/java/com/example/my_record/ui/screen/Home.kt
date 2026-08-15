package com.example.my_record.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel
import com.example.my_record.ui.components.AppButton
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadRecords()
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),              // 画面全体を使う
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
//        Text("ここはHome画面です。")
        Divider(thickness = 1.dp)
        Text(
            text = "今日の判断を記録しましょう",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Divider(thickness = 1.dp)

        AppButton(
            text = "記録へ",
            onClick = {
                navController.navigate("create")
            }
        )

        val pendingCount = viewModel.getPendingCount()

        Text(
            text = "評価待ち：$pendingCount 件",
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )

        AppButton(
            text = "一覧へ",
            onClick = {
                navController.navigate("list")
            },
            modifier = Modifier.navigationBarsPadding()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()              // 画面全体を使う
            .navigationBarsPadding(),   //ナビゲーションバーに被らないように
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var importUri by remember {
            mutableStateOf<Uri?>(null)
        }

        var showImportDialog by remember {
            mutableStateOf(false)
        }
        //エクスポート処理
        val createFileLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.CreateDocument("application/json")
            ) { uri ->

                if (uri != null) {
                    scope.launch {

                        val json = viewModel.exportJson()

                        context.contentResolver
                            .openOutputStream(uri)
                            ?.bufferedWriter()
                            ?.use { writer ->
                                writer.write(json)
                            }
                        Log.d("JSON_EXPORT", "保存完了")
                    }
                }
            }
        //インポート処理
        val openFileLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri ->

                if (uri != null) {
                    importUri = uri
                    showImportDialog = true
                }
            }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppButton(
                text = "エクスポート",
                onClick = {
                    createFileLauncher.launch("backup.json")
                }
            )

            AppButton(
                text = "インポート",
                onClick = {
                    openFileLauncher.launch(arrayOf("application/json"))
                }
            )
        }
        if (showImportDialog) {

            AlertDialog(

                onDismissRequest = {
                    showImportDialog = false
                },

                title = {
                    Text("バックアップを復元")
                },

                text = {
                    Text(
                        "現在の記録はすべて削除されます。\n\n本当に復元しますか？"
                    )
                },

                confirmButton = {

                    Button(
                        onClick = {

                            showImportDialog = false

                            scope.launch {

                                importUri?.let { uri ->

                                    val jsonText =
                                        context.contentResolver
                                            .openInputStream(uri)
                                            ?.bufferedReader()
                                            ?.use { it.readText() }

                                    if (jsonText != null) {
                                        try {
                                            viewModel.importJson(jsonText)

                                            Toast.makeText(
                                                context,
                                                "バックアップを復元しました",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            Log.d(
                                                "JSON_IMPORT",
                                                "インポート成功"
                                            )
                                        } catch (e: Exception) {

                                            Toast.makeText(
                                                context,
                                                "バックアップの読み込みに失敗しました",
                                                Toast.LENGTH_SHORT
//                                                e.message ?: "復元に失敗しました",
//                                                Toast.LENGTH_SHORT
                                            ).show()

                                            Log.e(
                                                "JSON_IMPORT",
                                                "インポート失敗",
                                                e
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text("復元する")
                    }
                },

                dismissButton = {
                    Button(
                        onClick = {
                            showImportDialog = false
                        }
                    ) {
                        Text("キャンセル")
                    }
                }
            )
        }
    }
}
