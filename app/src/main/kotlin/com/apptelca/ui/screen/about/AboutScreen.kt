package com.apptelca.ui.screen.about

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptelca.BuildConfig
import com.apptelca.R
import com.apptelca.ui.ads.AdsViewModel
import com.apptelca.ui.theme.AppTheme
import com.apptelca.core.Util

/**
 * Crea la pantalla "Acerca de".
 *
 * @param onNavToBack Define la acción de navegación hacia atrás.
 * @param adsViewModel Una instancia de AboutViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavToBack: () -> Unit,
    adsViewModel: AdsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val uiState by adsViewModel.uiState.collectAsStateWithLifecycle()

    AppTheme(windowWidthSizeClass = Util.getWindowWidthSizeClass(LocalActivity.current)) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.tab_item_about)) },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = onNavToBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.tab_back_button_description)
                            )
                        }
                    }
                )
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(text = BuildConfig.VERSION_NAME)

                    Icon(
                        modifier = Modifier
                            .size(256.dp)
                            .scale(1.5F),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = stringResource(R.string.developer_name))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = stringResource(R.string.developer_email))
                    }
                    Spacer(modifier = Modifier.height(64.dp))

                    if(uiState.isConsentRequired) {
                        Text(text = stringResource(R.string.ads_privacy_message))
                        Spacer(modifier = Modifier.height(16.dp))

                        val activity = LocalActivity.current
                        Button(
                            onClick = {
                                activity?.let {
                                    adsViewModel.showPrivacyOptionsForm(it)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = Color.Gray
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Text(text = stringResource(R.string.btn_ads_setup))
                        }
                        Spacer(modifier = Modifier.height(64.dp))
                    }

                    val privacyPolicyText = stringResource(R.string.privacy_policy_text)
                    val annotatedString = buildAnnotatedString {
                        withLink(
                            LinkAnnotation.Url(
                                url = stringResource(R.string.privacy_policy_link),
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            )
                        ) {
                            append(privacyPolicyText)
                        }
                    }

                    //Text(text = annotatedString)
                }
            }
        }
    }
}
