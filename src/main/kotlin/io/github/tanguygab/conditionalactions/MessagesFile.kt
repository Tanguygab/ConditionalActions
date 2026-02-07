package io.github.tanguygab.conditionalactions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class MessagesFile(file: File, val mm: MiniMessage) {

    private val file = YamlConfiguration.loadConfiguration(file)

    fun get(path: String, default: String, replace: Map<String, String> = emptyMap(), command: String = ""): Component {
        var component = file.getRichMessage(path) ?: mm.deserialize(default)
        for ((key, value) in replace) {
            component = component.replaceText { it
                .matchLiteral(key)
                .replacement(value)
            }
        }
        if (command != "") component = component.clickEvent(ClickEvent.runCommand(command)).insertion(command)
        return component
    }

    val commandsReloadSuccess = get("commands.reload.success", "<green>Plugin reloaded!</green>")

    fun commandsGroupListHeader(total: Int) = get(
        "commands.group.list.header",
        "Groups (<total>):",
        mapOf("<total>" to total.toString())
    )
    fun commandsGroupListLine(name: String) = get(
        "commands.group.list.line",
        "\n<gray> - <white><name>",
        mapOf("<name>" to name),
        "/conditionalactions group execute @s $name"
    )

    fun commandsConditionListHeader(total: Int) = get(
        "commands.condition.list.header",
        "Conditions (<total>):",
        mapOf("<total>" to total.toString())
    )
    fun commandsConditionListLine(name: String) = get(
        "commands.condition.list.line",
        "\n<gray> - <white><name>",
        mapOf("<name>" to name),
        "/conditionalactions condition check @s $name"
    )

    fun commandsConditionCheckSuccess(player: String) = get(
        "commands.condition.check.success",
        "<green><player> meets the condition!",
        mapOf("<player>" to player)
    )
    fun commandsConditionCheckFail(player: String) = get(
        "commands.condition.check.fail",
        "<red><player>does not meet the condition!",
        mapOf("<player>" to player)
    )
}