package com.dcns.dailycost.ui.login_register

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.LoginRegisterType
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.Status
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.model.remote.response.LoginResponse
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
fun LoginRegisterScreen(
    viewModel: LoginRegisterViewModel,
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
        viewModel.onAction(LoginRegisterAction.ClearData(context))
    }

    LaunchedEffect(state.resource) {
        when (state.resource?.status) {
            Status.Success -> {
                Timber.i("Login or register success")

                if (state.resource?.data is LoginResponse) {
                    context.getString(R.string.login_success).toast(context)
                } else context.getString(R.string.registration_success).toast(context)

                useCaseState.hide()

                if (!state.rememberMe) {
                    navigationActions.navigateTo(TopLevelDestinations.Home.dashboard)
                }
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

    BaseScreenWrapper(viewModel) { scaffoldPadding ->
        LoginScreenContent(
            state = state,
            paddingValues = scaffoldPadding,
            onShowPasswordCheckedChanged = { show ->
                viewModel.onAction(LoginRegisterAction.UpdateShowPassword(show))
            },
            onRememberMeCheckedChanged = { remember ->
                viewModel.onAction(LoginRegisterAction.UpdateRememberMe(remember))
            },
            onEmailChanged = { s ->
                viewModel.onAction(LoginRegisterAction.UpdateEmail(s))
            },
            onPasswordChanged = { s ->
                viewModel.onAction(LoginRegisterAction.UpdatePassword(s))
            },
            onUsernameChanged = { s ->
                viewModel.onAction(LoginRegisterAction.UpdateUsername(s))
            },
            onPasswordReChanged = { s ->
                viewModel.onAction(LoginRegisterAction.UpdatePasswordRe(s))
            },
            onSignUpOrLoginClicked = {
                viewModel.onAction(
                    LoginRegisterAction.UpdateLoginRegisterType(
                        if (state.loginRegisterType == LoginRegisterType.Login) {
                            LoginRegisterType.Register
                        } else LoginRegisterType.Login
                    )
                )
            },
            onLogin = {
                viewModel.onAction(LoginRegisterAction.Login(context))
            }
        )
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginRegisterState,
    paddingValues: PaddingValues = PaddingValues(),
    onShowPasswordCheckedChanged: (Boolean) -> Unit = {},
    onRememberMeCheckedChanged: (Boolean) -> Unit = {},
    onPasswordReChanged: (String) -> Unit = {},
    onUsernameChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onSignUpOrLoginClicked: () -> Unit = {},
    onLogin: () -> Unit = {}
) {

    val constraintSet = ConstraintSet {
        val (
            topContent,
            centerContent,
            bottomContent
        ) = createRefsFor(
            "topContent",
            "centerContent",
            "bottomContent"
        )

        constrain(topContent) {
            centerHorizontallyTo(parent)

            top.linkTo(parent.top)
            bottom.linkTo(centerContent.top)
        }

        constrain(centerContent) {
            centerTo(parent)

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
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = ColorPainter(Color.Gray),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(25))
                .layoutId("topContent")
        )

        CenterContent(
            username = state.username,
            email = state.email,
            emailError = state.emailError,
            password = state.password,
            passwordRe = state.passwordRe,
            passwordError = state.passwordError,
            showPassword = state.showPassword,
            rememberMe = state.rememberMe,
            loginRegisterType = state.loginRegisterType,
            onShowPasswordCheckedChanged = onShowPasswordCheckedChanged,
            onRememberMeCheckedChanged = onRememberMeCheckedChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onUsernameChanged = onUsernameChanged,
            onPasswordReChanged = onPasswordReChanged,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .layoutId("centerContent")
        )

        BottomContent(
            loginRegisterType = state.loginRegisterType,
            onSignUpOrLoginClicked = onSignUpOrLoginClicked,
            onLogin = onLogin,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .layoutId("bottomContent")
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CenterContent(
    username: String,
    email: String,
    emailError: String?,
    password: String,
    passwordRe: String,
    passwordError: String?,
    showPassword: Boolean,
    rememberMe: Boolean,
    loginRegisterType: LoginRegisterType,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit,
    onRememberMeCheckedChanged: (Boolean) -> Unit,
    onPasswordReChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {

    val uriHandler = LocalUriHandler.current
    val focusManager = LocalFocusManager.current

    val (
        usernameFocusRequester,
        passwordReFocusRequester,
        passwordFocusRequester,
        emailFocusRequester,
    ) = FocusRequester.createRefs()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = loginRegisterType == LoginRegisterType.Register,
            enter = expandVertically(
                animationSpec = tween(256)
            ),
            exit = shrinkVertically(
                animationSpec = tween(256)
            )
        ) {
            OutlinedTextField(
                value = username,
                singleLine = true,
                onValueChange = onUsernameChanged,
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
        }

        OutlinedTextField(
            value = email,
            singleLine = true,
            isError = emailError != null,
            onValueChange = onEmailChanged,
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
            visualTransformation = if (!showPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = if (loginRegisterType == LoginRegisterType.Login) ImeAction.Done
                else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
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

        AnimatedVisibility(
            visible = loginRegisterType == LoginRegisterType.Register,
            enter = expandVertically(
                animationSpec = tween(256)
            ),
            exit = shrinkVertically(
                animationSpec = tween(256)
            )
        ) {
            OutlinedTextField(
                value = passwordRe,
                singleLine = true,
                onValueChange = onPasswordReChanged,
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
                    Text(stringResource(id = R.string.re_password))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordReFocusRequester)
            )
        }

        AnimatedVisibility(
            visible = loginRegisterType == LoginRegisterType.Register,
            enter = expandVertically(
                animationSpec = tween(256)
            ),
            exit = shrinkVertically(
                animationSpec = tween(256)
            )
        ) {
            val registerTouAndPpText = buildAnnotatedString {
                val tou = stringResource(id = R.string.terms_of_use)
                val pp = stringResource(id = R.string.privacy_policy)
                val text = stringResource(
                    id = R.string.by_registering_txt,
                    tou,
                    pp
                )

                append(text)

                val startTou = text.indexOf(tou)
                val endTou = startTou + tou.length

                val startPp = text.indexOf(pp)
                val endPp = startPp + pp.length

                addStyle(
                    end = endTou,
                    start = startTou,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ).toSpanStyle()
                )

                addStyle(
                    end = endPp,
                    start = startPp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
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

        AnimatedVisibility(
            visible = loginRegisterType == LoginRegisterType.Login,
            enter = expandVertically(
                animationSpec = tween(256)
            ),
            exit = shrinkVertically(
                animationSpec = tween(256)
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = onRememberMeCheckedChanged
                )

                Text(text = stringResource(id = R.string.remember_me))

                Spacer(modifier = Modifier.weight(1f))

//                ClickableText(
//                    text = buildAnnotatedString {
//                        append(stringResource(id = R.string.forgot_password))
//                    },
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        fontWeight = FontWeight.Bold
//                    ),
//                    onClick = {
//
//                    }
//                )
            }
        }
    }
}

@Composable
private fun BottomContent(
    loginRegisterType: LoginRegisterType,
    modifier: Modifier = Modifier,
    onSignUpOrLoginClicked: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                stringResource(
                    id = if (loginRegisterType == LoginRegisterType.Login) R.string.login
                    else R.string.register
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(
                    id = if (loginRegisterType == LoginRegisterType.Login) R.string.dont_have_an_account
                    else R.string.already_have_an_account
                )
            )

            ClickableText(
                text = buildAnnotatedString {
                    append(
                        stringResource(
                            id = if (loginRegisterType == LoginRegisterType.Login) R.string.sign_up
                            else R.string.login
                        )
                    )
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                ),
                onClick = {
                    onSignUpOrLoginClicked()
                }
            )
        }
    }
}

@Preview("Login Screen", showBackground = true)
@Composable
fun LoginScreenContentPreview() {

    LoginScreenContent(
        state = LoginRegisterState()
    )
}
