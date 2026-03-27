package karldrabek.goobaapp.goobaapp

import karldrabek.goobaapp.goobaapp.backend.networkModule
import org.koin.core.context.startKoin

/**
 * WARNING: Do NOT call this from a composable. it should be called
 * exactly once on each platform before context is set and the app is launched.
 *
 * Starts Koin.
 */
fun initKoin() {
    startKoin {
        modules(networkModule)
    }
}
