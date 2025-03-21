import java.net.ServerSocket
import java.net.Socket
import java.util.Locale

object Sockets {


    /**
     * The server port used to host the server on
     */
    public const val SERVER_PORT = 12000

    /**
     * The buffer size to standardise the message sizes used between the client and server
     */
    public const val BUFFER_SIZE = 8192;

    /**
     * The server socket, hosting the actual server
     */
    public var server: ServerSocket? = null;

    /**
     * The socket used to communicate both from the server to the client and vice versa, based on the program
     * state. Thus, the "communication pipe". This pipe changes direction dynamically.
     */
    public var pipeSocket: Socket? = null;


    /**
     * Creates a SocketServer if it doesn't already exist
     */
    fun create_server() {
        server = if (server == null || server!!.isClosed) ServerSocket(SERVER_PORT) else server
    }

    /**
     * Waits for a message from the client side.
     * Once arrived, defines the pipeSocket to be in the client -> server direction.
     */
    fun wait_client() {
        pipeSocket = server?.accept()
    }

    /**
     * Connects a client to a server given its address, used on the client's part, changing the
     * pipe's direction to server -> client.
     *
     * @param serverAddr The server's address (hostname)
     */
    fun connect_server(serverAddr: String) {
        pipeSocket = Socket(serverAddr, SERVER_PORT)
    }

    /**
     * Builds a message to be sent between the server and client with the following
     * protocol:
     *
     * MESSAGE FORMAT: «FLAG»\u001F«PARAMETER»,«PARAMETER»,«PARAMETER» (...)
     *
     * FLAG: The flag defining the current message
     *
     * PARAM: The parameter(s) with more data added to the message.
     *
     * @param flag The flag defining the message type
     * @param data An array of strings to send through
     *
     * @return A string with the formatted message
     */
    fun build_message(flag: Flag, vararg data: Any): String =
        flag.value.toString() + "\\u001F" + data.joinToString(",")

    /**
     * Sends a message to either the server or client based on the direction on the pipe
     * @param msg The message to be sent to the endpoint
     */
    fun send_msg(msg: String) {
        val messageBytes = msg.uppercase(Locale.getDefault()).toByteArray()
        pipeSocket?.outputStream?.write(messageBytes)
    }

    /**
     * Receives a message from either the server or client based on the pipe direction
     */
    fun recv_msg(): String {

        val buffer = ByteArray(BUFFER_SIZE)

        // Load the message into the buffer and return the bytecount
        val bytecount: Int = pipeSocket?.inputStream?.read(buffer)!!
        return String(buffer.sliceArray(0..bytecount - 1))  // Return only the message received
    }


    /**
     * Sends a READY command.
     */
    fun send_ready() = send_msg(build_message(Flag.READY))

    /**
     * Waits for a READY message.
     */
    fun wait_ready() {

    }

    /**
     * Sends a SHOT command.
     * @param x The X coordinate of the shot.
     * @param y The Y coordinate of the shot.
     */
    fun send_shot(x: Int, y: Int) = send_msg(build_message(Flag.SHOT, x, y))

    fun wait_shot(): Pair<Int, Int> {

        TODO("Not yet implemented")
    }

    /**
     * Sends a GAMEOVER message
     * @param status Whether the game has ended or not.
     */
    fun send_gameover(status: Boolean) = send_msg(build_message(Flag.GAME_OVER, status))

    fun wait_gameover(): Boolean {

        TODO("Not yet implemented")
    }

    /**
     * Sends a RESULT command
     * @param res The result of a fired shot
     */
    fun send_result(res: Char) = send_msg(build_message(Flag.RESULT, res))

    fun wait_result(): Char {

        TODO("Not yet implemented")
    }
}
