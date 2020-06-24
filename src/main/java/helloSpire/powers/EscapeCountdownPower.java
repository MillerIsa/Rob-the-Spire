package helloSpire.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import helloSpire.DefaultMod;

public class EscapeCountdownPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID("EscapeCountdownPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public EscapeCountdownPower(AbstractCreature owner, int turns) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.updateDescription();
        this.loadRegion("fading");
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount;
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 1 && !this.owner.isDying) {
            if(isPlayer){
                escape();
            } else{
                throw new IllegalArgumentException("Escape Countdown buff can only be applied to a player.");
            }
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            this.updateDescription();
            System.out.println("Reducing Escaping Countdown by 1.");
        }

    }

    private void escape() {
        AbstractCreature target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.getCurrRoom().smoked = true;
            this.addToBot(new VFXAction(new SmokeBombEffect(target.hb.cX, target.hb.cY)));
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = 2.5F;
        }

    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}