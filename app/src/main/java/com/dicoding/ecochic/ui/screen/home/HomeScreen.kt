package com.dicoding.ecochic.ui.screen.home

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.ecochic.di.Injection
import com.dicoding.ecochic.model.Product
import com.dicoding.ecochic.ui.ViewModelFactory
import com.dicoding.ecochic.ui.common.UiState
import com.dicoding.ecochic.ui.components.ProductItem
import com.dicoding.ecochic.ui.components.SearchBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.productRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.search(query)
            }

            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChanged = viewModel::search,
                    listProduct = uiState.data,
                    onMarkIconClicked = { id, newState ->
                        viewModel.updateProduct(id, newState)
                        viewModel.updateProduct(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {}
        }
    }

}

@Composable
fun HomeContent(
    query: String,
    onQueryChanged: (String) -> Unit,
    listProduct: List<Product>,
    onMarkIconClicked: (id: Int, newState: Boolean) -> Unit,
    navigateToDetail: (Int) -> Unit,
    ) {
    val context = LocalContext.current
    Column {
        SearchBar(
            query = query,
            onQueryChange = onQueryChanged,
        )
        if (listProduct.isNotEmpty()) {
            ListProduct(
                listProduct = listProduct,
                onMarkIconClicked = onMarkIconClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            LaunchedEffect(query) {
                if (query.isNotEmpty()) {
                    Toast.makeText(
                        context,
                        "Sorry, the product is not available yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListProduct(
    listProduct: List<Product>,
    onMarkIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    contentPaddingOnTop: Dp = 4.dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = contentPaddingOnTop
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .testTag("lazy_list_column")
    ) {
        items(listProduct, key = { it.id }) { item ->
            ProductItem(
                id = item.id,
                image = item.image,
                title = item.title,
                product = item.product,
                price = item.price,
                isMark = item.isMark,
                onMarkIconClicked = onMarkIconClicked,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 300))
                    .clickable { navigateToDetail(item.id) }
            )
        }
    }
}