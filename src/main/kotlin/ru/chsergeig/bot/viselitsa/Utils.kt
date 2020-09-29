package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.CommandEvent

class Utils private constructor() {

    companion object {

        val url = "https://github.com/ChSergeiG/Viselitsa"

        @JvmStatic
        fun checkSingleArgAndGet(event: CommandEvent, ifNotSingleArg: String?, ifEmptyArg: String?): String {
            val args = event.args
            if (args.isNullOrEmpty()) {
                event.replyError(ifEmptyArg)
                throw RuntimeException(ifEmptyArg)
            }
            val strings = args.trim().split("\\s+".toRegex()).toTypedArray()
            if (strings[0].isEmpty()) {
                event.replyError(ifEmptyArg)
                throw RuntimeException(ifEmptyArg)
            }
            if (strings.size != 1) {
                event.replyError(ifNotSingleArg)
                throw RuntimeException(ifNotSingleArg)
            }
            return strings[0]
        }

        @JvmStatic
        fun checkSingleOrDoubleArgAndGet(event: CommandEvent, ifMoreTwoArg: String?, ifEmptyArg: String?): Array<String> {
            val args = event.args
            if (args.isNullOrEmpty()) {
                event.replyError(ifEmptyArg)
                throw RuntimeException(ifEmptyArg)
            }
            val strings = args.trim().split("\\s+".toRegex()).stream()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .toArray<String> { l -> arrayOfNulls(l) }
            when (strings.size) {
                0 -> {
                    event.replyError(ifEmptyArg)
                    throw RuntimeException(ifEmptyArg)
                }
                1 -> {
                    return arrayOf(strings[0], "")
                }
                2 -> {
                    return arrayOf(strings[0], strings[1])
                }
                else -> {
                    event.replyError(ifMoreTwoArg)
                    throw RuntimeException(ifMoreTwoArg)
                }
            }
        }

        @JvmStatic
        fun safeGetValueFromEvOrCli(args: Array<String>, prefix: String, valueDef: String, envVar: String?): String {
            val valueFromEnv: String? = System.getenv()[envVar]
            if (!valueFromEnv.isNullOrEmpty()) {
                return valueFromEnv
            }
            val value: String = try {
                args.first { arg: String -> arg.startsWith(prefix) }
            } catch (e: Exception) {
                throw RuntimeException("No CLI value with prefix $prefix", e)
            }
            if (value.isNotEmpty() && value.contains("=")) {
                return value.split("=")[1]
            } else {
                throw RuntimeException("$valueDef is empty or not valid")
            }
        }

        @JvmStatic
        fun purifyWord(word: String, dictionary: Map<String, Boolean>): String {
            return word.splitToSequence("").filter { charr: String ->
                dictionary.entries.stream()
                        .filter { entry: Map.Entry<String, Boolean> -> entry.key.contains(charr) }
                        .findFirst()
                        .isPresent
            }.joinToString(separator = "", truncated = "")
        }
    }

}
