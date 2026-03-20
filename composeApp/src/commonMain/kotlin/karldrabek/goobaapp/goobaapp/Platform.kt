package karldrabek.goobaapp.goobaapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform