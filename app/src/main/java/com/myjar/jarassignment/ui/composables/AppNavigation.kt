package com.myjar.jarassignment.ui.composables

import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()
    val navigate = remember { mutableStateOf<String>("") }

    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { selectedValue -> navController.navigate("item_detail/${selectedValue}") },
                navigate = navigate,
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
    navigate: MutableState<String>,
    navController: NavHostController
) {
    val items = viewModel.listStringData.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()
    Log.d("items", "${items.value}")
//    if (navigate.value.isNotBlank()) {
//        val currRoute = navController.currentDestination?.route.orEmpty()
//        if (!currRoute.contains("item_detail")) {
//            navController.navigate("item_detail/${navigate.value}")
//        }
//    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item { SearchBar(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.height(16.dp)) }

        items(items.value) { item ->
            if (item.doesMatchSearchQuery(searchQuery.value)) {
                ItemCard(
                    item = item,
                    onClick = { onNavigateToDetail(item.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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
        item.data?.color?.let { Text(text = "color: $it") }
        item.data?.capacity?.let { Text(text = "capacity: $it") }
        item.data?.price?.let { Text(text = "price: $it") }
        item.data?.capacityGB?.let { Text(text = "capacityGB: $it") }
        item.data?.screenSize?.let { Text(text = "screenSize: $it") }
        item.data?.description?.let { Text(text = "description: $it") }
        item.data?.generation?.let { Text(text = "generation: $it") }
        item.data?.strapColour?.let { Text(text = "strapColour: $it") }
        item.data?.caseSize?.let { Text(text = "caseSize: $it") }
        item.data?.cpuModel?.let { Text(text = "cpuModel: $it") }
        item.data?.hardDiskSize?.let { Text(text = "hardDiskSize: $it") }

    }
}

@Composable
fun ItemDetailScreen(itemId: String?) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository
    Text(
        text = "Item Details for ID: $itemId",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
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
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = "Search") },
        singleLine = true,
        shape = MaterialTheme.shapes.medium
    )
}


