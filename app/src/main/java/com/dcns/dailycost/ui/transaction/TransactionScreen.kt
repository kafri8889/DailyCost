package com.dcns.dailycost.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    navigationActions: NavigationActions,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(
        viewModel = viewModel,
    ) { _ ->
        ExpenseScreenContent(
            state = state,
            onNavigationIconClicked = {
                navigationActions.popBackStack()
            },
            modifier = Modifier
                .statusBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseScreenContent(
    state: TransactionState,
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                }
            )
        }

//        item {
//            OutlinedTextField(
//                value = state.name,
//                singleLine = true,
//                onValueChange = onUsernameChanged,
//                shape = RoundedCornerShape(20),
//                colors = OutlinedTextFieldDefaults.dailyCostColor(),
//                keyboardOptions = KeyboardOptions(
//                    imeAction = ImeAction.Next,
//                    keyboardType = KeyboardType.Text
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = {
//                        focusManager.moveFocus(FocusDirection.Next)
//                    }
//                ),
//                label = {
//                    Text(stringResource(id = R.string.username))
//                },
//                leadingIcon = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_profile),
//                        contentDescription = null
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(usernameFocusRequester)
//            )
//        }
    }
}
