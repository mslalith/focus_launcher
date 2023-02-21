package dev.mslalith.focuslauncher.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.SearchField
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.ui.viewmodels.PickPlaceViewModel

@Composable
fun PickPlaceForLunarPhaseScreen(
    pickPlaceViewModel: PickPlaceViewModel
) {
    val navController = LocalNavController.current
    val scaffoldState = rememberScaffoldState()

    fun goBack() = navController.popBackStack()

    LaunchedEffect(key1 = Unit) {
        pickPlaceViewModel.updateSearch("")
        pickPlaceViewModel.fetchCities()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Pick a Place",
                onBackPressed = { goBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchField(
                placeholder = "Search city...",
                query = pickPlaceViewModel.searchQueryStateFlow.collectAsState().value,
                onQueryChange = pickPlaceViewModel::updateSearch
            )
            CitiesList(
                cities = pickPlaceViewModel.citiesStateFlow.collectAsState().value,
                onCitySelected = { city ->
                    pickPlaceViewModel.pickCity(city) { goBack() }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CitiesList(
    cities: List<City>,
    onCitySelected: (City) -> Unit
) {
    LazyColumn {
        items(
            items = cities,
            key = { it.id }
        ) { city ->
            ListItem(
                text = {
                    Text(text = city.name)
                },
                modifier = Modifier.clickable { onCitySelected(city) }
            )
        }
    }
}
