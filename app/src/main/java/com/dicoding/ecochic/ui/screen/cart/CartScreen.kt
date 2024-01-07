package com.dicoding.ecochic.ui.screen.cart

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.ecochic.R
import com.dicoding.ecochic.di.Injection
import com.dicoding.ecochic.model.Product
import com.dicoding.ecochic.ui.ViewModelFactory
import com.dicoding.ecochic.ui.common.UiState
import com.dicoding.ecochic.ui.screen.home.ListProduct

@Composable
fun CartScreen(
    navigateToDetail: (Int) -> Unit,
    viewmodel: CartViewModel = viewModel(
        factory = ViewModelFactory(Injection.productRepository())
    )
) {

    viewmodel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewmodel.getMarkProduct()
            }

            is UiState.Success -> {
                CartProductInfo(
                    listProduct = uiState.data,
                    navigateToDetail = navigateToDetail,
                    onMarkIconClicked = { id, newState ->
                        viewmodel.updateProduct(id, newState)
                    }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartProductInfo(
    listProduct: List<Product>,
    navigateToDetail: (Int) -> Unit,
    onMarkIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.topbar_cart),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        val context = LocalContext.current
        if (listProduct.isNotEmpty()) {
            ListProduct(
                listProduct = listProduct,
                onMarkIconClicked = onMarkIconClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    "No Product in Cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
