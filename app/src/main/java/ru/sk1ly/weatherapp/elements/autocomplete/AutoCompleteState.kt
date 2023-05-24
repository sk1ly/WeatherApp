package ru.sk1ly.weatherapp.elements.autocomplete

// See this: https://github.com/pauloaapereira/Medium_JetpackCompose_AutoCompleteSearchBar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private typealias ItemSelected<T> = (T) -> Unit
private var lastLengthOfSearchValue: Int = 0;
@Stable
interface AutoCompleteScope<T : AutoCompleteEntity> : AutoCompleteDesignScope {
    var isSearching: Boolean
    fun filter(query: String)
    fun onItemSelected(block: ItemSelected<T> = {})
}

@Stable
interface AutoCompleteDesignScope {
    var boxWidthPercentage: Float
    var shouldWrapContentHeight: Boolean
    var boxMaxHeight: Dp
    var boxBorderStroke: BorderStroke
    var boxShape: Shape
}

class AutoCompleteState<T : AutoCompleteEntity>(private val startItems: List<T>) : AutoCompleteScope<T> {
    private var onItemSelectedBlock: ItemSelected<T>? = null

    fun selectItem(item: T) {
        onItemSelectedBlock?.invoke(item)
    }

    var filteredItems by mutableStateOf(startItems)
    override var isSearching by mutableStateOf(false)
    override var boxWidthPercentage by mutableStateOf(.9f)
    override var shouldWrapContentHeight by mutableStateOf(false)
    override var boxMaxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 3)
    override var boxBorderStroke by mutableStateOf(BorderStroke(2.dp, Color.White))
    override var boxShape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filter(query: String) {
        filteredItems =
            if (query.length <= 1 || query.length < lastLengthOfSearchValue) {
                startItems.filter { entity ->
                    entity.filter(query)
                }
            } else {
                filteredItems.filter { entity ->
                    entity.filter(query)
                }
            }
        lastLengthOfSearchValue = query.length
    }

    override fun onItemSelected(block: ItemSelected<T>) {
        onItemSelectedBlock = block
    }
}