package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.Command
import net.dv8tion.jda.api.Permission
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.reflect.ConstructorUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import ru.chsergeig.bot.viselitsa.commands.AbortGame
import ru.chsergeig.bot.viselitsa.commands.GetStatus
import ru.chsergeig.bot.viselitsa.commands.Goos
import ru.chsergeig.bot.viselitsa.commands.RandomWord
import ru.chsergeig.bot.viselitsa.commands.SecretRandomWord
import ru.chsergeig.bot.viselitsa.commands.SetRandomWordProvider
import ru.chsergeig.bot.viselitsa.commands.ShutDown
import ru.chsergeig.bot.viselitsa.commands.StartGame
import ru.chsergeig.bot.viselitsa.commands.SuggestChar
import ru.chsergeig.bot.viselitsa.commands.WaitTime
import java.util.stream.Stream

internal class CommandsTest {

    @ParameterizedTest
    @ArgumentsSource(CommandProvider::class)
    fun constructCommand(
            clazz: Class<*>,
            name: String,
            aliases: List<String>,
            help: String,
            permissions: Array<Permission>,
            guildOnly: Boolean,
            hidden: Boolean) {

        val commandInstance: Command = ConstructorUtils.invokeConstructor(clazz, emptyArray()) as Command

        Assertions.assertTrue(StringUtils.equals(commandInstance.name, name))
        for (alias in aliases) {
            Assertions.assertTrue(commandInstance.aliases.contains(alias))
        }
        Assertions.assertTrue(StringUtils.equals(commandInstance.help, help))
        for (permission in permissions) {
            Assertions.assertTrue(commandInstance.botPermissions.contains(permission))
        }
        Assertions.assertFalse(commandInstance.isGuildOnly.xor(guildOnly))
        Assertions.assertFalse(commandInstance.isHidden.xor(hidden))
    }

    class CommandProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            // command class, name, aliases, help, permissions, guildOnly, hidden
            return Stream.of(
                    Arguments.of(
                            AbortGame::class.java,
                            "abort",
                            ArrayList<String>(),
                            "Закончить игру",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            GetStatus::class.java,
                            "status",
                            ArrayList<String>(),
                            "Проверить статус игры",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            Goos::class.java,
                            "goose",
                            arrayListOf("бунд"),
                            "Запустить гуся",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            RandomWord::class.java,
                            "random",
                            ArrayList<String>(),
                            "Использовать случайное слово",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            SecretRandomWord::class.java,
                            "secret",
                            ArrayList<String>(),
                            "Использовать случайное слово, неизвестное автору",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            true,
                            false),
                    Arguments.of(
                            SetRandomWordProvider::class.java,
                            "provider",
                            ArrayList<String>(),
                            "Указать провайдер слов. Варианты: '${RandomWordProvider.Provider.values().contentToString()}'",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            ShutDown::class.java,
                            "shutdown",
                            ArrayList<String>(),
                            "Остановить бота",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            StartGame::class.java,
                            "start",
                            ArrayList<String>(),
                            "Начать игру",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false),
                    Arguments.of(
                            SuggestChar::class.java,
                            "c",
                            arrayListOf("с"),
                            "Предложить букву",
                            arrayOf(Permission.MESSAGE_WRITE),
                            true,
                            false),
                    Arguments.of(
                            WaitTime::class.java,
                            "wait",
                            ArrayList<String>(),
                            "Установить ожидание игроков",
                            arrayOf(Permission.MESSAGE_EMBED_LINKS),
                            false,
                            false)

            )
        }
    }

}
