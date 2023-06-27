package com.dcns.dailycost.ui.register

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.Status
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.extension.toast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.state.StateDialog
import com.maxkeppeler.sheets.state.models.ProgressIndicator
import com.maxkeppeler.sheets.state.models.State
import com.maxkeppeler.sheets.state.models.StateConfig
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
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

    LaunchedEffect(state.resource) {
        when (state.resource?.status) {
            Status.Success -> {
                Timber.i("register success")

                context.getString(R.string.registration_success).toast(context)

                useCaseState.hide()

                navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
            }
            Status.Error -> {
                Timber.i("Register error: ${state.resource?.message}")

                state.resource?.message.toast(context, Toast.LENGTH_LONG)

                useCaseState.hide()
            }
            Status.Loading -> {
                useCaseState.show()
            }
            else -> {}
        }
    }

    BackHandler {
        navigationActions.navigateTo(
            destination = TopLevelDestinations.LoginRegister.login,
            inclusivePopUpTo = true
        )
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
                            navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
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
            RegisterScreenContent(
                state = state,
                onShowPasswordCheckedChanged = { show ->
                    viewModel.onAction(RegisterAction.UpdateShowPassword(show))
                },
                onEmailChanged = { s ->
                    viewModel.onAction(RegisterAction.UpdateEmail(s))
                },
                onPasswordChanged = { s ->
                    viewModel.onAction(RegisterAction.UpdatePassword(s))
                },
                onSignUpClicked = {
                    viewModel.onAction(RegisterAction.SignUp(context))
                },
                onSignInClicked = {
                    navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun RegisterScreenContent(
    state: RegisterState,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit = {},
    onUsernameChanged: (String) -> Unit = {},
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
            username = state.username,
            emailError = state.emailError,
            passwordError = state.passwordError,
            showPassword = state.showPassword,
            onShowPasswordCheckedChanged = onShowPasswordCheckedChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onUsernameChanged = onUsernameChanged,
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
            text = stringResource(id = R.string.create_daily_cost_account),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(id = R.string.unlock_all_the_features_to_manage_income_expenses),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CenterContent(
    email: String,
    password: String,
    username: String,
    emailError: String?,
    passwordError: String?,
    showPassword: Boolean,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    val (
        emailFocusRequester,
        passwordFocusRequester,
        usernameFocusRequester
    ) = FocusRequester.createRefs()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = username,
            singleLine = true,
            onValueChange = onUsernameChanged,
            shape = RoundedCornerShape(20),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            label = {
                Text(stringResource(id = R.string.username))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(usernameFocusRequester)
        )

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
    }
}

@Composable
private fun BottomContent(
    modifier: Modifier = Modifier,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {

    val uriHandler = LocalUriHandler.current

    val registerTouAndPpText = buildAnnotatedString {
        val tou = stringResource(id = R.string.terms_of_use)
        val pp = stringResource(id = R.string.privacy_policy)
        var text = stringResource(
            id = R.string.by_registering_txt,
            tou,
            pp
        )

        text = text.replace(tou, "\n $tou")

        append(text)

        val startTou = text.indexOf(tou)
        val endTou = startTou + tou.length

        val startPp = text.indexOf(pp)
        val endPp = startPp + pp.length

        addStyle(
            end = endTou,
            start = startTou,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ).toSpanStyle()
        )

        addStyle(
            end = endPp,
            start = startPp,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ).toSpanStyle()
        )

        addStringAnnotation(
            tag = "tou",
            annotation = Constant.TERMS_OF_USE_URL,
            start = startTou,
            end = endTou
        )

        addStringAnnotation(
            tag = "pp",
            annotation = Constant.PRIVACY_POLICY_URL,
            start = startPp,
            end = endPp
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Button(
            onClick = onSignUpClicked,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.sign_up))
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
                    append(stringResource(id = R.string.sign_in))
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    onSignInClicked()
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ClickableText(
            text = registerTouAndPpText,
            onClick = { offset ->
                registerTouAndPpText
                    .getStringAnnotations("tou", offset, offset)
                    .firstOrNull()?.let {
                        uriHandler.openUri(it.item)

                        return@ClickableText
                    }

                registerTouAndPpText
                    .getStringAnnotations("pp", offset, offset)
                    .firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
            }
        )
    }
}

