package parkourterminal.util.AnimationUtils.intf;

import parkourterminal.gui.component.scrollBar.intf.AnimationMode;
import parkourterminal.util.AnimationUtils.FloatPoint;

import java.awt.*;

public abstract class AbstractAnimation {
    private final AnimationMode animationMode;
    private float startX, startY, endX, endY;
    private float totalTime; // 运动时间 (秒)
    private float newX,newY;
    private float elapsedTime = 0; // 已经过的时间
    public AbstractAnimation(float totalTime,FloatPoint startPoint,AnimationMode animationMode){
        this.animationMode=animationMode;
        this.totalTime=totalTime;
        this.startX=startPoint.getX();
        this.startY=startPoint.getY();
        this.endX=this.startX;
        this.endY=this.startY;
        this.newX=this.startX;
        this.newY=this.startY;
    }
    public void SetAnimationTime(float totalTime){
        this.totalTime=totalTime;
    }
    public void RestartAnimation(FloatPoint endPoint){
        if(animationMode==AnimationMode.FORCED&&InAnimation()){
            return;
        }
        if(animationMode==AnimationMode.BLENDED){
            this.startX=this.newX;
            this.startY=this.newY;
        }
        this.endX=endPoint.getX();
        this.endY=endPoint.getY();

        this.elapsedTime=0;
    }
//    private float Integration(){
//        float lastProgress=0f;
//        for(int i=0;i<totalTime/0.05f;i++){ //i=elapsedTime/0.05
//            float t = 0.05f*i / totalTime; // 归一化时间 (0 ~ 1)
//
//            // 计算速度因子
//            float speedFactor = this.speedFunction(t);
//
//            // 计算当前 progress（累积速度积分）
//             lastProgress +=(speedFactor * 0.05f/totalTime);
//
//        }
//        return lastProgress;
//    }
//    public FloatPoint Update(){
//        if (elapsedTime < totalTime) {
//            float t = elapsedTime / totalTime; // 归一化时间 (0 ~ 1)
//
//            // 计算速度因子（可调整控制点，例如 0.5 表示先慢后快）
//            float speedFactor = this.speedFunction(t)/Integration();
//
//            // 计算当前 progress（累积速度积分）
//            float progress = lastProgress + (speedFactor * 0.05f/totalTime); // 0.05 控制平滑度
//            lastProgress = progress;
//
//            // 确保最终 progress 恰好到 1
//            if (progress > 1) progress = 1;
//
//            // 计算直线插值
//            float newX = startX + (endX - startX) * progress;
//            float newY = startY + (endY - startY) * progress;
//
//            // 设置位置
//
//            // 更新时间
//            elapsedTime += 0.05f; // 每帧更新
//            System.out.printf("coords:%s,%s\n",newX,newY);
//            return new FloatPoint(newX,newY);
//        }
//        return new FloatPoint(endX,endY);
//    }
    public FloatPoint Update(){
        if (InAnimation()) {
            float t = elapsedTime / totalTime; // 归一化时间 (0 ~ 1)

            // 计算当前 progress
            float progress = this.ProgressFunction(t);

            // 计算直线插值
            newX = startX + (endX - startX) * progress;
            newY = startY + (endY - startY) * progress;

            // 设置位置

            // 更新时间
            elapsedTime += 0.05f; // 每帧更新
            return new FloatPoint(newX,newY);
        }
        newX=endX;
        newY=endY;
        return new FloatPoint(endX,endY);
    }
    public boolean InAnimation(){
        return elapsedTime < totalTime;
    }
//    public abstract float speedFunction(float t);
    public abstract float ProgressFunction(float t);
}
