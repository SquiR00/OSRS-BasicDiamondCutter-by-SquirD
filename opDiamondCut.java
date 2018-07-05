package Scriptz;


import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

@Script.Manifest(name = "Diamond Cutter", description = "Cuts diamonds to diamond bolt tips w/ banking", properties = "author=SquirD; topic=666; cleint=4;")

public class opDiamondCut extends PollingScript<ClientContext> {

    //ID of diamond
    final static int ID_DIA = 1601;
    //ID of chisel
    final static int ID_CHISEL = 1755;

    public boolean canCut() {

        return (ctx.inventory.select().count() > 27);
    }
    public boolean bank() {
        //boolean returns true if animation is not idle and inventory is less than 3
        return (ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 3);
    }
    public void withdraw() {
        //if bank is open, else open it
        if (ctx.bank.opened()) {
            Condition.sleep(350);
            //withdraws 26 diamond from bank
            ctx.bank.withdraw(ID_DIA, 26);
            Condition.sleep(300);
            ctx.bank.close();
            Condition.sleep(600);
        } else if (ctx.bank.open()) {
                //if bank no open, open and wait until bank opens, checking 10 time every 250ms
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ctx.bank.opened());
                    }
                }, 250, 10);
        }

    }
    public void Cut() {
        Item Diamond = ctx.inventory.select().id(ID_DIA).poll();
        Item Knife = ctx.inventory.select().id(ID_CHISEL).poll();

        Diamond.interact("Use");
        Condition.sleep(200);
        Knife.interact("Use");
        Condition.sleep(1000);
        ctx.input.send("{SPACE down}");
        Condition.sleep(50);
        ctx.input.send("{SPACE up}");
        Condition.sleep(5000);
    };

    @Override
    public void poll() {
        if(bank()){
            withdraw();
        } else if(canCut()) {
            Cut();
        }
    }
}
