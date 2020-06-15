package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent

class Utils private constructor() {

    companion object {

        val url = "https://github.com/ChSergeiG/Viselitsa"

        @JvmStatic
        fun checkSingleArgAndGet(event: CommandEvent, ifNotSingleArg: String?, ifEmptyArg: String?): String {
            val args = event.args
            val strings = args.split("\\s+".toRegex()).toTypedArray()
            if (strings.size != 1) {
                event.replyError(ifNotSingleArg)
                throw RuntimeException(ifNotSingleArg)
            }
            if (strings[0].isEmpty()) {
                event.replyError(ifEmptyArg)
                throw RuntimeException(ifEmptyArg)
            }
            return strings[0]
        }

        @JvmStatic
        fun safeGetValueFromEvOrCli(args: Array<String>, prefix: String, valueDef: String, envVar: String?): String {
            val valueFromEnv: String? = System.getenv()[envVar]
            if (!valueFromEnv.isNullOrEmpty()) {
                return valueFromEnv
            }
            val value: String = args.first { arg: String -> arg.startsWith(prefix) }
            if (value.isNotEmpty() && value.contains("=")) {
                return value.split("=")[1]
            } else {
                throw RuntimeException("$valueDef is empty or not valid")
            }
        }

        @JvmStatic
        fun purifyWord(word: String, dictionary: MutableMap<String, Boolean>): String {
            return word.splitToSequence("").filter { charr: String ->
                dictionary.entries.stream()
                        .filter { entry: Map.Entry<String, Boolean> -> entry.key.contains(charr) }
                        .findFirst()
                        .isPresent
            }.joinToString(separator = "", truncated = "")
        }
    }

}
