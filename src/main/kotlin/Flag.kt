
/**
 * This class defines every existing flag for server/client communication commands
 * @param value The flag value, containing the data signaling the message in an 8 bit representation
 */
enum class Flag(val value: Int) {

    READY(0x01),
    SHOT(0x02),
    GAME_OVER(0x03),
    RESULT(0x04),



}