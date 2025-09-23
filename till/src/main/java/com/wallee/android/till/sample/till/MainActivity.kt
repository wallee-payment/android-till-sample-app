package com.wallee.android.till.sample.till

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wallee.android.till.sdk.TillLog
import com.wallee.android.till.sdk.Utils

class MainActivity : ComponentActivity() {

    private var isSystemBarEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // init & bind
        TillLog.getInstance().bind(this)

        TillLog.debug("VSD Test Debug")
        TillLog.error("VSD Test Error")
        TillLog.warning("VSD Test Warning")
        TillLog.lAssert("VSD Send Assert")

        val version = getAppVersion()
        title = "Till Sample $version"

        setContent {
            TillSampleApp(
                appTitle = "$version",
                isSystemBarEnabled = isSystemBarEnabled,
                onToggleSystemBar = { enabled ->
                    if (enabled) enableSystemBar() else disableSystemBar()
                    isSystemBarEnabled = enabled
                },
                onOpenWalleeSettings = { walleeSettingsMenu() },
                onStartServiceTrx = { startTransactionFromService() },
                onStartDeepLinkTrx = { startDeepLinkTransaction() },
                onExit = { finish() },
                // Setting lambda to call function to open any app
                onOpenAnyApp = { pkg ->
                    openAppOrFallback(pkg)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        requestOverlayPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        // unbind
        TillLog.getInstance().unbind(this)
    }

    // Android 10+ needs overlay permission to get transaction response
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                enableSystemBar()
                isSystemBarEnabled = true
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            } else {
                if (isSystemBarEnabled) {
                    disableSystemBar()
                    isSystemBarEnabled = false
                }
            }
        }
    }

    private fun getAppVersion(): String = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            ).versionName ?: "unknown"
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        }
    } catch (e: PackageManager.NameNotFoundException) {
        "unknown"
    }

    private fun startTransactionFromService() {
        val intent = Intent(this, ServiceTransaction::class.java).apply {
            putExtra("amount", "15.00")
            putExtra("currency", "EUR")
        }
        startService(intent)
    }

    private fun startDeepLinkTransaction() {
        startActivity(Intent(this, DeepLinkRequestActivity::class.java))
    }

    private fun enableSystemBar() = Utils.enableSystemBar(this)
    private fun disableSystemBar() = Utils.disableSystemBar(this)
    private fun walleeSettingsMenu() = Utils.openSettings(this)

    // Logic to open the app
    private fun openAppOrFallback(packageName: String) {
        packageManager.getLaunchIntentForPackage(packageName)?.run {
            // In case we want to call service
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}

/* ============================
   Compose UI
   ============================ */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun TillSampleApp(
    appTitle: String,
    isSystemBarEnabled: Boolean,
    onToggleSystemBar: (Boolean) -> Unit,
    onOpenWalleeSettings: () -> Unit,
    onStartServiceTrx: () -> Unit,
    onStartDeepLinkTrx: () -> Unit,
    onExit: () -> Unit,
    // Callback to open any app
    onOpenAnyApp: (String) -> Unit,
) {
    val context = LocalContext.current
    var showOpenDialog by remember { mutableStateOf(false) }
    var pkgInput by remember { mutableStateOf("com.wallee.VTIdemo") } // default

    val actions = remember {
        listOf(

            Action("Open any app", Icons.Filled.Apps) {
                showOpenDialog = true
            },
            Action("Check API Service Compatibility", Icons.Filled.FindInPage) {
                context.startActivity(Intent(context, CheckApiServiceCompatibilityActivity::class.java))
            },
            Action("Authorize Transaction", Icons.Filled.CheckCircle) {
                context.startActivity(Intent(context, AuthorizeTransactionActivity::class.java))
            },
            Action("Cancel Last Operation", Icons.Filled.Cancel) {
                context.startActivity(Intent(context, CancelLastTransactionOperationActivity::class.java))
            },
            Action("Complete Transaction", Icons.Filled.AutoFixHigh) {
                context.startActivity(Intent(context, CompleteTransactionActivity::class.java))
            },
            Action("Void Transaction", Icons.Filled.SwipeDown) {
                context.startActivity(Intent(context, VoidTransactionActivity::class.java))
            },
            Action("Reprint Receipt", Icons.Filled.Print) {
                context.startActivity(Intent(context, ReprintReceiptActivity::class.java))
            },
            Action("Execute Transmission", Icons.Filled.Send) {
                context.startActivity(Intent(context, ExecuteTransmissionActivity::class.java))
            },
            Action("Execute Submission", Icons.Filled.SyncAlt) {
                context.startActivity(Intent(context, ExecuteSubmissionActivity::class.java))
            },
            Action("Execute Final Balance", Icons.Filled.CompareArrows) {
                context.startActivity(Intent(context, ExecuteFinalBalanceActivity::class.java))
            },
            Action("Generate PAN Token", Icons.Filled.Token) {
                context.startActivity(Intent(context, GeneratePanTokenActivity::class.java))
            },
            Action("Get Config Info", Icons.Filled.Description) {
                context.startActivity(Intent(context, PinpadInformationActivity::class.java))
            },
            Action("Configuration", Icons.Filled.SettingsApplications) {
                context.startActivity(Intent(context, ExecuteConfigurationActivity::class.java))
            },
            Action("Initialisation", Icons.Filled.PlayArrow) {
                context.startActivity(Intent(context, ExecuteInitialisationActivity::class.java))
            },
            Action("Terminal Config Data", Icons.Filled.FindInPage) {
                context.startActivity(Intent(context, TerminalConfigurationDataActivity::class.java))
            },
            Action("Custom Config", Icons.Filled.Shield) {
                context.startActivity(Intent(context, GetCustomConfigurationActivity::class.java))
            },
            Action("Exit", Icons.Filled.ExitToApp) {
                onExit()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = appTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "SDK Demo • Wallee",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleSystemBar(!isSystemBarEnabled) }) {
                        Icon(
                            imageVector = Icons.Filled.SettingsCell,
                            contentDescription = "Toggle System Bar"
                        )
                    }
                    IconButton(onClick = onOpenWalleeSettings) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = onStartServiceTrx) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Service Transaction")
                    }
                    IconButton(onClick = onStartDeepLinkTrx) {
                        Icon(imageVector = Icons.Filled.Link, contentDescription = "Deep Link Transaction")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        )
                    )
                )
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(14.dp)
            ) {
                Text(
                    "$appTitle",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    ActionCard(action)
                }
            }
        }
    }

    // NEW: Dialog view to open any app
    if (showOpenDialog) {
        AlertDialog(
            onDismissRequest = { showOpenDialog = false },
            title = { Text("Enter package name") },
            text = {
                OutlinedTextField(
                    value = pkgInput,
                    onValueChange = { pkgInput = it },
                    singleLine = true,
                    label = { Text("e.g. com.wallee.vtidemo") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val p = pkgInput.trim()
                    if (p.isNotEmpty()) onOpenAnyApp(p)
                    showOpenDialog = false
                }) { Text("Open") }
            },
            dismissButton = {
                TextButton(onClick = { showOpenDialog = false }) { Text("Cancel") }
            }
        )
    }
}

data class Action(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

private fun labelToId(label: String): String {
    return label.replace(Regex("[^A-Za-z0-9]"), "")
}

@Composable
private fun ActionCard(action: Action) {
    Card(
        onClick = action.onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.testTag(labelToId(action.title))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = action.icon, contentDescription = action.title)
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
