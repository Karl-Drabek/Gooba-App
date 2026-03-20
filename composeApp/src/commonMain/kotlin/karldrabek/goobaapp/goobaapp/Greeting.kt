package karldrabek.goobaapp.goobaapp

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Gooba, ${platform.name}!"
    }
}