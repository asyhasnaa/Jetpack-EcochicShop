package com.dicoding.ecochic.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.ecochic.R
import com.dicoding.ecochic.di.Injection
import com.dicoding.ecochic.ui.ViewModelFactory
import com.dicoding.ecochic.ui.common.UiState

@Composable
fun DetailScreen(
    productId: Int,
    navigateBack: () -> Unit,
    viewModel: DetailProductViewModel = viewModel(
        factory = ViewModelFactory(Injection.productRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getProductById(productId)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailProductInfo(
                    id = data.id,
                    image = data.image,
                    product = data.product,
                    price = data.price,
                    description = data.description,
                    size = data.size,
                    isMark = data.isMark,
                    navigateBack = navigateBack,
                    onMarkIconClicked = { id, state ->
                        viewModel.updateProduct(id, state)
                    }
                )

            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailProductInfo(
    id: Int,
    @DrawableRes image: Int,
    product: String,
    price: String,
    description: String,
    size: String,
    isMark: Boolean,
    navigateBack: () -> Unit,
    onMarkIconClicked: (id: Int, state: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier)
    {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                IconButton(
                    onClick = navigateBack,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                        .align(Alignment.TopStart)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                IconButton(
                    onClick = {
                        onMarkIconClicked(id, isMark)
                    },
                    modifier = Modifier
                        .padding(end = 16.dp, top = 8.dp)
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (isMark) R.drawable.cart_filled else R.drawable.cart_outline),
                        contentDescription = if (!isMark) stringResource(R.string.ecoprint_product) else stringResource(
                            R.string.delete_bookmark
                        ),
                        tint = if (isMark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            )
            {
                Text(
                    text = product,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
                Text(
                    text = "Description",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .fillMaxWidth()
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
                Text(
                    text = "Size",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = size,
                    fontSize = 14.sp,
                    lineHeight = 28.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }

        }

    }
}
