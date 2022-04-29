package net.snorps.lenientdeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static java.lang.Math.round;
import static net.minecraft.util.Mth.floor;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at=@At("HEAD"), method= "getExperienceReward", cancellable=true)
    public void getCurrentExperience(Player attacker, CallbackInfoReturnable<Integer> ci) {
            Player self = ((Player)(Object)this);
            float level = self.experienceLevel + self.experienceProgress;

            level = level * 0.7f;
            int points = 0;
            for (int i = 0; i < floor(level); i++) {
                self.experienceLevel = i;
                points += self.getXpNeededForNextLevel();
            }

            level = level - floor(level);
            points += round(self.getXpNeededForNextLevel() * level);

            ci.setReturnValue(points);
    }
}