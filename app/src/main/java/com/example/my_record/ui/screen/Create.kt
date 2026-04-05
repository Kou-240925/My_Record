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
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    // ★ プルダウン②：種類（例）
    val types = listOf("◎", "〇", "△")
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(types[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize(),              // 画面全体を使う
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
        Text("ここはRecorf画面です。")
//        Text(sample.title)
//        Text(sample.date)

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
                value = selectedCategory,
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

        // ▼ プルダウン②：種類
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = { expandedType = !expandedType }
        ) {
            TextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("評価") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expandedType = false
                        }
                    )
                }
            }
        }
        Button(
            onClick = {
                val record = RecordEntity(
                    id = 0, // 自動採番
                    title = titleState.value,
                    content = contentState.value,
                    category = selectedCategory,
                    rating = selectedType, // 必要なら数値に変換してもOK
                    date = System.currentTimeMillis()
                )

                viewModel.insert(record)

                navController.navigate("list")
            }
        ) {
            Text("保存する")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),              // 画面全体を使う
        verticalArrangement = Arrangement.Bottom, // 下に寄せる
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
        Row {
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text("ホーム画面へ")
            }
            Button(
                onClick = {
                    navController.navigate("list")
                }
            ) {
                Text("一覧へ")
            }
        }
    }
}