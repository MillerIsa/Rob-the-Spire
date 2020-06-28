package helloSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import helloSpire.DefaultMod;

public class CappedGoldStealPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(CappedGoldStealPower.class.getSimpleName());
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private final int goldCap;

    public CappedGoldStealPower(AbstractCreature owner, int amount) {

        type = AbstractPower.PowerType.DEBUFF;

        AbstractMonster m = (AbstractMonster) owner;
        if (m.hasPower(MinionPower.POWER_ID) || m.hasPower(RegrowPower.POWER_ID)) {
            goldCap = 0;
        } else if (m.type == AbstractMonster.EnemyType.BOSS) {
            goldCap = 50;
        } else if (m.type == AbstractMonster.EnemyType.ELITE) {
            goldCap = 25;
        } else {
            goldCap = 10;
        }

        this.amount = Math.min(goldCap, amount);

        updateDescription();

        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("fading"); //TODO Take the power Icon from "The Animator"
    }

    @Override
    public void updateDescription() {
        String[] desc = powerStrings.DESCRIPTIONS;
        this.description = desc[0] + this.amount + desc[1] + desc[2] + (goldCap - this.amount) + desc[3];
        //this.description = desc[0] + this.amount + desc[1] + (goldCap - this.amount) + desc[2];
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();

        int goldGain = Math.min(goldCap, amount);
        if (goldGain > 0) {
            //GameActions.Top.GainGold(goldGain);
            System.out.println("Should gain: " + goldGain + " gold now.");
            gainGold(goldGain);
        }
    }

    private void gainGold(int goldGain){
        gainGold( AbstractDungeon.player,this.owner, goldGain);
    }

    private void gainGold(AbstractCreature target, AbstractCreature source, int goldGain){
        AbstractDungeon.player.gainGold(goldGain);
        for (int i = 0; i < goldGain; ++i) {
            AbstractDungeon.effectList.add(new GainPennyEffect(target, source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, true));
        }//TODO: refactor this gold gain to a method in a utility class, also refactor to generalize source and target
    }

    @Override
    public void stackPower(int stackAmount) {
        int initialGold = amount;

        super.stackPower(stackAmount);

        if (this.amount > goldCap) {
            this.amount = goldCap;
        }

        int goldGain = this.amount - initialGold;
        if (goldGain > 0) {
            //GameActions.Top.GainGold(goldGain);
            System.out.println("Should gain: " + goldGain + " gold now.");

            gainGold(goldGain);

        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

}