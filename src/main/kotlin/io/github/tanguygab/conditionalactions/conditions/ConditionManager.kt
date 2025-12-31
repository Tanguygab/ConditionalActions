package io.github.tanguygab.conditionalactions.conditions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import io.github.tanguygab.conditionalactions.conditions.types.ConditionType
import io.github.tanguygab.conditionalactions.conditions.types.GroupCondition
import io.github.tanguygab.conditionalactions.conditions.types.NumericCondition
import io.github.tanguygab.conditionalactions.conditions.types.StringCondition
import me.neznamy.tab.api.event.plugin.TabLoadEvent
import me.neznamy.tab.shared.TAB
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class ConditionManager(plugin: ConditionalActions) {
    private val conditions = mutableMapOf<String, ConditionGroup>()
    private val conditionalOutputs = mutableMapOf<String, ConditionalOutput>()

    private val types = mutableMapOf<String, (String) -> ConditionType>().apply {
        put("<-") { StringCondition(it.split(" *<- *".toRegex(), limit = 2)) { left: String, right: String -> left.contains(right) } }
        put("-|") { StringCondition(it.split(" *-\\| *".toRegex(), limit = 2)) { left: String, right: String -> left.endsWith(right) } }
        put("|-") { StringCondition(it.split(" *\\|- *".toRegex(), limit = 2)) { left: String, right: String -> left.startsWith(right) } }

        put(">=") { NumericCondition(it.split(" *>= *".toRegex(), limit = 2)) { left: Double, right: Double -> left >= right } }
        put(">") { NumericCondition(it.split(" *> *".toRegex(), limit = 2)) { left: Double, right: Double -> left > right } }
        put("<=") { NumericCondition(it.split(" *<= *".toRegex(), limit = 2)) { left: Double, right: Double -> left <= right } }
        put("<") { NumericCondition(it.split(" *> *".toRegex(), limit = 2)) { left: Double, right: Double -> left < right } }

        put("!==") { StringCondition(it.split(" *!== *".toRegex(), limit = 2)) { left: String, right: String -> left != right } }
        put("!=") { StringCondition(it.split(" *!= *".toRegex(), limit = 2)) { left: String, right: String -> !left.equals(right, ignoreCase = true) } }

        put("==") { StringCondition(it.split(" *== *".toRegex(), limit = 2)) { left: String, right: String -> left == right } }
        put("=") { StringCondition(it.split(" *= *".toRegex(), limit = 2)) { left: String, right: String -> left.equals(right, ignoreCase = true) } }


        put("permission:") { object : ConditionType(it.split("permission: *".toRegex(), limit = 2)) {
                override fun isMet(player: OfflinePlayer?): Boolean {
                    val permission = rightSide
                    return player is Player && player.hasPermission(permission)
                }
            }
        }
    }

    init {
        Utils.updateFiles(plugin, "conditions.yml", "conditions/default-conditions.yml")

        Utils.loadFiles(plugin.dataFolder.resolve("conditions"), "") { file, _ ->
            YamlConfiguration.loadConfiguration(file).getValues(false).forEach { (key, value) ->
                if (value !is ConfigurationSection) return@forEach

                if (key.equals("conditions", ignoreCase = true)) {
                    value.getValues(false).forEach { (name: String, cfg: Any) ->
                        val builder = StringBuilder()
                        if (cfg is MutableList<*>) {
                            cfg.forEach { obj ->
                                @Suppress("UNCHECKED_CAST")
                                builder.append(if (obj is MutableList<*>) (obj as MutableList<String>).joinToString("&&") else obj)
                                builder.append("||")
                            }
                        } else builder.append(cfg)
                        if (builder.length >= 2 && builder.substring(builder.length - 2) == "||")
                            builder.delete(builder.length - 2, builder.length)
                        conditions[name] = ConditionGroup(this, builder.toString(), name)
                    }
                    return@forEach
                }
                if (key.equals("conditional-outputs", ignoreCase = true)) {
                    value.getValues(false).forEach { (name: String, cfg: Any) ->
                        if (cfg is ConfigurationSection) conditionalOutputs[name] = ConditionalOutput(this, cfg)
                    }
                }
            }
        }

        if (plugin.server.pluginManager.isPluginEnabled("TAB")) {
            TAB.getInstance().eventBus?.register(TabLoadEvent::class.java) {
                conditions.values.forEach { it.registerTABPlaceholder() }
            }
        }
    }

    fun getCondition(condition: String) = conditions.computeIfAbsent(condition) { ConditionGroup(this, condition) }

    fun getConditionalOutput(name: String) = conditionalOutputs[name]

    fun find(condition: String): ConditionType? {
        val inverted = condition.startsWith("!")
        val condition0 = condition.substring(if (inverted) 1 else 0)
        if (condition0 in conditions) return GroupCondition(conditions[condition0]!!, condition)

        for (separator in types.keys) if (condition.contains(separator)) return types[separator]!!(condition)
        return null
    }

    fun getConditions() = conditions.keys
}
