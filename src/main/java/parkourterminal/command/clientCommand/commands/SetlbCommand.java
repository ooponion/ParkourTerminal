package parkourterminal.command.clientCommand.commands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;
import net.minecraft.world.World;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.data.landingblock.intf.LBaxis;
import parkourterminal.data.landingblock.intf.LBbox;
import parkourterminal.data.landingblock.LandingBlockData;
import parkourterminal.util.BlockUtils;
import parkourterminal.util.SendMessageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetlbCommand extends TerminalCommandBase {
    private final  String regex = "(?:\\s+([xz~]))?(?:\\s+(-?\\d+))?(?:\\s+(-?\\d+))?(?:\\s+(-?\\d+))?(?:\\s+(target))?(?:\\s+(box))?(?:\\s+(divided))?";
    private final float blockReachDistance=10f;
    @Override
    public String getCommandName() {
        return "setlb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Sets landing blocks";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
//        if (args.length == 0) {
//            sender.addChatMessage(new ChatComponentText("setlb [[target] <~|x|z]>"));
//            return;
//        }
        if(!(sender instanceof EntityPlayerSP)){
            return;
        }
        World worldIn=sender.getEntityWorld();
        EntityPlayerSP player= (EntityPlayerSP) sender.getCommandSenderEntity();
        LandingBlockData landingBlockData=GlobalData.getLandingBlock();
        StringBuilder argString = new StringBuilder();
        for (String arg : args) {
            argString.append(" ");
            argString.append(arg);
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(argString);
        if (matcher.matches()) {
            // 提取参数
            String axis = matcher.group(1); // axis
            String num1 = matcher.group(2); // 数字1
            String num2 = matcher.group(3); // 数字2
            String num3 = matcher.group(4); // 数字3
            String target = matcher.group(5); // target
            String box = matcher.group(6); // box
            String divided = matcher.group(7); // box
            //set axis
            landingBlockData.setlBaxis(LBaxis.BOTH);
            if(axis != null ){
                if(axis.equals("x")){
                    landingBlockData.setlBaxis(LBaxis.X_AXIS);
                }else if (axis.equals("z")){
                    landingBlockData.setlBaxis(LBaxis.Z_AXIS);
                }
            }
            //set box
            landingBlockData.setlBbox(LBbox.NON_BOX);
            if (box!=null){
                landingBlockData.setlBbox(LBbox.BOX);
            }

            //set target
            if(target==null&&num3==null){
                List<AxisAlignedBB> lb=new ArrayList<AxisAlignedBB>();
                if(divided!=null){
                    lb.add(BlockUtils.getBiggestAABBUnderPlayerFeet(worldIn,player));
                }else{
                    lb.addAll(BlockUtils.getAABBsUnderPlayerFeet(worldIn,player));
                }
                GlobalData.getLandingBlock().setAABBs(lb);
                if(!lb.isEmpty()){
                    GlobalData.getLandingBlock().setOffsets(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    GlobalData.getLandingBlock().setPb(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    SendMessageHelper.addChatMessage(sender,"Set land block successfully");
                }else{
                    Block blockIn =BlockUtils.getBlockOnPlayerFeet(worldIn,player);
                    SendMessageHelper.addChatMessage(sender,"Sorry, you cannot setlb on "+blockIn.getRegistryName());
                }
            }else if(target!=null){
                List<AxisAlignedBB> lb=new ArrayList<AxisAlignedBB>();
                if(divided!=null){
                    lb.add(BlockUtils.getLookingAtAABB(player,blockReachDistance,0));
                }else{
                    lb.addAll(BlockUtils.getLookingAtAABBs(player,blockReachDistance,0));
                }
                GlobalData.getLandingBlock().setAABBs(lb);
                if(!lb.isEmpty()){
                    GlobalData.getLandingBlock().setOffsets(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    GlobalData.getLandingBlock().setPb(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    SendMessageHelper.addChatMessage(sender,"Set land block successfully");
                }else{
                    SendMessageHelper.addChatMessage(sender,"Block is out of range");
                }
            }else if(num1 != null && num2 != null){
                List<AxisAlignedBB> lb=new ArrayList<AxisAlignedBB>();
                BlockPos blockpos = new BlockPos(Integer.parseInt(num1),Integer.parseInt(num2),Integer.parseInt(num3));
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                block.addCollisionBoxesToList(worldIn,blockpos,iblockstate,new AxisAlignedBB(blockpos,blockpos.add(1,1,1)),lb,null);
                GlobalData.getLandingBlock().setAABBs(lb);
                if(!lb.isEmpty()){
                    GlobalData.getLandingBlock().setOffsets(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    GlobalData.getLandingBlock().setPb(new Double[]{Double.NaN,Double.NaN,Double.NaN});
                    SendMessageHelper.addChatMessage(sender,"Set land block successfully");
                }else{
                    SendMessageHelper.addChatMessage(sender,"Sorry, that block doesn't have collision bounding");
                }
            }else{
                SendMessageHelper.addChatMessage(sender,"Something went wrong, failed to setlb");
            }
        }else{
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }




//        for (TerminalCommandBase commandBase:commandBases){
//            if (commandBase.getCommandName().equalsIgnoreCase(args[0])){
//                sender.addChatMessage(new ChatComponentText(args[0]));
//                String[] newArray = new String[args.length - 1];
//                System.arraycopy(args, 1, newArray, 0, args.length - 1);
//                commandBase.processCommand(sender,newArray);
//                return;
//            }
//        }
        //sender.addChatMessage(new ChatComponentText(GlobalConfig.getLabelColor() +"set "));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length==0){
            return null;
        }
        String[] newArray = new String[args.length - 1];
        System.arraycopy(args, 0, newArray, 0, args.length - 1);
        List<String> suggestions=getSuggestions(newArray,new BlockPos(sender.getPositionVector()));
        return CommandBase.getListOfStringsMatchingLastWord(args,suggestions);
    }
    private List<String> getSuggestions(String[] args,BlockPos pos) {
        StringBuilder input= new StringBuilder();
        for (String arg : args) {
            input.append(" ");
            input.append(arg);
        }
        List<String> suggestions = new ArrayList<String>();
        int x = pos.getX();
        int y= pos.getY()-1;
        int z= pos.getZ();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.toString());
        if (matcher.matches()) {
            if (matcher.matches()) {
                boolean hasXZ = matcher.group(1) != null;
                boolean hasNum1 = matcher.group(2) != null;
                boolean hasNum2 = matcher.group(3) != null;
                boolean hasNum3 = matcher.group(4) != null;
                boolean hasTarget = matcher.group(5) != null;
                boolean hasBox = matcher.group(6) != null;
                boolean divided = matcher.group(7) != null;

                if (!hasTarget&&!hasBox&&!hasXZ&&!hasNum1&&!hasNum2&&!hasNum3) {
                    suggestions.addAll(Arrays.asList("x", "z","~"));
                    suggestions.add(String.valueOf(x));
                    suggestions.add("target");
                    suggestions.add("box");
                }

                // setlb x | setlb z
                if (hasXZ && !hasNum1&&!hasTarget) {

                    suggestions.add(String.valueOf(x));
                    suggestions.add("target");
                    suggestions.add("box");
                }

                // setlb x 123 |setlb 123
                if (hasNum1 && !hasNum2) {
                    suggestions.add(String.valueOf(y));
                }

                //  setlb x 123 456 |setlb 123 456
                if ( hasNum1 && hasNum2 && !hasNum3) {
                    suggestions.add(String.valueOf(z));
                }

                // x 123 456 789| setlb 123 456 789
                if (hasNum1 && hasNum2 && hasNum3) {
                    suggestions.add("box");
                }

                // setlb x| target
                if (hasTarget && !hasBox) {
                    suggestions.add("box");
                }

                // setlb x|null target box | setlb x|null 123 456 789 box
                if (hasBox) {
                    suggestions.clear();
                }
                if(!divided&&!hasNum1){
                    suggestions.add("divided");
                }
            }
        }


        return suggestions;
    }
}
