package com.example.my_record.ui.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel
import com.example.my_record.ui.components.AppButton
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataManagementScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var importUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showImportDialog by remember {
        mutableStateOf(false)
    }

    // =========================
    // JSONエクスポート
    // =========================

    val createFileLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument(
                "application/json"
            )
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

                    Log.d(
                        "JSON_EXPORT",
                        "保存完了"
                    )
                }
            }
        }

    // =========================
    // JSONインポート
    // =========================

    val openFileLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                importUri = uri
                showImportDialog = true
            }
        }

    // =========================
    // 画面
    // =========================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("データ管理")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Top

        ) {

            Text(
                text = "バックアップ"
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            AppButton(
                text = "エクスポート",
                onClick = {
                    createFileLauncher.launch("backup.json")
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            AppButton(
                text = "インポート",
                onClick = {
                    openFileLauncher.launch(
                        arrayOf("application/json")
                    )
                }
            )
        }
    }

    // =========================
    // インポート確認ダイアログ
    // =========================

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
                    "現在の記録はすべて削除されます。\n\n" +
                            "本当に復元しますか？"
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
                                        ?.use {
                                            it.readText()
                                        }

                                if (jsonText != null) {

                                    try {

                                        viewModel.importJson(
                                            jsonText
                                        )

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