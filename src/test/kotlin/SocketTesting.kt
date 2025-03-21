import kotlin.test.Test

class SocketTesting {

    @Test
    fun SocketAddressTest() {
        println(Sockets.server.localSocketAddress.toString())
    }

}