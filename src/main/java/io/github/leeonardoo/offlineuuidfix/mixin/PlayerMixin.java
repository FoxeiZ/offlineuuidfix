package io.github.leeonardoo.offlineuuidfix.mixin;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.leeonardoo.offlineuuidfix.PlayerLocator;
import net.minecraft.world.entity.player.Player;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin {

    private static final Logger LOGGER = LogManager.getLogger("OfflineUUIDFix");

    /**
     * Intercepts the createPlayerUUID method to use online UUIDs when
     * available. Falls back to the original offline UUID if the online UUID
     * cannot be retrieved.
     *
     * @param pUsername the player username to generate a UUID for
     * @param cir callback info returnable for the UUID
     */
    @Inject(method = "createPlayerUUID(Ljava/lang/String;)Ljava/util/UUID;", at = @At("HEAD"), cancellable = true, remap = false)
    @SuppressWarnings("unused")
    private static void onCreatePlayerUUID(String pUsername, CallbackInfoReturnable<UUID> cir) {
        UUID onlineUUID = PlayerLocator.getOnlineUUID(pUsername);

        if (onlineUUID != null) {
            LOGGER.info("The player " + pUsername + " will now use the online UUID: " + onlineUUID);
            cir.setReturnValue(onlineUUID);
        }
    }
}
