package ru.chsergeig.bot.viselitsa

import com.jagrosh.jdautilities.command.Command
import net.dv8tion.jda.api.Permission
import org.junit.jupiter.api.Assertions

class UtilsForTests private constructor() {

    companion object {

        @JvmStatic
        fun testCommand(
                command: Command,
                name: String,
                aliases: List<String>,
                help: String,
                permissions: Array<Permission>,
                permissionsSize: Int,
                isGuildOnly: Boolean,
                isHidden: Boolean) {

            Assertions.assertEquals(name, command.name)
            aliases.forEach { Assertions.assertTrue(command.aliases.contains(it)) }
            Assertions.assertEquals(help, command.help)
            permissions.forEach { Assertions.assertTrue(command.botPermissions.contains(it)) }
            Assertions.assertEquals(permissionsSize, command.botPermissions.size)
            Assertions.assertFalse(isGuildOnly.xor(command.isGuildOnly))
            Assertions.assertFalse(isHidden.xor(command.isHidden))
        }

    }

}
