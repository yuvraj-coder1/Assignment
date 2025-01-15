package com.myjar.jarassignment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.R
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()
    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { selectedValue -> navController.navigate("item_detail/${selectedValue}") },
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId, viewModel = viewModel)
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
) {
    val items = viewModel.listStringData.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()
    val filteredItems = items.value.filter { it.doesMatchSearchQuery(searchQuery.value) }
    val resultsFound = remember { mutableStateOf(false) }
    resultsFound.value = (filteredItems.isNotEmpty() && searchQuery.value.isNotBlank())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item { SearchBar(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        items(filteredItems) { item ->
            ItemCard(
                item = item,
                onClick = { onNavigateToDetail(item.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (!resultsFound.value && searchQuery.value.isNotBlank()) {
            item { Text(text = "No results found") }
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = item.name, fontWeight = FontWeight.Bold)
        item.data?.color?.let { Text(text = stringResource(R.string.color, it)) }
        item.data?.capacity?.let { Text(text = stringResource(R.string.capacity, it)) }
        item.data?.price?.let { Text(text = stringResource(R.string.price, it)) }
        item.data?.capacityGB?.let { Text(text = stringResource(R.string.capacitygb_gb, it) ) }
        item.data?.screenSize?.let { Text(text = stringResource(R.string.screensize_inch, it)) }
        item.data?.description?.let { Text(text = stringResource(R.string.description, it)) }
        item.data?.generation?.let { Text(text = stringResource(R.string.generation, it)) }
        item.data?.strapColour?.let { Text(text = stringResource(R.string.strapcolour, it)) }
        item.data?.caseSize?.let { Text(text = stringResource(R.string.casesize, it)) }
        item.data?.cpuModel?.let { Text(text = stringResource(R.string.cpumodel, it)) }
        item.data?.hardDiskSize?.let { Text(text = stringResource(R.string.harddisksize, it)) }

    }
}

@Composable
fun ItemDetailScreen(modifier: Modifier = Modifier,itemId: String?, viewModel: JarViewModel) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository
    val item = viewModel.listStringData.collectAsState().value.find {
        it.id == itemId
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item?.name?.let { Text(text = it, fontWeight = FontWeight.Bold) }
        item?.data?.color?.let { Text(text = stringResource(R.string.color, it)) }
        item?.data?.capacity?.let { Text(text = stringResource(R.string.capacity, it)) }
        item?.data?.price?.let { Text(text = stringResource(R.string.price, it)) }
        item?.data?.capacityGB?.let { Text(text = stringResource(R.string.capacitygb_gb, it) ) }
        item?.data?.screenSize?.let { Text(text = stringResource(R.string.screensize_inch, it)) }
        item?.data?.description?.let { Text(text = stringResource(R.string.description, it)) }
        item?.data?.generation?.let { Text(text = stringResource(R.string.generation, it)) }
        item?.data?.strapColour?.let { Text(text = stringResource(R.string.strapcolour, it)) }
        item?.data?.caseSize?.let { Text(text = stringResource(R.string.casesize, it)) }
        item?.data?.cpuModel?.let { Text(text = stringResource(R.string.cpumodel, it)) }
        item?.data?.hardDiskSize?.let { Text(text = stringResource(R.string.harddisksize, it)) }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, viewModel: JarViewModel) {
    val searchQuery = viewModel.searchQuery.collectAsState()
    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { viewModel.updateSearchQuery(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = "Search") },
        singleLine = true,
        shape = MaterialTheme.shapes.medium
    )
}


