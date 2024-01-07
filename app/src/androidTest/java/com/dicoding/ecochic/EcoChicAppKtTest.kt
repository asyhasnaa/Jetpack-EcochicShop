package com.dicoding.ecochic

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dicoding.ecochic.model.ProductData
import com.dicoding.ecochic.ui.navigation.Screen
import com.dicoding.ecochic.ui.theme.EcochicTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EcoChicAppKtTest{
    @get : Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            EcochicTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                EcoChicApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartIsHome() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_navigatesFromHome_ToDetailWithData() {
        composeTestRule.onNodeWithTag("lazy_list_column").performScrollToIndex(2)
        composeTestRule.onNodeWithText(ProductData.dummyProduct[2].product).performClick()
        navController.assertCurrentRouteName(Screen.DetailProduct.route)
        composeTestRule.onNodeWithText(ProductData.dummyProduct[2].product).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigationIsWorking() {
        composeTestRule.onNodeWithStringId(R.string.menu_cart).performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

}