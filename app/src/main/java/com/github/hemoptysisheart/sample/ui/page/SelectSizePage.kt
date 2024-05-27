package com.github.hemoptysisheart.sample.ui.page

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.PreviewActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.hemoptysisheart.sample.ui.navigation.SelectSizeNavigator
import com.github.hemoptysisheart.sample.ui.theme.AndroidLibraryTheme
import com.github.hemoptysisheart.sample.viewmodel.SelectSizeViewModel
import com.github.hemoptysisheart.ui.compose.OutlinedTextField
import com.github.hemoptysisheart.ui.navigation.compose.baseNavigator
import com.github.hemoptysisheart.ui.navigation.compose.baseViewModel
import com.github.hemoptysisheart.ui.state.ParsableTextFieldState

@Composable
fun SelectSizePage(
    navigator: SelectSizeNavigator,
    viewModel: SelectSizeViewModel = baseViewModel()
) {
    Log.v(TAG, "#SelectSizePage args : navigator=$navigator, viewModel=$viewModel")

    val width by viewModel.width.collectAsStateWithLifecycle()
    val height by viewModel.height.collectAsStateWithLifecycle()

    SelectSizePageContent(
        navigator = navigator,
        width = width,
        height = height,
        onClickGenerate = viewModel::onClickGenerate,
        onClickDefault = viewModel::onClickDefault
    )
}

@Composable
private fun SelectSizePageContent(
    navigator: SelectSizeNavigator,
    width: ParsableTextFieldState<Int>,
    height: ParsableTextFieldState<Int>,
    onClickGenerate: (() -> Unit) -> Unit = { },
    onClickDefault: () -> Unit = { }
) {
    Log.v(
        TAG,
        listOf(
            "navigator=$navigator",
            "width=$width",
            "height=$height",
            "onClickGenerate=$onClickGenerate",
            "onClickDefault=$onClickDefault"
        ).joinToString(", ", "#SelectSizePage args : ")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                state = width,
                modifier = Modifier.weight(1F),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                placeholder = {
                    Text(text = "Width", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            )
            Text(text = "x", modifier = Modifier.padding(10.dp, 0.dp))
            OutlinedTextField(
                state = height,
                modifier = Modifier.weight(1F),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                placeholder = {
                    Text(text = "Height", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier.widthIn(200.dp),
            onClick = {
                onClickGenerate {
                    navigator.maze(width.parse(), height.parse())
                }
            }
        ) {
            Text(text = "Generate Maze", modifier = Modifier.padding(10.dp), fontWeight = Bold)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(modifier = Modifier.widthIn(200.dp), onClick = onClickDefault) {
            Text(text = "Default Size", modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SelectSizePageContentPreview() {
    AndroidLibraryTheme {
        SelectSizePageContent(
            navigator = SelectSizeNavigator(baseNavigator(PreviewActivity())),
            width = ParsableTextFieldState(
                value = TextFieldValue("10"),
                onValueChange = { _, _ -> },
                _parser = { it.toInt() }),
            height = ParsableTextFieldState(
                value = TextFieldValue("10"),
                onValueChange = { _, _ -> },
                _parser = { it.toInt() }),
            onClickGenerate = {},
            onClickDefault = {}
        )
    }
}
