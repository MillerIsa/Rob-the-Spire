package robTheSpire.cards;


import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.DefaultMod;
import robTheSpire.actions.StealCardOfTypeAction;
import robTheSpire.characters.TheDefault;

import static robTheSpire.DefaultMod.makeCardPath;

public class StealASkill extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(StealASkill.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealASkill() {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF);
        //ExhaustiveVariable.setBaseValue(this,2);
        //this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StealCardOfTypeAction(CardType.SKILL));
    }

    public void upgrade() {
        if (!this.upgraded) {
            //this.magicNumber = this.baseMagicNumber = 2;
            this.exhaust = false;
            ExhaustiveVariable.setBaseValue(this, 2);
            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StealASkill();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    //
}