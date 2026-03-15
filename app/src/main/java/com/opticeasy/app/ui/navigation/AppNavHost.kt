package com.opticeasy.app.ui.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.ui.screens.auth.InicioScreen
import com.opticeasy.app.ui.screens.auth.LoginScreen
import com.opticeasy.app.ui.screens.auth.RegisterScreen
import com.opticeasy.app.ui.screens.calculadora.CalculadoraLCScreen
import com.opticeasy.app.ui.screens.clientes.BuscarClienteScreen
import com.opticeasy.app.ui.screens.clientes.ClienteDetalleScreen
import com.opticeasy.app.ui.screens.clientes.ClientesScreen
import com.opticeasy.app.ui.screens.clientes.ListadoClientesScreen
import com.opticeasy.app.ui.screens.menu.MenuScreen
import com.opticeasy.app.ui.screens.revisiones.ListadoRevisionesScreen
import com.opticeasy.app.ui.screens.revisiones.gafa.NuevaRevisionGafaScreen
import com.opticeasy.app.ui.screens.revisiones.lc.NuevaRevisionLcScreen
import com.opticeasy.app.ui.screens.revisiones.pdf.PdfViewerScreen
import com.opticeasy.app.ui.screens.rgpd.FirmaRgpdScreen
import com.opticeasy.app.ui.screens.splash.SplashScreen
import com.opticeasy.app.viewmodel.clientes.ClientesBuscarViewModel
import com.opticeasy.app.viewmodel.revisiones.gafa.NuevaRevisionGafaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val clientesBuscarViewModel: ClientesBuscarViewModel = viewModel()

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val adminUsuarios by sessionManager.adminUsuarios.collectAsState(initial = 0)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {


        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.INICIO) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.INICIO) {
            InicioScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onCrearUsuarioClick = { navController.navigate(Routes.REGISTRO) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MENU) {
                        popUpTo(Routes.INICIO) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.REGISTRO) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTRO) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.MENU) {
            MenuScreen(
                onCrearCliente = {
                    navController.navigate(Routes.CREAR_CLIENTE)
                },
                onConsultarCliente = {
                    clientesBuscarViewModel.reset()
                    navController.navigate(Routes.BUSCAR_CLIENTE)
                },
                onCalculadoraLC = {
                    navController.navigate(Routes.CALCULADORA_LC)
                },
                onCrearUsuario = {
                    navController.navigate(Routes.REGISTRO)
                },
                onLogout = {
                    CoroutineScope(Dispatchers.Main).launch {
                        sessionManager.clear()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                adminUsuarios = adminUsuarios
            )
        }

        composable(Routes.CALCULADORA_LC) {
            CalculadoraLCScreen(
                onMenuPrincipal = { navController.popBackStack() }
            )
        }


        composable(Routes.CREAR_CLIENTE) {
            ClientesScreen(
                onMenuPrincipal = {
                    navController.navigate(Routes.MENU) {
                        popUpTo(Routes.MENU) { inclusive = true }
                    }
                },
                onFirmaRgpd = { idCliente ->
                    navController.navigate("${Routes.FIRMA_RGPD}/$idCliente")
                }
            )
        }


        composable(route = "${Routes.FIRMA_RGPD}/{clienteId}") { backStackEntry ->
            val clienteId =
                backStackEntry.arguments?.getString("clienteId")?.toIntOrNull() ?: 0

            FirmaRgpdScreen(
                clienteId = clienteId,
                onBack = { navController.popBackStack() },
                onFirmaOk = { id ->
                    navController.navigate("${Routes.CLIENTE_DETALLE}/$id") {
                        popUpTo(Routes.CREAR_CLIENTE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(Routes.BUSCAR_CLIENTE) {
            BuscarClienteScreen(
                vm = clientesBuscarViewModel,
                onShowListado = { navController.navigate(Routes.LISTADO_CLIENTES) },
                onMenuPrincipal = {
                    clientesBuscarViewModel.reset()
                    navController.navigate(Routes.MENU) {
                        popUpTo(Routes.MENU) { inclusive = true }
                    }
                }
            )
        }


        composable(Routes.LISTADO_CLIENTES) {
            ListadoClientesScreen(
                vm = clientesBuscarViewModel,
                onBack = {
                    clientesBuscarViewModel.reset()
                    navController.popBackStack()
                },
                onClienteClick = { cliente ->
                    navController.navigate("${Routes.CLIENTE_DETALLE}/${cliente.idCliente}")
                }
            )
        }

        composable(route = "${Routes.CLIENTE_DETALLE}/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments
                ?.getString("clienteId")
                ?.toIntOrNull() ?: 0

            ClienteDetalleScreen(
                clienteId = clienteId,
                onNuevaFirmaRgpd = { id ->
                    navController.navigate("${Routes.FIRMA_RGPD}/$id")
                },
                onNuevaRevisionGafa = { id, nombre, apellidos, codigoCliente ->
                    navController.navigate(
                        "${Routes.NUEVA_REVISION_GAFA}/$id/$nombre/$apellidos/$codigoCliente"
                    )
                },
                onMenuPrincipal = {
                    navController.navigate(Routes.MENU) {
                        popUpTo(Routes.MENU) { inclusive = true }
                    }
                },
                onNuevaRevisionLc = { id, nombre, apellidos, _ ->
                    navController.navigate("${Routes.NUEVA_REVISION_LC}/$id/$nombre/$apellidos")
                },
                onListadoRevisiones = { id, nombre, apellidos ->
                    navController.navigate("${Routes.LISTADO_REVISIONES}/$id/$nombre/$apellidos")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.NUEVA_REVISION_GAFA}/{clienteId}/{nombre}/{apellidos}/{codigoCliente}"
        ) { backStackEntry ->

            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toLongOrNull() ?: 0L
            val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()
            val apellidos = backStackEntry.arguments?.getString("apellidos").orEmpty()
            val codigoCliente = backStackEntry.arguments?.getString("codigoCliente").orEmpty()

            val vm: NuevaRevisionGafaViewModel = viewModel()

            LaunchedEffect(clienteId) {
                vm.init(clienteId, nombre, apellidos, codigoCliente)
            }

            NuevaRevisionGafaScreen(
                vm = vm,
                onVolver = { navController.popBackStack() },
            )
        }

        composable(
            route = "${Routes.NUEVA_REVISION_LC}/{clienteId}/{nombre}/{apellidos}",
            arguments = listOf(
                navArgument("clienteId") { type = NavType.LongType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("apellidos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getLong("clienteId") ?: 0L
            val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()
            val apellidos = backStackEntry.arguments?.getString("apellidos").orEmpty()

            NuevaRevisionLcScreen(
                clienteId = clienteId,
                nombre = nombre,
                apellidos = apellidos,
                onBack = { navController.popBackStack() },
                onGuardadoOk = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.LISTADO_REVISIONES}/{clienteId}/{nombre}/{apellidos}",
            arguments = listOf(
                navArgument("clienteId") { type = NavType.LongType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("apellidos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getLong("clienteId") ?: 0L
            val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()
            val apellidos = backStackEntry.arguments?.getString("apellidos").orEmpty()

            ListadoRevisionesScreen(
                clienteId = clienteId,
                nombre = nombre,
                apellidos = apellidos,
                onBack = { navController.popBackStack() },
                onIrCliente = { navController.popBackStack() },
                onAbrirPdf = { pdfPath, titulo ->
                    navController.navigate(
                        "${Routes.PDF_VIEWER}/${Uri.encode(pdfPath)}/${Uri.encode(titulo)}"
                    )
                }
            )
        }

        composable(
            route = "${Routes.PDF_VIEWER}/{pdfPath}/{titulo}"
        ) { backStackEntry ->

            val pdfPath = backStackEntry.arguments?.getString("pdfPath") ?: ""
            val titulo = backStackEntry.arguments?.getString("titulo") ?: "Informe PDF"

            PdfViewerScreen(
                pdfPath = Uri.decode(pdfPath),
                titulo = Uri.decode(titulo),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
