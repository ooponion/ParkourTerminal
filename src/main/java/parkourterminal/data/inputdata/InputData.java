package parkourterminal.data.inputdata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import parkourterminal.data.inputdata.intf.ActionPulse;
import parkourterminal.data.inputdata.intf.IndexedStack;

public class InputData {

    private TickInput operation = new TickInput(false, false, false, false, false, false, false, false);
    private IndexedStack<ActionPulse> pulseStack = new IndexedStack<ActionPulse>(20);
    private IndexedStack<ActionPulse> wadPulseStack = new IndexedStack<ActionPulse>(20);
    private String strat = "none";
    private String sidestep = "none";
    private boolean lastOnGround = false;
    private String currentStart = "unknown";
    private String currentWad = "unknown";

    public TickInput getOperation() {
        return operation;
    }

    public void UpdateOperation(EntityPlayerSP player) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        boolean forward = settings.keyBindForward.isKeyDown();
        boolean back = settings.keyBindBack.isKeyDown();
        boolean left = settings.keyBindLeft.isKeyDown();
        boolean right = settings.keyBindRight.isKeyDown();
        boolean sprint = player.isSprinting();
        boolean jump = player.movementInput.jump;
        boolean sneak = player.isSneaking();
        operation = new TickInput(left, forward, back, right, sneak, sprint, jump, lastOnGround);

        lastOnGround = player.onGround;
        if (!operation.equalsStrat(currentStart)) {
            currentStart = operation.getStrategy();
            pulseStack.push(new ActionPulse(0, operation));
        }
        if (!operation.equalsWad(currentWad)) {
            currentWad=operation.getWad();
            wadPulseStack.push(new ActionPulse(0, operation));
        }
        if (pulseStack.get(0) != null) {
            pulseStack.get(0).setDuration(pulseStack.get(0).getDuration() + 1);
        }
        if (wadPulseStack.get(0) != null) {
            wadPulseStack.get(0).setDuration(wadPulseStack.get(0).getDuration() + 1);
        }
        String matchedStrat = getMatchedStrat();
        if (matchedStrat != null) {
            strat = matchedStrat;
        }
        String matchedWad = getMatchedWad();
        if (matchedWad != null) {
            sidestep=matchedWad;
        }
    }

    public String getSidestep() {
        return sidestep;
    }

    public String getStrat() {
        return strat;
    }

    private boolean wadMatched(String... args){
        if (args.length > wadPulseStack.size()) {
            return false;
        }
        boolean allMatched = true;
        for (int i = 0; i < args.length; i++) {
            ActionPulse wadPulse = wadPulseStack.get(i);
            allMatched &= (wadPulse != null && wadPulse.getTickInput().getWad().equals(args[args.length - i - 1]));
        }
        return allMatched;
    }
    private boolean stratsMatched(String... args) {
        if (args.length > pulseStack.size()) {
            return false;
        }
        boolean allMatched = true;
        for (int i = 0; i < args.length; i++) {
            ActionPulse actionPulse = pulseStack.get(i);
            allMatched &= (actionPulse != null && actionPulse.getTickInput().getStrategy().contains(args[args.length - i - 1]));
        }
        return allMatched;
    }

    private String getMatchedStrat() {
        if (stratsMatched("burstStart", "hhStart", "Jam")) {
            ActionPulse actionPulse = pulseStack.get(2);
            if (actionPulse != null) {
                return "Burst " + actionPulse.getDuration() + "t";
            }
            return "Burst";
        } else if (stratsMatched("walkJam", "walkAir", "sprintAir")) {
            ActionPulse actionPulse = pulseStack.get(1);
            if (actionPulse != null) {
                return "Fmm " + (actionPulse.getDuration() + 1) + "t";
            }
            return "Fmm";
        } else if (stratsMatched("walkJam", "sprintAir")) {
            return "Max Fmm";
        } else if (stratsMatched("markStart", "Air")) {
            return "Max Mark";
        } else if (stratsMatched("markStart", "markMid", "Air")) {
            ActionPulse actionPulse = pulseStack.get(1);
            if (actionPulse != null) {
                return "Mark " + (1 + actionPulse.getDuration()) + "t";
            }
            return "Mark";
        } else if (stratsMatched("jumpOnly", "none", "Air") || stratsMatched("jumpOnly", "none", "markMid")) {
            ActionPulse actionPulse = pulseStack.get(1);
            if (actionPulse != null) {
                return "Pessi " + (actionPulse.getDuration() + 1) + "t";
            }
            return "Pessi";
        } else if (stratsMatched("jumpOnly", "Air") || stratsMatched("jumpOnly", "markMid")) {
            return "Max Pessi";
        } else if (stratsMatched("hhStart", "Jam")) {
            ActionPulse actionPulse = pulseStack.get(1);
            if (actionPulse != null) {
                return "HH " + actionPulse.getDuration() + "t";
            }
            return "HH";
        } else if (stratsMatched("none", "bwJam")) {
            return "BWJam";
        } else if (stratsMatched("none", "Jam")) {
            return "Jam";
        }
        return null;
    }
    private String getMatchedWad() {
        if (wadMatched("wa","J","wd")||wadMatched("wd","J","wa")
        ||wadMatched("wa","J","wa")||wadMatched("wd","J","wd")) {//wad 1t
            return "WAD";
        }

        else if (wadMatched("wa","J","w","wd")||wadMatched("wd","J","w","wa")
        ||wadMatched("wa","J","w","wa")||wadMatched("wd","J","w","wd")) {//wad >=2t
            ActionPulse actionPulse = wadPulseStack.get(1);
            return "WAD " +( actionPulse.getDuration()+1) + "t";
        }

        else if (wadMatched("wd","waJ","wa")||wadMatched("wa","wdJ","wd")) { //WAWD
            ActionPulse actionPulse = wadPulseStack.get(1);
            if (actionPulse != null&&actionPulse.getDuration()==1) {
                return "WAWD";
            }
            return null;
        }
        return null;
    }
}
