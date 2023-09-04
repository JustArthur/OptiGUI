package opekope2.optigui.internal.selector

import net.minecraft.entity.passive.HorseColor
import net.minecraft.entity.passive.HorseMarking
import net.minecraft.util.DyeColor
import opekope2.optigui.annotation.Selector
import opekope2.optigui.api.interaction.Interaction
import opekope2.optigui.api.selector.ISelector
import opekope2.optigui.filter.*
import opekope2.optigui.properties.IDonkeyProperties
import opekope2.optigui.properties.IHorseProperties
import opekope2.optigui.properties.ILlamaProperties
import opekope2.util.*


@Selector("donkey.has_chest")
object DonkeyChestSelector : ISelector {
    override fun createFilter(selector: String): IFilter<Interaction, *> =
        PreProcessorFilter.nullGuarded(
            { (it.data as? IDonkeyProperties)?.hasChest },
            IFilter.Result.mismatch(),
            EqualityFilter(selector.toBooleanStrict())
        )
}

@Selector("horse.variants")
object HorseVariantSelector : ISelector {
    override fun createFilter(selector: String): IFilter<Interaction, *>? =
        selector.splitIgnoreEmpty(*delimiters)
            ?.assertNotEmpty()
            ?.map({ color -> color.takeIf { HorseColor.values().any { it.name.lowercase() == color } } }) {
                throw RuntimeException("Invalid horse variants: ${joinNotFound(it)}")
            }
            ?.assertNotEmpty()
            ?.let { colors ->
                PreProcessorFilter.nullGuarded(
                    { (it.data as? IHorseProperties)?.variant },
                    IFilter.Result.mismatch(),
                    ContainingFilter(colors)
                )
            }
}

@Selector("horse.markings")
object HorseMarkingSelector : ISelector {
    override fun createFilter(selector: String): IFilter<Interaction, *>? =
        selector.splitIgnoreEmpty(*delimiters)
            ?.assertNotEmpty()
            ?.map({ marking -> marking.takeIf { HorseMarking.values().any { it.name.lowercase() == marking } } }) {
                throw RuntimeException("Invalid horse markings: ${joinNotFound(it)}")
            }
            ?.assertNotEmpty()
            ?.let { markings ->
                PreProcessorFilter.nullGuarded(
                    { (it.data as? IHorseProperties)?.marking },
                    IFilter.Result.mismatch(),
                    ContainingFilter(markings)
                )
            }
}

@Selector("llama.colors")
object LlamaColorSelector : ISelector {
    override fun createFilter(selector: String): IFilter<Interaction, *>? =
        selector.splitIgnoreEmpty(*delimiters)
            ?.assertNotEmpty()
            ?.map({ color -> color.takeIf { DyeColor.byName(color, null) != null } }) {
                throw RuntimeException("Invalid llama colors: ${joinNotFound(it)}")
            }
            ?.assertNotEmpty()
            ?.let { variants ->
                PreProcessorFilter.nullGuarded(
                    { (it.data as? ILlamaProperties)?.carpetColor },
                    IFilter.Result.mismatch(), // No carpet is mismatch, because at this point, a carpet is required
                    ContainingFilter(variants)
                )
            }
}

typealias LlamaVariant = net.minecraft.entity.passive.LlamaEntity.Variant

@Selector("llama.variants")
object LlamaVariantSelector : ISelector {
    override fun createFilter(selector: String): IFilter<Interaction, *>? =
        selector.splitIgnoreEmpty(*delimiters)
            ?.assertNotEmpty()
            ?.map({ variant -> variant.takeIf { LlamaVariant.values().any { it.name.lowercase() == variant } } }) {
                throw RuntimeException("Invalid llama variants: ${joinNotFound(it)}")
            }
            ?.assertNotEmpty()
            ?.let { colors ->
                PreProcessorFilter.nullGuarded(
                    { (it.data as? ILlamaProperties)?.variant },
                    IFilter.Result.mismatch(),
                    ContainingFilter(colors)
                )
            }
}
