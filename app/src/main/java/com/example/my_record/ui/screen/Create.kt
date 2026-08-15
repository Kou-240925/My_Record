package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.my_record.RecordEntity
import com.example.my_record.RecordViewModel
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.graphics.Color
import com.example.my_record.ui.components.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {
    // ★ TextField の状態
    val titleState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf("") }

    // ★ プルダウン①：カテゴリ
    val categories = listOf("投資", "学習", "生活")
    var expandedCategory by remember { mutableStateOf(false) }
//    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedCategory by remember { mutableStateOf("") }

    // ★ プルダウン②：種類（例）
    val types = listOf("評価待ち","◎", "〇", "△")
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(types[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()              // 画面全体を使う
            .padding(16.dp)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()) // ← 横向き対策
    ) {
//        Text("ここはCreate画面です。")

        TextField(
            value = titleState.value,
            onValueChange = { titleState.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("タイトル") }
        )
        TextField(
            value = contentState.value,
            onValueChange = { contentState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text("内容") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // ▼ プルダウン①：カテゴリ
        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            TextField(
                value = if (selectedCategory.isEmpty()) "選択してください" else selectedCategory,//selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("カテゴリ") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expandedCategory = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        //スライダー
        var successRate by remember { mutableStateOf(50f) }

        Text(text = "成功確率: ${successRate.toInt()}%")
//        Spacer(modifier = Modifier.height(1.dp))
        Slider(
            value = successRate,
            onValueChange = { successRate = it },
            valueRange = 0f..100f,
            steps = 9,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF1E3A5F),
                activeTrackColor = Color(0xFF1E3A5F),
                inactiveTrackColor = Color(0xFFD8D8D8)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "保存する",
            onClick = {
                val record = RecordEntity(
                    id = 0, // 自動採番
                    title = titleState.value,
                    content = contentState.value,
                    category = selectedCategory,
                    rating = selectedType, // 必要なら数値に変換してもOK
                    date = System.currentTimeMillis(),
                    successRate = successRate.toInt()
                )
                viewModel.insert(record)
                navController.navigate("list")
            }
        )

        // 画面下に押し下げる
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppButton(
                text = "ホーム画面へ",
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier.navigationBarsPadding()
            )
            AppButton(
                text = "一覧へ",
                onClick = {
                    navController.navigate("list")
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}