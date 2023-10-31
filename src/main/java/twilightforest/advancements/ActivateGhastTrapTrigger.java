package twilightforest.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;

import java.util.Optional;

public class ActivateGhastTrapTrigger extends SimpleCriterionTrigger<ActivateGhastTrapTrigger.Instance> {

    public static final ResourceLocation ID = TwilightForestMod.prefix("activate_ghast_trap");

    @Override
    protected ActivateGhastTrapTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext ctx) {
        return new ActivateGhastTrapTrigger.Instance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }


    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(Optional<ContextAwarePredicate> player) {
            super(player);
        }

        public static Instance activateTrap() {
            return new Instance(Optional.empty());
        }
    }
}
