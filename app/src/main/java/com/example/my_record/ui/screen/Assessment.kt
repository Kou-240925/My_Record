package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.my_record.ui.components.AppButton

@Composable
fun AssessmentScreen(
    navController: NavController,
    viewModel: RecordViewModel,
    id: Int
) {
    // 対象レコードを取得
    val record = viewModel.getRecordById(id)

    if (record == null) {
        Text("データが見つかりません")
        return
    }

    // UI 状態
    var selectedRating by remember {
        mutableStateOf(
            record.rating
//            if (record.rating.isBlank()) "評価待ち" else record.rating
        )
    }
    var memo by remember { mutableStateOf(record.content) } // 評価メモとして内容を再利用

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 画面タイトル
//        Text("評価画面", fontSize = 24.sp)
//        Text("ここは評価画面です。")
        // 保存済みデータの表示
        Text("タイトル：${record.title}", fontSize = 20.sp)
        Text("カテゴリ：${record.category}", fontSize = 18.sp)
        Text("成功確率：${record.successRate}%", fontSize = 18.sp)
        Text("内容：${record.content}", fontSize = 18.sp)

        // 内容（評価メモの元）
//        Text("内容（メモ）：", fontSize = 18.sp)
//        androidx.compose.material3.TextField(
//            value = memo,
//            onValueChange = { memo = it },
//            modifier = Modifier.fillMaxWidth()
//        )


        // 評価プルダウン
        var expanded by remember { mutableStateOf(false) }
        Column {
            Text("評価：$selectedRating", fontSize = 18.sp)

//            Button(onClick = { expanded = true }) {
//                Text("評価を選択")
//            }
            AppButton(
                text = "評価を選択",
                onClick = { expanded = true }
            )
            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("◎", "〇", "△").forEach { rating ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(rating) },
                        onClick = {
                            selectedRating = rating
                            expanded = false
                        }
                    )
                }
            }
        }

        val contentState = remember { mutableStateOf("") }

        TextField(
            value = contentState.value,
            onValueChange = { contentState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text("評価メモ") }
        )

        // 保存ボタン
//        Button(
//            onClick = {
//                val updated = record.copy(
//                    rating = selectedRating,
//                    assessmentMemo = contentState.value,
//                    updatedAt = System.currentTimeMillis()
//                )
//                viewModel.updateRecord(updated)
//                navController.popBackStack()
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("保存する")
//        }
        AppButton(
            text = "保存する",
            onClick = {
                val updated = record.copy(
                    rating = selectedRating,
                    assessmentMemo = contentState.value,
                    updatedAt = System.currentTimeMillis()
                )
                viewModel.updateRecord(updated)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        )

//        Button(onClick = { navController.popBackStack() }) {
//            Text("戻る")
//        }
        AppButton(
            text = "戻る",
            onClick = { navController.popBackStack() },
            modifier = Modifier.navigationBarsPadding()
        )
    }
}
