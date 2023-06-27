package com.dcns.dailycost.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.Status
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.theme.DailyCostTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.state.StateDialog
import com.maxkeppeler.sheets.state.models.ProgressIndicator
import com.maxkeppeler.sheets.state.models.State
import com.maxkeppeler.sheets.state.models.StateConfig
import timber.log.Timber

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenContentPreview() {
    DailyCostTheme {
        LoginScreenContent(state = LoginState())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigationActions: NavigationActions
) {

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val useCaseState = rememberUseCaseState(
        visible = false,
        onCloseRequest = {}
    )

    val stateDialogState = State.Loading(
        "Wait a moment",
        ProgressIndicator.Circular()
    )

    LaunchedEffect(Unit) {
        // called on first recomposition
        viewModel.onAction(LoginAction.ClearData(context))
    }

    LaunchedEffect(state.resource) {
        when (state.resource?.status) {
            Status.Success -> {
                Timber.i("Login or register success")

                context.getString(R.string.login_success).toast(context)

                useCaseState.hide()

                navigationActions.navigateTo(TopLevelDestinations.Home.dashboard)
            }
            Status.Error -> {
                Timber.i("Login error: ${state.resource?.message}")

                state.resource?.message.toast(context, Toast.LENGTH_LONG)

                useCaseState.hide()
            }
            Status.Loading -> {
                useCaseState.show()
            }
            else -> {}
        }
    }

    StateDialog(
        state = useCaseState,
        config = StateConfig(
            state = stateDialogState
        )
    )

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationActions.navigateTo(TopLevelDestinations.Onboarding.onboarding)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            LoginScreenContent(
                state = state,
                onShowPasswordCheckedChanged = { show ->
                    viewModel.onAction(LoginAction.UpdateShowPassword(show))
                },
                onRememberMeCheckedChanged = { remember ->
                    viewModel.onAction(LoginAction.UpdateRememberMe(remember))
                },
                onEmailChanged = { s ->
                    viewModel.onAction(LoginAction.UpdateEmail(s))
                },
                onPasswordChanged = { s ->
                    viewModel.onAction(LoginAction.UpdatePassword(s))
                },
                onSignInClicked = {
                    viewModel.onAction(LoginAction.SignIn(context))
                },
                onSignUpClicked = {
                    navigationActions.navigateTo(TopLevelDestinations.LoginRegister.register)
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginState,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit = {},
    onRememberMeCheckedChanged: (Boolean) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onSignInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {}
) {
    val constraintSet = ConstraintSet {
        val (
            topContent,
            centerContent,
            bottomContent
        ) = createRefsFor(
            "topContent",
            "centerContent",
            "bottomContent",
        )

        constrain(topContent) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
        }

        constrain(centerContent) {
            centerHorizontallyTo(parent)

            top.linkTo(topContent.bottom)
            bottom.linkTo(bottomContent.top)
        }

        constrain(bottomContent) {
            centerHorizontallyTo(parent)

            top.linkTo(centerContent.bottom)
            bottom.linkTo(parent.bottom)
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        TopContent(
            modifier = Modifier
                .layoutId("topContent")
        )

        CenterContent(
            email = state.email,
            password = state.password,
            rememberMe = state.rememberMe,
            emailError = state.emailError,
            passwordError = state.passwordError,
            showPassword = state.showPassword,
            onShowPasswordCheckedChanged = onShowPasswordCheckedChanged,
            onRememberMeCheckedChanged = onRememberMeCheckedChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            modifier = Modifier
                .layoutId("centerContent")
        )

        BottomContent(
            onSignInClicked = onSignInClicked,
            onSignUpClicked = onSignUpClicked,
            modifier = Modifier
                .layoutId("bottomContent")
        )
    }
}

@Composable
private fun TopContent(
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.sign_in_to_daily_cost),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(id = R.string.record_all_your_financial_activities_anywhere),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CenterContent(
    email: String,
    password: String,
    rememberMe: Boolean,
    emailError: String?,
    passwordError: String?,
    showPassword: Boolean,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit,
    onRememberMeCheckedChanged: (Boolean) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    val (
        emailFocusRequester,
        passwordFocusRequester
    ) = FocusRequester.createRefs()

    val rememberMeConstraintSet = ConstraintSet {
        val (
            checkbox,
            titleText,
            bodyText
        ) = createRefsFor(
            "checkbox",
            "titleText",
            "bodyText"
        )

        constrain(checkbox) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(titleText) {
            top.linkTo(checkbox.top)
            bottom.linkTo(checkbox.bottom)
            start.linkTo(checkbox.end)
        }

        constrain(bodyText) {
            top.linkTo(titleText.bottom, 8.dp)
            start.linkTo(checkbox.end)
            end.linkTo(parent.end)

            width = Dimension.fillToConstraints
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = email,
            singleLine = true,
            isError = emailError != null,
            onValueChange = onEmailChanged,
            shape = RoundedCornerShape(20),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            label = {
                Text(stringResource(id = R.string.email))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email_or_sms),
                    contentDescription = null
                )
            },
            supportingText = if (emailError != null) {
                {
                    Text(emailError)
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
        )

        OutlinedTextField(
            value = password,
            isError = passwordError != null,
            singleLine = true,
            onValueChange = onPasswordChanged,
            shape = RoundedCornerShape(20),
            visualTransformation = if (!showPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            label = {
                Text(stringResource(id = R.string.password))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onShowPasswordCheckedChanged(!showPassword)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (showPassword) R.drawable.ic_eye
                            else R.drawable.ic_eye_slash
                        ),
                        contentDescription = null
                    )
                }
            },
            supportingText = if (passwordError != null) {
                {
                    Text(passwordError)
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
        )

        ConstraintLayout(
            constraintSet = rememberMeConstraintSet,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { remember ->
                    onRememberMeCheckedChanged(remember)
                },
                modifier = Modifier
                    .layoutId("checkbox")
            )
            
            Text(
                text = stringResource(id = R.string.keep_me_signed_in),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .layoutId("titleText")
            )

            Text(
                text = stringResource(id = R.string.by_checking_this_box_you_wont_to_sign),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .layoutId("bodyText")
            )
        }
    }
}

@Composable
private fun BottomContent(
    modifier: Modifier = Modifier,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Button(
            onClick = onSignInClicked,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.sign_in))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.already_have_an_account)
            )

            ClickableText(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.sign_up))
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                onClick = {
                    onSignUpClicked()
                }
            )
        }
    }
}
